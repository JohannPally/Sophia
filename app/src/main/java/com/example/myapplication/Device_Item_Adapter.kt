package com.example.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView


class Device_Item_Adapter (private val catArg: String, private val devs: Set<String>, private val navMod: NavMod, private val navCtrl: NavController) :
    RecyclerView.Adapter<Device_Item_Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val devNameTextView = itemView.findViewById<TextView>(R.id.device_name)
        val selectDevButton = itemView.findViewById<Button>(R.id.select_device)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Device_Item_Adapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val deviceSetView = inflater.inflate(R.layout.device_item_layout, parent, false)
        // Return a new holder instance
        return ViewHolder(deviceSetView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: Device_Item_Adapter.ViewHolder, position: Int) {
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
    }

    override fun getItemCount(): Int {
        return devs.size
    }
}