package com.rabbiter.oes.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.Student;
import com.rabbiter.oes.mapper.StudentMapper;
import com.rabbiter.oes.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public IPage<Student> findAll(
            Page<Student> page, String name, String grade,
            String tel, String institute, String major, String clazz,
            String campus, String educationSystem) {
        name = ("@".equals(name) ? "" : name);
        grade = ("@".equals(grade) ? "" : grade);
        tel = ("@".equals(tel) ? "" : tel);
        institute = ("@".equals(institute) ? "" : institute);
        major = ("@".equals(major) ? "" : major);
        clazz = ("@".equals(clazz) ? "" : clazz);
        campus = ("@".equals(campus) ? "" : campus);
        educationSystem = ("@".equals(educationSystem) ? "" : educationSystem);
        return studentMapper.findAll(page, name, grade, tel, institute, major, clazz, campus, educationSystem);
    }

    @Override
    public Student findById(String studentId) {
        return studentMapper.findById(studentId);
    }

    @Override
    public int deleteById(String studentId) {
        return studentMapper.deleteById(studentId);
    }

    @Override
    public int update(Student student) {
        return studentMapper.update(student);
    }

    @Override
    public int updatePwd(Student student) {
        return studentMapper.updatePwd(student);
    }

    @Override
    public int add(Student student) {
        return studentMapper.add(student);
    }
}
