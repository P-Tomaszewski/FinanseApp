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
    private var scaley1 = 0F
    private var scalex1 = 0F
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
        textSize = 20f
    }

        private val axisLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }



    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        var scaley = (height)/(yMax -yMin).toFloat()

        dataSet.forEachIndexed { index, currentDataPoint ->
            val realX = currentDataPoint.xVal.toRealX()
            val realY = height - currentDataPoint.yVal.toRealY()

            if (index < dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]
                val startX = currentDataPoint.xVal.toRealX()
                val startY = currentDataPoint.yVal.toRealY()
                val endX = nextDataPoint.xVal.toRealX()
                val endY =  nextDataPoint.yVal.toRealY()
                canvas.drawLine(startX  , height - startY , endX, height - endY, dataPointLinePaint)

            }
            canvas.drawCircle(realX, realY, 7f, dataPointFillPaint)
            canvas.drawCircle(realX, realY, 7f, dataPointPaint)
        }

        var xMaxInt = 31


        for (i in 0..xMaxInt) {
            if( i % 2 != 0){
                canvas.drawText(i.toString(),0f + i.toFloat()*((width)/xMaxInt)+40f,height-yMin*scaley + 40f,textPaint)
            }
        }

        for (i in yMin..yMax) {
            if( i % 10== 0){
                canvas.drawText(i.toString(),0f,height.toFloat()-(i+ yMin)*((height)/(yMax)),textPaint)
            }
        }

        canvas.drawLine(40f, 0f, 40f, height.toFloat(), axisLinePaint)
        canvas.drawLine(40f, height-yMin*scaley, width.toFloat(), height-yMin*scaley, axisLinePaint)
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

    private fun Int.toRealX() = toFloat() / xMax  * width
    private fun Int.toRealY() = toFloat()  / yMax * height
}



data class DataPoint(
    val xVal: Int,
    val yVal: Int
)