package pl.edu.pjatk.finanseapp.adapter


import android.app.AlertDialog
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.finanseapp.AddPaymentActivity
import pl.edu.pjatk.finanseapp.PaymentViewHolder
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.database.PaymentDto
import pl.edu.pjatk.finanseapp.databinding.ItemCardPaymentBinding
import pl.edu.pjatk.finanseapp.model.Payment
import java.math.RoundingMode
import kotlin.concurrent.thread


class PaymentAdapter(private val db: PaymentDatabase): RecyclerView.Adapter<PaymentViewHolder>() {
    private var data: List<Payment> = emptyList()
    private var sum: Double = 0.0
    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
       val view = ItemCardPaymentBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
       )
        return PaymentViewHolder(view).also { holder ->
            view.root.setOnLongClickListener{
                removeItem(holder.layoutPosition,parent)
            }
            view.root.setOnClickListener{
                val item = data[holder.layoutPosition].id
                parent.context.startActivity(Intent(parent.context, AddPaymentActivity::class.java).putExtra("id", item), null)

            }
        }
        return PaymentViewHolder(view)
    }

//    private fun updateItem(position: Int, parent: ViewGroup): Boolean
//    {
//        onBindViewHolder(, position)
//    }

    private fun removeItem(position: Int, parent: ViewGroup): Boolean
    {
//        TODO: check once more
        val builder = AlertDialog.Builder(parent.context)
        builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    deleteItem(position)
                    notifyItemChanged(position)
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
    }
    override fun getItemCount(): Int = data.size

    fun getSum(): Double = sum.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()


    fun loadSum() = thread{
        sum = db.payments.getSum()

        main.post{
            notifyDataSetChanged()
        }
    }

    fun load() = thread{
        data = db.payments.getAll().map { it.toModel() }
        main.post{
            notifyDataSetChanged()
        }
    }

    fun deleteItem(position: Int){
        val item = data[position]
        thread {
            db.payments.deletePaymentById(item.id)
            load()
        }
    }
}