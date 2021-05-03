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
    val amount: Double,
    val date: String,
    val type: String

) {
    fun toModel() = Payment(
            id,
            place,
            category,
            amount,
            date,
            type
    )
}


