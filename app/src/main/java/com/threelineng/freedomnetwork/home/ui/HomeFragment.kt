package com.threelineng.freedomnetwork.home.ui

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
import androidx.recyclerview.widget.GridLayoutManager
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.FragmentHomeBinding
import com.threelineng.freedomnetwork.home.domain.HomeItemType
import com.tegritech.commons.utils.viewBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val binding by viewBinding<FragmentHomeBinding>()

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            val homeAdapter = HomeGridViewAdapter(requireContext(), HomeViewModel.homeList) {
                when (it.type) {

                    HomeItemType.WITHDRAW -> {
                        findNavController().navigate(R.id.withdrawal_nav_graph)
                    }

                    HomeItemType.TRANSFER -> {
                        findNavController().navigate(R.id.transfer_nav_graph)
                    }

                    HomeItemType.END_OF_DAY -> {
                        findNavController().navigate(R.id.end_of_day_nav_graph)

                    }

                    HomeItemType.AIRTIME -> {
//                        findNavController().navigate(R.id.action_homeFragment_to_airtime_nav_graph)
                    }

                    HomeItemType.TRANS_HISTORY -> {
                        findNavController().navigate(R.id.action_homeFragment_to_transactionRecordFragment)
                    }

                    HomeItemType.BANK_NETWORK -> {
                        findNavController().navigate(R.id.action_homeFragment_to_network_nav_graph)
                    }

                    HomeItemType.CHECK_CARD_BALANCE -> {
//                        findNavController().apply {
//                            setGraph(R.navigation.card_nav_graph)
//                            navigate(R.id.selectAccountFragment)
//                        }
                    }

                    HomeItemType.BILLS -> {
                        findNavController().navigate(R.id.action_homeFragment_to_bills_nav_graph)
                    }
                }
            }

            binding.homeGrid.apply {
                adapter = homeAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
//                setHasFixedSize(true)
//                isNestedScrollingEnabled = false
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    homeViewModel.uiState.collect { state ->
                        with(state) {
                            agentNameTextView.text = agentName
                        }
                    }
                }
            }
        }
    }
}