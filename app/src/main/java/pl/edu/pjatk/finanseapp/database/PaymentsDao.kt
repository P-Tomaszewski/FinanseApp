package pl.edu.pjatk.finanseapp.database

import androidx.room.*
import pl.edu.pjatk.finanseapp.database.PaymentDto

@Dao
interface PaymentsDao {
    @Query("SELECT * FROM payment;")
    fun getAll(): List<PaymentDto>

    @Query("SELECT SUM(amount) FROM payment;")
    fun getSum(): Double

    @Insert
    fun insert(payment: PaymentDto): Long

    @Query("DELETE FROM payment WHERE id = :id")
    fun deletePaymentById(id: Long)

    @Query("Select * FROM payment WHERE id = :id")
    fun getPaymentById(id: Long): PaymentDto

    @Update
    fun updatePayment(payment: PaymentDto)
}