package fm.battery.info.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import fm.battery.info.core.enums.ChargingType

class BatteryInfoBroadcast: BroadcastReceiver() {

    private var batteryInfoListener: BatteryInfoListener? = null

    fun setBatterInfoListener(batteryInfoListener: BatteryInfoListener){
        this.batteryInfoListener = batteryInfoListener
    }


    override fun onReceive(context: Context, intent: Intent) {
        val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        batteryInfoListener?.chargingStateChanged(isCharging)


        // How are we charging?
        val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val usbCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
        val wirelessCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS

        val type = when {
            wirelessCharge -> ChargingType.PLUG_WIRELESS
            acCharge -> ChargingType.PLUG_AC
            usbCharge -> ChargingType.PLUG_USB
            else -> ChargingType.NONE
        }
        batteryInfoListener?.chargingType(type)


        val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level * 100 / scale.toFloat()
        batteryInfoListener?.chargingPercentage(batteryPct)

    }


}