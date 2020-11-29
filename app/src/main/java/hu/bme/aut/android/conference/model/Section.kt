package hu.bme.aut.android.conference.model

data class Section(
    val id: Long?,
    val name: String?,
    val startTime: String?,
    val endTime: String?,
    val lectures: List<Any>?
)
