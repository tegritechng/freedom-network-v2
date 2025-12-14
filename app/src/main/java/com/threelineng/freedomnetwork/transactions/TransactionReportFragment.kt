package com.threelineng.freedomnetwork.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.showSnack
import com.threelineng.freedomnetwork.databinding.FragmentTransactionReportBinding
import com.threelineng.freedomnetwork.transactions.ui.TransactionsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TransactionRecordFragment : Fragment() {

    private val binding by viewBinding<FragmentTransactionReportBinding>()
    private val viewModel: TransactionsViewModel by viewModels()

    private lateinit var transactionsListAdapter: TransactionsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }
            transactionsListAdapter = TransactionsListAdapter {
                findNavController().navigate(
                    R.id.action_transactionRecordFragment_to_transactionReceiptFragment
                )
//                findNavController().navigate(
//                    TransactionHistoryFragmentDirections.actionTransactionHistoryFragmentToTransactionItemDetailsFragment(
//                        it
//                    )
//                )
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = transactionsListAdapter
                setHasFixedSize(true)
//                addOnScrollListener(createInfiniteScrollListener(layoutManager as LinearLayoutManager))
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.errorState.collectLatest { error ->
                        showSnack(error)
                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collectLatest { state ->
                        with(state) {
                            if (isTransactionListLoading) {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.emptyContent.visibility = View.GONE
                                binding.recyclerView.visibility = View.GONE
                            } else if (transactionList.isEmpty()) {
                                binding.progressBar.visibility = View.GONE
                                binding.emptyContent.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                            } else if (isLoadingMoreTransactions) {
                                binding.loadingMoreBar.visibility = View.VISIBLE
                            } else {
                                binding.emptyContent.visibility = View.GONE
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                binding.loadingMoreBar.visibility = View.GONE
                                transactionsListAdapter.submitList(transactionList)
                            }
//                        binding.swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }

        }
    }
}