<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue'
import { useTransition } from '@vueuse/core'
import { ChatLineRound, Male } from '@element-plus/icons-vue'
import * as echarts from 'echarts/core';
import { PieChart, LineChart, BarChart } from 'echarts/charts';
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

const topicChartRef = ref<HTMLElement | null>(null);
const topicChart = ref<echarts.ECharts | null>(null);

const userChartRef = ref<HTMLElement | null>(null);
const userChart = ref<echarts.ECharts | null>(null);

const ratioChartRef = ref<HTMLElement | null>(null); // 新增环形图 ref
const ratioChart = ref<echarts.ECharts | null>(null);

const feedbackChartRef = ref<HTMLElement | null>(null); // 新增柱状图 ref
const feedbackChart = ref<echarts.ECharts | null>(null);

const userOption = {
  title: {
    text: '用户增长趋势',
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
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '新增用户',
      type: 'line',
      stack: 'Total',
      data: [120, 200, 150, 80, 70, 110, 130]
    }
  ]
};

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
      data: [
        { value: 335, name: '直接访问' },
        { value: 234, name: '联盟广告' },
        { value: 1548, name: '搜索引擎' }
      ],
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

const ratioOption = {
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
      data: [
        { value: 138, name: '男性用户' },
        { value: 100 - 138, name: '其他用户' } // 假设总用户数为 100
      ]
    }
  ]
};

const feedbackOption = {
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
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '反馈数量',
      type: 'bar',
      data: [50, 80, 65, 90, 75, 100, 85]
    }
  ]
};

const source = ref(0)
const outputValue = useTransition(source, {
  duration: 1500,
})
source.value = 172000

onMounted(() => {
  if (topicChartRef.value) {
    topicChart.value = echarts.init(topicChartRef.value);
    topicChart.value.setOption(topicOption);
  }
  if (userChartRef.value) {
    userChart.value = echarts.init(userChartRef.value);
    userChart.value.setOption(userOption);
  }
  if (ratioChartRef.value) {
    ratioChart.value = echarts.init(ratioChartRef.value);
    ratioChart.value.setOption(ratioOption);
  }
  if (feedbackChartRef.value) {
    feedbackChart.value = echarts.init(feedbackChartRef.value);
    feedbackChart.value.setOption(feedbackOption);
  }
});
</script>

<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-statistic title="今日活跃用户数" :value="268500" />
      <div ref="userChartRef" style="height: 300px; width: 300px; margin-left: 25%"></div>
    </el-col>
    <el-col :span="12">
      <el-statistic>
        <template #title>
          用户比例
        </template>
        <template #value>{{ 138 }}</template>
        <template #suffix>
          <div style="display: inline-flex; align-items: center;">
            <el-icon style="margin-left: 4px" :size="12">
              <Male />
            </el-icon>
            /100
          </div>
        </template>
      </el-statistic>
      <div ref="ratioChartRef" style="height: 150px; width: 150px; margin-left: 37%; margin-top: 10px;"></div>
    </el-col>
  </el-row>
  <el-row :gutter="20" style="margin-top: 20px">
    <el-col :span="12">
      <el-statistic title="帖子发表总数" :value="outputValue" />
      <div ref="topicChartRef" id="forum-total" style="height: 300px; width: 300px; margin-left: 25%"></div>
    </el-col>
    <el-col :span="12">
      <el-statistic title="反馈数量" :value="562">
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