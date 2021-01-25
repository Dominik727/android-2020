package hu.bme.aut.android.conference.extensions

import android.widget.EditText
import hu.bme.aut.android.conference.R

fun EditText.validateNonEmpty(): Boolean {
    if (text.isEmpty()) {
        error = context.getString(R.string.Required)
        return false
    }
    return true
}
