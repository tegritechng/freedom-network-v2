package com.tegritech.commons.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.Locale

class CommaAmountTextWatcher(private val editText: EditText) : TextWatcher {

    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val input = s.toString()
        if (input == current) return

        editText.removeTextChangedListener(this)

        val clean = input.replace(",", "")
        if (clean.isEmpty()) {
            current = ""
            editText.setText("")
            editText.addTextChangedListener(this)
            return
        }

        try {
            val number = clean.toBigInteger()
            val formatted = NumberFormat.getNumberInstance(Locale.US).format(number)

            current = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        editText.addTextChangedListener(this)
    }
}