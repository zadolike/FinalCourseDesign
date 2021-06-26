package com.example.coursesystem.shiro;

import cn.hutool.core.bean.BeanUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.coursesystem.entity.Admin;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.entity.Teacher;
import com.example.coursesystem.service.AdminService;
import com.example.coursesystem.service.StudentService;
import com.example.coursesystem.service.TeacherService;
import com.example.coursesystem.util.JwtUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;
    @Autowired
    AdminService adminService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = principalCollection.getPrimaryPrincipal();
        Set<String> roles = new HashSet<>();
        roles.add(principal.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        //获取userId
        String subject = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        String[] a = subject.split(","); //a[0]为role,a[1]为no
        if ("Student".equals(a[0])){
            Student student = studentService.getOne(new QueryWrapper<Student>().eq("sno", a[1]));
            if (student==null){
                throw new UnknownAccountException("账户为空");
            }
            AccountProfile profile =  new AccountProfile();
            profile.setRole(a[0]);
            profile.setId(Long.parseLong(a[1]));
            SimpleAuthenticationInfo info  = new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
            return info;
        }
        else if ("Teacher".equals(a[0])){
            Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("tno", a[1]));
            if (teacher==null){
                throw new UnknownAccountException("账户为空");
            }
            AccountProfile profile =  new AccountProfile();
            profile.setRole(a[0]);
            profile.setId(Long.parseLong(a[1]));
            return new SimpleAuthenticationInfo(profile,token.getCredentials(),getName());
        }
        else if ("Admin".equals(a[0])){
            Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("ano", a[1]));
            if (admin==null){
                throw new UnknownAccountException("账户为空");
            }
            AccountProfile profile =  new AccountProfile();
            profile.setRole(a[0]);
            profile.setId(Long.parseLong(a[1]));

            return new SimpleAuthenticationInfo(profile,token.getCredentials(),getName());
        }
        throw new AuthenticationException();
    }

}
