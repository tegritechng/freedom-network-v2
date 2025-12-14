package com.threelineng.freedomnetwork.eod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.FragmentEndOfDayBinding

class EndOfDayFragment : Fragment() {
    private val binding by viewBinding<FragmentEndOfDayBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_of_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }
            continueButton.setOnClickListener {
                findNavController().navigate(R.id.action_endOfDayFragment_to_endOfDaySummaryFragment)
            }
        }
    }
}