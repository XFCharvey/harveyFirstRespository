package com.cbox.reward.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Maps;
import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.reward.CheckPhone;
import com.cbox.reward.service.RewardService;

/**
 * 抽奖
 *
 */
@RestController
@RequestMapping("/lottery")
public class RewardController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RewardService lotteryService;

    private Random random = new Random();
    // 奖品索引-- ID对应关系
    private static Map<Integer, Integer> prize = Maps.newHashMap();
    // 联通奖品列表
    private static List<Integer> ltList = Arrays.asList(0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 5, 6, 7);
    // 移动奖品列表
    private static List<Integer> ydList = Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 6, 7);
    // 电信奖品列表
    private List<Integer> dxList = Arrays.asList(6, 7);

    @Value("${lottery.max:1000}")
    private int max;// 最大抽奖数
    private boolean open = true;// 抽奖开关
    static {
        prize.put(0, 1);// 话费3元
        prize.put(1, 2);// 话费5元
        prize.put(2, 3);// 话费6元
        prize.put(3, 4);// 话费7元
        prize.put(5, 5);// 话费8元
        prize.put(6, 6);// 爱奇艺VIP会员7天卡
        prize.put(7, 7);// 优酷土豆黄金会员周卡
        prize.put(8, 8);// 谢谢参与

        // 打乱顺序
        Collections.shuffle(ltList);
        Collections.shuffle(ydList);

    }

    /**
     * * 校验手机号、设备id是否抽奖过
     * 
     * @param param
     * @return
     */
    @PostMapping("check")
    public ResponseBodyVO check(@RequestBody Map<String, String> param) {
        System.out.println(param);
        String phone = param.get("phone");
        String deviceid = param.get("deviceid");
        // 校验必填参数
        String checkResult = this.validInput("deviceid", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        return lotteryService.check(phone, deviceid);
    }

    /**
     * * 获取奖品索引号
     * 
     * @param param
     * @return
     */
    @PostMapping("get")
    public ResponseBodyVO get(@RequestBody Map<String, String> param) {
        String phone = param.get("phone");
        int index = 8;
        if (StringUtils.isEmpty(phone)) {
            return ServerRspUtil.error("手机号请求参数为空");
        }
        if (open) {
            if (lotteryService.checkPhoneReward(phone)) {
                logger.info("该手机号已参加过抽奖");
                return ServerRspUtil.success(index);
            }
            String qbank_id = "170";
            int total = lotteryService.countReward(qbank_id);
            if (total > max) {
                logger.info("已超过最大抽奖数量{}", max);
                open = false;
            } else {
                if (CheckPhone.isChinaMobilePhoneNum(phone)) {// 移动
                    index = ydList.get(random.nextInt(ydList.size()));
                } else if (CheckPhone.isChinaUnicomPhoneNum(phone)) {// 联通
                    index = ltList.get(random.nextInt(ltList.size()));
                } else if (CheckPhone.isChinaTelecomPhoneNum(phone)) { // 电信
                    index = dxList.get(random.nextInt(dxList.size()));
                } else {

                }
            }
        } else {
            logger.info("抽奖已关闭");
        }
        return ServerRspUtil.success(index);
    }

    /**
     * 更新中奖奖品
     * 
     * @param param
     * @return
     */
    @PostMapping("save")
    public ResponseBodyVO save(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("recId,idx", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        param.put("prize_id", prize.get(param.get("idx")));
        return lotteryService.save(param);
    }

}