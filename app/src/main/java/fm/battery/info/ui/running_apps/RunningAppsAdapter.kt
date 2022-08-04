package fm.battery.info.ui.running_apps

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fm.battery.info.core.models.RunningApp
import fm.battery.info.databinding.ItemRunningAppBinding

class RunningAppsAdapter(
    context: Context
): RecyclerView.Adapter<RunningAppsAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    var data: List<RunningApp> = listOf()

    fun updateData(newData: List<RunningApp>){
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRunningAppBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val app = data[position]
            appIcon.setImageDrawable(app.icon)
            appName.text = app.applicationName
            root.setOnLongClickListener {
                Toast.makeText(root.context, app.applicationName, Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(val binding: ItemRunningAppBinding): RecyclerView.ViewHolder(binding.root)
}