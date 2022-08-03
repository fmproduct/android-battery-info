package fm.battery.info

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fm.battery.info.core.BatteryInfoBroadcast
import fm.battery.info.core.BatteryInfoListener
import fm.battery.info.core.enums.ChargingType
import fm.battery.info.databinding.FragmentBatteryInfoBinding


class BatteryInfoFragment: Fragment(R.layout.fragment_battery_info), BatteryInfoListener {

    private var _binding: FragmentBatteryInfoBinding? = null
    private val binding get() = _binding!!

    private val broadcast = BatteryInfoBroadcast().apply {
        setBatterInfoListener(this@BatteryInfoFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBatteryInfoBinding.bind(view)
        setupBroadcast()
    }

    private fun setupBroadcast(){
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
        when(percent){
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