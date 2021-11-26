package com.cbox.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cbox.base.core.domain.entity.SysUser;
import com.cbox.base.core.domain.model.LoginUser;
import com.cbox.base.enums.UserStatus;
import com.cbox.base.exception.BaseException;
import com.cbox.base.utils.StringUtils;
import com.cbox.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author cbox
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private AuthLevelService authLevelService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (!"1".equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用或未激活.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用或未激活");
        }

        // 设置用户的级别信息
        // user.setLevelAuth(authLevelService.getUserLevelAuth(user, false));
        // user.setLevelSelfAuth(authLevelService.getUserLevelAuth(user, true));

        // 设置用户的部门向上和向下的权限信息
        user.setUpDeptAuth(authLevelService.getUserUpDeptAuth(user));

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
