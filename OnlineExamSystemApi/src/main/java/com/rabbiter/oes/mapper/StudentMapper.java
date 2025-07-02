package com.rabbiter.oes.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.Student;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentMapper {

    /**
     * 分页查询所有学生
     * @param page
     * @return List<Student>
     */
    @Select("select * from student where " +
            "studentName like concat('%',#{name},'%') " +
            "and grade like concat('%',#{grade},'%') " +
            "and tel like concat('%',#{tel},'%') " +
            "and major like concat('%',#{major},'%') " +
            "and institute like concat('%',#{institute},'%') " +
            "and clazz like concat('%',#{clazz},'%') " +
            "and campus like concat('%',#{campus},'%') " +
            "and educationSystem like concat('%',#{educationSystem},'%')")
    IPage<Student> findAll(Page page, @Param("name") String name, @Param("grade") String grade,
                           @Param("tel") String tel,  @Param("institute") String institute,
                           @Param("major")String major, @Param("clazz") String clazz,
                           @Param("campus") String campus, @Param("educationSystem") String educationSystem);

    @Select("select * from student where studentId = #{studentId}")
    Student findById(String studentId);

    @Delete("delete from student where studentId = #{studentId}")
    int deleteById(String studentId);

    /**
     *更新所有学生信息
     * @param student 传递一个对象
     * @return 受影响的记录条数
     */
    @Update("update student set studentName = #{studentName},grade = #{grade},major = #{major},clazz = #{clazz}," +
            "institute = #{institute},tel = #{tel},email = #{email},pwd = #{pwd},cardId = #{cardId},sex = #{sex},role = #{role}," +
            "campus = #{campus},educationSystem = #{educationSystem} " +
            "where studentId = #{studentId}")
    int update(Student student);

    /**
     * 更新密码
     * @param student
     * @return 受影响的记录条数
     */
    @Update("update student set pwd = #{pwd} where studentId = #{studentId}")
    int updatePwd(Student student);

    @Insert("insert into student(studentId, studentName, grade, major, clazz, institute, tel, email, pwd, cardId, sex, role, campus, educationSystem) values " +
            "(#{studentId}, #{studentName}, #{grade}, #{major}, #{clazz}, #{institute}, #{tel}, #{email}, #{pwd}, #{cardId}, #{sex}, #{role}, #{campus}, #{educationSystem})")
    int add(Student student);
}
