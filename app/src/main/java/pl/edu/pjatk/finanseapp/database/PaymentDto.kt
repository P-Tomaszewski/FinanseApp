package pl.edu.pjatk.finanseapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.edu.pjatk.finanseapp.model.Payment

@Entity(tableName = "payment")
data class PaymentDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val place: String,
    val category: String,
    val date: String,
    val amount: String
){
    fun toModel() = Payment(
        place,
        category,
       amount,
        date
    )
}


