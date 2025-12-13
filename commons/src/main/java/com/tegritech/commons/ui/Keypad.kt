package com.tegritech.commons.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.tegritech.commons.R
import vibrate

class Keypad : LinearLayout {

    private var view: View? = null
    private var output: TextView? = null
    private var outputResId: Int = -1

    private var isNumberPad = false

    // Amount mode --------------------------------------------
    private var amountCents = 0L
    private var maxDigits = 11          // prevent overflow (999,999,999.99)
    private var currencySymbol = "₦"     // "$" or "€" etc.
    // ---------------------------------------------------------

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        view = inflate(context, R.layout.number_pad_layout, this)

        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.Keypad, 0, 0).apply {
                isNumberPad = this.getBoolean(R.styleable.Keypad_isNumberPad, false)
                outputResId = this.getResourceId(R.styleable.Keypad_receiver, -1)
            }
        }

        updateUI()
        setupButtons()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        output = (parent as? View)?.findViewById(outputResId)
        updateAmountText()
    }

    // ------------------------------------------------------------
    // BUTTON HANDLING
    // ------------------------------------------------------------

    private fun setupButtons() {
        val buttons = listOf(
            R.id.btn_0 to 0,
            R.id.btn_1 to 1,
            R.id.btn_2 to 2,
            R.id.btn_3 to 3,
            R.id.btn_4 to 4,
            R.id.btn_5 to 5,
            R.id.btn_6 to 6,
            R.id.btn_7 to 7,
            R.id.btn_8 to 8,
            R.id.btn_9 to 9,
            R.id.btn_00 to 0   // will get treated as single zero input
        )

        buttons.forEach { (id, digit) ->
            view!!.findViewById<MaterialCardView>(id).setOnClickListener {
                onDigitPressed(digit)
            }
        }

        // DELETE button
        view!!.findViewById<MaterialCardView>(R.id.btn_Del).setOnClickListener {
            deleteDigit()
        }
    }

    // ------------------------------------------------------------
    // AMOUNT LOGIC (THIS IS THE IMPORTANT PART)
    // ------------------------------------------------------------

    private fun onDigitPressed(digit: Int) {
        vibrate()

        val digitCount = amountCents.toString().length
        if (digitCount >= maxDigits) return

        amountCents = amountCents * 10 + digit

        updateAmountText()
    }

    private fun deleteDigit() {
        vibrate()
        amountCents /= 10
        updateAmountText()
    }

    private fun updateAmountText() {
        val formatted = String.format("%s%,.2f", currencySymbol, amountCents / 100.0)
        output?.text = formatted
    }

    // ------------------------------------------------------------
    // SETTINGS
    // ------------------------------------------------------------

    fun setCurrencySymbol(symbol: String) {
        this.currencySymbol = symbol
        updateAmountText()
    }

    fun setMaxDigits(max: Int) {
        this.maxDigits = max
    }

    fun clearAmount() {
        amountCents = 0
        updateAmountText()
    }

    fun getAmountCents(): Long = amountCents

    // ------------------------------------------------------------
    // EXISTING FEATURES
    // ------------------------------------------------------------

    private fun updateUI() {
        if (!isNumberPad) {
            view!!.findViewById<MaterialCardView>(R.id.btn_00).visibility = View.GONE
            view!!.findViewById<MaterialCardView>(R.id.btn_0)
                .layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 2.0F)
        } else {
            view!!.findViewById<MaterialCardView>(R.id.btn_00).visibility = View.VISIBLE
            view!!.findViewById<MaterialCardView>(R.id.btn_0).layoutParams =
                view!!.findViewById<MaterialCardView>(R.id.btn_00).layoutParams
        }
        invalidate()
    }

    private fun vibrate() {
        context.vibrate()
    }
}
