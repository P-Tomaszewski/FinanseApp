package pl.edu.pjatk.finanseapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_chart.*
import pl.edu.pjatk.finanseapp.DataPoint

import pl.edu.pjatk.finanseapp.R
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.database.PaymentDto
import java.time.LocalDate
import kotlin.concurrent.thread


class ChartActivity : AppCompatActivity() {

    private val db by lazy {
        Room.databaseBuilder(this, PaymentDatabase::class.java, "payment").build()
    }
    private var data: List<PaymentDto> = emptyList()
    private var sum: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        setContentView(R.layout.activity_chart)
        chart_view.setData(setData(data))


    }

    private fun getData() = thread {
        data = db.payments.getAll()
    }

    private fun setData(dataList: List<PaymentDto>): List<DataPoint> {
        var dataPoints: List<DataPoint> = emptyList()
       data = data.sortedBy { convertStringToDate(it.date).dayOfMonth}
//        return (0..20).map {
//            DataPoint(it, random.nextInt(50) + 1)
//        }
        for (row in data) {
            sum+= row.amount.toInt()
            dataPoints += DataPoint( convertStringToDate(row.date).dayOfMonth, sum)
        }
        return dataPoints
    }

    fun convertStringToDate(date: String): LocalDate =
        LocalDate.of(
            date.substring(0, 4).toInt(),
            date.substring(6, 7).toInt(),
            date.substring(8, 10).toInt()
        )
}