package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.exception.GuiguException;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jwt.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import result.Result;
import utils.MD5;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台登录登出
 * </p>
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo ) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token","admin-token");
//        return Result.ok(map);
        //1 获取输入用户名和密码
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        if(sysUser==null){
            throw new GuiguException(201,"用户不存在");
        }

        //4判断密码
        String password_db = sysUser.getPassword();
        String password_input =MD5.encrypt(loginVo.getPassword()) ;

        if(!password_db.equals(password_input)){
            throw new GuiguException(201,"密码错误");

        }
        //5判断用户是否被禁用
        if(sysUser.getStatus().intValue()==0){
            throw new GuiguException(201,"用户已经被禁用");
        }
        //6使用jwt根据用户id和用户名生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);

        //
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info( HttpServletRequest request ) {
        //1从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");
        //2 从token 字符串获取用户id 或 用户名称
        Long userId = JwtHelper.getUserId(token);
        //3 根据用户id 查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);
        //4 根据用户id获取用户可以操作菜单列表
        //查询数据库动态构建路由结构，进行显示
        List<RouterVo> routerVoList =  sysMenuService.findUserMenuListByUserId(userId);
        //5根据用户id获取用户可以操作按钮列表
        List<String> permsList =  sysMenuService.findUserPermsByUserId(userId);
        //6 返回相应的数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        //返回用户可以操作菜单
        System.out.println("==========================================================");
        map.put("routers",routerVoList);
        //返回用户可以操作按钮
        map.put("buttons",permsList);
        return Result.ok(map);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }

}
