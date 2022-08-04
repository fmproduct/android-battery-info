package fm.battery.info.ui.battery_history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fm.battery.info.R
import fm.battery.info.core.data.AppBatteryHistoryStorage
import fm.battery.info.databinding.FragmentBatteryHistoryBinding

class BatteryHistoryFragment: Fragment(R.layout.fragment_battery_history) {


    private var _binding: FragmentBatteryHistoryBinding? = null
    private val binding get() = _binding!!

    private val storage by lazy { AppBatteryHistoryStorage(requireContext()) }
    private val adapter by lazy { BatterHistoryAdapter(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBatteryHistoryBinding.bind(view)

        binding.historyRecycler.adapter = adapter
        binding.refreshFab.setOnClickListener {
            fetchHistory()
        }
        fetchHistory()
    }

    private fun fetchHistory(){
        adapter.updateData(storage.getAllValue())
    }

}