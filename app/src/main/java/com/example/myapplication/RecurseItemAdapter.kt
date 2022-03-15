package com.example.myapplication
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.*


class Recurse_Item_Adapter (private val levels: Set<LevelSQL>, private val mrs: Set<MaintenanceRecordSQL>, private val navMod: NavMod, private val navCtrl: NavController) :
    RecyclerView.Adapter<Recurse_Item_Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val itemTextView: TextView = itemView.findViewById(R.id.item_name_recurse)
        val selectButton: Button = itemView.findViewById(R.id.select_button_recurse)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Recurse_Item_Adapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val setView = inflater.inflate(R.layout.recurse_item_layout, parent, false)
        // Return a new holder instance
        return ViewHolder(setView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: Recurse_Item_Adapter.ViewHolder, position: Int) {
        if(position < levels.size){
            val item = levels.elementAt(position)
            val item_tv = viewHolder.itemTextView
            val select_button = viewHolder.selectButton

            item_tv.setText(item.levelName)
            select_button.setText(">")
            select_button.setOnClickListener() {
                navMod.RecursetoRecurse(navCtrl, item.id)
            }
        }
        else{
            val item = mrs.elementAt(position-levels.size)
            val item_tv = viewHolder.itemTextView
            val select_button = viewHolder.selectButton

            item_tv.setText(item.deviceName)
            item_tv.setTextColor(R.color.brown_neutral)
            select_button.setText(">")
            select_button.setOnClickListener() {
                navMod.RecursetoInfo(navCtrl, item.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return levels.size+mrs.size
    }
}
