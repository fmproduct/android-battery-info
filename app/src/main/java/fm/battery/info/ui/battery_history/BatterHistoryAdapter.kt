package fm.battery.info.ui.battery_history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fm.battery.info.R
import fm.battery.info.core.models.BatteryHistory
import fm.battery.info.toAppDateFormat
import fm.battery.info.toAppTimeFormat
import java.util.*

class BatterHistoryAdapter(
    context: Context
) : RecyclerView.Adapter<BatterHistoryAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var data = listOf<BatteryHistory>()

    fun updateData(newData: List<BatteryHistory>){
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_battery_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsData = data[position]
        val calendar = Calendar.getInstance().apply {
            timeInMillis = itemsData.timeInMillis
        }
        val date = calendar.toAppDateFormat()
        val time = calendar.toAppTimeFormat()
        holder.textView.text = "%s %s: %.00f%%".format(date, time, itemsData.percent)
    }

    override fun getItemCount() = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.tv)
    }
}