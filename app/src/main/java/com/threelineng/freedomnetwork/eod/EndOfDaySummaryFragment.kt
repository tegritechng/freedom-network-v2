package com.threelineng.freedomnetwork.eod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.FragmentEndOfDaySummaryBinding

class EndOfDaySummaryFragment : Fragment() {
    private val binding by viewBinding<FragmentEndOfDaySummaryBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_of_day_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            printButton.setOnClickListener {

            }
        }
    }

}