package com.threelineng.freedomnetwork.withdrawal

import android.graphics.Color
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.threelineng.freedomnetwork.R

class ReadingCardFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_card, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(InsetDrawable(Color.TRANSPARENT.toDrawable(), 10))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        Glide.with(requireContext()).load(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.reading_card_gif
            )
        ).into(view.findViewById(R.id.card))

    }

    companion object {
        val TAG = "ReadingCardFragment"
    }
}