<!--考生答题界面-->
<template>
    <div class="answer-container">
        <!--顶部信息栏-->
        <div class="exam-header">
            <div class="header-content">
                <div class="exam-title">
                    <i class="el-icon-menu" @click="slider_flag = !slider_flag"></i>
                    <i class="el-icon-document"></i>
                    <span>{{ examData.type }}-{{ examData.source }}</span>
                </div>
                <div class="exam-info">
                    <div class="user-info" @click="flag = !flag">
                        <i class="el-icon-user"></i>
                        <span>{{ userInfo.name }}</span>
                        <div class="user-detail" v-if="flag" @click.stop>
                            <div class="detail-item">
                                <span class="label">姓名：</span>
                                <span class="value">{{ userInfo.name }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="label">准考证号：</span>
                                <span class="value">{{ userInfo.id }}</span>
                            </div>
                        </div>
                    </div>
                    <div class="exam-timer" v-if="!isPractice">
                        <i class="el-icon-time"></i>
                        <span>倒计时：<b>{{ time }}</b> 分钟</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="exam-content">
            <!--左边题目编号区-->
            <transition name="slide-fade">
                <div class="question-nav" v-if="slider_flag">
                    <div class="nav-legend">
                        <div class="legend-item">
                            <span class="legend-icon current"></span>
                            <span class="legend-text">当前</span>
                        </div>
                        <div class="legend-item">
                            <span class="legend-icon unanswered"></span>
                            <span class="legend-text">未答</span>
                        </div>
                        <div class="legend-item">
                            <span class="legend-icon answered"></span>
                            <span class="legend-text">已答</span>
                        </div>
                        <div class="legend-item">
                            <span class="legend-icon marked"></span>
                            <span class="legend-text">标记</span>
                        </div>
                    </div>

                    <div class="question-list">
                        <div class="question-section" v-if="topicCount && topicCount[0] > 0">
                            <div class="section-title">
                                <i class="el-icon-s-operation"></i>
                                <span>选择题部分</span>
                            </div>
                            <div class="section-items">
                                <div 
                                    class="question-item" 
                                    v-for="(list, index1) in topic[1]" 
                                    :key="'choice-'+index1"
                                    @click="change(index1)"
                                    :class="{
                                        'current': index == index1 && currentType == 1,
                                        'answered': bg_flag && topic[1][index1].isClick == true,
                                        'marked': topic[1][index1].isMark == true
                                    }">
                                    {{ index1 + 1 }}
                                </div>
                            </div>
                        </div>

                        <div class="question-section" v-if="topicCount && topicCount[1] > 0">
                            <div class="section-title">
                                <i class="el-icon-edit"></i>
                                <span>填空题部分</span>
                            </div>
                            <div class="section-items">
                                <div 
                                    class="question-item" 
                                    v-for="(list, index2) in topic[2]" 
                                    :key="'fill-'+index2"
                                    @click="fill(index2)"
                                    :class="{
                                        'current': index == index2 && currentType == 2,
                                        'answered': fillAnswer[index2][3] == true,
                                        'marked': topic[2][index2].isMark == true
                                    }">
                                    {{ topicCount[0] + index2 + 1 }}
                                </div>
                            </div>
                        </div>

                        <div class="question-section" v-if="topicCount && topicCount[2] > 0">
                            <div class="section-title">
                                <i class="el-icon-check"></i>
                                <span>判断题部分</span>
                            </div>
                            <div class="section-items">
                                <div 
                                    class="question-item" 
                                    v-for="(list, index3) in topic[3]" 
                                    :key="'judge-'+index3"
                                    @click="judge(index3)"
                                    :class="{
                                        'current': index == index3 && currentType == 3,
                                        'answered': bg_flag && topic[3][index3].isClick == true,
                                        'marked': topic[3][index3].isMark == true
                                    }">
                                    {{ topicCount[0] + topicCount[1] + index3 + 1 }}
                                </div>
                            </div>
                        </div>

                        <div class="question-section" v-if="topicCount && topicCount[3] > 0">
                            <div class="section-title">
                                <i class="el-icon-document-checked"></i>
                                <span>简答题部分</span>
                            </div>
                            <div class="section-items">
                                <div 
                                    class="question-item" 
                                    v-for="(list, index4) in topic[4]" 
                                    :key="'short-'+index4"
                                    @click="shortAnswer(index4)"
                                    :class="{
                                        'current': index == index4 && currentType == 4,
                                        'answered': shortAnswerList[index4] != null && shortAnswerList[index4].trim() != '',
                                        'marked': topic[4][index4].isMark == true
                                    }">
                                    {{ topicCount[0] + topicCount[1] + topicCount[2] + index4 + 1 }}
                                </div>
                            </div>
                        </div>
                        
                        <el-button type="danger" class="finish-exam-btn" @click="finishExam()">
                            <i class="el-icon-finished"></i> 结束考试
                        </el-button>
                    </div>
                </div>
            </transition>

            <!--右边答题区-->
            <div class="question-content">
                <div class="content-header">
                    <div class="question-type">
                        <i class="el-icon-edit-outline"></i>
                        <span>{{ title }}</span>
                    </div>
                    <div class="question-progress">
                        <span>全卷共 {{ topicCount && topicCount.length > 0 ? topicCount.reduce((a, b) => a + b, 0) : 0 }} 题</span>
                    </div>
                </div>

                <div class="question-body">
                    <div class="question-text">
                        <span class="question-number">{{ number }}</span>
                        <div class="question-content-text" v-html="showQuestion"></div>
                    </div>

                    <!-- 选择题 -->
                    <div v-if="currentType == 1" class="choice-question">
                        <el-radio-group v-model="radio[index]" @change="getChangeLabel" class="option-group">
                            <el-radio :label="1" class="option-item">{{ showAnswer.answerA }}</el-radio>
                            <el-radio :label="2" class="option-item">{{ showAnswer.answerB }}</el-radio>
                            <el-radio :label="3" class="option-item">{{ showAnswer.answerC }}</el-radio>
                            <el-radio :label="4" class="option-item">{{ showAnswer.answerD }}</el-radio>
                        </el-radio-group>

                        <div class="question-analysis" v-if="isPractice && radio[index] != undefined">
                            <div class="analysis-title">
                                <el-tag type="success">正确答案：</el-tag>
                                <span class="correct-answer">{{ reduceAnswer.rightAnswer }}</span>
                            </div>
                            <div class="analysis-content">
                                <el-tag>题目解析：</el-tag>
                                <div class="analysis-text">
                                    {{ reduceAnswer.analysis == null ? "暂无解析" : reduceAnswer.analysis }}
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 填空题 -->
                    <div class="fill-question" v-if="currentType == 2">
                        <div v-for="(item, currentIndex) in part" :key="currentIndex" class="fill-item">
                            <el-input 
                                placeholder="请填在此处" 
                                v-model="fillAnswer[index][currentIndex]" 
                                clearable
                                @blur="fillBG"
                                class="fill-input">
                            </el-input>
                        </div>

                        <div class="question-analysis" v-if="isPractice && fillAnswer[index][0] != undefined">
                            <div class="analysis-title">
                                <el-tag type="success">正确答案：</el-tag>
                                <span class="correct-answer">{{ topic[2][index].answer }}</span>
                            </div>
                            <div class="analysis-content">
                                <el-tag>题目解析：</el-tag>
                                <div class="analysis-text">
                                    {{ topic[2][index].analysis == null ? "暂无解析" : topic[2][index].analysis }}
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 判断题 -->
                    <div class="judge-question" v-if="currentType == 3">
                        <el-radio-group v-model="judgeAnswer[index]" @change="getJudgeLabel" class="judge-options">
                            <el-radio :label="1" class="judge-option">
                                <i class="el-icon-check"></i> 正确
                            </el-radio>
                            <el-radio :label="2" class="judge-option">
                                <i class="el-icon-close"></i> 错误
                            </el-radio>
                        </el-radio-group>

                        <div class="question-analysis" v-if="isPractice && judgeAnswer[index] != undefined">
                            <div class="analysis-title">
                                <el-tag type="success">正确答案：</el-tag>
                                <span class="correct-answer">{{ topic[3][index].answer == "T" ? "正确" : "错误" }}</span>
                            </div>
                            <div class="analysis-content">
                                <el-tag>题目解析：</el-tag>
                                <div class="analysis-text">
                                    {{ topic[3][index].analysis == null ? "暂无解析" : topic[3][index].analysis }}
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 简答题 -->
                    <div class="short-answer-question" v-if="currentType == 4">
                        <el-input 
                            type="textarea" 
                            v-model="shortAnswerList[index]" 
                            placeholder="请在此处作答" 
                            :rows="8" 
                            resize="none" 
                            @blur="shortAnswerBG"
                            class="short-answer-input">
                        </el-input>

                        <div class="question-analysis" v-if="isPractice && shortAnswerList[index] != undefined">
                            <div class="analysis-title">
                                <el-tag type="success">参考答案：</el-tag>
                                <div class="correct-answer">{{ topic[4][index].answer }}</div>
                            </div>
                            <div class="analysis-content">
                                <el-tag>题目解析：</el-tag>
                                <div class="analysis-text">
                                    {{ topic[4][index].analysis == null ? "暂无解析" : topic[4][index].analysis }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="question-actions">
                    <el-button @click="previous()" class="action-btn prev-btn" type="primary" plain>
                        <i class="el-icon-arrow-left"></i> 上一题
                    </el-button>
                    <el-button @click="mark()" class="action-btn mark-btn" type="warning" plain>
                        <i class="el-icon-star-off"></i> 标记
                    </el-button>
                    <el-button @click="next()" class="action-btn next-btn" type="primary" plain>
                        下一题 <i class="el-icon-arrow-right"></i>
                    </el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from "vuex";
export default {
    data() {
        return {
            startTime: null, //考试开始时间
            endTime: null, //考试结束时间
            time: null, //考试持续时间
            reduceAnswer: [], //vue官方不支持3层以上数据嵌套,如嵌套则会数据渲染出现问题,此变量直接接收3层嵌套时的数据。
            answerScore: 0, //答题总分数
            bg_flag: false, //已答标识符,已答改变背景色
            isFillClick: false, //选择题是否点击标识符
            slider_flag: true, //左侧显示隐藏标识符
            flag: false, //个人信息显示隐藏标识符
            currentType: 1, //当前题型类型  1--选择题  2--填空题  3--判断题  4--简答题
            radio: [], //保存考生所有选择题的选项
            title: "请选择正确的选项",
            index: 0, //全局index
            userInfo: {
                //用户信息
                name: null,
                id: null,
            },
            topicCount: [], //每种类型题目的总数
            score: [], //每种类型分数的总数
            examData: {
                //考试信息
                // source: null,
                // totalScore: null,
            },
            topic: {
                //试卷信息
            },
            showQuestion: [], //当前显示题目信息
            showAnswer: {}, //当前题目对应的答案选项
            number: 1, //题号
            part: null, //填空题的空格数量
            fillAnswer: [[]], //二维数组保存所有填空题答案
            judgeAnswer: [], //保存所有判断题答案
            shortAnswerList: [], //保存所有简答题答案
            topic1Answer: [], //学生选择题作答编号,
            rightAnswer: "",
            timer: "",
        };
    },
    created() {
        this.getCookies();
        if (!this.isPractice) {
            this.getExamData();
            this.showTime();
        } else {
            this.getPracticeExamData();
        }
    },
    methods: {
        finishExam() {
            if (this.isPractice) {
                // 练习交卷
                this.$confirm("结束本次练习？", "友情提示", {
                    confirmButtonText: "立即交卷",
                    cancelButtonText: "继续练习",
                    type: "warning",
                })
                    .then(() => {
                        this.commit();
                    })
                    .catch(() => { });
            } else if (this.time > 0) {
                // 提前交卷
                this.$confirm("考试结束时间未到,是否提前交卷", "友情提示", {
                    confirmButtonText: "提交",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        this.commit();
                    })
                    .catch(() => { });
            }
        },
        getTime(date) {
            //日期格式化
            let year = date.getFullYear();
            let month =
                date.getMonth() + 1 < 10
                    ? "0" + (date.getMonth() + 1)
                    : date.getMonth() + 1;
            let day =
                date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            let hours =
                date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            let minutes =
                date.getMinutes() < 10
                    ? "0" + date.getMinutes()
                    : date.getMinutes();
            let seconds =
                date.getSeconds() < 10
                    ? "0" + date.getSeconds()
                    : date.getSeconds();
            // 拼接
            return (
                year +
                "-" +
                month +
                "-" +
                day +
                " " +
                hours +
                ":" +
                minutes +
                ":" +
                seconds
            );
        },
        getCookies() {
            //获取cookie
            this.userInfo.name = this.$cookies.get("cname");
            this.userInfo.id = this.$cookies.get("cid");
        },
        calcuScore() {
            //计算答题分数
        },
        getPracticeExamData() {
            //获取当前练习所有信息
            let date = new Date();
            this.startTime = this.getTime(date);
            let examCode = this.$route.query.examCode; //获取路由传递过来的试卷编号
            this.$axios(`/api/exam/${examCode}`).then((res) => {
                //通过examCode请求试卷详细信息
                this.examData = { ...res.data.data }; //获取考试详情
                this.index = 0;
                this.$axios(`/api/practice/${this.examData.source}`).then((res) => {
                    //通过examCode获取练习题目信息（根据科目名称读取所有试题）
                    this.topic = { ...res.data };
                    let reduceAnswer = this.topic[1][this.index];
                    this.reduceAnswer = reduceAnswer;
                    let keys = Object.keys(this.topic); //对象转数组
                    keys.forEach((e) => {
                        let data = this.topic[e];
                        this.topicCount.push(data.length);
                        let currentScore = 0;
                        for (let i = 0; i < data.length; i++) {
                            //循环每种题型,计算出总分
                            currentScore += data[i].score;
                        }
                        this.score.push(currentScore); //把每种题型总分存入score
                    });
                    let len = this.topicCount[1];
                    let father = [];
                    for (let i = 0; i < len; i++) {
                        //根据填空题数量创建二维空数组存放每道题答案
                        let children = [null, null, null, null];
                        father.push(children);
                    }
                    this.fillAnswer = father;
                    let dataInit = this.topic[1];
                    this.number = 1;
                    this.showQuestion = dataInit[0].question;
                    this.showAnswer = dataInit[0];
                });
            });
        },
        getExamData() {
            //获取当前试卷所有信息
            let date = new Date();
            this.startTime = this.getTime(date);
            let examCode = this.$route.query.examCode; //获取路由传递过来的试卷编号
            this.$axios(`/api/exam/${examCode}`).then((res) => {
                //通过examCode请求试卷详细信息
                this.examData = { ...res.data.data }; //获取考试详情
                this.index = 0;
                this.time = this.examData.totalTime; //获取分钟数
                let paperId = this.examData.paperId;
                this.$axios(`/api/paper/${paperId}`).then((res) => {
                    //通过paperId获取试题题目信息
                    this.topic = { ...res.data };
                    let reduceAnswer = this.topic[1][this.index];
                    this.reduceAnswer = reduceAnswer;
                    let keys = Object.keys(this.topic); //对象转数组
                    keys.forEach((e) => {
                        let data = this.topic[e];
                        this.topicCount.push(data.length);
                        let currentScore = 0;
                        for (let i = 0; i < data.length; i++) {
                            //循环每种题型,计算出总分
                            currentScore += data[i].score;
                        }
                        this.score.push(currentScore); //把每种题型总分存入score
                    });
                    let len = this.topicCount[1];
                    let father = [];
                    for (let i = 0; i < len; i++) {
                        //根据填空题数量创建二维空数组存放每道题答案
                        let children = [null, null, null, null];
                        father.push(children);
                    }
                    this.fillAnswer = father;
                    let dataInit = this.topic[1];
                    this.number = 1;
                    this.showQuestion = dataInit[0].question;
                    this.showAnswer = dataInit[0];
                });
            });
        },
        change(index) {
            //选择题
            this.index = index;
            let reduceAnswer = this.topic[1][this.index];
            this.reduceAnswer = reduceAnswer;
            this.isFillClick = true;
            this.currentType = 1;
            let len = this.topic[1].length;
            if (this.index < len) {
                if (this.index <= 0) {
                    this.index = 0;
                }
                console.log(`总长度${len}`);
                console.log(`当前index:${index}`);
                this.title = "请选择正确的选项";
                let Data = this.topic[1];
                // console.log(Data)
                this.showQuestion = Data[this.index].question; //获取题目信息
                this.showAnswer = Data[this.index];
                this.number = this.index + 1;
            } else if (this.index >= len) {
                this.index = 0;
                this.fill(this.index);
            }
        },
        fillBG() {
            //填空题已答题目 如果已答该题目,设置第四个元素为true为标识符
            if (this.fillAnswer[this.index][0] != null) {
                this.fillAnswer[this.index][3] = true;
            }
        },
        fill(index) {
            //填空题
            let len = this.topic[2].length;
            this.currentType = 2;
            this.index = index;
            if (index < len) {
                if (index < 0) {
                    index = this.topic[1].length - 1;
                    this.change(index);
                } else {
                    this.title = "请在横线处填写答案";
                    let Data = this.topic[2];
                    console.log(Data);
                    this.showQuestion = Data[index].question; //获取题目信息
                    let part = this.showQuestion.split("()").length - 1; //根据题目中括号的数量确定填空横线数量
                    this.part = part;
                    this.number = this.topicCount[0] + index + 1;
                }
            } else if (index >= len) {
                this.index = 0;
                this.judge(this.index);
            }
        },
        judge(index) {
            //判断题
            let len = this.topic[3].length;
            this.currentType = 3;
            this.index = index;
            if (this.index < len) {
                if (this.index < 0) {
                    this.index = this.topic[2].length - 1;
                    this.fill(this.index);
                } else {
                    console.log(`总长度${len}`);
                    console.log(`当前index:${this.index}`);
                    this.title = "请作出正确判断";
                    let Data = this.topic[3];
                    console.log(Data);
                    this.showQuestion = Data[index].question; //获取题目信息
                    this.number =
                        this.topicCount[0] + this.topicCount[1] + index + 1;
                }
            } else if (this.index >= len) {
                if (this.topic[4] && this.topic[4].length > 0) {
                    this.index = 0;
                    this.shortAnswer(this.index);
                } else {
                    this.index = 0;
                    this.change(this.index);
                }
            }
        },
        getChangeLabel(val) {
            //获取选择题作答选项
            this.radio[this.index] = val; //当前选择的序号
            if (val) {
                let data = this.topic[1];
                this.bg_flag = true;
                data[this.index]["isClick"] = true;
            }
            /* 保存学生答题选项 */
            this.topic1Answer[this.index] = val;
        },
        getJudgeLabel(val) {
            //获取判断题作答选项
            this.judgeAnswer[this.index] = val;
            if (val) {
                let data = this.topic[3];
                this.bg_flag = true;
                data[this.index]["isClick"] = true;
            }
        },
        previous() {
            //上一题
            this.index--;
            switch (this.currentType) {
                case 1:
                    this.change(this.index);
                    break;
                case 2:
                    this.fill(this.index);
                    break;
                case 3:
                    this.judge(this.index);
                    break;
                case 4:
                    this.shortAnswer(this.index);
                    break;
            }
        },
        next() {
            //下一题
            this.index++;
            switch (this.currentType) {
                case 1:
                    this.change(this.index);
                    break;
                case 2:
                    this.fill(this.index);
                    break;
                case 3:
                    this.judge(this.index);
                    break;
                case 4:
                    this.shortAnswer(this.index);
                    break;
            }
        },
        mark() {
            //标记功能
            switch (this.currentType) {
                case 1:
                    this.topic[1][this.index]["isMark"] = true; //选择题标记
                    break;
                case 2:
                    this.topic[2][this.index]["isMark"] = true; //填空题标记
                    break;
                case 3:
                    this.topic[3][this.index]["isMark"] = true; //判断题标记
                    break;
                case 4:
                    this.topic[4][this.index]["isMark"] = true; //简答题标记
                    break;
            }
        },
        commit() {
            //答案提交计算分数
            /* 计算选择题总分 */
            let topic1Answer = this.topic1Answer;
            let finalScore = 0;
            topic1Answer.forEach((element, index) => {
                //循环每道选择题根据选项计算分数
                let right = null;
                if (element != null) {
                    switch (
                    element //选项1,2,3,4 转换为 "A","B","C","D"
                    ) {
                        case 1:
                            right = "A";
                            break;
                        case 2:
                            right = "B";
                            break;
                        case 3:
                            right = "C";
                            break;
                        case 4:
                            right = "D";
                    }
                    if (right == this.topic[1][index].rightAnswer) {
                        // 当前选项与正确答案对比
                        finalScore += this.topic[1][index].score; // 计算总分数
                    }
                }
            });

            /**计算填空题总分 */
            let fillAnswer = this.fillAnswer;
            console.log(this.topic[2])
            fillAnswer.forEach((element, index) => {
                //此处index和 this.index数据不一致，注意
                element.forEach((inner) => {
                    if (this.topic[2][index].answer.includes(inner)) {
                        //判断填空答案是否与数据库一致
                        finalScore += this.topic[2][index].score;
                    }
                });
            });
            /** 计算判断题总分 */
            let topic3Answer = this.judgeAnswer;
            topic3Answer.forEach((element, index) => {
                let right = null;
                switch (element) {
                    case 1:
                        right = "T";
                        break;
                    case 2:
                        right = "F";
                }
                if (right == this.topic[3][index].answer) {
                    // 当前选项与正确答案对比
                    finalScore += this.topic[3][index].score; // 计算总分数
                }
            });
            /** 计算简答题总分 - 简答题需要老师手动评分，这里不自动计算分数 */
            // 简答题的答案会被提交到数据库，但不参与自动计分，需要教师手动评分
            
            // 提交分数到数据库
            let date = new Date();
            this.endTime = this.getTime(date);
            if (this.isPractice) {
                // 练习，不提交到数据库
                this.$router.push({
                    path: "/studentScore",
                    query: {
                        score: finalScore,
                        startTime: this.startTime,
                        endTime: this.endTime,
                    },
                });
                return;
            }

            let answerDate = this.endTime.substr(0, 10);
            //提交成绩信息
            this.$axios({
                url: "/api/score",
                method: "post",
                data: {
                    examCode: this.examData.examCode, //考试编号
                    studentId: this.userInfo.id, //学号
                    subject: this.examData.source, //课程名称
                    ptScore: ((finalScore / this.examData.totalScore) * 100 >= 60) ? 1 : 0, //是否及格
                    etScore: finalScore, //答题成绩
                    score: this.examData.totalScore, //试卷总成绩
                    answerDate: answerDate, //答题日期
                },
            }).then((res) => {

                console.log("a006")
                if (res.data.code == 200) {
                    this.$router.push({
                        path: "/studentScore",
                        query: {
                            score: finalScore,
                            startTime: this.startTime,
                            endTime: this.endTime,
                        },
                    });

                    console.log("a007")
                }
            });
        },
        showTime() {
            //倒计时
            this.timer = setInterval(() => {
                this.time -= 1;
                if (this.time == 10) {
                    this.$message({
                        showClose: true,
                        type: "error",
                        message: "考生注意,考试时间还剩10分钟！！！",
                    });
                }

                if (this.time <= 0) {
                    this.$message({
                        showClose: true,
                        type: "error",
                        message: "考试时间到，系统自动提交试卷",
                    });
                    this.commit();
                }
            }, 1000 * 60);
        },
        shortAnswer(index) {
            //简答题
            let len = this.topic[4].length;
            this.currentType = 4;
            this.index = index;
            if (this.index < len) {
                if (this.index < 0) {
                    this.index = this.topic[3].length - 1;
                    this.judge(this.index);
                } else {
                    console.log(`总长度${len}`);
                    console.log(`当前index:${this.index}`);
                    this.title = "请作答简答题";
                    let Data = this.topic[4];
                    console.log(Data);
                    this.showQuestion = Data[index].question; //获取题目信息
                    this.number =
                        this.topicCount[0] + this.topicCount[1] + this.topicCount[2] + index + 1;
                }
            } else if (this.index >= len) {
                this.index = 0;
                this.change(this.index);
            }
        },
        shortAnswerBG() {
            //简答题已答题目标记
            if (this.shortAnswerList[this.index] != null && this.shortAnswerList[this.index].trim() != '') {
                this.topic[4][this.index]["isClick"] = true;
            }
        },
    },
    beforeDestroy() {
        // 清除计时器
        clearInterval(this.timer);
    },
    computed: mapState(["isPractice"]),
};
</script>

<style lang="less" scoped>
.answer-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;

  .exam-header {
    background-color: #409EFF;
    color: #fff;
    padding: 15px 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      max-width: 1400px;
      margin: 0 auto;
      
      .exam-title {
        display: flex;
        align-items: center;
        font-size: 20px;
        font-weight: 600;
        
        i {
          margin-right: 10px;
          font-size: 22px;
          cursor: pointer;
          
          &:first-child {
            margin-right: 15px;
            transition: all 0.3s;
            
            &:hover {
              transform: rotate(90deg);
            }
          }
        }
      }
      
      .exam-info {
        display: flex;
        align-items: center;
        
        .user-info {
          display: flex;
          align-items: center;
          cursor: pointer;
          margin-right: 25px;
          position: relative;
          
          i {
            margin-right: 5px;
          }
          
          .user-detail {
            position: absolute;
            top: 40px;
            right: 0;
            background-color: #fff;
            color: #606266;
            padding: 15px;
            border-radius: 4px;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
            z-index: 100;
            width: 200px;
            
            .detail-item {
              margin-bottom: 10px;
              
              &:last-child {
                margin-bottom: 0;
              }
              
              .label {
                font-weight: 600;
                margin-right: 5px;
              }
            }
          }
        }
        
        .exam-timer {
          display: flex;
          align-items: center;
          font-size: 16px;
          
          i {
            margin-right: 5px;
            font-size: 18px;
          }
          
          b {
            color: #fff;
            margin: 0 3px;
          }
        }
      }
    }
  }

  .exam-content {
    flex: 1;
    display: flex;
    max-width: 1400px;
    margin: 0 auto;
    padding: 20px;
    width: 100%;
    box-sizing: border-box;

    .question-nav {
      width: 280px;
      margin-right: 20px;
      
      .nav-legend {
        background-color: #fff;
        border-radius: 4px;
        padding: 15px;
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        
        .legend-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          
          .legend-icon {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            margin-bottom: 5px;
            
            &.current {
              border: 2px solid #409EFF;
            }
            
            &.unanswered {
              border: 1px solid #dcdfe6;
              background-color: #fff;
            }
            
            &.answered {
              background-color: #409EFF;
            }
            
            &.marked {
              position: relative;
              border: 1px solid #dcdfe6;
              background-color: #fff;
              
              &:after {
                content: '';
                position: absolute;
                top: -2px;
                right: -2px;
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #E6A23C;
              }
            }
          }
          
          .legend-text {
            font-size: 12px;
            color: #606266;
          }
        }
      }
      
      .question-list {
        background-color: #fff;
        border-radius: 4px;
        padding: 15px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        
        .question-section {
          margin-bottom: 20px;
          
          .section-title {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            color: #303133;
            
            i {
              margin-right: 8px;
              color: #409EFF;
            }
          }
          
          .section-items {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            
            .question-item {
              width: 35px;
              height: 35px;
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 14px;
              border: 1px solid #dcdfe6;
              background-color: #fff;
              cursor: pointer;
              transition: all 0.3s;
              position: relative;
              
              &:hover {
                border-color: #409EFF;
                color: #409EFF;
              }
              
              &.current {
                border: 2px solid #409EFF;
                color: #409EFF;
                font-weight: 600;
              }
              
              &.answered {
                background-color: #409EFF;
                border-color: #409EFF;
                color: #fff;
              }
              
              &.marked:after {
                content: '';
                position: absolute;
                top: -3px;
                right: -3px;
                width: 10px;
                height: 10px;
                border-radius: 50%;
                background-color: #E6A23C;
              }
            }
          }
        }
        
        .finish-exam-btn {
          width: 100%;
          margin-top: 15px;
          font-size: 16px;
        }
      }
    }

    .question-content {
      flex: 1;
      background-color: #fff;
      border-radius: 4px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      display: flex;
      flex-direction: column;
      
      .content-header {
        padding: 15px 20px;
        border-bottom: 1px solid #ebeef5;
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .question-type {
          font-size: 18px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;
          
          i {
            margin-right: 10px;
            color: #409EFF;
          }
        }
        
        .question-progress {
          color: #606266;
          font-size: 14px;
        }
      }
      
      .question-body {
        flex: 1;
        padding: 20px;
        overflow-y: auto;
        
        .question-text {
          margin-bottom: 25px;
          
          .question-number {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            width: 28px;
            height: 28px;
            background-color: #409EFF;
            color: #fff;
            border-radius: 4px;
            font-weight: 600;
            margin-right: 10px;
            font-size: 16px;
            vertical-align: middle;
          }
          
          .question-content-text {
            display: inline;
            font-size: 16px;
            line-height: 1.5;
            color: #303133;
          }
        }
        
        .choice-question, .fill-question, .judge-question, .short-answer-question {
          padding: 10px 0;
        }
        
        .option-group {
          display: flex;
          flex-direction: column;
          
          .option-item {
            margin-bottom: 15px;
            padding: 10px 15px;
            border-radius: 4px;
            transition: all 0.3s;
            
            &:hover {
              background-color: #ecf5ff;
            }
          }
        }
        
        .fill-item {
          margin-bottom: 15px;
          
          .fill-input {
            width: 100%;
          }
        }
        
        .judge-options {
          display: flex;
          
          .judge-option {
            margin-right: 30px;
            padding: 10px 20px;
            border-radius: 4px;
            transition: all 0.3s;
            
            &:hover {
              background-color: #ecf5ff;
            }
          }
        }
        
        .short-answer-input {
          margin-bottom: 15px;
        }
        
        .question-analysis {
          margin-top: 25px;
          padding: 15px;
          background-color: #f8f8f8;
          border-radius: 4px;
          border-left: 4px solid #67c23a;
          
          .analysis-title {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            
            .correct-answer {
              margin-left: 10px;
              color: #67c23a;
              font-weight: 600;
            }
          }
          
          .analysis-content {
            .analysis-text {
              margin-top: 10px;
              padding: 10px;
              background-color: #fff;
              border-radius: 4px;
              color: #606266;
              line-height: 1.6;
            }
          }
        }
      }
      
      .question-actions {
        padding: 15px 20px;
        border-top: 1px solid #ebeef5;
        display: flex;
        justify-content: center;
        
        .action-btn {
          margin: 0 15px;
          padding: 12px 25px;
          font-size: 16px;
          
          &.mark-btn {
            padding: 12px 35px;
          }
        }
      }
    }
  }
}

.slide-fade-enter-active, .slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter, .slide-fade-leave-to {
  transform: translateX(-20px);
  opacity: 0;
}
</style>
