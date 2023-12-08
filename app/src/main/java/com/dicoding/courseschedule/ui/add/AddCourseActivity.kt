package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private val viewModel by viewModels<AddCourseViewModel> {
        AddCourseViewModelFactory.createFactory(this)
    }
    private lateinit var binding : ActivityAddCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    //create menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //create option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                binding.apply {
                    val edCorseName = edCourseName.text.toString()
                    val spinner = spinnerDay.selectedItem.toString()
                    val spinnerDay = getDay(spinner)
                    val tvStartTime = tvStartTime.text.toString()
                    val tvEndTime = tvEndTime.text.toString()
                    val edLecture = edLecture.text.toString()
                    val edNote = edNote.text.toString()

                    viewModel.insertCourse(edCorseName,spinnerDay,tvStartTime,tvEndTime,edLecture,edNote)
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //setTime
    fun setTimePicker(view:View){
        val pickTime = when(view.id){
            R.id.ib_start_time -> SET_PICKER_START_TIME
            R.id.ib_end_time -> SET_PICKER_END_TIME
            else -> SET_PICKER_DEFAULT
        }

        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager,pickTime)
    }

    //getDay
    private fun getDay(day:String):Int{
        val days = resources.getStringArray(R.array.day)
        return days.indexOf(day)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY,hour)
        calender.set(Calendar.MINUTE, minute)

        val timesFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            SET_PICKER_START_TIME ->{
                binding.tvStartTime.text = timesFormat.format(calender.time)
            }
            SET_PICKER_END_TIME -> {
                binding.tvEndTime.text = timesFormat.format(calender.time)
            }
        }
    }

    companion object{
        private const val SET_PICKER_START_TIME = "SetPickerStartTime"
        private const val SET_PICKER_END_TIME = "SetPickerEndTime"
        private const val SET_PICKER_DEFAULT = "SetPickerDefault"
    }
}