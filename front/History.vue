<template>
  <div id="container">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>历史信息</el-breadcrumb-item>
    </el-breadcrumb>
    <div ref="echartsPeriod" id="echarts-period">
    </div>
  </div>
</template>
<script lang="ts">
// 引入 echarts 核心模块，核心模块提供了 echarts 使用必须要的接口。
import * as echarts from 'echarts/core';
// 引入柱状图图表，图表后缀都为 Chart
import {BarChart, BarSeriesOption, LineChart, LineSeriesOption} from 'echarts/charts';
// 引入提示框，标题，直角坐标系，数据集，内置数据转换器组件，组件后缀都为 Component
import {
  DatasetComponent,
  DatasetComponentOption,
  GridComponent,
  GridComponentOption,
  TitleComponent,
  TitleComponentOption,
  TooltipComponent,
  TooltipComponentOption,
  TransformComponent
} from 'echarts/components';
// 标签自动布局，全局过渡动画等特性
import {LabelLayout, UniversalTransition} from 'echarts/features';
// 引入 Canvas 渲染器，注意引入 CanvasRenderer 或者 SVGRenderer 是必须的一步
import {CanvasRenderer} from 'echarts/renderers';

import {defineComponent, Ref, ref} from "vue";
import axios from "axios";

import store from "@/store";
import {ElMessage} from "element-plus/es";

// 通过 ComposeOption 来组合出一个只有必须组件和图表的 Option 类型
type ECOption = echarts.ComposeOption<| BarSeriesOption
    | LineSeriesOption
    | TitleComponentOption
    | TooltipComponentOption
    | GridComponentOption
    | DatasetComponentOption>;

export default defineComponent({
  setup() {
    const echartsPeriod = ref<HTMLElement>() as Ref<HTMLElement>
    const street = ref('')
    const trainDataset = ref('')
    const testDataset = ref('')
    const id = ref('')
    const lr = ref('')
    const lambda = ref('')
    const epochNum = ref('')
    return {
      trainDataset,
      testDataset,
      echartsPeriod,
      street,
      id,
      lr,
      lambda,
      epochNum,
    }
  },
  created() {
    // 注册必须的组件
    echarts.use([
      TitleComponent,
      TooltipComponent,
      GridComponent,
      DatasetComponent,
      TransformComponent,
      BarChart,
      LabelLayout,
      UniversalTransition,
      CanvasRenderer,
      LineChart
    ]);

  },
  mounted() {
    this.id = this.$route.params.id as string
    this.lr = this.$route.params.lr as string
    this.lambda = this.$route.params.lambda as string
    this.epochNum =this.$route.params.epochNum as string
    this.trainDataset = this.$route.params.trainDataSetPath as string//'PDE.csv'
    this.testDataset = this.$route.params.testDataSetPath as string//'JDT.csv'
    this.load()
  },
  methods: {
    load() {
      store.commit('setBg', 'assets/img/bg/bg_data.jpg')

      const myChart = echarts.init(this.echartsPeriod)
      const option2: ECOption = {
        title: {
          text: '损失函数随训练轮数变化'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          },
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          splitLine: {
            show: false
          }
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '40%'],
          splitLine: {
            show: true
          }
        },
        series: [
          {
            name: '损失函数',
            type: 'line',
            showSymbol: false,
            data: [],
            smooth: true,
            sampling: 'average',
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {offset: 0, color: 'rgba(58,77,233,1)'},
                {offset: 1, color: 'rgba(58,77,233,0.25)'}
              ])
            }
          }
        ]
      };
      myChart.setOption(option2)

      axios.post("http://192.168.43.55:8080/alg/logPredict", {
        id:this.id,
        lr:this.lr,
        lambda:this.lambda,
        epochNum:this.epochNum,
        trainDataSetPath: this.trainDataset,//'PDE.csv',
        testDataSetPath: this.testDataset//'JDT.csv',
      }).then(res => {
        const {data} = res
        const option3: ECOption = {
          series: [{data: data}]
        }
        myChart.setOption(option3);
      })
    }
  }
})


</script>
<style scoped>

#echarts-week {
  width: 800px;
  height: 500px;
  margin: 40px auto;
  background-color: white;
  padding: 15px;
  border-radius: 10px;
}
#echarts-period {
  width: 800px;
  height: 500px;
  margin: 40px auto;
  background-color: white;
  padding: 15px;
  border-radius: 10px;
}
#echarts-month {
  width: 800px;
  height: 500px;
  margin: 40px auto;
  background-color: white;
  padding: 15px;
  border-radius: 10px;
}

/*#container{*/
/*  height: 100%;*/
/*  width: 100%;*/
/*  background-color: #42b983;*/
/*}*/
/*
站点一
站点二
站点三
站点四
站点五
站点六
站点七
站点八
站点九
站点十
*/
</style>




