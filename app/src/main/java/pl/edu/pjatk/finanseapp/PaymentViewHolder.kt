package pl.edu.pjatk.finanseapp

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.finanseapp.adapter.PaymentAdapter
import pl.edu.pjatk.finanseapp.databinding.ItemCardPaymentBinding
import pl.edu.pjatk.finanseapp.model.Payment

class PaymentViewHolder(private val viewBinding: ItemCardPaymentBinding): RecyclerView.ViewHolder(viewBinding.root){

//fun setText(text: String){
//    viewBinding.textView2.text = text
//}

    fun bind(payment: Payment){
        with(viewBinding){
            place.text = payment.place
            category.text = payment.category
            amount.text = payment.amount.toString()
            date.text = payment.date.toString()
            type.text = payment.type

        }
    }
}