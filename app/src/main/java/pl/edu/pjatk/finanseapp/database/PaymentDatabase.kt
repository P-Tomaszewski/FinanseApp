package pl.edu.pjatk.finanseapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PaymentDto::class],
    version = 1
)
abstract class PaymentDatabase : RoomDatabase(){
    abstract val payments: PaymentsDao
}
