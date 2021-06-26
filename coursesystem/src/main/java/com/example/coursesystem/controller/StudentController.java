package com.example.coursesystem.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursesystem.common.lang.Result;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.service.StudentService;
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
@RequestMapping("/student")
public class StudentController extends BaseController{
    @Autowired
    StudentService studentService;
    @CrossOrigin
    @GetMapping("/list")
    public Result list(String sno,Integer current,Integer size) {
        if (current!=null&size!=null){
            IPage page= new Page<>(current,size);
            Page<Student> pageData = (Page<Student>) studentService.page(page,new QueryWrapper<Student>().like(StrUtil.isNotBlank(sno),"sno",sno).or().like(StrUtil.isNotBlank(sno),"sname",sno));
            return Result.success(pageData);
        }
        Page<Student> pageData = (Page<Student>) studentService.page(getPage(),
                new QueryWrapper<Student>()
                        .like(StrUtil.isNotBlank(sno), "sno", sno)
        );
        return Result.success(pageData);
    }
    //学生信息获取
    @GetMapping("/Info/{id}")
    public Result getStuInfo(@PathVariable String id) {
        Student student = studentService.getById(id);
        Assert.notNull(student, "找不到该学生！");
        return Result.success(student);
    }
    //学生信息更新
    @CrossOrigin
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Student student) {
        if (student.getSex().equals("1")){
            student.setSex("女");
        }
        else {
            student.setSex("男");
        }
        if (StrUtil.isNotBlank(student.getPwd())) {
            studentService.updateById(student);
        }
        return Result.success(student);
    }
    //学生信息保存
    @CrossOrigin
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Student student) {
        if (student.getSex().equals("1")){
            student.setSex("女");
        }
        else {
            student.setSex("男");
        }
        if (StrUtil.isNotBlank(student.getPwd())) {
            studentService.save(student);
        }
        return Result.success(student);
    }

    //学生删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        studentService.removeByIds(Arrays.asList(ids));
        studentService.remove(new QueryWrapper<Student>().in("id", ids));
        return Result.success("操作成功");
    }

    //获取所有学生学号
    @GetMapping("/getsno")
    public Result getSno(){
        List<String> list = new ArrayList<>();
        List<Student> stuList = studentService.list();
        for (Student student:stuList
             ) {
            list.add(student.getSno());
        }
        return Result.success(list);
    }
}
