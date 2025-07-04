package com.rabbiter.oes.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.JudgeQuestion;
import com.rabbiter.oes.mapper.JudgeQuestionMapper;
import com.rabbiter.oes.service.JudgeQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeQuestionServiceImpl implements JudgeQuestionService {

    @Autowired
    private JudgeQuestionMapper judgeQuestionMapper;

    @Override
    public List<JudgeQuestion> findByIdAndType(Integer paperId) {
        return judgeQuestionMapper.findByIdAndType(paperId);
    }

    @Override
    public IPage<JudgeQuestion> findAll(Page<JudgeQuestion> page) {
        return judgeQuestionMapper.findAll(page);
    }

    @Override
    public JudgeQuestion findOnlyQuestionId() {
        return judgeQuestionMapper.findOnlyQuestionId();
    }

    @Override
    public int add(JudgeQuestion judgeQuestion) {
        return judgeQuestionMapper.add(judgeQuestion);
    }

    @Override
    public List<Integer> findBySubject(String subject, Integer pageNo) {
        return judgeQuestionMapper.findBySubject(subject,pageNo);
    }

    @Override
    public int edit(JudgeQuestion judgeQuestion) {
        return judgeQuestionMapper.edit(judgeQuestion);
    }

    @Override
    public int update(JudgeQuestion judgeQuestion) {
        return judgeQuestionMapper.update(judgeQuestion);
    }

    @Override
    public List<JudgeQuestion> findBySubject(String subject) {
        return judgeQuestionMapper.findQuestionBySubject(subject);
    }

    @Override
    public JudgeQuestion findById(Integer id) {
        return judgeQuestionMapper.findById(id);
    }

    @Override
    public int deleteById(Integer id) {
        return judgeQuestionMapper.deleteById(id);
    }
}
