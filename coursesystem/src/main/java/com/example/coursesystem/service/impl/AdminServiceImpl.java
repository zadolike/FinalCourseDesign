package com.example.coursesystem.service.impl;

import com.example.coursesystem.entity.Admin;
import com.example.coursesystem.mapper.AdminMapper;
import com.example.coursesystem.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yz
 * @since 2021-06-17
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
