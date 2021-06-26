package com.example.coursesystem.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.coursesystem.common.dto.LoginDto;
import com.example.coursesystem.common.lang.Result;
import com.example.coursesystem.entity.Admin;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.entity.Teacher;
import com.example.coursesystem.service.AdminService;
import com.example.coursesystem.service.CourseService;
import com.example.coursesystem.service.StudentService;
import com.example.coursesystem.service.TeacherService;
import com.example.coursesystem.shiro.JwtToken;
import com.example.coursesystem.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController

public class AccountController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;
    @Autowired
    AdminService adminService;
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        String roleType = loginDto.getRole();
        String jwt;
        JwtToken jwtToken;
        switch (roleType){
            case "Student":
                Student student = studentService.getOne(new QueryWrapper<Student>().eq("sno", loginDto.getUsername()));
                Assert.notNull(student, "用户不存在");
                if(!student.getPwd().equals(loginDto.getPassword())) {
                    return Result.fail("密码错误！");
                }
                jwt = jwtUtils.generateToken(student.getSno(),"Student");
                JwtToken token = new JwtToken(jwt);
                response.setHeader("Authorization", jwt);
                response.setHeader("Access-control-Expose-Headers", "Authorization");
                // 用户可以另一个接口
                return Result.success(MapUtil.builder()
                        .put("id",student.getId())
                        .put("sno", student.getSno())
                        .put("sex", student.getSex())
                        .put("sname", student.getSname())
                        .put("role","Student")
                        .map()
                );

            case "Teacher":
                Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("tno", loginDto.getUsername()));
                Assert.notNull(teacher, "用户不存在");
                if(!teacher.getPwd().equals(loginDto.getPassword())) {
                    return Result.fail("密码错误！");
                }
                jwt = jwtUtils.generateToken(teacher.getTno(),"Teacher");
                response.setHeader("Authorization", jwt);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
                
                // 用户可以另一个接口
                return Result.success(MapUtil.builder()
                        .put("id",teacher.getId())
                        .put("sno", teacher.getTno())
                        .put("sex", teacher.getSex())
                        .put("sname", teacher.getTname())
                        .put("role","Teacher")
                        .map()
                );
            case "Admin":
                Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("ano", loginDto.getUsername()));
                Assert.notNull(admin, "用户不存在");
                if(!admin.getPwd().equals(loginDto.getPassword())) {
                    return Result.fail("密码错误！");
                }

                jwt = jwtUtils.generateToken(admin.getAno(),"Admin");
                response.setHeader("Authorization", jwt);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
                // 用户可以另一个接口
                return Result.success(MapUtil.builder()
                        .put("id",admin.getId())
                        .put("sno", admin.getAno())
                        .put("role","Admin")
                        .map()
                );
        }
        return Result.fail("未选择角色");
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }
}
