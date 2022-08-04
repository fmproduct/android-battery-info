package fm.battery.info

import android.app.usage.UsageStats
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import fm.battery.info.core.models.RunningApp
import java.util.*


fun Calendar.toAppDateFormat(): String {
    val day = get(Calendar.DAY_OF_MONTH)
    val month = get(Calendar.MONTH) + 1
    val year = get(Calendar.YEAR)

    return "%02d-%02d-%d".format(day, month, year)
}

fun Calendar.toAppTimeFormat(): String {
    val hourOfDay = get(Calendar.HOUR_OF_DAY)
    val minute = get(Calendar.MINUTE)
    return "%02d:%02d".format(hourOfDay, minute)
}

fun UsageStats.toRunningApp(packageManager: PackageManager): RunningApp? {
    return try {
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val applicationName = packageManager.getApplicationLabel(applicationInfo).toString()
        val appIcon = packageManager.getApplicationIcon(applicationInfo)

        RunningApp(packageName, applicationName, appIcon, this)
    }
    catch (e: PackageManager.NameNotFoundException){
        null
    }
}