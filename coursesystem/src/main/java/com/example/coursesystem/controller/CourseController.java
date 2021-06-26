package com.example.coursesystem.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursesystem.common.dto.CourseDto;
import com.example.coursesystem.common.lang.Result;
import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.SCourse;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.entity.Teacher;
import com.example.coursesystem.service.CourseService;
import com.example.coursesystem.service.SCourseService;
import com.example.coursesystem.service.StudentService;
import com.example.coursesystem.service.TeacherService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yz
 * @since 2021-06-17
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController{
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    SCourseService sCourseService;
    @Autowired
    StudentService studentService;
    @CrossOrigin
    @GetMapping("/list")
    public Result list(String cno, Integer current, Integer size) {
        if (current!=null&size!=null){
            IPage page= new Page<>(current,size);
            Page<Course> pageData = (Page<Course>) courseService.page(page,new QueryWrapper<Course>().like(StrUtil.isNotBlank(cno),"cno",cno).or().like(StrUtil.isNotBlank(cno),"cname",cno));
            return Result.success(pageData);
        }
        Page<Course> pageData = (Page<Course>) courseService.page(getPage(),
                new QueryWrapper<Course>()
                        .like(StrUtil.isNotBlank(cno), "cno", cno)
        );
        return Result.success(pageData);
    }
    //课程信息获取
    @GetMapping("/tno/list")
    public Result getTnoList() {
        List<String> tnoList = new ArrayList<>();
        List<Teacher> courseList = teacherService.list();
        for (Teacher teacher:courseList
             ) {
            tnoList.add(teacher.getTno());
        }
        return Result.success(tnoList);
    }
    //课程信息获取
    @GetMapping("/Info/{id}")
    public Result getCourseInfo(@PathVariable String id) {
        Course course = courseService.getById(id);
        Assert.notNull(course, "找不到该课程！");
        return Result.success(course);
    }
    //课程信息更新
    @CrossOrigin
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Course course) {
        if (StrUtil.isNotBlank(course.getTno())) {
            courseService.updateById(course);
        }
        return Result.success(course);
    }
    //课程信息保存
    @CrossOrigin
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Course course) {
        if (StrUtil.isNotBlank(course.getTno())) {
           courseService.save(course);
        }
        return Result.success(course);
    }

    //课程删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        courseService.removeByIds(Arrays.asList(ids));
        courseService.remove(new QueryWrapper<Course>().in("id", ids));
        return Result.success("操作成功");
    }
    //教师获取课程
    @GetMapping("/getCourse/{id}")
    public Result getCourse(@PathVariable String id){
        List<Course> list= courseService.list(new QueryWrapper<Course>().eq(StrUtil.isNotBlank(id),"tno",id));
        List<CourseDto> courseList = new ArrayList<>();
        for (Course course:list
             ) {
            CourseDto courseDto = new CourseDto();
            courseDto.setCname(course.getCname());
            courseDto.setCno(course.getCno());
           courseList.add(courseDto);
        }
        return Result.success(courseList);
    }
    //学生获取课程
    @GetMapping("/getStuCourse/{id}")
    public Result getStuCourse(@PathVariable String id){
        List<SCourse> list= sCourseService.list(new QueryWrapper<SCourse>().eq(StrUtil.isNotBlank(id),"sno",id));
        List<Course> courseList = new ArrayList<>();
        for (SCourse scourse:list
             ) {
            Course course = courseService.getOne(new QueryWrapper<Course>().eq(StrUtil.isNotBlank(scourse.getCno()),"cno",scourse.getCno()));
            courseList.add(course);
        }
        return Result.success(courseList);
    }
    //课程号获取学生列表
    @GetMapping("/listStudent")
    public Result getStuList(String cno){
        List<SCourse> list= sCourseService.list(new QueryWrapper<SCourse>().eq("cno",cno));
        List<Student> StuList = new ArrayList<>();
        for (SCourse scourse:list
        ) {
            Student student = studentService.getOne(new QueryWrapper<Student>().eq("sno",scourse.getSno()));
            StuList.add(student);
        }
        return Result.success(StuList);
    }
}
