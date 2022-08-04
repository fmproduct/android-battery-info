package fm.battery.info.core.models

import android.app.usage.UsageStats
import android.graphics.drawable.Drawable

data class RunningApp(
    val packageName: String?,
    val applicationName: String?,
    val icon: Drawable?,
    val usageStats: UsageStats,
)