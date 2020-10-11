<script>
import {Line} from 'vue-chartjs'
export default {
    extends: Line,
    name: "LineChart",
    props: [
        'chartData'
    ],
    computed: {
        lineChartData: function () {
            return this.chartData.datasets[0].data
        }
    },
    watch: {
        lineChartData: function () {
            this._data._chart.update()
        }
    },
    mounted() {
        this.renderChart(this.chartData, {
            legend: {
                display: false
            },
            title: {
                display: false
            },
            responsive: true,
            maintainAspectRatio: false
        })
    }
}
</script>

<style scoped>

</style>