package com.rabbiter.oes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ApiResult;
import com.rabbiter.oes.entity.MultiQuestion;
import com.rabbiter.oes.serviceimpl.MultiQuestionServiceImpl;
import com.rabbiter.oes.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MultiQuestionController {

    @Autowired
    private MultiQuestionServiceImpl multiQuestionService;

    @GetMapping("/multiQuestionId")
    public ApiResult findOnlyQuestion() {
        MultiQuestion res = multiQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @GetMapping("/multiQuestionPage/{page}/{size}")
    public ApiResult findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        Page<MultiQuestion> multiQuestionPage = new Page<>(page,size);
        IPage<MultiQuestion> res = multiQuestionService.findAll(multiQuestionPage);
        return ApiResultHandler.buildApiResult(200,"分页查询所有选择题",res);
    }

    @GetMapping("/multiQuestion/{questionId}")
    public ApiResult findById(@PathVariable("questionId") Integer questionId) {
        MultiQuestion res = multiQuestionService.findById(questionId);
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @DeleteMapping("/multiQuestion/{questionId}")
    public ApiResult deleteById(@PathVariable("questionId") Integer questionId) {
        int res = multiQuestionService.deleteById(questionId);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"删除成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"删除失败",res);
    }

    @PostMapping("/MultiQuestion")
    public ApiResult add(@RequestBody MultiQuestion multiQuestion) {
        int res = multiQuestionService.add(multiQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }

    @PostMapping("/editMultiQuestion")
    public ApiResult update(@RequestBody MultiQuestion multiQuestion) {
        int res = multiQuestionService.update(multiQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"修改成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"修改失败",res);
    }
}
