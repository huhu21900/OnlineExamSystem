package com.rabbiter.oes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.Student;

public interface StudentService {

    IPage<Student> findAll(Page<Student> page, String name, String grade,
                           String tel, String institute, String major, String clazz,
                           String campus, String educationSystem);

    Student findById(String studentId);

    int deleteById(String studentId);

    int update(Student student);

    int updatePwd(Student student);
    int add(Student student);
}
