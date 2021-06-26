package com.example.coursesystem.service.impl;

import com.example.coursesystem.entity.News;
import com.example.coursesystem.mapper.NewsMapper;
import com.example.coursesystem.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yz
 * @since 2021-06-22
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
