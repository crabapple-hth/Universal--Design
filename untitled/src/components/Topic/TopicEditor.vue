<script setup>
import {Document} from "@element-plus/icons-vue";
import {Delta,Quill, QuillEditor} from "@vueup/vue-quill";
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import ImageReSize from "quill-image-resize-vue"
import {ImageExtend,QuillWatch} from  "quill-image-super-solution-module"
import {reactive,ref,defineEmits,defineComponent,computed} from "vue";
import axios from "axios";
import {takeAccessToken, creatTopic, getTypeList} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {useStore} from "@/store/index.js";



defineProps({
  show:Boolean
})

const sensitiveWords = [
  '你妈',
  '傻逼',
  '他妈的',
];

const containsSensitiveWords = (text) => {
  if (!text) return false;
  return sensitiveWords.some(word => text.includes(word));
};

const store=useStore()

const topic=reactive({
  type:"",
  title:"",
  text:"",
  loading:false,
  types:[]
})

const emit=defineEmits(['close','success'])

const refEditor=ref()

function initTopic(){
  refEditor.value.setContents('','user')
  topic.title=""
  topic.type=null
}



Quill.register('modules/imageReSize',ImageReSize)
Quill.register('modules/imageExtend',ImageExtend)

function deltaToTex(delta){
  if (!delta.ops) return ""
  let str=""
  for (let op of delta.ops) {
    str += op.insert
  }
  return str.replace(/\s/g,"")
}

const contentLength=computed(()=> deltaToTex(topic.text).length)

const quillOptions={
  modules: {
    toolbar: {
      container: [
        "bold", "italic", "underline", "strike", "clean",
        {color: []}, {'background': []},
        {size: ["small", false, "large", "huge"]},
        {header: [1, 2, 3, 4, 5, 6, false]},
        {list: "ordered"}, {list: "bullet"}, {align: []},
        "blockquote", "code-block", "link", "image",
        {indent: '-1'}, {indent: '+1'}
      ],
      handlers: {
        'image':function (){
          QuillWatch.emit(this.quill.id)
        }
      }
    },
    imageReSize:{
      modules:[
          'Resize','DisplaySize'
      ]
    },
    imageExtend: {
      action:  axios.defaults.baseURL + '/api/image/topicImage',
      name: 'file',
      size: 5,
      loading: true,
      accept: 'image/png, image/jpeg',
      response: (resp) => {
        if(resp.data) {
          return axios.defaults.baseURL +"/images/" +resp.data
        } else {
          return null
        }
      },
      methods: 'POST',
      headers: xhr => {
        xhr.setRequestHeader('Authorization', 'Bearer '+takeAccessToken());
      },
      start: () => topic.uploading = true,
      success: () => {
        ElMessage.success('图片上传成功!')
        topic.uploading = false
      },
      error: () => {
        ElMessage.warning('图片上传失败，请联系管理员!')
        topic.uploading = false
      }
    }
  }
}

const submitTopic=()=>{


  if(!topic.type){
    ElMessage.warning("请选择帖子的类型")
  }else if (!topic.title){
    ElMessage.warning("请输入帖子的标题")
  }else if (contentLength.value>2000){
    ElMessage.warning("字数超过限制，请进行修改")
  }else if (containsSensitiveWords(deltaToTex(topic.text))) {
    ElMessage.warning("评论包含不合适内容，请修改后再发表");
  }else {
    creatTopic({
      type:topic.type,
      text:topic.text,
      title:topic.title
    },()=>{
      ElMessage.success("发表成功")
      emit('success')
    },()=>{
      ElMessage.warning("发表出现错误，请稍后再试")
    })
  }
}
</script>

<template>
  <div>
    <el-drawer
        :model-value="show"
        direction="btt"
        :size="655"
        @open="initTopic"
        :close-on-click-modal="false"
        @close="emit('close')"
    >
      <template #header>
        <div>
          <div style="font-weight: bold">发表新的帖子</div>
          <div style="font-size: 13px">请遵守社区发言规范，文明发言</div>
        </div>
      </template>
      <div style="display: flex;gap: 10px">
        <div  style="width: 120px">
          <el-select placeholder="选择帖子类型" value-key="id" v-model="topic.type">
            <el-option v-for="item in store.forum.types" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </div>
        <div style="flex: 1">
          <el-input placeholder="请输入帖子标题" :prefix-icon="Document" v-model="topic.title"/>
        </div>
      </div>
      <div style="margin-top: 15px;height: 460px;overflow: hidden" v-loading="topic.loading" element-loading-text="正在上传图片，请稍后">
        <quill-editor style="height: calc(100% - 45px)" v-model:content="topic.text"
                      content-type="delta" :options="quillOptions" ref="refEditor"/>
      </div>
      <div style="display: flex;justify-content: space-between;margin-top: 10px">
        <div style="color: grey;font-size: 13px">
          当前字数 {{contentLength}} （最大支持2000）
        </div>
        <div>
          <el-button type="success" @click="submitTopic" plain>立即发表</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
  :deep(.el-drawer){
    width: 800px;
    margin: auto;
    border-radius: 10px 10px 0 0;
  }

  :deep(.el-drawer__header){
    margin: 0;
  }

</style>