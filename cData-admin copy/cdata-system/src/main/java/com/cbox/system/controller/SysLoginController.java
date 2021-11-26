package com.cbox.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.entity.SysMenu;
import com.cbox.base.core.domain.entity.SysUser;
import com.cbox.base.core.domain.model.LoginBody;
import com.cbox.base.core.domain.model.LoginUser;
import com.cbox.base.exception.user.UserPasswordNotMatchException;
import com.cbox.base.utils.ServletUtils;
import com.cbox.framework.web.service.SysLoginService;
import com.cbox.framework.web.service.SysPermissionService;
import com.cbox.framework.web.service.TokenService;
import com.cbox.system.service.ISysMenuService;

/**
 * 登录验证
 * 
 * @author cbox
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = null;// AjaxResult.success();
        try {
            // 生成令牌
            Map<String, Object> tokenMap = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
            // ajax.put(Constants.TOKEN, token);
            ajax = AjaxResult.success("", tokenMap);
            ajax.put("openCheck", "1");
        } catch (UserPasswordNotMatchException e) {
            ajax = AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            ajax = AjaxResult.error(e.getMessage());
        }
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
