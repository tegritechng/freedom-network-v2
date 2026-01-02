package com.threelineng.freedomnetwork.network

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.showSnack
import com.threelineng.freedomnetwork.databinding.FragmentNetworkBinding
import com.threelineng.freedomnetwork.network.ui.NetworkListViewAdapter
import com.threelineng.freedomnetwork.network.ui.NetworkViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class NetworkFragment : Fragment() {

    private val binding by viewBinding<FragmentNetworkBinding>()
    private lateinit var listViewAdapter: NetworkListViewAdapter
    private val viewModel: NetworkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            listViewAdapter = NetworkListViewAdapter {

            }

            recyclerView.apply {
                adapter = listViewAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            editTextSearch.doOnTextChanged { text, start, before, count ->
                viewModel.searchFilterChanged(text?.trim().toString())
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
                            if (isLoading) {
                                progressBar.visibility = View.VISIBLE
                                emptyContent.root.visibility = View.GONE
                                recyclerView.visibility = View.GONE
                            } else if (networks.isEmpty()) {
                                progressBar.visibility = View.GONE
                                emptyContent.root.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                            } else {
                                recyclerView.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                                emptyContent.root.visibility = View.GONE
                                listViewAdapter.submitList(networks)
                            }
                        }
                    }
                }
            }

        }
    }
}