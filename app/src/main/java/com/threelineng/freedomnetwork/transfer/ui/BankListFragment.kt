package com.threelineng.freedomnetwork.transfer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.showSnack
import com.threelineng.freedomnetwork.databinding.FragmentBankListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BankListFragment : Fragment() {

    private val binding by viewBinding<FragmentBankListBinding>()

    private val bankViewModel: BankViewModel by viewModels()
    private lateinit var bankListViewAdapter: BankListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        bankListViewAdapter = BankListViewAdapter {
            setFragmentResult(BANK_ITEM_REQUEST, bundleOf(BANK_ITEM_BUNDLE to it))
            findNavController().popBackStack()
        }

        binding.recyclerView.apply {
            adapter = bankListViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.editTextSearch.doOnTextChanged { text, start, before, count ->
            bankViewModel.searchFilterChanged(text?.trim().toString())
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bankViewModel.errorState.collectLatest { error ->
                    showSnack(error)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bankViewModel.uiState.collect { state ->
                    with(state) {
                        if (isLoading) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.emptyContent.root.visibility = View.GONE
                            binding.recyclerView.visibility = View.GONE
                        } else if (banks.isEmpty()) {
                            binding.progressBar.visibility = View.GONE
                            binding.emptyContent.root.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            bankListViewAdapter.submitList(banks)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "BankListFragment"
        const val bankKey = "bankKey"

        const val BANK_ITEM_REQUEST = "bankItemRequest"
        const val BANK_ITEM_BUNDLE = "bankItemBundle"
    }

}