package fm.battery.info.core.data

import android.content.Context
import com.google.gson.Gson
import fm.battery.info.core.models.BatteryHistory

class AppBatteryHistoryStorage(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    fun setValue(key: String, value: BatteryHistory){
        val json = gson.toJson(value)
        sharedPreferences.edit()
            .putString(key, json)
            .apply()
    }

    fun getAllValue(): List<BatteryHistory>{
        val result = mutableListOf<BatteryHistory>()
        sharedPreferences.all.forEach {
            val json = it.value as String
            val item = gson.fromJson(json, BatteryHistory::class.java)
            result.add(item)
        }
        return result
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "app_storage_preferences_test02"
    }

}