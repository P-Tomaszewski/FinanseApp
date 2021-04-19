package pl.edu.pjatk.finanseapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pl.edu.pjatk.finanseapp.database.PaymentDto

@Dao
interface PaymentsDao {
    @Query("SELECT * FROM payment;")
    fun getAll(): List<PaymentDto>

    @Insert
    fun insert(payment: PaymentDto): Long
}