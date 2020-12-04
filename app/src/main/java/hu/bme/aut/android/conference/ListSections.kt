/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 4
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.model.Section
import hu.bme.aut.filmdatabase.network.SectionNetworkManager
import kotlinx.android.synthetic.main.fragment_list_sections.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListSections : Fragment(), SectionAdapter.OnSectionSelectedListener {

    private lateinit var adapter: SectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_list_sections, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        MainRecyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = SectionAdapter(this)
        onSectionAdded()
        MainRecyclerView.adapter = adapter
    }

    private fun onSectionAdded() {
        SectionNetworkManager.getSections().enqueue(object : Callback<List<Section>> {
            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<List<Section>>, response: Response<List<Section>>) {
                for (x in response.body()!!) {
                    adapter.addSection(x)
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<Section>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onSectionmSelected(section: Section?) {
        TODO("Not yet implemented")
    }
}
