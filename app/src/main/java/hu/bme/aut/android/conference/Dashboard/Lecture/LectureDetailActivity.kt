/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 12
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Lecture

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.Network.LectureNetWorkManager
import hu.bme.aut.android.conference.Network.RoomNetWorkManager
import hu.bme.aut.android.conference.Network.SectionNetworkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Lecture
import hu.bme.aut.android.conference.model.Room
import hu.bme.aut.android.conference.model.Section
import kotlinx.android.synthetic.main.activity_lecture_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LectureDetailActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        var listener: LecturenAddedListener? = null
        var lecture = Lecture()
    }

    private var sections: ArrayList<Section> = ArrayList()
    private var sectionList: ArrayList<String> = ArrayList()
    private var rooms: ArrayList<Room> = ArrayList()
    private var roomList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_detail)

        editTextTextLecturenName.setText(lecture.name)
        editTextTextLectureDescription.setText(lecture.description)
        editTextTextLecturePrice.setText(lecture.price.toString())

        if (lecture.id != null) disableFields()

        HomeDashboard.Auth_KEY?.let { token ->
            SectionNetworkManager.getSections(token).enqueue(object : Callback<List<Section>> {
                override fun onResponse(
                    call: Call<List<Section>>,
                    response: Response<List<Section>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach {
                            sections.add(it)
                            it.name?.let { it1 -> sectionList.add(it1) }
                        }
                        sectionSpinnerLoader()
                    }
                }

                override fun onFailure(call: Call<List<Section>>, t: Throwable) {
                    toast(getString(R.string.error))
                }
            })
        }
        HomeDashboard.Auth_KEY?.let { token ->
            RoomNetWorkManager.getAllRoom(token).enqueue(object : Callback<List<Room>> {
                override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                    if (response.isSuccessful) {
                        response.body()?.forEach {
                            rooms.add(it)
                            it.name?.let { it1 -> roomList.add(it1) }
                        }
                        roomSpinnerLoader()
                    }
                }

                override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                    toast(getString(R.string.error))
                }
            })
        }

        save_lecture_fab.setOnClickListener {
            lecture.name = editTextTextLecturenName.text.toString()
            lecture.description = editTextTextLectureDescription.text.toString()
            lecture.price = editTextTextLecturePrice.text.toString().toInt()

            HomeDashboard.Auth_KEY?.let { it1 ->
                LectureNetWorkManager.newLecture(it1, lecture).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            toast(getString(R.string.lecture_saved))
                            listener?.lectureAdded()
                            onBackPressed()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        toast(getString(R.string.no))
                    }
                })
            }
        }

        btnInterest.setOnClickListener {
            if (HomeDashboard.USER?.lectures?.contains(lecture) != true) {
                lecture.id?.let { lectureID ->
                    HomeDashboard.Auth_KEY?.let { token ->
                        HomeDashboard.USER?.let { user ->
                            LectureNetWorkManager.addUserToLecture(
                                token, lectureID,
                                user
                            ).enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.isSuccessful) {
                                        HomeDashboard.USER!!.lectures.add(
                                            lecture
                                        )
                                        btnInterest.text = getString(R.string.unsubscribe_btn)
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    toast(getString(R.string.Add_unsuccess))
                                }
                            })
                        }
                    }
                }
            } else {
                lecture.id?.let { lectureId ->
                    HomeDashboard.Auth_KEY?.let { token ->
                        LectureNetWorkManager.removeUserFromLecture(
                            token,
                            lectureId, HomeDashboard.USER!!
                        ).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    HomeDashboard.USER!!.lectures.remove(lecture)
                                    btnInterest.text =
                                        getString(R.string.interest_lecture)
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                toast(getString(R.string.delete_unsuccess))
                            }
                        })
                    }
                }
            }
        }
    }

    private fun sectionSpinnerLoader() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(section_spinner) {
            id = 0
            adapter = aa
            setSelection(0, true)
            onItemSelectedListener = this@LectureDetailActivity
            prompt = context.getString(R.string.select_section)
            gravity = Gravity.CENTER
        }

        if (lecture.section != null) {
            section_spinner.setSelection(sections.indexOfFirst { it.id == lecture.section!!.id })
        } else {
            lecture.section = sections.first()
        }
    }

    private fun roomSpinnerLoader() {
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, roomList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(room_spinner) {
            id = 1.toInt()
            adapter = bb
            setSelection(0, true)
            onItemSelectedListener = this@LectureDetailActivity
            prompt = context.getString(R.string.select_room)
            gravity = Gravity.CENTER
        }

        if (lecture.room != null) {
            section_spinner.setSelection(rooms.indexOfFirst { it.id == lecture.room!!.id })
        } else {
            lecture.section = sections.first()
        }
    }

    private fun disableFields() {
        editTextTextLectureDescription.isEnabled = false
        editTextTextLecturenName.isEnabled = false
        editTextTextLecturePrice.isEnabled = false
        room_spinner.isEnabled = false
        section_spinner.isEnabled = false
        btnInterest.visibility = View.VISIBLE
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            0 -> {
                lecture.section = sections[position]
            }
            1 -> {
                lecture.room = rooms[position]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        toast("Nem választott ki semmit!")
    }

    interface LecturenAddedListener {
        fun lectureAdded()
    }
}
