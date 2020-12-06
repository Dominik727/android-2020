/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.model

import hu.bme.aut.android.conference.enum.userType

data class User(
    val id: Long?,
    val username: String,
    val password: String,
    val email: String,
    val role: userType,
    val phoneNumber: String?,
    val verified: Boolean,
    var sections: ArrayList<Section>,
    var lectures: ArrayList<Lecture>
)
