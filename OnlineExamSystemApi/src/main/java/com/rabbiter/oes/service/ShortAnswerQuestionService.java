package com.rabbiter.oes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ShortAnswerQuestion;

import java.util.List;

public interface ShortAnswerQuestionService {

    List<ShortAnswerQuestion> findByIdAndType(Integer paperId);

    IPage<ShortAnswerQuestion> findAll(Page<ShortAnswerQuestion> page);

    ShortAnswerQuestion findOnlyQuestionId();

    int add(ShortAnswerQuestion shortAnswerQuestion);

    List<Integer> findBySubject(String subject, Integer pageNo);

    int edit(ShortAnswerQuestion shortAnswerQuestion);

    int update(ShortAnswerQuestion shortAnswerQuestion);

    List<ShortAnswerQuestion> findBySubject(String subject);

    ShortAnswerQuestion findById(Integer id);

    int deleteById(Integer id);
} 