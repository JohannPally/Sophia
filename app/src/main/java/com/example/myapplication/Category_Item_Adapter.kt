package com.example.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView


class Category_Item_Adapter (private val cats: Set<String>, private val navMod: NavMod, private val navCtrl: NavController) :
    RecyclerView.Adapter<Category_Item_Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val catNameTextView = itemView.findViewById<TextView>(R.id.category_name)
        val selectCatButton = itemView.findViewById<Button>(R.id.select_category)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Category_Item_Adapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val catSetView = inflater.inflate(R.layout.category_item_layout, parent, false)
        // Return a new holder instance
        return ViewHolder(catSetView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: Category_Item_Adapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val cat: String = cats.elementAt(position)
        // Set item views based on your views and data model
        val textView = viewHolder.catNameTextView
        textView.setText(cat)
        val button = viewHolder.selectCatButton
        //button.text = if (contact.isOnline) "Message" else "Offline"
        button.text = "Select"
        //button.isEnabled = contact.isOnline
        button.setOnClickListener() {
            navMod.L1toL2(cat, navCtrl)
        }
    }

    override fun getItemCount(): Int {
        return cats.size
    }
}