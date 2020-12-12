/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

data class Lecture(
    val id: Long?,
    val name: String?,
    val description: String?,
    val price: String?,
    val section: Section?,
    val room: Room?
) {
    constructor() : this(null, null, null, null, null, null)
}

