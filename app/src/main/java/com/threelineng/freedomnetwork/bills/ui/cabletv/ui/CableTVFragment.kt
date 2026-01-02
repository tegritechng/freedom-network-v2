package com.threelineng.freedomnetwork.bills.ui.cabletv.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.bills.ui.cabletv.CableTvViewModel
import com.threelineng.freedomnetwork.common.utils.showSnack
import com.threelineng.freedomnetwork.common.utils.verifyIfEditTextIsFilled
import com.threelineng.freedomnetwork.databinding.FragmentCableTVBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class CableTVFragment : Fragment() {

    private val args: CableTVFragmentArgs by navArgs()
    private val binding by viewBinding<FragmentCableTVBinding>()

    private val viewModel: CableTvViewModel by hiltNavGraphViewModels(R.id.bills_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cable_t_v, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            iconBg.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FCFCFC"))

            with(args.providerItem) {
                biller.text = name
                billerIcon.setImageDrawable(requireContext().getDrawable(icon))
            }

            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            packageTextField.apply {
                isFocusable = false
                isEnabled = true
                isCursorVisible = false
                doAfterTextChanged {

                }
                setOnClickListener {

                }
            }
            continueButton.setOnClickListener {
                if (verifyIfEditTextIsFilled(
                        smartCardNumberTextField, amountTextField,
                        errorMessage = getString(R.string.error_required)
                    )
                ) {
                    val smartCardNumber = smartCardNumberTextField.text.toString().trim()
                    val amount = amountTextField.text.toString().trim()
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.errorState.collectLatest { error ->
                        showSnack(error)
                    }
                }
            }
        }
    }
}