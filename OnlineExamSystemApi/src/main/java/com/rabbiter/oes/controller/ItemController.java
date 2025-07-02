package com.rabbiter.oes.controller;

import com.rabbiter.oes.entity.ApiResult;
import com.rabbiter.oes.entity.PaperManage;
import com.rabbiter.oes.service.PaperService;
import com.rabbiter.oes.serviceimpl.FillQuestionServiceImpl;
import com.rabbiter.oes.serviceimpl.JudgeQuestionServiceImpl;
import com.rabbiter.oes.serviceimpl.MultiQuestionServiceImpl;
import com.rabbiter.oes.serviceimpl.ShortAnswerQuestionServiceImpl;
import com.rabbiter.oes.util.ApiResultHandler;
import com.rabbiter.oes.vo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

    @Autowired
    MultiQuestionServiceImpl multiQuestionService;

    @Autowired
    FillQuestionServiceImpl fillQuestionService;

    @Autowired
    JudgeQuestionServiceImpl judgeQuestionService;
    
    @Autowired
    ShortAnswerQuestionServiceImpl shortAnswerQuestionService;

    @Autowired
    PaperService paperService;

    @PostMapping("/item")
    public ApiResult ItemController(@RequestBody Item item) {
        // 清空试卷
        paperService.deleteByPaperId(item.getPaperId());
        // 选择题
        Integer changeNumber = item.getChangeNumber() != null ? item.getChangeNumber() : 0;
        // 填空题
        Integer fillNumber = item.getFillNumber() != null ? item.getFillNumber() : 0;
        // 判断题
        Integer judgeNumber = item.getJudgeNumber() != null ? item.getJudgeNumber() : 0;
        // 简答题
        Integer shortAnswerNumber = item.getShortAnswerNumber() != null ? item.getShortAnswerNumber() : 0;
        //出卷id
        Integer paperId = item.getPaperId();
        
        // 检查是否至少有一种题型数量大于0
        if (changeNumber == 0 && fillNumber == 0 && judgeNumber == 0 && shortAnswerNumber == 0) {
            return ApiResultHandler.buildApiResult(400, "请至少设置一种题型的数量大于0", null);
        }

        // 数据库获取数据
        List<Integer> changeNumbers = changeNumber > 0 ? multiQuestionService.findBySubject(item.getSubject(), changeNumber) : new ArrayList<>();
        List<Integer> fills = fillNumber > 0 ? fillQuestionService.findBySubject(item.getSubject(), fillNumber) : new ArrayList<>();
        List<Integer> judges = judgeNumber > 0 ? judgeQuestionService.findBySubject(item.getSubject(), judgeNumber) : new ArrayList<>();
        List<Integer> shortAnswers = shortAnswerNumber > 0 ? shortAnswerQuestionService.findBySubject(item.getSubject(), shortAnswerNumber) : new ArrayList<>();

        if(changeNumber > 0 && (changeNumbers == null || changeNumbers.size() != changeNumber)){
            return ApiResultHandler.buildApiResult(400,"科目【" + item.getSubject() + "】题库【选择题】题目数量不足【" + changeNumber + "】，组卷失败",null);
        }
        if(fillNumber > 0 && (fills == null || fills.size() != fillNumber)) {
            return ApiResultHandler.buildApiResult(400,"科目【" + item.getSubject() + "】题库【填空题】题目数量不足【" + fillNumber + "】，组卷失败",null);
        }
        if(judgeNumber > 0 && (judges == null || judges.size() != judgeNumber)){
            return ApiResultHandler.buildApiResult(400,"科目【" + item.getSubject() + "】题库【判断题】题目数量不足【" + judgeNumber + "】，组卷失败",null);
        }
        if(shortAnswerNumber > 0 && (shortAnswers == null || shortAnswers.size() != shortAnswerNumber)){
            return ApiResultHandler.buildApiResult(400,"科目【" + item.getSubject() + "】题库【简答题】题目数量不足【" + shortAnswerNumber + "】，组卷失败",null);
        }

        // 符合组题条件，执行组题
        // 选择题
        for (Integer number : changeNumbers) {
            PaperManage paperManage = new PaperManage(paperId,1,number);
            int index = paperService.add(paperManage);
            if(index==0)
                return ApiResultHandler.buildApiResult(400,"选择题组卷保存失败",null);
        }

        // 填空题
        for (Integer fillNum : fills) {
            PaperManage paperManage = new PaperManage(paperId,2,fillNum);
            int index = paperService.add(paperManage);
            if(index==0)
                return ApiResultHandler.buildApiResult(400,"填空题题组卷保存失败",null);
        }
        // 判断题
        for (Integer judge : judges) {
            PaperManage paperManage = new PaperManage(paperId,3,judge);
            int index = paperService.add(paperManage);
            if(index==0)
                return ApiResultHandler.buildApiResult(400,"判断题题组卷保存失败",null);
        }
        // 简答题
        for (Integer shortAnswer : shortAnswers) {
            PaperManage paperManage = new PaperManage(paperId,4,shortAnswer);
            int index = paperService.add(paperManage);
            if(index==0)
                return ApiResultHandler.buildApiResult(400,"简答题题组卷保存失败",null);
        }


          return ApiResultHandler.buildApiResult(200,"试卷组卷成功",null);
    }
}
