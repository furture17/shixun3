<template>
  <div id="container">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>预测图表</el-breadcrumb-item>
    </el-breadcrumb>
    <div ref="echartsBar" id="echarts-bar">
    </div>
  </div>
</template>
<script lang="ts">
// 引入 echarts 核心模块，核心模块提供了 echarts 使用必须要的接口。
import * as echarts from 'echarts/core';
// 引入柱状图图表，图表后缀都为 Chart
import {ScatterChart, ScatterSeriesOption} from 'echarts/charts';
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


// 通过 ComposeOption 来组合出一个只有必须组件和图表的 Option 类型
type ECOption = echarts.ComposeOption<
    | GridComponentOption
    | ScatterSeriesOption
    | TitleComponentOption
    | TooltipComponentOption
    | DatasetComponentOption>;

export default defineComponent({
  setup() {
    const echartsBar = ref<HTMLElement>() as Ref<HTMLElement>
    const trainDataset = ref('')
    const testDataset = ref('')
    const electNum = ref('')
    const neighbor = ref('')
    const id = ref('')
    return {
      trainDataset,
      testDataset,
      echartsBar,
      electNum,
      neighbor,
      id,
    }
  },
  created() {
    // 注册必须的组件
    echarts.use([
      ScatterChart,
      TitleComponent,
      TooltipComponent,
      GridComponent,
      DatasetComponent,
      TransformComponent,
      LabelLayout,
      UniversalTransition,
      CanvasRenderer,
    ]);
  },
  mounted() {
    this.id = this.$route.params.id as string
    this.electNum = this.$route.params.electNum as string
    this.neighbor = this.$route.params.neighbor as string
    this.trainDataset = this.$route.params.trainDataSetPath as string//'PDE.csv'
    this.testDataset = this.$route.params.testDataSetPath as string//'JDT.csv'
    this.load()
  },
  methods: {
    load() {
      //store.commit('setBg', 'assets/img/bg/bg_data.jpg')

      const myChart= echarts.init(this.echartsBar)
      const option: ECOption = {
        title: {
          text: '数据降维可视化',
        },
        // grid: {
        //   left: '3%',
        //   right: '7%',
        //   bottom: '7%',
        //  containLabel: true
        // },
        // tooltip: {
        //   showDelay: 0,
        //   formatter: function (params: any) {
        //     if (params.value.length > 1) {
        //       return (
        //           params.seriesName +
        //           ' :<br/>' +
        //           params.value[0] +
        //           'cm ' +
        //           params.value[1] +
        //           'kg '
        //       );
        //     } else {
        //       return (
        //           params.seriesName +
        //           ' :<br/>' +
        //           params.name +
        //           ' : ' +
        //           params.value +
        //           'kg '
        //       );
        //     }
        //   }
        // },
        // axisPointer: {
        //   show: true,
        //   type: 'cross',
        //   lineStyle: {
        //     type: 'dashed',
        //     width: 1
        //   }
        // },
        // toolbox: {
        //   feature: {
        //     dataZoom: {},
        //     brush: {
        //       type: ['rect', 'polygon', 'clear']
        //     }
        //   }
        // },
        // brush: {},
        // legend: {
        //   data: ['Female', 'Male'],
        //   left: 'center',
        //   bottom: 10
        // },
        xAxis: [
          {
            type: 'value',
            //scale: true,
            min: -2,
            max: 2,
            //axisLabel: {
             // formatter: '{value} cm'
            //},
            //splitLine: {
              //show: false
            //}
          }
        ],
        yAxis: [
          {
            type: 'value',
            //scale: true,
            min: -2,
            max: 2,
            //axisLabel: {
              //formatter: '{value} kg'
            //},
            //splitLine: {
              //show: false
            //}
          }
        ],
        series: [{
          name: 'Female',
          symbolSize: 8,
          data: [[1,1]],
          type: 'scatter',
        },
        {
          name: 'Male',
          type: 'scatter',
          symbolSize: 8,
          data: [[-1,-1]]
        }]
      };
      myChart.setOption(option)

      axios.post("http://192.168.43.55:8080/alg/svmPredict", {
        id:this.id,
        electNum: this.electNum,
        neighbor: this.neighbor,
        trainDataSetPath: this.trainDataset,//'PDE.csv',
        testDataSetPath: this.testDataset,//'PDE.csv',
      }).then(res => {
        const {data1,data2} = res.data
        const option1: ECOption = {
          series: [
            {
              name: 'Female',
              data: data1,
            }, {
              name: 'Male',
              data: data2
            }]
          }
          myChart.setOption(option1);
        }).catch(err => console.log(err))
    },
  },
});

</script>
<style scoped>

#echarts-bar {
  width: 1100px;
  height: 800px;
  margin: 20px auto;
  background-color: white;
  padding: 15px;
  border-radius: 10px;
}

</style>