package com.rabbiter.oes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ApiResult;
import com.rabbiter.oes.entity.ShortAnswerQuestion;
import com.rabbiter.oes.serviceimpl.ShortAnswerQuestionServiceImpl;
import com.rabbiter.oes.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShortAnswerQuestionController {

    @Autowired
    private ShortAnswerQuestionServiceImpl shortAnswerQuestionService;

    @GetMapping("/shortAnswerQuestionId")
    public ApiResult findOnlyQuestionId() {
        ShortAnswerQuestion res = shortAnswerQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200, "查询成功", res);
    }
    
    @GetMapping("/shortAnswerQuestion/{id}")
    public ApiResult findById(@PathVariable("id") Integer id) {
        ShortAnswerQuestion res = shortAnswerQuestionService.findById(id);
        return ApiResultHandler.buildApiResult(200, "查询成功", res);
    }
    
    @DeleteMapping("/shortAnswerQuestion/{id}")
    public ApiResult deleteById(@PathVariable("id") Integer id) {
        int res = shortAnswerQuestionService.deleteById(id);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200, "删除成功", res);
        }
        return ApiResultHandler.buildApiResult(400, "删除失败", res);
    }

    @PostMapping("/shortAnswerQuestion")
    public ApiResult add(@RequestBody ShortAnswerQuestion shortAnswerQuestion) {
        int res = shortAnswerQuestionService.add(shortAnswerQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200, "添加成功", res);
        }
        return ApiResultHandler.buildApiResult(400, "添加失败", res);
    }

    @PostMapping("/editShortAnswerQuestion")
    public ApiResult update(@RequestBody ShortAnswerQuestion shortAnswerQuestion) {
        int res = shortAnswerQuestionService.update(shortAnswerQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200, "修改成功", res);
        }
        return ApiResultHandler.buildApiResult(400, "修改失败", res);
    }
} 