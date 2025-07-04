package com.rabbiter.oes.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.FillQuestion;
import com.rabbiter.oes.mapper.FillQuestionMapper;
import com.rabbiter.oes.service.FillQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FillQuestionServiceImpl implements FillQuestionService {

    @Autowired
    private FillQuestionMapper fillQuestionMapper;

    @Override
    public List<FillQuestion> findByIdAndType(Integer paperId) {
        return fillQuestionMapper.findByIdAndType(paperId);
    }

    @Override
    public IPage<FillQuestion> findAll(Page<FillQuestion> page) {
        return fillQuestionMapper.findAll(page);
    }

    @Override
    public FillQuestion findOnlyQuestionId() {
        return fillQuestionMapper.findOnlyQuestionId();
    }

    @Override
    public int add(FillQuestion fillQuestion) {
        return fillQuestionMapper.add(fillQuestion);
    }

    @Override
    public List<Integer> findBySubject(String subject, Integer pageNo) {
        return fillQuestionMapper.findBySubject(subject,pageNo);
    }

    @Override
    public int edit(FillQuestion fillQuestion) {
        return fillQuestionMapper.edit(fillQuestion);
    }

    @Override
    public int update(FillQuestion fillQuestion) {
        return fillQuestionMapper.update(fillQuestion);
    }

    @Override
    public List<FillQuestion> findBySubject(String subject) {
        return fillQuestionMapper.findQuestionBySubject(subject);
    }

    @Override
    public FillQuestion findById(Integer id) {
        return fillQuestionMapper.findById(id);
    }

    @Override
    public int deleteById(Integer id) {
        return fillQuestionMapper.deleteById(id);
    }
}
