<script setup>

import axios from "axios";
import {replyCommentList} from "@/net/index.js";
import {reactive,ref} from "vue";
import CommentEditor from "@/components/comment/CommentEditor.vue";

const comment_info=reactive({
  reply_cid:null,
  top_comment_id:null
})

const props=defineProps({
  item:{
    type:Object
  },
  tid:{
    type:Number
  }
})

const show=ref(false)

const currentPage=ref(1)

const replies=reactive({
  show:false,
  size:5,
  list:[]
})

function lookClick(item){
  replyCommentList(item.cid,currentPage.value,replies.size,(data)=>{
    replies.list=data
    replies.show=!replies.show
    props.item.replies=replies.list
  })
}

function showEditor(replyId,topCommentId){
  show.value=!show.value
  comment_info.reply_cid=replyId
  comment_info.top_comment_id=topCommentId
}

const handleCurrentChange = (val) => {
  currentPage.value=val
  replyCommentList(props.item.cid,currentPage.value,replies.size,(data)=>{
    replies.list=data
    props.item.replies=replies.list
  })
}
</script>

<template>
    <div style="margin-top: 20px">
      <div style="display: flex; align-items: center; flex-wrap: wrap">
        <el-avatar :src="axios.defaults.baseURL + '/images/' + props.item.avatar" />
        <div style="margin-left: 10px">{{ props.item.username }}</div>
      </div>
      <div style="margin-left: 50px">{{ props.item.content }}</div>
      <div>
        <div style="margin-left: 50px; margin-top: 15px; display: flex; align-items: center">
          <div style="color: grey; font-size: 13px; width: 15%">{{ new Date(props.item.time).toLocaleString() }}</div>
          <el-button text style="" size="small" @click="showEditor(item.cid,item.cid)">
            回复
          </el-button>
        </div>
      </div>
      <div v-for="reply in props.item.replies" style="margin-left: 50px; margin-top: 20px">
        <div style="display: flex; align-items: center; flex-wrap: wrap;">
          <el-avatar style="margin-top: 7px;height: 24px;width: 24px"
                     :src="axios.defaults.baseURL + '/images/' + reply.avatar" />
          <div style="margin-left: 10px">{{ reply.username }} :</div>
          <div style="margin-left: 10px">{{ reply.content }}</div>
        </div>
        <div>
          <div style="margin-left: 30px; display: flex; align-items: center">
            <div style="color: grey; font-size: 13px; width: 15%">{{ new Date(reply.time).toLocaleString() }}</div>
            <el-button text style="" size="small" @click="showEditor(reply.cid,item.cid)">
              回复
            </el-button>
          </div>
        </div>
      </div>
      <div class="clickable-text" @click="lookClick(item)"
           v-show="item.replyCount >= 3" v-if="!replies.show">
        共 {{item.replyCount}} 条回复，点击查看
      </div>
      <div class="pagination" v-if="replies.show">
        <el-pagination
            :hide-on-single-page="true"
            v-model:current-page="currentPage"
            :page-size="replies.size"
            layout="total,prev, pager, next"
            :total=item.replyCount
            @current-change="handleCurrentChange"
        />
      </div>
      <comment-editor v-if="show" :tid="props.tid" :comment-info="comment_info" />
    </div>
</template>

<style scoped>

.clickable-text {
  font-size: 13px;
  color: grey;
  margin-left: 30px;
  cursor: pointer;
  padding: 5px;
}

.clickable-text:hover {
  color: skyblue;
}

.pagination{
  margin-left: 30px;
}
</style>