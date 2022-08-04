package fm.battery.info.core

import fm.battery.info.core.enums.ChargingType

interface BatteryInfoListener {

    fun chargingStateChanged(isCharging: Boolean)
    fun chargingType(type: ChargingType)
    fun chargingPercentage(percent: Float)
    fun batteryVoltage(voltage: Int)
    fun batteryTemperature(temperature: Int)
    fun batteryTechnology(technology: String)

    fun batteryHealth(batteryHealth: String)
    fun batteryCapacityInMicroampere(capacity: Int) // µAh
    fun batteryChargeTimeRemaining(time: Long)
}