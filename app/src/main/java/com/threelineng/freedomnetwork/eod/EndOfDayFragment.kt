package com.threelineng.freedomnetwork.eod

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tegritech.commons.utils.viewBinding
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.toDateString
import com.threelineng.freedomnetwork.databinding.FragmentEndOfDayBinding
import com.threelineng.freedomnetwork.eod.domain.EndOfDayReportType
import com.threelineng.freedomnetwork.eod.ui.EndOfDayViewModel
import com.threelineng.freedomnetwork.home.ui.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.getValue

class EndOfDayFragment : Fragment() {
    private val binding by viewBinding<FragmentEndOfDayBinding>()
    private val viewModel: EndOfDayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_of_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            backIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            // Get today and 7 days back in UTC millis
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val sevenDaysBack = today - TimeUnit.DAYS.toMillis(7)
            textFieldBg.setOnClickListener {
                val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText(getString(R.string.select_date_range))
                    .setSelection(Pair(sevenDaysBack, today))
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointBackward.now())
                            .build()
                    )
                    .build()

                // Show the picker
                dateRangePicker.show(
                    childFragmentManager,
                    "DATE_RANGE_PICKER"
                )

                dateRangePicker.addOnPositiveButtonClickListener { selection ->
                    val start = selection.first
                    val end = selection.second

                    // Only proceed if both dates are non-null
                    start?.let { startDate ->
                        val startText = startDate.toDateString("MMM dd, yyyy")
                        val endText = end?.toDateString("MMM dd, yyyy")

                        tvTime.text = listOfNotNull(startText, endText).joinToString(" - ")

                        viewModel.setDates(
                            startDate = startDate.toDateString(),
                            endDate = end?.toDateString()
                        )
                    }

                }
            }

            fullReportTextView.setOnClickListener {
                viewModel.changeReportType(EndOfDayReportType.FULL_REPORT)
            }

            summaryTextView.setOnClickListener {
                viewModel.changeReportType(EndOfDayReportType.SUMMARY)
            }

            previewSummaryButton.setOnClickListener {
                findNavController().navigate(R.id.action_endOfDayFragment_to_endOfDaySummaryFragment)
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collectLatest { state ->
                        with(state) {
                            when (reportType) {
                                EndOfDayReportType.SUMMARY -> {
                                    summaryTextView.setSelectedStyle()
                                    fullReportTextView.setUnselectedStyle()
                                }

                                EndOfDayReportType.FULL_REPORT -> {
                                    fullReportTextView.setSelectedStyle()
                                    summaryTextView.setUnselectedStyle()
                                }

                                null -> {
                                    summaryTextView.setUnselectedStyle()
                                    fullReportTextView.setUnselectedStyle()
                                }
                            }

                        }
                    }
                }
            }

        }
    }

    private fun TextView.setSelectedStyle() {
        setTextColor(
            ColorStateList.valueOf(requireContext().getColor(R.color.white))
        )
        backgroundTintList = ColorStateList.valueOf(
            Color.parseColor("#5D6374")
        )
    }

    private fun TextView.setUnselectedStyle() {
        setTextColor(Color.parseColor("#455770"))
        backgroundTintList = ColorStateList.valueOf(
            Color.parseColor("#F1F2F6")
        )
    }

}