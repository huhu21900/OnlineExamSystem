package com.rabbiter.oes.vo;

//题目模型
public class Item {

    private String subject;

    private Integer paperId;

    private Integer changeNumber;

    private Integer fillNumber;

    private Integer judgeNumber;
    
    private Integer shortAnswerNumber;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(Integer changeNumber) {
        this.changeNumber = changeNumber;
    }

    public Integer getFillNumber() {
        return fillNumber;
    }

    public void setFillNumber(Integer fillNumber) {
        this.fillNumber = fillNumber;
    }

    public Integer getJudgeNumber() {
        return judgeNumber;
    }

    public void setJudgeNumber(Integer judgeNumber) {
        this.judgeNumber = judgeNumber;
    }
    
    public Integer getShortAnswerNumber() {
        return shortAnswerNumber;
    }

    public void setShortAnswerNumber(Integer shortAnswerNumber) {
        this.shortAnswerNumber = shortAnswerNumber;
    }

    @Override
    public String toString() {
        return "Item{" +
                "subject='" + subject + '\'' +
                ", paperId=" + paperId +
                ", changeNumber=" + changeNumber +
                ", fillNumber=" + fillNumber +
                ", judgeNumber=" + judgeNumber +
                ", shortAnswerNumber=" + shortAnswerNumber +
                '}';
    }
}
