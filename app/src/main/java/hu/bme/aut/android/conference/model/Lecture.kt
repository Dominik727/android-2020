/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

data class Lecture(
    val id: Long?,
    var name: String?,
    var description: String?,
    var price: Int?,
    var section: Section?,
    var room: Room?
) {
    constructor() : this(null, null, null, null, null, null)
}

