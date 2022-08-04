package fm.battery.info.workders

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.icu.util.Calendar
import android.os.BatteryManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import fm.battery.info.core.data.AppBatteryHistoryStorage
import fm.battery.info.core.models.BatteryHistory


class BatteryHistoryWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private val storage by lazy {
        AppBatteryHistoryStorage(applicationContext)
    }

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()

        val batteryManager = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
        val percent = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        val item = BatteryHistory(
            calendar.timeInMillis,
            percent.toFloat()
        )
        storage.setValue(calendar.timeInMillis.toString(), item)
        return Result.success()
    }
}