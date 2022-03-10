package com.example.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView


class Recurse_Item_Adapter (private val navMod: NavMod, private val navCtrl: NavController) :
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
        TODO("Not yet implemented")
    }
}
