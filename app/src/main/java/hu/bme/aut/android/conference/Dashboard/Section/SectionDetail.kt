/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Section

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Base.DateFormatter
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
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
        var section: Section = Section()
    }

    var dateType: DateType? = null
    private var startTime: Calendar = Calendar.getInstance()
    private var endTime: Calendar = Calendar.getInstance()
    private lateinit var adapter: SectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        if (HomeDashboard.USER?.sections?.contains(section) == true) {
            btnInterest.text = getString(R.string.unsubscribe_btn)
            section = HomeDashboard.USER!!.sections.first { it.id == section.id }.copy()
        }
        initfab()

        adapter = SectionAdapter(this)
        if (section.name != null) {
            this.title =
                getString(R.string.Section_detail_title) + section.name
            startDateEditText.setText(
                section.startTime?.let {
                    DateFormatter.shared.formatStringToShow(it)
                }
            )
            endDateEditText.setText(
                section.endTime?.let {
                    DateFormatter.shared.formatStringToShow(it)
                }
            )
            editTextTextSectionName.setText(section.name)
        } else {
            this.title = getString(R.string.New_section_title)
        }

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
            if (startDateEditText.text.isEmpty()) {
                endDateEditText.error = getString(R.string.endTextErrorText)
                endDateEditText.requestFocus()
                return@setOnClickListener
            }
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = startTime.timeInMillis
            dateType = DateType.END
            datePickerDialog.show()
        }
    }

    private fun initfab() {
        save_fab.setOnClickListener {
            if (startTime.timeInMillis >= endTime.timeInMillis) {
                endDateEditText.error = getString(R.string.startTextErrorText)
                endDateEditText.requestFocus()
                return@setOnClickListener
            }
            section?.name = editTextTextSectionName.text.toString()
            section?.endTime = endTime.let { it1 ->
                DateFormatter.shared.dateToGsonDateFormatDate(Date(it1.timeInMillis))
            }
            section?.startTime = startTime.let { it1 ->
                DateFormatter.shared.dateToGsonDateFormatDate(Date(it1.timeInMillis))
            }
            section?.let { it1 ->
                HomeDashboard.Auth_KEY?.let { it2 ->
                    SectionNetworkManager.newSection(it2, it1).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                toast(getString(R.string.Section_saved))
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
        btnInterest.setOnClickListener {
            if (HomeDashboard.USER?.sections?.contains(section) != true) {
                section.id?.let { it1 ->
                    SectionNetworkManager.addUserToSection(
                        HomeDashboard.Auth_KEY!!,
                        HomeDashboard.USER!!, it1
                    ).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                HomeDashboard.USER!!.sections.add(section!!)
                                btnInterest.text = getString(R.string.unsubscribe_btn)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            toast(getString(R.string.Add_unsuccess))
                        }
                    })
                }
            } else {
                SectionNetworkManager.deleteUserFromSection(
                    HomeDashboard.Auth_KEY!!,
                    HomeDashboard.USER!!, section.id!!
                ).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.code() == 404) {
                            HomeDashboard.USER!!.sections.remove(section!!)
                            btnInterest.text =
                                getString(R.string.interest_section_Button)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        toast(getString(R.string.delete_unsuccess))
                    }
                })
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        when (dateType) {
            DateType.START -> {
                startTime.set(Calendar.YEAR, year)
                startTime.set(Calendar.MONTH, month)
                startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            DateType.END -> {
                endTime.set(Calendar.YEAR, year)
                endTime.set(Calendar.MONTH, month)
                endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
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
                startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                startTime.set(Calendar.MINUTE, minute)
                startDateEditText.setText(
                    startTime.let {
                        DateFormatter.shared.dateToFormattedTimestampStringWithoutSeconds(
                            Date(it.timeInMillis)
                        )
                    }
                )
            }
            DateType.END -> {
                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                endTime.set(Calendar.MINUTE, minute)
                endDateEditText.setText(
                    endTime.let {
                        DateFormatter.shared.dateToFormattedTimestampStringWithoutSeconds(
                            Date(it.timeInMillis)
                        )
                    }
                )
            }
        }
    }

    override fun onSectionSelected(section: Section) {
    }

    override fun onLongSectionListener(section: Section) {
    }

    interface SectionAddedListener {
        fun sectionAdded()
    }
}
