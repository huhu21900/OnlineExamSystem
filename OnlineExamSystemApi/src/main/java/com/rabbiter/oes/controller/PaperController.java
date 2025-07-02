package com.rabbiter.oes.controller;

import com.rabbiter.oes.entity.*;
import com.rabbiter.oes.serviceimpl.FillQuestionServiceImpl;
import com.rabbiter.oes.serviceimpl.JudgeQuestionServiceImpl;
import com.rabbiter.oes.serviceimpl.MultiQuestionServiceImpl;
import com.rabbiter.oes.serviceimpl.PaperServiceImpl;
import com.rabbiter.oes.serviceimpl.ShortAnswerQuestionServiceImpl;
import com.rabbiter.oes.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PaperController {

    @Autowired
    private PaperServiceImpl paperService;

    @Autowired
    private JudgeQuestionServiceImpl judgeQuestionService;

    @Autowired
    private MultiQuestionServiceImpl multiQuestionService;

    @Autowired
    private FillQuestionServiceImpl fillQuestionService;
    
    @Autowired
    private ShortAnswerQuestionServiceImpl shortAnswerQuestionService;
    
    @GetMapping("/papers")
    public ApiResult<PaperManage> findAll() {
       ApiResult res =  ApiResultHandler.buildApiResult(200,"请求成功",paperService.findAll());
       return  res;
    }

    @GetMapping("/paper/{paperId}")
    public Map<Integer, List<?>> findById(@PathVariable("paperId") Integer paperId) {
        List<MultiQuestion> multiQuestionRes = multiQuestionService.findByIdAndType(paperId);   //选择题题库 1
        List<FillQuestion> fillQuestionsRes = fillQuestionService.findByIdAndType(paperId);     //填空题题库 2
        List<JudgeQuestion> judgeQuestionRes = judgeQuestionService.findByIdAndType(paperId);   //判断题题库 3
        List<ShortAnswerQuestion> shortAnswerQuestionRes = shortAnswerQuestionService.findByIdAndType(paperId); //简答题题库 4
        Map<Integer, List<?>> map = new HashMap<>();
        map.put(1,multiQuestionRes);
        map.put(2,fillQuestionsRes);
        map.put(3,judgeQuestionRes);
        map.put(4,shortAnswerQuestionRes);
        return  map;
    }

    @GetMapping("/practice/{source}")
    public Map<Integer, List<?>> findBySubject(@PathVariable("source") String subject) {
        List<MultiQuestion> multiQuestionRes = multiQuestionService.findBySubject(subject);   //选择题题库 1
        List<FillQuestion> fillQuestionsRes = fillQuestionService.findBySubject(subject);     //填空题题库 2
        List<JudgeQuestion> judgeQuestionRes = judgeQuestionService.findBySubject(subject);   //判断题题库 3
        List<ShortAnswerQuestion> shortAnswerQuestionRes = shortAnswerQuestionService.findBySubject(subject); //简答题题库 4
        Map<Integer, List<?>> map = new HashMap<>();
        map.put(1,multiQuestionRes);
        map.put(2,fillQuestionsRes);
        map.put(3,judgeQuestionRes);
        map.put(4,shortAnswerQuestionRes);
        return  map;
    }

    @PostMapping("/paperManage")
    public ApiResult add(@RequestBody PaperManage paperManage) {
        int res = paperService.add(paperManage);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }

    /**
     * 删除试卷中的某条试题
     *
     * @param paperId 试卷id
     * @param type 题目类型。1选择，2填空，3判断，4简答
     * @param questionId 题目id
     */
    @GetMapping("/paper/delete/{paperId}/{type}/{questionId}")
    public ApiResult delete(
            @PathVariable("paperId") String paperId,
            @PathVariable("type") String type,
            @PathVariable("questionId") String questionId
    ) {
        paperService.delete(paperId, type, questionId);
        return ApiResultHandler.buildApiResult(200,"删除成功", null);
    }
}
