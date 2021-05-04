package pl.edu.pjatk.finanseapp

import android.app.Application
import androidx.room.Room
import pl.edu.pjatk.finanseapp.database.PaymentDatabase

class App : Application() {
    val db by lazy {
        Room.databaseBuilder(this, PaymentDatabase::class.java, "payment").build()
    }
}