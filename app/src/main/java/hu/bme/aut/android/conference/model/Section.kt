package hu.bme.aut.android.conference.model

import java.util.*

data class Section(
    val id: Long?,
    val name: String?,
    val startTime: Date?,
    val endTime: Date?,
    val lectures: List<Any>?
)
