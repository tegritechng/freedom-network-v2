package com.threelineng.freedomnetwork.bills

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.ContentBillsItemBinding
import com.threelineng.freedomnetwork.databinding.FragmentBillsBinding

enum class Bills(val icon: Int, val title: String) {
    AIRTIME_DATA(R.drawable.ic_airtime, "Airtime/Data"), ELECTRICITY(
        R.drawable.ic_electricity, "Electricity"
    ),
    CABLE_TV(R.drawable.ic_cable_tv, "Cable TV"), BETTING(R.drawable.ic_betting, "Sport Betting")
}

class BillsFragment : Fragment() {
    private val binding by viewBinding<FragmentBillsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            Bills.entries.map { bill ->
                bodyContent.addView(
                    ContentBillsItemBinding.inflate(
                        LayoutInflater.from(context), null, false
                    ).apply {
                        icon.setImageDrawable(requireContext().getDrawable(bill.icon))
                        title.text = bill.title
                    }.root.apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 12, 0, 0) // Spacing between rows
                        }
                        setOnClickListener {
                            when (bill) {
                                Bills.AIRTIME_DATA -> {}
                                Bills.ELECTRICITY -> {
                                    findNavController().navigate(R.id.action_billsFragment_to_selectElectricityDiscoFragment)
                                }

                                Bills.CABLE_TV -> {
                                    findNavController().navigate(R.id.action_billsFragment_to_selectCableTvProviderFragment)
                                }

                                Bills.BETTING -> {}
                            }
                        }
                    })
            }
        }
    }
}