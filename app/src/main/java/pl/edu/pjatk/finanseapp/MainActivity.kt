package pl.edu.pjatk.finanseapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import pl.edu.pjatk.finanseapp.activity.AddPaymentActivity
import pl.edu.pjatk.finanseapp.activity.ChartActivity
import pl.edu.pjatk.finanseapp.adapter.PaymentAdapter
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.databinding.ActivityMainBinding
import pl.edu.pjatk.finanseapp.databinding.ItemCardPaymentBinding
import java.math.RoundingMode
import java.time.LocalDate
import kotlin.concurrent.thread

private const val REQUEST_ADD_PAYMENT = 2

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val paymentAdapter by lazy {
        PaymentAdapter(db, this)
    }

    private val db by lazy {
        Room.databaseBuilder(this, PaymentDatabase::class.java, "payment").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupPaymentList()
        setupAddButton()
        setupChartButton()
        loadSum()
    }

    private fun setupPaymentList() {
        binding.recycler.apply {
            adapter = paymentAdapter
            layoutManager = LinearLayoutManager(context)
        }
        paymentAdapter.load()

    }

    private fun setupAddButton() = binding.buttonAdd.setOnClickListener {
        val intent = Intent(this, AddPaymentActivity::class.java)
        startActivityForResult(
            intent, REQUEST_ADD_PAYMENT
        )
    }

    fun setupAddButton2(item: Long) {
        var intent = Intent(this, AddPaymentActivity::class.java)
        startActivityForResult(intent.putExtra("id", item), REQUEST_ADD_PAYMENT)
    }


    private fun setupChartButton() = binding.buttonChart.setOnClickListener {
        val intent = Intent(this, ChartActivity::class.java)
        startActivityForResult(
            intent, REQUEST_ADD_PAYMENT
        )
    }

    fun loadSum(){
        thread {
            var sum = paymentAdapter.loadSum()
            runOnUiThread() {
                findViewById<TextView>(R.id.balance).text = sum.toString()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_PAYMENT && resultCode == Activity.RESULT_OK) {
                paymentAdapter.load()
                loadSum()
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }
}


