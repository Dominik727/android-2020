/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 5
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Section

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Base.DateFormatter
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Section
import java.util.*
import kotlinx.android.synthetic.main.activity_home_dashboard.*
import kotlinx.android.synthetic.main.activity_section_detail.*

class SectionDetail :
    BaseActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener { // ktlint-disable max-line-length

    enum class DateType {
        START,
        END
    }

    var section: Section? = null
    var dateType: DateType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        if (section == null) {
            this.section = Section()
        }

        this.title = "Szekció"

        startDateEditText.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = Date().time
            dateType = DateType.START
            datePickerDialog.show()
        }

        endDateEditText.setOnClickListener {
            if (section?.startTime == null) {
                endDateEditText.error = "Kérem töltse ki a kezdő dátumot!"
                endDateEditText.requestFocus()
                return@setOnClickListener
            }
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = section?.startTime?.time ?: Date().time
            dateType = DateType.END
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        when (dateType) {
            DateType.START -> {
                section?.startTime = Date(calendar.timeInMillis)
            }
            DateType.END -> {
                section?.endTime = Date(calendar.timeInMillis)
            }
        }

        val timePickerDialog = TimePickerDialog(
            this, this, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        when (dateType) {
            DateType.START -> {
                section?.startTime?.hours = hourOfDay
                section?.startTime?.minutes = minute
                startDateEditText.setText(
                    section?.startTime?.let {
                        DateFormatter.shared.dateToFormattedTimestampStringWithoutSeconds(
                            it
                        )
                    }
                )
            }
            DateType.END -> {
                section?.endTime?.hours = hourOfDay
                section?.endTime?.minutes = minute
                endDateEditText.setText(
                    section?.endTime?.let {
                        DateFormatter.shared.dateToFormattedTimestampStringWithoutSeconds(
                            it
                        )
                    }
                )
            }
        }
    }
}
