package com.threelineng.freedomnetwork.transfer

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.ui.LoadingDialogFragment
import com.threelineng.freedomnetwork.common.utils.cancelProgress
import com.threelineng.freedomnetwork.common.utils.shortToast
import com.threelineng.freedomnetwork.common.utils.showProgress
import com.threelineng.freedomnetwork.common.utils.showSnack
import com.threelineng.freedomnetwork.common.utils.toEditable
import com.threelineng.freedomnetwork.common.utils.verifyIfEditTextIsFilled
import com.threelineng.freedomnetwork.databinding.FragmentTransferBinding
import com.threelineng.freedomnetwork.transfer.domain.BankItemModel
import com.threelineng.freedomnetwork.transfer.ui.BankListFragment
import com.threelineng.freedomnetwork.transfer.ui.TransferViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TransferFragment : Fragment() {
    private val binding by viewBinding<FragmentTransferBinding>()
    private val transferViewModel: TransferViewModel by viewModels()
    private val loadingDialog by lazy { LoadingDialogFragment() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setFragmentResultListener(BankListFragment.BANK_ITEM_REQUEST) { requestKey, bundle ->
            val bank = bundle.getParcelable<BankItemModel>(requestKey)
            bank?.let {
                bankTextField.text = it.name.toEditable()
                transferViewModel.setBankItemModel(it)
            }
        }
            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            bankTextField.apply {
                isFocusable = false
                isEnabled = true
                isCursorVisible = false
                setOnClickListener {
                    findNavController().navigate(R.id.action_transferFragment_to_bankListFragment)
                }
            }

            binding.accountNumberTextField.doOnTextChanged { text, start, before, count ->
                transferViewModel.accountNumberChanged(text?.trim().toString())
            }
            continueButton.setOnClickListener {
                if (verifyIfEditTextIsFilled(
                        accountNumberTextField,
                        amountTextField,
                        errorMessage = getString(R.string.error_required)
                    )
                ) {
                    if (transferViewModel.uiState.value.selectedBank == null) {
                        requireContext().shortToast("Select a bank!")
                        return@setOnClickListener
                    }
                    val accountNumber = accountNumberTextField.text.toString().trim()
                    val amount = amountTextField.text.toString().trim()

                }
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    transferViewModel.errorState.collectLatest { error ->
                        showSnack(error)
                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    transferViewModel.paymentSuccess.collectLatest { receipt ->

                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    transferViewModel.uiState.collect { state ->
                        with(state) {
                            if (isAccountNameLoading) {
                                continueButton.showProgress()
                            } else {
                                continueButton.cancelProgress()
                            }
                            if (isTransferring) {
                                if (!loadingDialog.isAdded) {
                                    loadingDialog.show(childFragmentManager, LoadingDialogFragment.TAG)
                                }
                            } else {
                                if (loadingDialog.isAdded) {
                                    loadingDialog.dismiss()
                                }
                            }
                            accountName?.let {
                                if (it.isNotEmpty()) { //if account has been validated
                                    accountNameTextview.text = it
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}