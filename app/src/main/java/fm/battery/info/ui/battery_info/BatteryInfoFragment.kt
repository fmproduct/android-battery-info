package fm.battery.info.ui.battery_info

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fm.battery.info.R
import fm.battery.info.core.BatteryInfoBroadcast
import fm.battery.info.core.BatteryInfoListener
import fm.battery.info.core.enums.ChargingType
import fm.battery.info.databinding.FragmentBatteryInfoBinding


class BatteryInfoFragment : Fragment(R.layout.fragment_battery_info), BatteryInfoListener {

    private var _binding: FragmentBatteryInfoBinding? = null
    private val binding get() = _binding!!

    private val broadcast = BatteryInfoBroadcast().apply {
        setBatterInfoListener(this@BatteryInfoFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBatteryInfoBinding.bind(view)
        binding.batteryHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_batteryInfoFragment_to_batteryHistoryFragment)
        }
        binding.runningAppsButton.setOnClickListener {
            if (checkUsageStatsPermissionGranted() && checkWriteExternalPermission()) {
                findNavController().navigate(R.id.action_batteryInfoFragment_to_runningAppsFragment)
            } else {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }
        setupBroadcast()
    }

    private fun checkUsageStatsPermissionGranted(): Boolean {
        val info =
            requireContext().packageManager.getApplicationInfo(requireContext().packageName, 0)
        val appOpsManager =
            requireContext().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        return if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                info.uid,
                info.packageName);
            (mode == AppOpsManager.MODE_ALLOWED)
        } else false
    }

    private fun checkWriteExternalPermission(): Boolean {
        val permission = Manifest.permission.KILL_BACKGROUND_PROCESSES
        val res = context!!.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun setupBroadcast() {
        requireActivity().applicationContext.let { context ->
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            context.registerReceiver(broadcast, iFilter)
        }
    }

    override fun chargingStateChanged(isCharging: Boolean) {
        binding.tvChargingState.text = "Charging state: $isCharging"
    }

    override fun chargingType(type: ChargingType) {
        binding.tvChargingType.text = "Charging type: ${type.name.uppercase()}"
    }

    override fun chargingPercentage(percent: Float) {
        binding.tvPercent.text = "Percent: %.0f%%".format(percent)
        when (percent) {
            in 0f..20f -> {
                binding.tvChargingLevel.text = "Level: Low"
            }
            in 90f..100f -> {
                binding.tvChargingLevel.text = "Level: High"
            }
            else -> {
                binding.tvChargingLevel.text = "Level: Medium"
            }
        }
    }


}