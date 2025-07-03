// 给我留言页面
<template>
  <div class="message-container">
    <div class="page-header">
      <i class="el-icon-chat-line-round"></i>
      <span>交流区</span>
    </div>

    <div class="message-wrapper">
      <div class="message-form">
        <div class="form-header">
          <i class="el-icon-edit-outline"></i>
          <span>发布留言</span>
        </div>
        
        <div class="form-content">
          <div class="title-input">
            <el-input 
              placeholder="留言标题" 
              v-model="title" 
              prefix-icon="el-icon-edit" 
              clearable>
            </el-input>
          </div>
          
          <div class="content-input">
            <el-input 
              type="textarea" 
              :rows="4" 
              placeholder="留言内容" 
              v-model="content" 
              resize="none"
              clearable>
            </el-input>
          </div>
          
          <div class="submit-btn">
            <el-button type="primary" @click="submit()" icon="el-icon-s-promotion">
              提交留言
            </el-button>
          </div>
        </div>
      </div>

      <div class="message-list-container">
        <div class="list-header">
          <i class="el-icon-s-comment"></i>
          <span>留言列表</span>
        </div>
        
        <div class="message-list" v-if="msg.length > 0">
          <div 
            class="message-item" 
            v-for="(data, index) in msg" 
            :key="index"
            @mouseenter="enter(index)" 
            @mouseleave="leave(index)">
            
            <div class="message-header">
              <div class="user-info">
                <i class="el-icon-user"></i>
                <span class="message-title">{{ data.title }}</span>
              </div>
              <div class="message-date">
                <i class="el-icon-date"></i>
                <span>{{ data.time }}</span>
              </div>
            </div>
            
            <div class="message-body">
              <p>{{ data.content }}</p>
            </div>
            
            <div class="message-replies" v-if="data.replays && data.replays.length > 0">
              <div class="reply-item" v-for="(replayData, index2) in data.replays" :key="index2">
                <i class="el-icon-chat-dot-round"></i>
                <span>{{ replayData.replay }}</span>
              </div>
            </div>
            
            <div class="message-actions">
              <el-button 
                type="text" 
                size="small" 
                @click="replay(data.id)" 
                class="reply-btn">
                <i class="el-icon-s-promotion"></i> 回复
              </el-button>
            </div>
          </div>
        </div>
        
        <div class="empty-message" v-else>
          <i class="el-icon-chat-dot-square"></i>
          <p>暂无留言数据</p>
        </div>

        <div class="message-pagination">
          <el-pagination 
            @size-change="handleSizeChange" 
            @current-change="handleCurrentChange"
            :current-page="pagination.current" 
            :page-sizes="[4, 6, 8, 10]" 
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" 
            :total="pagination.total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  // name: 'message'
  data() {
    return {
      flag: false,
      current: 0,
      title: "",
      content: "",
      pagination: { //分页后的留言列表
        current: 1, //当前页
        total: null, //记录条数
        size: 4 //每页条数
      },
      msg: []
    }
  },
  created() {
    this.getMsg()
  },
  // watch: {

  // },
  methods: {
    getMsg() {
      this.$axios(`/api/messages/${this.pagination.current}/${this.pagination.size}`).then(res => {
        let status = res.data.code
        if (status == 200) {
          this.msg = res.data.data.records
          this.pagination = res.data.data
        }
      })
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.pagination.size = val
      this.getMsg()
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getMsg()
    },
    // formatTime(date) { //日期格式化
    //   let year = date.getFullYear()
    //   let month= date.getMonth()+ 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
    //   let day=date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    //   let hours=date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    //   let minutes=date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    //   let seconds=date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    //   // 拼接
    //   return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
    // },
    submit() {
      let date = new Date()
      if (this.title.length == 0 || this.content.length == 0) { //非空判断
        this.$message({
          type: 'error',
          message: '留言标题或内容不能为空',
        })
      } else {
        this.$axios({
          url: "/api/message",
          method: "post",
          data: {
            title: this.title,
            content: this.content,
            time: date
          }
        }).then(res => {
          let code = res.data.code
          if (code == 200) {
            this.$message({
              type: "success",
              message: "留言成功"
            })
          }
          this.getMsg()
        })
      }
      this.title = ""
      this.content = ""
      this.getMsg()
    },
    replay(messageId) { //回复留言功能
      this.$prompt('回复留言', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
        inputErrorMessage: '回复不能为空'
      }).then(({ value }) => {
        let date = new Date()
        console.log(messageId)
        this.$axios({
          url: '/api/replay',
          method: 'post',
          data: {
            replay: value,
            replayTime: date,
            messageId: messageId
          }
        }).then(res => {
          this.getMsg()
        })
        this.$message({
          type: 'success',
          message: '回复成功'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    enter(index) {
      this.flag = true
      this.current = index
    },
    leave(index) {
      this.flag = false;
      this.current = index;
    }
  }
}
</script>

<style lang="less" scoped>
.message-container {
  width: 1100px;
  margin: 0 auto;

  .page-header {
    display: flex;
    align-items: center;
    margin: 30px 0;
    font-size: 28px;
    font-weight: 600;
    color: #303133;
    
    i {
      font-size: 32px;
      margin-right: 12px;
      color: #409EFF;
    }
  }

  .message-wrapper {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }

  .message-form {
    margin-bottom: 30px;
    border-bottom: 1px solid #EBEEF5;
    padding-bottom: 20px;
    
    .form-header {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
      font-size: 22px;
      font-weight: 500;
      
      i {
        font-size: 24px;
        margin-right: 10px;
        color: #409EFF;
      }
    }
    
    .form-content {
      .title-input {
        margin-bottom: 15px;
      }
      
      .content-input {
        margin-bottom: 20px;
      }
      
      .submit-btn {
        display: flex;
        justify-content: flex-end;
        
        .el-button {
          padding: 12px 25px;
          font-size: 16px;
        }
      }
    }
  }

  .message-list-container {
    .list-header {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
      font-size: 22px;
      font-weight: 500;
      
      i {
        font-size: 24px;
        margin-right: 10px;
        color: #409EFF;
      }
    }
    
    .message-list {
      margin-bottom: 20px;
      
      .message-item {
        background-color: #f9f9f9;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 15px;
        transition: all 0.3s;
        border-left: 4px solid #409EFF;
        
        &:hover {
          box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
          transform: translateY(-2px);
        }
        
        .message-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 15px;
          
          .user-info {
            display: flex;
            align-items: center;
            
            i {
              font-size: 18px;
              margin-right: 8px;
              color: #409EFF;
            }
            
            .message-title {
              font-size: 18px;
              font-weight: 600;
              color: #303133;
            }
          }
          
          .message-date {
            display: flex;
            align-items: center;
            color: #909399;
            font-size: 14px;
            
            i {
              margin-right: 5px;
              color: #909399;
            }
          }
        }
        
        .message-body {
          padding: 10px 0;
          margin-bottom: 15px;
          color: #606266;
          line-height: 1.6;
        }
        
        .message-replies {
          background-color: #f0f2f5;
          border-radius: 4px;
          padding: 10px 15px;
          margin-bottom: 15px;
          
          .reply-item {
            display: flex;
            align-items: flex-start;
            padding: 8px 0;
            border-bottom: 1px dashed #e0e3e9;
            
            &:last-child {
              border-bottom: none;
            }
            
            i {
              color: #67C23A;
              margin-right: 8px;
              font-size: 16px;
              margin-top: 3px;
            }
            
            span {
              color: #606266;
              line-height: 1.6;
            }
          }
        }
        
        .message-actions {
          display: flex;
          justify-content: flex-end;
          
          .reply-btn {
            color: #409EFF;
            
            i {
              margin-right: 5px;
            }
            
            &:hover {
              color: #66b1ff;
            }
          }
        }
      }
    }
    
    .empty-message {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      height: 200px;
      color: #909399;
      
      i {
        font-size: 60px;
        margin-bottom: 20px;
      }
      
      p {
        font-size: 16px;
      }
    }
    
    .message-pagination {
      display: flex;
      justify-content: center;
      padding: 20px 0;
    }
  }
}
</style>
