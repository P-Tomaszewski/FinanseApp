package pl.edu.pjatk.finanseapp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.room.Room
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.database.PaymentDto
import pl.edu.pjatk.finanseapp.databinding.ActivityAddPaymentBinding
import java.time.LocalDate
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
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.payment_type,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }



        val id: Long = (intent.extras?.get("id") ?: -1L) as Long
        if(id != -1L) {
            setupSave(true, id)
            fillWithData(id)}
        else setupSave(false, 0L)
    }

    private fun fillWithData(id: Long) = thread {
        val transaction = db.payments.getPaymentById(id)
        findViewById<EditText>(R.id.payment_place).setText(transaction.place)
        findViewById<EditText>(R.id.payment_amount).setText(transaction.amount.toString())
        findViewById<EditText>(R.id.payment_date).setText(transaction.date)
        findViewById<EditText>(R.id.payment_category).setText(transaction.category)
//        findViewById<Spinner>(R.id.spinner).setSelection(transaction.type.toInt()) //Do poprawy
    }


    private fun setupSave(edit: Boolean, id: Long) = view.saveButton.setOnClickListener {

        if(!edit){ //new
            val paymentDto = PaymentDto(
                    0,
                    place = view.paymentPlace.text.toString(),
                    category = view.paymentCategory.text.toString(),
                    amount = amountType(view.paymentAmount.text.toString() ,view.spinner.selectedItem.toString()),
                    date = view.paymentDate.text.toString(),
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
                    amount = amountType(view.paymentAmount.text.toString() ,view.spinner.selectedItem.toString()),
                    date = view.paymentDate.text.toString(),
                    type = view.spinner.selectedItem.toString()
            )
            updateItem(paymentDto)
        }
    }

    private fun amountType(amount: String, type: String): Double{
        return if(type == "Wydatek"){
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

    fun updateItem(payment: PaymentDto){
     pool.submit{
         db.payments.updatePayment(payment)
         setResult(Activity.RESULT_OK)
         finish()
     }
    }
}