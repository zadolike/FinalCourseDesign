package com.example.coursesystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.coursesystem.common.lang.Result;
import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.SCourse;
import com.example.coursesystem.service.SCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yz
 * @since 2021-06-17
 */
@RestController
@RequestMapping("/scourse")
public class SCourseController {
    @Autowired
    SCourseService sCourseService;
    //添加学生到对应课程
    @PostMapping("/addStudent")
    public Result addStudent(@RequestBody SCourse sCourse){
//        SCourse sCourse = new SCourse();
//        sCourse.setCno(cno);
//        sCourse.setSno(sno);
        sCourseService.save(sCourse);
        return Result.success("操作成功");
    }
}
