/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 12
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Lecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.conference.R

class ListLectureFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_list, container, false)
    }
}
