package com.example.coursesystem.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursesystem.common.lang.Result;
import com.example.coursesystem.entity.News;
import com.example.coursesystem.service.NewsService;
import com.example.coursesystem.service.StudentService;
import com.example.coursesystem.service.TeacherService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yz
 * @since 2021-06-17
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    NewsService newsService;

    @GetMapping("/news")
    public Result news(Integer currentPage) {
        if(currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page(currentPage, 5);
        IPage pageData = newsService.page(page, new QueryWrapper<News>().orderByDesc("created"));
        return Result.success(pageData);
    }
    @GetMapping("/news/{id}")
    public Result detail(@PathVariable(name = "id") String id) {
        News news = newsService.getById(id);
        Assert.notNull(news, "该新闻已删除！");
        return Result.success(news);
    }

    @PostMapping("/news/edit")
    public Result edit(@Validated @RequestBody News news) {
        System.out.println(news.toString());
        News temp = null;
        if(news.getId() != null) {
            temp = newsService.getById(news.getId());
        } else {
            temp = new News();
            temp.setCreated(LocalDateTime.now());
        }
        BeanUtil.copyProperties(news, temp, "id", "created");
        try {
            newsService.saveOrUpdate(temp);
        }
        catch (Exception e){
            return Result.fail("添加文章失败，文章格式错误");
        }
        return Result.success("操作成功");
    }

}
