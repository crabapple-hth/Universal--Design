<script setup>
import { reactive, onMounted } from 'vue';
import 'vue3-emoji-picker/css'
import {getComments} from "@/net/index.js";
import CommentEditor from "@/components/comment/CommentEditor.vue";
import Comment from "@/components/comment/Comment.vue"

const comments = reactive({
  list: []
})

const props = defineProps({
  tid: {
    type: Number
  },
  show: {
    type: Boolean
  }
})

const comment = reactive({
  tid: props.tid,
  content: "",
  reply_cid: null,
  top_comment_id: null
})



onMounted(() => {
  getComments(props.tid, (data) => {
    comments.list = data.map(item => ({ ...item, showReply: false }));
  })

})

</script>

<template>
  <div>
    <comment-editor v-if="show"  :tid="props.tid" />
    <div class="show_comment" v-for="item in comments.list" :key="item.cid">
      <comment :item="item" :tid="props.tid" />
      <el-divider />
    </div>
  </div>
</template>

<style scoped>
.v3-emoji-picker .v3-footer {
  display: none;
}

.show_comment {
  margin-top: 20px;
  border-radius: 5px;
  width: 100%;
}

</style>