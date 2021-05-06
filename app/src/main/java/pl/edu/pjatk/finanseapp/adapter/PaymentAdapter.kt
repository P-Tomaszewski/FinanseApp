package pl.edu.pjatk.finanseapp.adapter


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.finanseapp.MainActivity
import pl.edu.pjatk.finanseapp.activity.AddPaymentActivity
import pl.edu.pjatk.finanseapp.PaymentViewHolder
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.databinding.ItemCardPaymentBinding
import pl.edu.pjatk.finanseapp.model.Payment
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread


class PaymentAdapter(private val db: PaymentDatabase,val mainActivity: MainActivity): RecyclerView.Adapter<PaymentViewHolder>() {
    private var data: List<Payment> = emptyList()
    private var sum: Double = 0.0
    private val main = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
       val view = ItemCardPaymentBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
       )


        return PaymentViewHolder(view)
    }





    private fun removeItem(position: Long): Boolean
    {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    deleteItem(position)
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
        val alert = builder.create()
        alert.show()
        return true
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(data[position])

            holder.itemView.setOnLongClickListener{
                val item = data[holder.layoutPosition].id
                removeItem(item)
            }

            holder.itemView.setOnClickListener{
                val item = data[holder.layoutPosition].id
                mainActivity.setupAddButton2(item)
            }

    }
    override fun getItemCount(): Int = data.size



    fun load() = thread{
        data = db.payments.getAll().map { it.toModel() }
        main.post{
            notifyDataSetChanged()
        }
    }

    fun loadSum(): Double{
        var sum = 0.0
        var data = db.payments.getAll()
        for (row in data) {
            if (convertStringToDate(row.date).month == LocalDate.now().month) {
                sum += row.amount
            }
        }
        return sum.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    }

    fun deleteItem(position: Long){
        thread {
            db.payments.deletePaymentById(position)
            load()
            mainActivity.loadSum()
        }
    }
    fun convertStringToDate(date: String): LocalDate =
            LocalDate.of(
                    date.substring(0, 4).toInt(),
                    date.substring(6, 7).toInt(),
                    date.substring(8, 10).toInt()
            )
}