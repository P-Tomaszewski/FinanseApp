package pl.edu.pjatk.finanseapp.model

import java.time.LocalDate

data class Payment(
        val id: Long,
        val place: String,
        val category: String,
        val amount: Double,
        val date: String,
        val type: String

)
