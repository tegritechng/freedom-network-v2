package com.threelineng.freedomnetwork.withdrawal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.threelineng.freedomnetwork.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertCardFragment : Fragment(R.layout.fragment_insert_card) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToSelectAccountType()
    }

    private fun navigateToSelectAccountType() {
        lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(R.id.action_insertCardFragment_to_selectAccountTypeFragment)
        }
    }

}