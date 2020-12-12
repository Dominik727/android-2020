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
        private var lecture = Lecture()
    }

    private var sections: ArrayList<Section> = ArrayList()
    private var sectionList: ArrayList<String> = ArrayList()
    private var rooms: ArrayList<Room> = ArrayList()
    private var roomList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_detail)

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
    }

    private fun roomSpinnerLoader() {
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, roomList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(room_spinner) {
            adapter = bb
            setSelection(0, true)
            onItemSelectedListener = this@LectureDetailActivity
            prompt = context.getString(R.string.select_room)
            gravity = Gravity.CENTER
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        toast(position.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        toast("Nem választott ki szekciót!")
    }
}
