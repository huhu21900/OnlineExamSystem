package com.rabbiter.oes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.oes.entity.ApiResult;
import com.rabbiter.oes.entity.FillQuestion;
import com.rabbiter.oes.serviceimpl.FillQuestionServiceImpl;
import com.rabbiter.oes.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FillQuestionController {

    @Autowired
    private FillQuestionServiceImpl fillQuestionService;

    @GetMapping("/fillQuestionId")
    public ApiResult findOnlyQuestionId() {
        FillQuestion res = fillQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @GetMapping("/fillQuestion/{id}")
    public ApiResult findById(@PathVariable("id") Integer id) {
        FillQuestion res = fillQuestionService.findById(id);
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @DeleteMapping("/fillQuestion/{id}")
    public ApiResult deleteById(@PathVariable("id") Integer id) {
        int res = fillQuestionService.deleteById(id);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"删除成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"删除失败",res);
    }

    @PostMapping("/fillQuestion")
    public ApiResult add(@RequestBody FillQuestion fillQuestion) {
        int res = fillQuestionService.add(fillQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }

    @PostMapping("/editFillQuestion")
    public ApiResult update(@RequestBody FillQuestion fillQuestion) {
        int res = fillQuestionService.update(fillQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"修改成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"修改失败",res);
    }
}
