package pl.edu.pjatk.finanseapp

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Size
import android.view.View
import kotlin.math.absoluteValue

class ChartView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {


    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0

    private val dataPointPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 7f
        style = Paint.Style.STROKE
    }

    private val dataPointFillPaint = Paint().apply {
        color = Color.WHITE
    }

    private val dataPointLinePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 7f
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
       color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 50F
    }

        private val axisLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    private val ayisLinePaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        dataSet.forEachIndexed { index, currentDataPoint ->
            val realX = currentDataPoint.xVal.toRealX()
            val realY = currentDataPoint.yVal.toRealY()

            if (index < dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]
                val startX = currentDataPoint.xVal.toRealX()
                val startY = currentDataPoint.yVal.toRealY()
                val endX = nextDataPoint.xVal.toRealX()
                val endY = nextDataPoint.yVal.toRealY()
                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)

            }

            canvas.drawCircle(realX, realY, 7f, dataPointFillPaint)
            canvas.drawCircle(realX, realY, 7f, dataPointPaint)
        }

//        val all = dataSet

//        var minValue: Int = 1
//        var maxValue: Int = 20
//        var balance = 0
//        all.forEach {
//            balance += it
//            if (balance>maxValue) maxValue = balance
//            if (balance<minValue) minValue = balance
//        }
//        val heightOf0 = if(maxValue>0 && minValue <0) maxValue/(maxValue+minValue.absoluteValue)
//        else if(maxValue<=0) 0.0
//        else 1.0
//        val scaledHeightOf0 = height.toFloat() * .1f + height.toFloat() * .8f * heightOf0.toFloat()


//        canvas.drawText(dataSet.get(1).toString(), 10F, 10F, textPaint)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), axisLinePaint)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), axisLinePaint)
    }

    fun setData(newDataSet: List<DataPoint>) {
        xMin = newDataSet.minOf { it.xVal }
        xMax = newDataSet.maxOf { it.xVal }
        yMin = newDataSet.minOf { it.yVal }
        yMax = newDataSet.maxOf { it.yVal }
        dataSet.clear()
        dataSet.addAll(newDataSet)
        invalidate()
    }

    private fun Int.toRealX() = toFloat() / xMax * width
    private fun Int.toRealY() = toFloat()  / yMax * height
}



data class DataPoint(
    val xVal: Int,
    val yVal: Int
)