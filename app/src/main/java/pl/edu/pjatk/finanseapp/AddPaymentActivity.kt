package pl.edu.pjatk.finanseapp

import android.app.Activity
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.database.PaymentDto
import pl.edu.pjatk.finanseapp.databinding.ActivityAddPaymentBinding
import java.util.*
import java.util.concurrent.Executors

class AddPaymentActivity : AppCompatActivity() {
    private val pool by lazy {
        Executors.newSingleThreadExecutor()
    }
    private val db by lazy{
        Room.databaseBuilder(this, PaymentDatabase::class.java, "payment" ).build()
    }
    private val view by lazy {
        ActivityAddPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        setupSave()
    }

    private fun setupSave() = view.saveButton.setOnClickListener{
        val paymentDto = PaymentDto(
            0,
            place = view.paymentPlace.text.toString(),
            category = view.paymentCategory.text.toString(),
            amount = view.paymentAmount.text.toString(),
            date = view.paymentDate.text.toString()
        )
        pool.submit{
            db.payments.insert(paymentDto)
            setResult(Activity.RESULT_OK)
            finish()
            }
    }
}