package com.rabbiter.oes.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ExamManage;
import com.rabbiter.oes.mapper.ExamManageMapper;
import com.rabbiter.oes.service.ExamManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ExamManageServiceImpl implements ExamManageService {
    @Autowired
    private ExamManageMapper examManageMapper;
    @Autowired
    private PaperServiceImpl paperService;

    private void setMaxScore(List<ExamManage> examManageList) {
        for (ExamManage examManage : examManageList) {
            examManage.setTotalScore(paperService.getMaxScore(examManage.getPaperId()));
        }
    }

    @Override
    public List<ExamManage> findAll() {
        Page<ExamManage> examManage = new Page<>(0,9999);
        List<ExamManage> examManageList = examManageMapper.findAll(examManage).getRecords();
        setMaxScore(examManageList);
        return examManageList;
    }

    @Override
    public IPage<ExamManage> findAll(Page<ExamManage> page) {
        IPage<ExamManage> iPage = examManageMapper.findAll(page);
        setMaxScore(iPage.getRecords());
        return iPage;
    }

    @Override
    public IPage<ExamManage> findByKeyword(Page<ExamManage> page, String keyword, String type) {
        // 获取所有试卷
        List<ExamManage> allExams = findAll();
        
        // 根据类型和关键词过滤
        List<ExamManage> filteredExams = allExams.stream()
            .filter(exam -> {
                if ("source".equals(type)) {
                    return exam.getSource() != null && exam.getSource().contains(keyword);
                } else if ("institute".equals(type)) {
                    return exam.getInstitute() != null && exam.getInstitute().contains(keyword);
                } else if ("major".equals(type)) {
                    return exam.getMajor() != null && exam.getMajor().contains(keyword);
                } else if ("type".equals(type)) {
                    return exam.getType() != null && exam.getType().contains(keyword);
                } else {
                    // 默认按试卷名称搜索
                    return exam.getSource() != null && exam.getSource().contains(keyword);
                }
            })
            .collect(Collectors.toList());
        
        // 手动分页
        int total = filteredExams.size();
        // 修复从long转换到int可能导致的损失问题
        long startLong = (page.getCurrent() - 1) * page.getSize();
        int start = (int) Math.min(startLong, Integer.MAX_VALUE);
        if (start < 0) {
            start = 0; // 防止溢出变成负数
        }
        int end = Math.min(start + (int)page.getSize(), total);
        
        if (start >= total) {
            start = 0;
            end = Math.min((int)page.getSize(), total);
        }
        
        List<ExamManage> pageRecords = filteredExams.subList(start, end);
        
        // 设置分页结果
        Page<ExamManage> resultPage = new Page<>(page.getCurrent(), page.getSize(), total);
        resultPage.setRecords(pageRecords);
        
        return resultPage;
    }

    @Override
    public ExamManage findById(Integer examCode) {
        ExamManage examManage = examManageMapper.findById(examCode);
        examManage.setTotalScore(paperService.getMaxScore(examManage.getPaperId()));
        return examManage;
    }

    @Override
    public int delete(Integer examCode) {
        // 移除题目关联
        ExamManage examManage = examManageMapper.findById(examCode);
        if(examManage == null) {
            return 0;
        }
        paperService.deleteByPaperId(examManage.getPaperId());
        return examManageMapper.delete(examCode);
    }

    @Override
    public int update(ExamManage exammanage) {
        return examManageMapper.update(exammanage);
    }

    @Override
    public int add(ExamManage exammanage) {
        // 设置一个随机的paperId
        Random random = new Random();
        Integer paperId = random.nextInt(1000);
        exammanage.setPaperId(paperId);
        return examManageMapper.add(exammanage);
    }

    @Override
    public ExamManage findOnlyPaperId() {
        return examManageMapper.findOnlyPaperId();
    }
}
