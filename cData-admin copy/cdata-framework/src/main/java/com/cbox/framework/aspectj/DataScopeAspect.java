package com.cbox.framework.aspectj;

import java.lang.reflect.Method;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.cbox.base.annotation.DataScope;
import com.cbox.base.core.domain.BaseEntity;
import com.cbox.base.core.domain.entity.SysRole;
import com.cbox.base.core.domain.entity.SysUser;
import com.cbox.base.core.domain.model.LoginUser;
import com.cbox.base.utils.ServletUtils;
import com.cbox.base.utils.StringUtils;
import com.cbox.base.utils.spring.SpringUtils;
import com.cbox.framework.web.service.TokenService;

/**
 * 数据过滤处理
 *
 * @author cbox
 */
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.cbox.base.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(), controllerDataScope.userAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user 用户
     * @param userAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();

        if (StringUtils.isBlank(deptAlias) && StringUtils.isNotBlank(userAlias)) {
            // 部门别名为空，用户别名不为空，则说明强制按照用户过滤权限.add by qiuzq 2020-12-8
            sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
        } else {

            // 按照角色的权限去生成权限过滤代码
            for (SysRole role : user.getRoles()) {
                String dataScope = role.getDataScope();
                if (DATA_SCOPE_ALL.equals(dataScope)) {
                    sqlString = new StringBuilder();
                    break;
                } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                    sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias, role.getRoleId()));
                } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                    sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
                } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                    sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", deptAlias, user.getDeptId(),
                            user.getDeptId()));
                } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                    if (StringUtils.isNotBlank(userAlias)) {
                        sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                    } else {
                        // 数据权限为仅本人且没有userAlias别名不查询任何数据
                        sqlString.append(" OR 1=0 ");
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params)) {
                if (params instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) params;
                    baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
                } else if (params instanceof Map) {
                    Map map = (Map) params;
                    map.put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
                }
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
