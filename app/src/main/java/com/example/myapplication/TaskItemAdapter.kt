package com.example.myapplication
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.*


class TaskItemAdapter (private val tasks: Array<Triple<String, Date, Int>>, private val startdate: Date, private val cycle: Int, private val navMod: NavMod, private val navCtrl: NavController) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val taskNameTV: TextView = itemView.findViewById(R.id.task_name_info)
        val taskDateET: EditText = itemView.findViewById<EditText>(R.id.task_date_info)
        val taskStatButton: Button = itemView.findViewById(R.id.task_stat_button_info)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val setView = inflater.inflate(R.layout.task_item_layout, parent, false)
        // Return a new holder instance
        return ViewHolder(setView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskItemAdapter.ViewHolder, position: Int) {
        val task = tasks.elementAt(position)
        val task_name_tv = holder.taskNameTV
        val task_date_et = holder.taskDateET
        val task_stat_button = holder.taskStatButton

        task_name_tv.setText(task.first)
        task_date_et.setText(task.second.toString())
        task_stat_button.setText(task.third.toString())
        task_stat_button.setOnClickListener(){
            task_date_et.setText(task.second.toString())
            //TODO finish up setting new date and changing color
        }
        setButtonColor(task, holder)


    }

    //TODO have to implemenet changes for modding over startdate and number of days remaining
    @SuppressLint("ResourceAsColor")
    public fun  setButtonColor(task: Triple<String, Date, Int>, holder: TaskItemAdapter.ViewHolder){
        //TODO might change from passing task to passing int
        if(task.third == 0){
            holder.taskStatButton.setBackgroundColor(R.color.hazard_red);
        } else if(task.third == 1){
            holder.taskStatButton.setBackgroundColor(R.color.active_green);
        }
    }

    // Involves populating data into the item through holder
//    @SuppressLint("ResourceAsColor")
//    override fun onBindViewHolder(viewHolder: Recurse_Item_Adapter.ViewHolder, position: Int) {
//        if(position < levels.size){
//            val item = levels.elementAt(position)
//            val item_tv = viewHolder.itemTextView
//            val select_button = viewHolder.selectButton
//
//            item_tv.setText(item.levelName)
//            select_button.setText(">")
//            select_button.setOnClickListener() {
//                navMod.RecursetoRecurse(navCtrl, item.id)
//            }
//        }
//        else{
//            val item = mrs.elementAt(position-levels.size)
//            val item_tv = viewHolder.itemTextView
//            val select_button = viewHolder.selectButton
//
//            item_tv.setText(item.deviceName)
//            item_tv.setTextColor(R.color.brown_neutral)
//            select_button.setText(">")
//            select_button.setOnClickListener() {
//                item.id?.let { it1 -> navMod.RecursetoInfo(navCtrl, it1) }
//            }
//        }
//    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
