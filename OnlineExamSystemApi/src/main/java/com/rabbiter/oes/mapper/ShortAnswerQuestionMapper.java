package com.rabbiter.oes.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ShortAnswerQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

//简答题

@Mapper
public interface ShortAnswerQuestionMapper {

    @Select("select * from short_answer_question where questionId in (select questionId from paper_manage where questionType = 4 and paperId = #{paperId})")
    List<ShortAnswerQuestion> findByIdAndType(Integer paperId);

    @Select("select * from short_answer_question")
    IPage<ShortAnswerQuestion> findAll(Page page);

    /**
     * 查询最后一条记录的questionId
     * @return ShortAnswerQuestion
     */
    @Select("select questionId from short_answer_question order by questionId desc limit 1")
    ShortAnswerQuestion findOnlyQuestionId();

    @Insert("insert into short_answer_question(subject,question,answer,analysis,level,section) values " +
            "(#{subject},#{question},#{answer},#{analysis},#{level},#{section})")
    int add(ShortAnswerQuestion shortAnswerQuestion);

    @Select("select questionId from short_answer_question where subject=#{subject} order by rand() desc limit #{pageNo}")
    List<Integer> findBySubject(@Param("subject") String subject, @Param("pageNo") Integer pageNo);

    @Update("update short_answer_question set subject = #{subject}, question = #{question}, answer = #{answer}, section = #{section}, analysis = #{analysis}, level = #{level} where questionId = #{questionId}")
    int edit(ShortAnswerQuestion shortAnswerQuestion);
    
    @Update("update short_answer_question set subject = #{subject}, question = #{question}, answer = #{answer}, section = #{section}, analysis = #{analysis}, level = #{level} where questionId = #{questionId}")
    int update(ShortAnswerQuestion shortAnswerQuestion);

    @Select("select * from short_answer_question where subject=#{subject}")
    List<ShortAnswerQuestion> findQuestionBySubject(@Param("subject") String subject);
    
    @Select("select * from short_answer_question where questionId = #{id}")
    ShortAnswerQuestion findById(Integer id);
    
    @Delete("delete from short_answer_question where questionId = #{id}")
    int deleteById(Integer id);
} 