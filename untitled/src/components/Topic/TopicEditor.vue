<script setup>
import {Document} from "@element-plus/icons-vue";
import {QuillEditor} from "@vueup/vue-quill";
import '@vueup/vue-quill/dist/vue-quill.snow.css'

defineProps({
  show:Boolean
})

const emit=defineEmits(['close'])
</script>

<template>
  <div>
    <el-drawer
        :model-value="show"
        direction="btt"
        :size="655"
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
          <el-select placeholder="选择帖子类型">
            <el-option>111</el-option>
          </el-select>
        </div>
        <div style="flex: 1">
          <el-input placeholder="请输入帖子标题" :prefix-icon="Document"/>
        </div>
      </div>
      <div style="margin-top: 15px;height: 460px;overflow: hidden">
        <quill-editor style="height: calc(100% - 45px)" />
      </div>
      <div style="display: flex;justify-content: space-between;margin-top: 10px">
        <div style="color: grey;font-size: 13px">
          当前字数 666 （最大支持2000）
        </div>
        <div>
          <el-button type="success" plain>立即发表</el-button>
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

  :deep()
</style>