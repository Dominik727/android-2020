/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 5
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

import java.util.Date

data class Section(
    val id: Long?,
    var name: String?,
    var startTime: String?,
    var endTime: String?,
    val lectures: List<Any>?,
    val users: List<User>?
) {
    constructor() : this(null, null, null, null, null, null)
}
