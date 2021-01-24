/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 13
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Rooms

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.Network.RoomNetWorkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Room
import kotlinx.android.synthetic.main.activity_room_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailActivity : BaseActivity(), OnMapReadyCallback {

    private var hMap: HuaweiMap? = null

    companion object {
        var listener: roomnAddedListener? = null
        var room = Room()
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.setApiKey("CgB6e3x9cSEuuhrXP7y/ScSrAiDpKFPGLFYc8DnpoJ4aXHEBdhh4URkwBYmumoQSE4oklurrSpRIy+shHGQju13F")
        setContentView(R.layout.activity_room_detail)


        initHuaweiMap(savedInstanceState)

        if (room.id != null) {
            disableFields()
        } else {
            this.title = getString(R.string.addRoomTitle)
        }
        initfab()
    }

    private fun initHuaweiMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        map_view?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@RoomDetailActivity)
        }
    }

    override fun onMapReady(map: HuaweiMap?) {
        hMap = map
        hMap?.isMyLocationEnabled = true // Enable the my-location overlay.
        hMap?.uiSettings?.isMyLocationButtonEnabled = true // Enable the my-location icon.
        if (room.id != null) {
            addMarker("${room.zipCode} ${room.city} ${room.address}")
        }
    }

    private fun initfab() {
        save_room_fab.setOnClickListener {
            room.city = RoomCityEdittext.text.toString()
            room.address = RoomAddressEdittext.text.toString()
            room.capacity = editTextRoomCapacity.text.toString().toInt()
            room.zipCode = RoomZipcodeEditText.text.toString().toInt()
            room.name = RoomNameEditText.text.toString()
            HomeDashboard.Auth_KEY?.let { token ->
                RoomNetWorkManager.newRoom(token, room).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            toast(getString(R.string.Section_saved))
                            listener?.roomAdded()
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

    private fun disableFields() {
        this.title =
            getString(R.string.Room_detail_Title) + room.name
        save_room_fab.visibility = View.GONE
        RoomNameEditText.setText(room.name)
        RoomNameEditText.isEnabled = false
        editTextRoomCapacity.setText(room.capacity.toString())
        editTextRoomCapacity.isEnabled = false
        RoomZipcodeEditText.setText(room.zipCode.toString())
        RoomZipcodeEditText.isEnabled = false
        RoomAddressEdittext.setText(room.address)
        RoomAddressEdittext.isEnabled = false
        RoomCityEdittext.setText(room.city)
        RoomCityEdittext.isEnabled = false
    }

    private fun addMarker(address: String) {
        val coder = Geocoder(baseContext)
        val latlong = coder.getFromLocationName(address, 1)
        if (latlong.isNotEmpty()) {
            val point = LatLng(latlong[0].latitude, latlong[0].longitude)
            hMap?.addMarker(MarkerOptions().position(point).title("Helyszin"))
            hMap?.moveCamera(CameraUpdateFactory.newLatLng(point))
            val cp = CameraPosition.Builder()
                .zoom(12.5F)
                .target(point)
                .tilt(45.0f)
                .bearing(35.0f)
                .build()
            hMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cp))
        }
    }

    override fun onStart() {
        map_view?.onStart()
        super.onStart()
    }

    override fun onStop() {
        map_view?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        hMap?.clear()
        hMap = null
        map_view?.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        map_view?.onPause()
        super.onPause()
    }

    override fun onResume() {
        map_view?.onResume()
        super.onResume()
    }

    override fun onLowMemory() {
        map_view?.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        map_view.onSaveInstanceState(mapViewBundle)
    }

    interface roomnAddedListener {
        fun roomAdded()
    }
}
