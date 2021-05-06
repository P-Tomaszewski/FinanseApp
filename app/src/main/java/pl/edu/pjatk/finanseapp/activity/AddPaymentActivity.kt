package pl.edu.pjatk.finanseapp.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import pl.edu.pjatk.finanseapp.MainActivity
import pl.edu.pjatk.finanseapp.R
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.database.PaymentDto
import pl.edu.pjatk.finanseapp.databinding.ActivityAddPaymentBinding
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread


class AddPaymentActivity : AppCompatActivity() {
    private val pool by lazy {
        Executors.newSingleThreadExecutor()
    }
    private val db by lazy {
        Room.databaseBuilder(this, PaymentDatabase::class.java, "payment").build()
    }
    private val view by lazy {
        ActivityAddPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
                this,
                R.array.payment_type,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        setupShareButton()


        val id: Long = (intent.extras?.get("id") ?: -1L) as Long
        if(id != -1L) {
            setupSave(true, id)
            templateWithData(id)}
        else setupSave(false, 0L)


        view.paymentDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val picker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth -> this.view.paymentDate.setText("$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}")},
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            picker.show()
        }
    }

    private fun setupShareButton() = view.share.setOnClickListener {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
                    "Miejsce: " + view.paymentPlace.text + " " + "\n" +
                            "Kategoria: " + view.paymentCategory.text + "\n" +
                            "Data: " + view.paymentDate.text + "\n" +
                            "Typ: " + view.spinner.selectedItem.toString() + "\n" +
                            "Kwota: " + view.paymentAmount.text.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent) //share
    }

    private fun templateWithData(id: Long) = thread {
        val transaction = db.payments.getPaymentById(id)
        findViewById<EditText>(R.id.payment_place).setText(transaction.place)
        findViewById<EditText>(R.id.payment_amount).setText(transaction.amount.toString())
        findViewById<EditText>(R.id.payment_date).setText(transaction.date)
        findViewById<EditText>(R.id.payment_category).setText(transaction.category)
    }

    private fun setupSave(edit: Boolean, id: Long) = view.saveButton.setOnClickListener {

        if(!edit){ //new
            val paymentDto = PaymentDto(
                    0,
                    place = view.paymentPlace.text.toString(),
                    category = view.paymentCategory.text.toString(),
                    amount = amountType(view.paymentAmount.text.toString(), view.spinner.selectedItem.toString()),
                    date = setDate(view.paymentDate.text.toString()),
                    type = view.spinner.selectedItem.toString()
            )
        pool.submit {
            db.payments.insert(paymentDto)
            setResult(Activity.RESULT_OK)
            finish()
        }}
        else{ //update
            val paymentDto = PaymentDto(
                    id,
                    place = view.paymentPlace.text.toString(),
                    category = view.paymentCategory.text.toString(),
                    amount = amountType(view.paymentAmount.text.toString(), view.spinner.selectedItem.toString()),
                    date = setDate(view.paymentDate.text.toString()),
                    type = view.spinner.selectedItem.toString()
            )
            pool.submit{
                db.payments.updatePayment(paymentDto)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setDate(date: String): String{
        if(date.isNullOrEmpty()){
            return LocalDate.now().toString()
        }
        else return date
    }

    private fun amountType(amount: String, type: String): Double{
        return if(type == "Wydatek" && amount.isNotEmpty()){
            ParseDouble("-$amount")
        } else
            ParseDouble(amount)
    }


    private fun ParseDouble(strNumber: String?): Double {
        return if (strNumber != null && strNumber.isNotEmpty()) {
            try {
                strNumber.toDouble()
            } catch (e: Exception) {
                (-1).toDouble()
            }
        } else 0.0
    }
}