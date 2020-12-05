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
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Base.DateFormatter
import hu.bme.aut.android.conference.HomeDashboard
import hu.bme.aut.android.conference.Network.SectionNetworkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Section
import java.util.*
import kotlinx.android.synthetic.main.activity_home_dashboard.*
import kotlinx.android.synthetic.main.activity_section_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SectionDetail :
    BaseActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener,
    SectionAdapter.OnSectionSelectedListener { // ktlint-disable max-line-length

    enum class DateType {
        START,
        END
    }

    companion object {
        var listener: SectionAddedListener? = null
    }

    var section: Section? = null
    var dateType: DateType? = null
    var startTime: Date? = null
    var endTime: Date? = null
    private lateinit var adapter: SectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        if (section == null) {
            this.section = Section()
        }
        initfab()

        adapter = SectionAdapter(this)
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
            if (startTime == null) {
                endDateEditText.error = "Kérem töltse ki a kezdő dátumot!"
                endDateEditText.requestFocus()
                return@setOnClickListener
            }
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = startTime?.time ?: Date().time
            dateType = DateType.END
            datePickerDialog.show()
        }
    }

    fun initfab() {
        save_fab.setOnClickListener {
            if (startTime?.time ?: 1 >= endTime?.time ?: 0) {
                endDateEditText.error = "A végző dátum nem lehet kisebb a kezdő dátumnál"
                endDateEditText.requestFocus()
                return@setOnClickListener
            }
            section?.name = editTextTextSectionName.text.toString()
            section?.endTime = endTime?.let { it1 ->
                DateFormatter.shared.dateToGsonDateFormatDate(it1)
            }
            section?.startTime = startTime?.let { it1 ->
                DateFormatter.shared.dateToGsonDateFormatDate(it1)
            }
            section?.let { it1 ->
                HomeDashboard.Auth_KEY?.let { it2 ->
                    SectionNetworkManager.newSection(it2, it1).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                toast("Szekció elmentve!")
                                listener?.sectionAdded()
                                onBackPressed()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            toast(getString(R.string.no))
                        }
                    })
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        when (dateType) {
            DateType.START -> {
                startTime = Date(calendar.timeInMillis)
            }
            DateType.END -> {
                endTime = Date(calendar.timeInMillis)
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
                startTime?.hours = hourOfDay
                startTime?.minutes = minute
                startDateEditText.setText(
                    startTime?.let {
                        DateFormatter.shared.dateToFormattedTimestampStringWithoutSeconds(
                            it
                        )
                    }
                )
            }
            DateType.END -> {
                endTime?.hours = hourOfDay
                endTime?.minutes = minute
                endDateEditText.setText(
                    endTime?.let {
                        DateFormatter.shared.dateToFormattedTimestampStringWithoutSeconds(
                            it
                        )
                    }
                )
            }
        }
    }

    override fun onSectionSelected(section: Section) {
    }

    override fun OnLongSectionListener(section: Section) {
        TODO("Not yet implemented")
    }

    interface SectionAddedListener {
        fun sectionAdded()
    }
}
