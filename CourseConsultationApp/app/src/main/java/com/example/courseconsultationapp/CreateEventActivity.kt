package com.example.courseconsultationapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.text.SimpleDateFormat
import android.view.View.OnFocusChangeListener
import android.widget.TextView
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateEventActivity : AppCompatActivity() {
    private lateinit var startDate:TextInputEditText
    private lateinit var endDate:TextInputEditText

    private lateinit var startTime:TextInputEditText
    private lateinit var endTime:TextInputEditText

    private lateinit var focusedDateInput:TextInputEditText

    private lateinit var newEventTitle:TextInputEditText
    private lateinit var newEventLocation:TextInputEditText
    private lateinit var newEventInviteList:TextInputEditText

    private var calendar: Calendar = Calendar.getInstance()

    // DatePicker OnDateSetListener called every time the DatePicker is shown
    private val datePickerListener = DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateInputForDate(calendar)
    }

    // TimePicker OnTimeSetListener called every time the TimePicker is shown
    private val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hour, minutes ->
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, minutes)
        updateInputForTime(calendar)
    }
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

            val saveEvent = findViewById<Button>(R.id.btnSaveNewEvent)
            saveEvent.setOnClickListener {
                Toast.makeText(this, "Event detail saved", Toast.LENGTH_SHORT).show()
            }

            startDate = findViewById(R.id.inputNewEventStartingDate)       // Getting the input field by ID
            // I need to set the onFocus and onClick
            // The onFocus is for the first ever click at input field
            startDate.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker(startDate)
                }
            }
            // When user clicked again the input field
            startDate.setOnClickListener {
                showDatePicker(startDate)
            }

            endDate = findViewById(R.id.inputNewEventEndingDate)        // Getting the input field by ID
            // I need to set the onFocus and onClick
            // The onFocus is for the first ever click at input field
            endDate.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker(endDate)
                }
            }
            // When user clicked again the input field
            endDate.setOnClickListener {
                showDatePicker(endDate)
            }

            startTime = findViewById(R.id.inputNewEventStartingTime)        // Getting the input field by ID
            // I need to set the onFocus and onClick
            // The onFocus is for the first ever click at input field
            startTime.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showTimePicker(startTime)
                }
            }
            // When user clicked again the input field
            startTime.setOnClickListener {
                showTimePicker(startTime)
            }

            endTime = findViewById(R.id.inputNewEventEndingTime)        // Getting the input field by ID
            // I need to set the onFocus and onClick
            // The onFocus is for the first ever click at input field
            endTime.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showTimePicker(endTime)
                }
            }
            // When user clicked again the input field
            endTime.setOnClickListener {
                showTimePicker(endTime)
            }
            val studSendEmail=findViewById<Button>(R.id.btn_studentSendEmail)

            //onclick -> button
            val btnSaveNewEvent = findViewById<Button>(R.id.btnSaveNewEvent)
            btnSaveNewEvent.setOnClickListener {
                createEvent()

            }
        }

    private fun createEvent(){
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val df = db.collection("users")

        newEventTitle = findViewById(R.id.inputNewEventTitle)
        newEventLocation = findViewById(R.id.inputNewEventLocation)
        startDate = findViewById(R.id.inputNewEventStartingDate)
        endDate = findViewById(R.id.inputNewEventEndingDate)
        startTime = findViewById(R.id.inputNewEventStartingTime)
        endTime = findViewById(R.id.inputNewEventEndingTime)
        newEventInviteList = findViewById(R.id.inputNewEventList)

        val eventTitle = newEventTitle.text.toString()
        val eventLocation = newEventLocation.text.toString()
        val eventDateStart = startDate.text.toString()
        val eventDateEnd = endDate.text.toString()
        val eventTimeStart = startTime.text.toString()
        val eventTimeEnd = endTime.text.toString()
        val eventInviteList = newEventInviteList.text.toString()

        val userInfo = hashMapOf(
            "uid" to user!!.uid,
            "eventTitle" to eventTitle,
            "eventLocation" to eventLocation,
            "eventDateStart" to eventDateStart,
            "eventDateEnd" to eventDateEnd,
            "eventTimeStart" to eventTimeStart,
            "eventTimeEnd" to eventTimeEnd,
            "eventInviteList" to eventInviteList,
            )

        db.collection("events")
            .add(userInfo)
            .addOnSuccessListener { Log.d(RegisterActivity.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(RegisterActivity.TAG, "Error writing document", e) }

        startActivity(Intent(this, StudentMainActivity::class.java))
        finish()
    }

    // Accepts the input that called the function then shows DatePicker
    // This accepts the input field and pass state to another input field
    // Why? because theres no other way to pass it to startDatePicker unless you made it this way
    // If you have other then please let me know
    private fun showDatePicker(inputToBeUpdated:TextInputEditText){
        calendar = Calendar.getInstance()
        focusedDateInput = inputToBeUpdated
        DatePickerDialog(
            this,
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Updates the input field that calls the showDatePicker
    private fun updateInputForDate(calendar: Calendar) {
        val dateFormat = "MM-dd-yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)

        focusedDateInput.setText(sdf.format(calendar.time))
    }

    // Accepts the input that called the function then shows TimePicker
    // This accepts the input field and pass state to another input field
    // Why? because theres no other way to pass it to startTimePicker unless you made it this way
    // If you have other then please let me know
    private  fun showTimePicker(inputToBeUpdated:TextInputEditText){
        calendar = Calendar.getInstance()
        focusedDateInput = inputToBeUpdated
        val isSystem24Hour = is24HourFormat(this)

        TimePickerDialog(
            this,
            timePickerListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            isSystem24Hour
        ).show()
    }

    // Updates the input field that calls the showTimePicker
    private fun updateInputForTime(calendar: Calendar) {
        val timeFormat = "HH:mm a"
        val sdf = SimpleDateFormat(timeFormat, Locale.US)
        focusedDateInput.setText(sdf.format(calendar.time))
    }
}