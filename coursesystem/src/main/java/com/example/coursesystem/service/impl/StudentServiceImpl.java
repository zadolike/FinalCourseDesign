package com.example.coursesystem.service.impl;

import com.example.coursesystem.entity.Student;
import com.example.coursesystem.mapper.StudentMapper;
import com.example.coursesystem.service.StudentService;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
