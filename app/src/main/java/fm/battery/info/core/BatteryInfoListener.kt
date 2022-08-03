package fm.battery.info.core

import fm.battery.info.core.enums.ChargingType

interface BatteryInfoListener {

    fun chargingStateChanged(isCharging: Boolean)
    fun chargingType(type: ChargingType)
    fun chargingPercentage(percent: Float)
}