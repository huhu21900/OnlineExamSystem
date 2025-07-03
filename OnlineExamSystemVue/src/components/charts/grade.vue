<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
// 成绩统计页面
<template>
  <div id="grade">
    <el-card class="grade-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-data-analysis"></i>
        <span>成绩统计分析</span>
      </div>
      
      <div v-if="!isNull && !errorMsg" class="chart-container">
        <div class="chart-info">
          <div class="score-summary">
            <div class="summary-item" v-if="highestScore">
              <div class="label">最高分</div>
              <div class="value">{{ highestScore }}</div>
            </div>
            <div class="summary-item" v-if="averageScore">
              <div class="label">平均分</div>
              <div class="value">{{ averageScore.toFixed(2) }}</div>
            </div>
            <div class="summary-item" v-if="lowestScore">
              <div class="label">最低分</div>
              <div class="value">{{ lowestScore }}</div>
            </div>
          </div>
        </div>

        <div ref="box" class="chart-box"></div>
      </div>
      
      <div class="notFound" v-if="isNull">
        <el-empty 
          description="该考生未参加考试" 
          :image-size="200">
          <i class="el-icon-warning-outline empty-icon"></i>
        </el-empty>
      </div>
      
      <div v-if="errorMsg" class="error-container">
        <el-alert
          :title="errorMsg"
          type="error"
          show-icon
          center
          :closable="false">
        </el-alert>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "grade",
  data() {
    return {
      isNull: false, //原始数据
      tableDataX: [], //x轴数据 保存次数
      tableDataY: [], //y轴数据 保存分数
      errorMsg: "", // 错误信息
      highestScore: null, // 最高分
      lowestScore: null,  // 最低分
      averageScore: null  // 平均分
    };
  },
  mounted() {
    this.score();
  },
  methods: {
    score() {
      let studentId = this.$route.query.studentId;
      
      // 记录studentId的类型和值，用于调试
      console.log("studentId类型:", typeof studentId);
      console.log("studentId值:", studentId);
      
      // 确保studentId是字符串类型
      if (typeof studentId === 'number') {
        studentId = studentId.toString();
        console.log("转换后的studentId:", studentId);
      }
      
      if (!studentId) {
        this.errorMsg = "未获取到学生ID，请返回重试";
        this.isNull = true;
        return;
      }
      
      this.$axios(`/api/score/${studentId}`)
        .then((res) => {
          //根据学生Id查询成绩
          console.log("成绩查询结果:", res.data);
          
          if (res.data.code == 200) {
            let rootData = res.data.data;
            
            if (!rootData || rootData.length === 0) {
              this.isNull = true;
              return;
            }
            
            // 计算统计数据
            let scores = [];
            
            rootData.forEach((element, index) => {
              // 科目名称转列显示
              var insertIntervalString = (
                originStr,
                disNum = 10,
                insertStr = "\n"
              ) =>
                originStr.replace(
                  new RegExp("(.{" + disNum + "})", "g"),
                  "$1" + insertStr
                );
              // 每隔一个字符插入一个回车
              var subject = insertIntervalString(element.subject, 3, "\n");

              this.tableDataX.push(subject);
              this.tableDataY.push(element.etScore);
              scores.push(element.etScore);
            });
            
            // 计算统计值
            if (scores.length > 0) {
              this.highestScore = Math.max(...scores);
              this.lowestScore = Math.min(...scores);
              this.averageScore = scores.reduce((a, b) => a + b, 0) / scores.length;
            }
            
            this.$nextTick(() => {
              let boxDom = this.$refs["box"];
              if (boxDom) {
                let scoreCharts = this.$echarts.init(boxDom);
                let option = {
                  title: {
                    text: '学生成绩趋势',
                    left: 'center',
                    textStyle: {
                      fontSize: 16
                    }
                  },
                  tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                      type: 'shadow'
                    }
                  },
                  grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                  },
                  xAxis: {
                    type: "category",
                    data: this.tableDataX,
                    axisLabel: {
                      interval: 0,
                      rotate: 30
                    }
                  },
                  yAxis: {
                    type: "value",
                    name: '分数',
                    splitLine: {
                      lineStyle: {
                        type: 'dashed'
                      }
                    }
                  },
                  series: [
                    {
                      data: this.tableDataY,
                      type: "line",
                      smooth: true,
                      symbolSize: 8,
                      itemStyle: { 
                        normal: { 
                          color: '#409EFF',
                          label: { 
                            show: true,
                            formatter: '{c}分',
                            fontSize: 12,
                            fontWeight: 'bold'
                          } 
                        } 
                      },
                      lineStyle: {
                        width: 3,
                        shadowColor: 'rgba(0,0,0,0.3)',
                        shadowBlur: 10,
                        shadowOffsetY: 10
                      },
                      areaStyle: {
                        color: {
                          type: 'linear',
                          x: 0,
                          y: 0,
                          x2: 0,
                          y2: 1,
                          colorStops: [
                            {
                              offset: 0,
                              color: 'rgba(64, 158, 255, 0.7)'
                            },
                            {
                              offset: 1,
                              color: 'rgba(64, 158, 255, 0.1)'
                            }
                          ]
                        }
                      }
                    },
                  ],
                };
                scoreCharts.setOption(option);
                
                // 自适应窗口大小
                window.addEventListener('resize', function() {
                  scoreCharts.resize();
                });
              } else {
                console.error("未找到图表容器元素");
              }
            });
          } else {
            this.isNull = true;
            this.errorMsg = res.data.message || "获取成绩失败";
          }
        })
        .catch((error) => {
          console.error("获取成绩出错:", error);
          this.isNull = true;
          this.errorMsg = "获取成绩时发生错误，请稍后重试";
        });
    },
  },
};
</script>

<style lang="less" scoped>
#grade {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .grade-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .card-header {
      display: flex;
      align-items: center;
      font-size: 16px;
      font-weight: 600;
      
      i {
        margin-right: 8px;
        font-size: 18px;
        color: #409EFF;
      }
    }
    
    .chart-container {
      padding: 10px 0;
      
      .chart-info {
        margin-bottom: 20px;
        
        .score-summary {
          display: flex;
          justify-content: space-around;
          padding: 10px 0;
          
          .summary-item {
            text-align: center;
            padding: 10px 20px;
            border-radius: 6px;
            background-color: #f0f5ff;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            min-width: 100px;
            
            .label {
              font-size: 14px;
              color: #606266;
              margin-bottom: 5px;
            }
            
            .value {
              font-size: 22px;
              color: #409EFF;
              font-weight: 600;
            }
          }
        }
      }
      
      .chart-box {
        width: 100%;
        height: 400px;
        margin: 0 auto;
      }
    }
  }

  .notFound {
    padding: 40px 0;
    text-align: center;
    
    .empty-icon {
      font-size: 32px;
      color: #909399;
      margin-bottom: 10px;
    }
    
    ::v-deep .el-empty__description {
      color: #909399;
      font-size: 16px;
    }
  }
  
  .error-container {
    padding: 20px;
    
    ::v-deep .el-alert {
      padding: 15px 20px;
      
      .el-alert__title {
        font-size: 16px;
        line-height: 1.5;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  #grade {
    padding: 10px;
    
    .grade-card {
      .chart-container {
        .chart-info {
          .score-summary {
            flex-direction: column;
            align-items: center;
            
            .summary-item {
              margin-bottom: 10px;
              width: 80%;
            }
          }
        }
      }
    }
  }
}
</style>
