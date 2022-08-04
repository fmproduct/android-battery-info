package fm.battery.info.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import fm.battery.info.core.enums.ChargingType

class BatteryInfoBroadcast: BroadcastReceiver() {

    private var batteryInfoListener: BatteryInfoListener? = null

    fun setBatterInfoListener(batteryInfoListener: BatteryInfoListener){
        this.batteryInfoListener = batteryInfoListener
    }


    override fun onReceive(context: Context, intent: Intent) {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        batteryInfoListener?.chargingStateChanged(isCharging)


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

        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        batteryInfoListener?.batteryVoltage(voltage)



        val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -99)
        batteryInfoListener?.batteryTemperature(temperature)

        val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
        batteryInfoListener?.batteryTechnology(technology ?: "None")


        val health = when (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -99)) {
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Unknown"
            else -> "Unknown"
        }
        batteryInfoListener?.batteryHealth(health)


        val propertyChargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
        batteryInfoListener?.batteryCapacityInMicroampere(propertyChargeCounter)

        // Approximation for how much time (in milliseconds) remains until the battery is fully charged.
        var chargeTimeRemaining: Long? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            chargeTimeRemaining = batteryManager.computeChargeTimeRemaining()
        }
        batteryInfoListener?.batteryChargeTimeRemaining(chargeTimeRemaining!!)
    }


}