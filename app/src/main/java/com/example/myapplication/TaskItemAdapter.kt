package com.example.myapplication
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class TaskItemAdapter (private val tasks: Set<TaskSQL>, private val checklist: CheckListSQL, private val navMod: NavMod, private val navCtrl: NavController) :
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

//        TODO onFocusChangeListener for all ET's
//        serviceProviderTextView.onFocusChangeListener =
//            View.OnFocusChangeListener { p0, hasFocus ->
//                if (localManRec != null && !hasFocus) {
//                    if (localManRec.serviceProvider != serviceProviderTextView.text.toString()) {
//                        localManRec.serviceProvider = serviceProviderTextView.text.toString()
//                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
//                    }
//                };
//            }

        task_name_tv.setText(task.name)
        task_date_et.setText(task.updatedate)
        setStatus(task, holder)
        task_stat_button.setOnClickListener(){
            //TODO change update date in TaskSQL
            //TODO call setStatus again with the updated task
        }


    }

    /**
     * This function determines what the current status of the task is based on the
     * current date, the last update and the cycle length of the task.
     * It mutates the TaskSQL object to reflect the new status.
     * Returns nothing.
     *
     * Assumption: Since all dates are in String, they must conform to this format
     * EEE MMM dd HH:mm:ss z yyyy
     * e.g. Mon Mar 14 16:02:37 GMT 2011
     */
    public fun getTaskStatus(task: TaskSQL) {

        val currentDate : Calendar = Calendar.getInstance()

        val lastUpdateDate: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        lastUpdateDate.time = sdf.parse(task.updatedate)

        val elapsedDays = (currentDate.timeInMillis - lastUpdateDate.timeInMillis) / 86400000
        val checkListObject : CheckListSQL? = task.parent?.let { MainActivity.testDB.CheckListDAO().findById(it) }
        val cycleLen : Int = checkListObject?.cycle ?: -1

        if (cycleLen != -1) {
            if (elapsedDays >= cycleLen) {
                task.status = 0
            }
            else {
                task.status = 1
            }
        }
        MainActivity.testDB.TaskSQLDAO().pushUpdate(task)
    }

    //TODO have to implemenet changes for modding over startdate and number of days remaining
    @SuppressLint("ResourceAsColor")
    public fun  setStatus(task: TaskSQL, holder: TaskItemAdapter.ViewHolder){
        //TODO might change from passing task to passing int
        if(task.status == 0){
            holder.taskStatButton.setBackgroundColor(R.color.hazard_red);
        } else if(task.status == 1){
            holder.taskStatButton.setBackgroundColor(R.color.active_green);
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
