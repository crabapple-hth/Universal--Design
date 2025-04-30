<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue'
import { useTransition } from '@vueuse/core'
import { ChatLineRound, Male } from '@element-plus/icons-vue'
import * as echarts from 'echarts/core';
import { PieChart, LineChart, BarChart } from 'echarts/charts';
import { apiTypesNum, apiLastSeven, apiGender } from "@/net/index.js";
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  DatasetComponent,
  TransformComponent,
  GridComponent,
  PolarComponent,
} from 'echarts/components';
import { LabelLayout, UniversalTransition } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  DatasetComponent,
  TransformComponent,
  LineChart,
  PieChart,
  BarChart, // 引入柱状图
  LabelLayout,
  UniversalTransition,
  CanvasRenderer,
  GridComponent,
  PolarComponent,
]);


const LastSevenTopics = reactive({
  date: [],
  count: []
})
const Gender = reactive({
  data: []
})
const TypesTopicNum = reactive({
  data: []
})
const count=reactive(
    {count:[]}
)

function getall() {
  apiLastSeven((data) => {
    LastSevenTopics.date = data.map(item => item.date.substring(6, 10))
    LastSevenTopics.count = data.map(item => item.count)
    updateUserChart(); // 调用更新用户趋势图的方法
  }), apiGender((data) => {
    Gender.data = data
    count.count=data.map(item=>item.count)
    updateRatioChart(); // 调用更新用户比例图的方法
  }), apiTypesNum((data) => {
    TypesTopicNum.data = data
    updateTopicChart(); // 调用更新帖子分类图的方法
  })
  console.log(LastSevenTopics, Gender, TypesTopicNum)
}



const topicChartRef = ref<HTMLElement | null>(null);
const topicChart = ref<echarts.ECharts | null>(null);

const userChartRef = ref<HTMLElement | null>(null);
const userChart = ref<echarts.ECharts | null>(null);

const ratioChartRef = ref<HTMLElement | null>(null); // 新增环形图 ref
const ratioChart = ref<echarts.ECharts | null>(null);

const feedbackChartRef = ref<HTMLElement | null>(null); // 新增柱状图 ref
const feedbackChart = ref<echarts.ECharts | null>(null);

const userOption = reactive({
  title: {
    text: '七日帖子发表趋势',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: LastSevenTopics.date // 使用从接口获取的日期数据
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '新增帖子',
      type: 'line',
      stack: 'Total',
      data: LastSevenTopics.count // 使用从接口获取的帖子数量数据
    }
  ]
});

const topicOption = reactive({
  title: {
    text: '帖子分类统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'horizontal',
    bottom: '5%'
  },
  series: [
    {
      name: '帖子分类',
      type: 'pie',
      radius: '50%',
      data: TypesTopicNum.data.map(item => ({ value: item.count, name: item.typeName })), // 使用接口获取的分类数据
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
});

const ratioOption = reactive({
  series: [
    {
      type: 'pie',
      radius: ['40%', '70%'], // 设置内外半径创建环形图
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 20,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: Gender.data.map(item => ({ value: item.count, name: item.name }))
    }
  ]
});

const feedbackOption = reactive({
  title: {
    text: '每日反馈数量',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] // 这里可以替换为从接口获取的日期数据，如果接口提供的话
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '反馈数量',
      type: 'bar',
      data: [50, 80, 65, 90, 75, 100, 85] // 这里可以替换为从接口获取的反馈数量数据，如果接口提供的话
    }
  ]
});

const totalPostsToday = ref(0);
const totalPosts = ref(0);
const totalFeedback = ref(0);

function updateStatistics() {
  apiLastSeven((data) => {
    totalPostsToday.value = data.reduce((sum, item) => sum + item.count, 0);
  });
  apiTypesNum((data) => {
    totalPosts.value = data.reduce((sum, item) => sum + item.count, 0);
  });
  totalFeedback.value = 562;
}

function updateUserChart() {
  userOption.xAxis.data = LastSevenTopics.date;
  userOption.series[0].data = LastSevenTopics.count;
  userChart.value?.setOption(userOption);
}

function updateTopicChart() {
  topicOption.series[0].data = TypesTopicNum.data.map(item => ({ value: item.count, name: item.typeName }));
  topicChart.value?.setOption(topicOption);
}

function updateRatioChart() {
  ratioOption.series[0].data = Gender.data.map(item => ({ value: item.count, name: item.name }));
  ratioChart.value?.setOption(ratioOption);
}


const source = ref(0)
const outputValue = useTransition(source, {
  duration: 1500,
})

onMounted(() => {
  getall(); // 在组件挂载后获取数据

  topicChart.value = echarts.init(topicChartRef.value);
  topicChart.value.setOption(topicOption);

  userChart.value = echarts.init(userChartRef.value);
  userChart.value.setOption(userOption);

  ratioChart.value = echarts.init(ratioChartRef.value);
  ratioChart.value.setOption(ratioOption);

  feedbackChart.value = echarts.init(feedbackChartRef.value);
  feedbackChart.value.setOption(feedbackOption);

  updateStatistics(); // 初始化统计数据
  source.value = 172000; // 保持这个初始值或从接口获取
});
</script>

<template>
  <el-button @click="getall">更新数据</el-button>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-statistic title="今日新增帖子数" :value="totalPostsToday" />
      <div ref="userChartRef" style="height: 300px; width: 300px; margin-left: 25%"></div>
    </el-col>
    <el-col :span="12">
      <el-statistic :value="count.count[0]">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            用户比例
            <el-icon style="margin-left: 4px" :size="12">
              <Male />
            </el-icon>
          </div>
        </template>
        <template #suffix>/{{count.count[1]}}</template>
      </el-statistic>
      <div ref="ratioChartRef" style="height: 150px; width: 150px; margin-left: 37%; margin-top: 10px;"></div>
    </el-col>
  </el-row>
  <el-row :gutter="20" style="margin-top: 20px">
    <el-col :span="12">
      <el-statistic title="帖子发表总数" :value="totalPosts" />
      <div ref="topicChartRef" id="forum-total" style="height: 300px; width: 300px; margin-left: 25%"></div>
    </el-col>
    <el-col :span="12">
      <el-statistic title="反馈数量" :value="totalFeedback">
        <template #suffix>
          <el-icon style="vertical-align: -0.125em">
            <ChatLineRound />
          </el-icon>
        </template>
      </el-statistic>
      <div ref="feedbackChartRef" style="height: 300px; width: 300px; margin-left: 25%; margin-top: 20px;"></div>
    </el-col>
  </el-row>
</template>

<style scoped>
.el-col {
  text-align: center;
}
</style>