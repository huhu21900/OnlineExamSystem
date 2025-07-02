package com.rabbiter.oes.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ShortAnswerQuestion;
import com.rabbiter.oes.mapper.ShortAnswerQuestionMapper;
import com.rabbiter.oes.service.ShortAnswerQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortAnswerQuestionServiceImpl implements ShortAnswerQuestionService {

    @Autowired
    private ShortAnswerQuestionMapper shortAnswerQuestionMapper;

    @Override
    public List<ShortAnswerQuestion> findByIdAndType(Integer paperId) {
        return shortAnswerQuestionMapper.findByIdAndType(paperId);
    }

    @Override
    public IPage<ShortAnswerQuestion> findAll(Page<ShortAnswerQuestion> page) {
        return shortAnswerQuestionMapper.findAll(page);
    }

    @Override
    public ShortAnswerQuestion findOnlyQuestionId() {
        return shortAnswerQuestionMapper.findOnlyQuestionId();
    }

    @Override
    public int add(ShortAnswerQuestion shortAnswerQuestion) {
        return shortAnswerQuestionMapper.add(shortAnswerQuestion);
    }

    @Override
    public List<Integer> findBySubject(String subject, Integer pageNo) {
        return shortAnswerQuestionMapper.findBySubject(subject, pageNo);
    }

    @Override
    public int edit(ShortAnswerQuestion shortAnswerQuestion) {
        return shortAnswerQuestionMapper.edit(shortAnswerQuestion);
    }

    @Override
    public int update(ShortAnswerQuestion shortAnswerQuestion) {
        return shortAnswerQuestionMapper.update(shortAnswerQuestion);
    }

    @Override
    public List<ShortAnswerQuestion> findBySubject(String subject) {
        return shortAnswerQuestionMapper.findQuestionBySubject(subject);
    }

    @Override
    public ShortAnswerQuestion findById(Integer id) {
        return shortAnswerQuestionMapper.findById(id);
    }

    @Override
    public int deleteById(Integer id) {
        return shortAnswerQuestionMapper.deleteById(id);
    }
} 