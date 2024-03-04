package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {
    //注入
    @Autowired
    private SysRoleMapper mapper;
    @Test
    public void getAll(){
        List<SysRole> list = mapper.selectList(null);
        System.out.println(list);
    }
    @Test
    public void add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");
        int rows = mapper.insert(sysRole);
        System.out.println(rows);
        System.out.println(sysRole);
    }
    @Test
    public void update(){
        SysRole role = mapper.selectById(9);
        role.setRoleName("atguigu角色管理员");
        int rows = mapper.updateById(role);
        System.out.println(rows);
    }
    @Test
    public void deleteId(){
        int rows = mapper.deleteById(9);
        System.out.println(rows);

    }
    @Test
    public void testDeleteBatchIds(){
        int result = mapper.deleteBatchIds(Arrays.asList(1,2));
        System.out.println(result);
    }
    @Test
    public void testQuery1(){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name","系统管理员");
        List<SysRole> list =mapper.selectList(wrapper);
        System.out.println(list);
    }
    @Test
    public void testQuery2(){
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName,"系统管理员");
        List<SysRole> list =mapper.selectList(wrapper);
        System.out.println(list);
    }
}
