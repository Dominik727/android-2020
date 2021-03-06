/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 5
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Base

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    companion object {
        var shared = DateFormatter()
    }

    fun dateToFormattedTimestampStringWithoutSeconds(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }

    fun dateToGsonDateFormat(date: String): String {
        return date.replace(" ", "T")
    }

    fun dateToGsonDateFormatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date).replace(" ", "T")
    }

    fun formatStringToShow(date: String): String {
        return date.dropLast(10).replace("T", " ")
    }
}
