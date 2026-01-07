package com.threelineng.freedomnetwork.bills.ui.airtime

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tegritech.commons.ui.CommaAmountTextWatcher
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.bills.ui.airtime.ui.AirtimeDataViewModel
import com.threelineng.freedomnetwork.bills.ui.airtime.ui.AmountAdapter
import com.threelineng.freedomnetwork.common.utils.cleanAmount
import com.threelineng.freedomnetwork.common.utils.toEditable
import com.threelineng.freedomnetwork.common.utils.verifyIfEditTextIsFilled
import com.threelineng.freedomnetwork.databinding.FragmentAirtimeDataBinding

class AirtimeDataFragment : Fragment() {

    private val viewModel: AirtimeDataViewModel by viewModels()
    private val binding by viewBinding<FragmentAirtimeDataBinding>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_airtime_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Called when a tab enters the selected state
                    val position = tab?.position
                    // Handle tab selection
                    viewModel.toggleType()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Called when a tab exits the selected state
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Called when an already selected tab is selected again
                }
            })

            packageTextField.apply {
                isFocusable = false
                isEnabled = true
                isCursorVisible = false
                doAfterTextChanged {}
                setOnClickListener {}
            }
            amountTextField.addTextChangedListener(CommaAmountTextWatcher(amountTextField))

            val amountAdapter = AmountAdapter {
                amountTextField.text = it.toEditable()
            }

            amountRecyclerView.apply {
                adapter = amountAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }
            continueButton.setOnClickListener {
                val state = viewModel.uiState.value

                val fields = buildList {
                    add(phoneNumberTextField)
                    add(providerTextField)
                    if (!state.isAirtime) {
                        add(packageTextField)
                    }
                    add(amountTextField)
                }
                if (verifyIfEditTextIsFilled(*fields.toTypedArray(), errorMessage = getString(R.string.error_required))) {
                    val amount = amountTextField.text.toString().trim().cleanAmount().toLongOrNull() ?: 0

                }
            }
        }
    }
}