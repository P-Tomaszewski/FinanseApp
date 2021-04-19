package pl.edu.pjatk.finanseapp

import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import pl.edu.pjatk.finanseapp.databinding.ItemCardPaymentBinding
import pl.edu.pjatk.finanseapp.model.Payment
import java.util.logging.Handler
import kotlin.concurrent.thread

class PaymentAdapter(private val db: PaymentDatabase): RecyclerView.Adapter<PaymentViewHolder>() {
    private var data: List<Payment> = emptyList()
    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
       val view = ItemCardPaymentBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
       holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun load() = thread{
        data = db.payments.getAll().map { it.toModel() }
        main.post{
            notifyDataSetChanged()
        }
    }
}