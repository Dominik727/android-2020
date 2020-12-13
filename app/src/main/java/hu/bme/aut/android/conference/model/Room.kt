/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

data class Room(
        val id: Long?,
        var capacity: Int?,
        var name: String?,
        val lectures: ArrayList<Lecture>?,
        var zipCode: Int?,
        var address: String?,
        var city: String?
) {
    constructor() : this(null, null, null, null, null, null, null)
}
