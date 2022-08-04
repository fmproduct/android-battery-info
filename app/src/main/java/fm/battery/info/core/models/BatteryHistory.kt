package fm.battery.info.core.models

data class BatteryHistory(
    val timeInMillis: Long,
    val percent: Float,
)