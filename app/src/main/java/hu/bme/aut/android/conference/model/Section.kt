/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 5
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

import java.util.*

data class Section(
    val id: Long?,
    val name: String?,
    var startTime: Date?,
    var endTime: Date?,
    val lectures: List<Any>?
) {
    constructor() : this(null, null, null, null, null)
}
