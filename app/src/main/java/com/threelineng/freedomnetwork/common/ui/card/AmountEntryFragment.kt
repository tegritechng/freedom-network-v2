package com.threelineng.freedomnetwork.common.ui.card

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.FragmentAmountEntryBinding

class AmountEntryFragment : Fragment(R.layout.fragment_amount_entry) {
    private val binding by viewBinding<FragmentAmountEntryBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.continueButton?.setOnClickListener {
            findNavController().navigate(R.id.action_amountEntryFragment_to_insertCardFragment)
        }
    }
}