/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

data class Room(
    val id: Long,
    val capacity: Int,
    val lectures: ArrayList<Lecture>
)
