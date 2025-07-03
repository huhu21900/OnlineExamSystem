<template>
  <div class="part">
    <el-card class="chart-card" shadow="hover">
      <div slot="header" class="card-header">
        <div class="header-left">
          <i class="el-icon-pie-chart"></i>
          <span>{{ name || '考试' }}成绩分段统计</span>
        </div>
        
        <div v-if="!isNull" class="header-right">
          <el-radio-group v-model="chartType" size="small" @change="switchChartType">
            <el-radio-button label="pie">饼图</el-radio-button>
            <el-radio-button label="bar">柱状图</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      
      <div v-if="!isNull" class="chart-container">
        <div class="statistics-summary">
          <div class="stat-card" v-for="(value, key) in category" :key="key">
            <div class="stat-title">{{ key }}</div>
            <div class="stat-value">{{ value }}人</div>
          </div>
        </div>
        
        <div class="chart-box" ref="box"></div>
        
        <div class="chart-analysis">
          <div class="analysis-title">
            <i class="el-icon-info"></i> 分数段分析
          </div>
          <div class="analysis-content">
            <p v-if="totalStudents > 0">
              本次考试共有 <span class="highlight">{{ totalStudents }}</span> 名学生参加，
              其中 <span class="highlight">{{ category['90分及以上'] }}</span> 名学生成绩优秀(90分以上)，
              占比 <span class="highlight">{{ ((category['90分及以上'] / totalStudents) * 100).toFixed(1) }}%</span>；
              <span class="highlight">{{ category['60分以下'] }}</span> 名学生未达到及格线，
              占比 <span class="highlight">{{ ((category['60分以下'] / totalStudents) * 100).toFixed(1) }}%</span>。
            </p>
          </div>
        </div>
      </div>
      
      <div v-if="isNull" class="empty-container">
        <el-empty 
          description="该门考试还没人参考，请提醒学生参加考试" 
          :image-size="200">
          <template #image>
            <i class="el-icon-data-analysis empty-icon"></i>
          </template>
          <el-button type="primary" @click="goBack">返回考试列表</el-button>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isNull: false, //是否有成绩标志位
      name: null,
      category: { //保存分数段
        '90分及以上': 0,
        '80-89分': 0,
        '70-79分': 0,
        '60-69分': 0,
        '60分以下': 0,
      },
      chartType: 'pie', // 默认为饼图
      colors: [
        '#91cc75', // 90分以上 - 绿色
        '#5470c6', // 80-89分 - 蓝色
        '#fac858', // 70-79分 - 黄色
        '#ee6666', // 60-69分 - 橙红色
        '#73c0de', // 60分以下 - 浅蓝色
      ],
      chartInstance: null
    }
  },
  computed: {
    totalStudents() {
      return Object.values(this.category).reduce((sum, count) => sum + count, 0);
    }
  },
  created() {
    this.getScoreInfo()
  },
  methods: {
    getScoreInfo() {
      let examCode = this.$route.query.examCode
      this.name = this.$route.query.source
      
      if (!examCode) {
        this.$message.error('未获取到考试编码，请返回重试');
        this.isNull = true;
        return;
      }
      
      this.$axios(`/api/scores/${examCode}`)
        .then(res => {
          let data = res.data.data
          if(data && data.length > 0) {
            // 初始化分数段数据
            for (let key in this.category) {
              this.category[key] = 0;
            }
            
            // 统计各分数段人数
            data.forEach(element => {
              switch(Math.floor(element.etScore / 10)) {
                case 10:
                case 9:
                  this.category["90分及以上"]++
                  break
                case 8:
                  this.category['80-89分']++
                  break
                case 7:
                  this.category["70-79分"]++
                  break
                case 6:
                  this.category['60-69分']++
                  break
                default:
                  this.category['60分以下']++
              }
            });
            
            this.$nextTick(() => {
              this.renderChart();
            });
          } else {
            this.isNull = true;
          }
        })
        .catch(error => {
          console.error('获取成绩数据失败:', error);
          this.$message.error('获取成绩数据失败，请稍后重试');
          this.isNull = true;
        });
    },
    
    renderChart() {
      let boxDom = this.$refs['box']
      if (!boxDom) return;
      
      // 销毁旧的图表实例
      if (this.chartInstance) {
        this.chartInstance.dispose();
      }
      
      this.chartInstance = this.$echarts.init(boxDom);
      
      let option;
      const chartData = Object.keys(this.category).map((key, index) => {
        return {
          value: this.category[key], 
          name: key,
          itemStyle: {
            color: this.colors[index]
          }
        };
      });
      
      if (this.chartType === 'pie') {
        option = {
          title: {
            text: `${this.name || '考试'}分数段分布`,
            left: 'center',
            top: 0,
            textStyle: {
              fontSize: 16,
              fontWeight: 'normal'
            }
          },
          tooltip: {
            trigger: 'item',
            formatter: "{b} <br/> {c}人 ({d}%)",
            textStyle: {
              fontSize: 14
            }
          },
          legend: {
            orient: 'horizontal',
            bottom: 0,
            data: Object.keys(this.category),
            textStyle: {
              fontSize: 12
            }
          },
          series: [
            {
              name: '分数段',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['50%', '50%'],
              avoidLabelOverlap: true,
              itemStyle: {
                borderRadius: 8,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: true,
                formatter: '{b}: {c}人 ({d}%)',
                fontSize: 12
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 14,
                  fontWeight: 'bold'
                },
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              data: chartData
            }
          ]
        };
      } else {
        option = {
          title: {
            text: `${this.name || '考试'}分数段分布`,
            left: 'center',
            top: 0,
            textStyle: {
              fontSize: 16,
              fontWeight: 'normal'
            }
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            },
            formatter: "{b}: {c}人"
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: Object.keys(this.category),
              axisTick: {
                alignWithLabel: true
              },
              axisLabel: {
                interval: 0,
                rotate: 15
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '人数',
              nameTextStyle: {
                padding: [0, 0, 0, 30]
              }
            }
          ],
          series: [
            {
              name: '人数',
              type: 'bar',
              barWidth: '60%',
              data: chartData,
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}人'
              },
              itemStyle: {
                borderRadius: [4, 4, 0, 0]
              }
            }
          ]
        };
      }
      
      this.chartInstance.setOption(option);
      
      // 自适应窗口大小
      window.addEventListener('resize', () => {
        this.chartInstance && this.chartInstance.resize();
      });
    },
    
    switchChartType() {
      this.$nextTick(() => {
        this.renderChart();
      });
    },
    
    goBack() {
      this.$router.push('/selectExamToPart');
    }
  },
  beforeDestroy() {
    // 组件销毁前清除事件监听
    window.removeEventListener('resize', this.chartInstance && this.chartInstance.resize);
    if (this.chartInstance) {
      this.chartInstance.dispose();
      this.chartInstance = null;
    }
  }
}
</script>

