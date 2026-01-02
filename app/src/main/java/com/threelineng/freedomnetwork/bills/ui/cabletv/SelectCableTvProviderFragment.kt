package com.threelineng.freedomnetwork.bills.ui.cabletv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.showSnack
import com.threelineng.freedomnetwork.databinding.FragmentSelectCableTvProviderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SelectCableTvProviderFragment : Fragment() {

    private val binding by viewBinding<FragmentSelectCableTvProviderBinding>()
    private lateinit var listViewAdapter: CableTvProviderListAdapter
    private val viewModel: CableTvViewModel by hiltNavGraphViewModels(R.id.bills_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_cable_tv_provider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            listViewAdapter = CableTvProviderListAdapter {
                findNavController().navigate(SelectCableTvProviderFragmentDirections.actionSelectCableTvProviderFragmentToCableTVFragment(it))
            }
            recyclerView.apply {
                adapter = listViewAdapter
                layoutManager = LinearLayoutManager(requireContext())
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
                            } else if (providers.isEmpty()) {
                                progressBar.visibility = View.GONE
                                emptyContent.root.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                            } else {
                                recyclerView.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                                emptyContent.root.visibility = View.GONE
                                listViewAdapter.submitList(providers)
                            }
                        }
                    }
                }
            }
        }
    }
}