package fm.battery.info.ui.running_apps

import android.app.ActivityManager
import android.app.AlertDialog
import android.app.usage.UsageStatsManager
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fm.battery.info.R
import fm.battery.info.databinding.FragmentRunningAppsBinding
import fm.battery.info.toRunningApp

class RunningAppsFragment : Fragment(R.layout.item_running_app) {

    private var _binding: FragmentRunningAppsBinding? = null
    private val binding get() = _binding!!


    private val adapter: RunningAppsAdapter by lazy {
        RunningAppsAdapter(requireContext())
    }
    private val activityManager by lazy {
        requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRunningAppsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        fetchRunningApps()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
    }

    private fun fetchRunningApps() {
        val usageStatsManager =
            requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1)
        }
        val queryStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
            calendar.timeInMillis,
            System.currentTimeMillis())

        val packageManager = requireContext().packageManager
        val adapterItems = queryStats.map { it.toRunningApp(packageManager) }.filterNotNull()
        adapter.updateData(adapterItems)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}