<style lang="less" scoped>
.part {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .chart-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .header-left {
        display: flex;
        align-items: center;
        
        i {
          margin-right: 8px;
          font-size: 18px;
          color: #409EFF;
        }
        
        span {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }
    }
    
    .chart-container {
      padding: 10px;
      
      .statistics-summary {
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;
        margin-bottom: 20px;
        padding: 10px 0;
        background-color: #f8f9fa;
        border-radius: 6px;
        
        .stat-card {
          padding: 10px 15px;
          margin: 5px;
          text-align: center;
          border-radius: 4px;
          transition: all 0.3s;
          
          &:hover {
            transform: translateY(-3px);
            box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
          }
          
          &:nth-child(1) {
            background-color: rgba(145, 204, 117, 0.2);
            border-left: 3px solid #91cc75;
          }
          
          &:nth-child(2) {
            background-color: rgba(84, 112, 198, 0.2);
            border-left: 3px solid #5470c6;
          }
          
          &:nth-child(3) {
            background-color: rgba(250, 200, 88, 0.2);
            border-left: 3px solid #fac858;
          }
          
          &:nth-child(4) {
            background-color: rgba(238, 102, 102, 0.2);
            border-left: 3px solid #ee6666;
          }
          
          &:nth-child(5) {
            background-color: rgba(115, 192, 222, 0.2);
            border-left: 3px solid #73c0de;
          }
          
          .stat-title {
            font-size: 14px;
            color: #606266;
            margin-bottom: 5px;
          }
          
          .stat-value {
            font-size: 20px;
            font-weight: 600;
          }
        }
      }
      
      .chart-box {
        width: 100%;
        height: 400px;
        margin: 0 auto 20px auto;
      }
      
      .chart-analysis {
        margin-top: 20px;
        padding: 15px;
        border-radius: 6px;
        background-color: #f8f9fa;
        
        .analysis-title {
          font-size: 15px;
          font-weight: 600;
          margin-bottom: 10px;
          color: #303133;
          
          i {
            color: #409EFF;
            margin-right: 5px;
          }
        }
        
        .analysis-content {
          color: #606266;
          line-height: 1.6;
          
          .highlight {
            color: #409EFF;
            font-weight: 600;
            padding: 0 3px;
          }
        }
      }
    }
    
    .empty-container {
      padding: 40px 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .empty-icon {
        font-size: 60px;
        color: #909399;
        margin-bottom: 15px;
      }
      
      ::v-deep .el-empty__description {
        color: #909399;
        font-size: 16px;
        margin-top: 15px;
        margin-bottom: 20px;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .part {
    padding: 10px;
    
    .chart-card {
      .chart-container {
        .statistics-summary {
          flex-direction: column;
          align-items: center;
          
          .stat-card {
            width: 80%;
            margin: 5px 0;
          }
        }
        
        .chart-box {
          height: 300px;
        }
      }
    }
  }
}
</style>


