package com.cbox.business.question.analysis.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.question.examrecorddetail.mapper.ExamRecordDetailMapper;
import com.cbox.business.question.questiontitle.mapper.QuestiontitleMapper;
import com.cbox.business.question.questiontitlegroup.mapper.QuestiontitlegroupMapper;
import com.cbox.business.question.questiontitleoption.mapper.QuestionTitleOptionMapper;

/**
 * @ClassName: QuestionAnalysisService
 * @Function: 问卷交叉分析
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuestionAnalysisService2 extends BaseService {

    @Autowired
    private QuestiontitlegroupMapper questiontitlegroupMapper;

    @Autowired
    private QuestiontitleMapper questiontitleMapper;

    @Autowired
    private QuestionTitleOptionMapper questionTitleOptionMapper;

    @Autowired
    private ExamRecordDetailMapper examRecordDetailMapper;

    /**
     * getQuestionResult:
     *
     * @date: 2021年10月19日 下午10:32:48 
     * @author qiuzq 
     * @param param 传参question_id
     * @return
     */
    public ResponseBodyVO getQuestionResult(@RequestBody Map<String, Object> param) {


        // 获取分组数据
        List<Map<String, Object>> listGroup = questiontitlegroupMapper.listQuestiontitlegroup(param);

        // 获取title数据
        List<Map<String, Object>> listTitles = questiontitleMapper.listQuestiontitle(param);

        // 获取Options选项数据
        List<Map<String, Object>> listOptions = questionTitleOptionMapper.listQuestionTitleOption(param);

        List<Map<String, Object>> optionRecord = examRecordDetailMapper.listExamRecordDetail(param);

        // 设置默认的空分组
        Map<String, Object> mapNullGroup = new HashMap<String, Object>();
        mapNullGroup.put("group_name", "");
        mapNullGroup.put("rec_id", "0");
        listGroup.add(mapNullGroup);

        for (int i = 0; i < listGroup.size(); i++) {
            Map<String, Object> mapGroup = listGroup.get(i);
            String groupId = StrUtil.getMapValue(mapGroup, "rec_id");

            List<Map<String, Object>> listGroupTitles = new ArrayList<Map<String, Object>>();
            // 遍历所有的题目
            for (int j = 0; j < listTitles.size(); j++) {
                Map<String, Object> mapTitle = listTitles.get(j);
                String titleGroupId = StrUtil.getMapValue(mapTitle, "group_id");
                String titleId = StrUtil.getMapValue(mapTitle, "rec_id");

                if (StrUtil.isNull(titleGroupId)) {
                    titleGroupId = "0";
                }

                if (!groupId.equals(titleGroupId)) {
                    continue;
                }

                List<Map<String, Object>> listTitleOptions = new ArrayList<Map<String, Object>>();
                // 遍历所有的选项
                for (int h = 0; h < listOptions.size(); h++) {
                    Map<String, Object> mapOption = listOptions.get(h);
                    String optionTitleId = StrUtil.getMapValue(mapOption, "title_id");
                    String opRecId = StrUtil.getMapValue(mapOption, "rec_id");

                    if (!titleId.equals(optionTitleId)) {
                        continue;
                    }

                    // 计算票数和比例
                    double choiceNum = 0;
                    int joinTotal = 0;
                    for (int g = 0; g < optionRecord.size(); g++) {
                        Map<String, Object> mapOptionRecord = optionRecord.get(g);
                        String recordAnswer = StrUtil.getMapValue(mapOptionRecord, "answers");
                        String recordTitleId = StrUtil.getMapValue(mapOptionRecord, "title_id");

                        if (StrUtil.isNull(recordAnswer)) {
                            continue;
                        }

                        if (titleId.equals(recordTitleId)) {
                            joinTotal++;
                        }

                        String[] answerArr = recordAnswer.split(",");
                        boolean iscontain = Arrays.asList(answerArr).contains(opRecId);
                        if (iscontain) {
                            choiceNum = choiceNum + 1;
                        }
                    }
                    mapOption.put("choiceNum", choiceNum);
                    if (choiceNum != 0) {
                        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
                        double choicePercent = (choiceNum / joinTotal) * 100;
                        mapOption.put("choicePercent", df.format(choicePercent));
                    } else {
                        mapOption.put("choicePercent", 0);
                    }

                    if (j == 0) {
                        System.out.println("joinTotal:" + joinTotal);
                    }

                    listTitleOptions.add(mapOption);
                }

                mapTitle.put("options", listTitleOptions);
                listGroupTitles.add(mapTitle);
            }
            mapGroup.put("titles", listGroupTitles);
        }

        return ServerRspUtil.success(listGroup);
    }

}
