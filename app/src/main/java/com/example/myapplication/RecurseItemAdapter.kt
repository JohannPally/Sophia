package com.example.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.*


class Recurse_Item_Adapter (private val level_mr_set: Set<Objects>, private val navMod: NavMod, private val navCtrl: NavController) :
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
    override fun onBindViewHolder(viewHolder: Recurse_Item_Adapter.ViewHolder, position: Int) {
        val item = level_mr_set.elementAt(position)
        val item_tv = viewHolder.itemTextView
        val select_button = viewHolder.selectButton

        try{
            item as LevelSQL
            item_tv.setText(item.levelName)
            select_button.setText(">")
            select_button.setOnClickListener() {
                navMod.RecursetoRecurse(navCtrl, item.parent)
            }

        } catch (e: Exception){
            try{
                item as MaintenanceRecordSQL
                item_tv.setText(item.deviceName)
                select_button.setText("info")
                select_button.setOnClickListener() {
                    navMod.RecursetoInfo(navCtrl, item.id)
                }
            } catch (e: Exception){
                throw e
            }
        }

        /*
        // Get the data model based on position
        val dev: String = devs.elementAt(position)
        // Set item views based on your views and data model
        val textView = viewHolder.devNameTextView
        textView.text = dev
        val button = viewHolder.selectDevButton
        //button.text = if (contact.isOnline) "Message" else "Offline"
        button.text = "Select"
        //button.isEnabled = contact.isOnline
        button.setOnClickListener() {
            navMod.L2toL3(catArg, dev, navCtrl)
        }
         */
    }

    override fun getItemCount(): Int {
        return level_mr_set.size
    }
}
