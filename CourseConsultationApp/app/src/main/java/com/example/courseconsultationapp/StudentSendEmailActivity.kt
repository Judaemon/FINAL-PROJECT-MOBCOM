package com.example.courseconsultationapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class StudentSendEmailActivity : AppCompatActivity() {

    private lateinit var email: TextInputLayout
    private lateinit var subject: TextInputLayout
    private lateinit var message: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_send_email)

        // Getting the input field by ID
        email = findViewById(R.id.email)
        subject = findViewById(R.id.subject)
        message = findViewById(R.id.message)

        var emailValue: String
        var subjectValue: String
        var messageValue: String

        var iSend:Intent

        val btnProceedStudentEmail = findViewById<Button>(R.id.btn_proceedStudentEmail)
        btnProceedStudentEmail.setOnClickListener {
            if (validateEmail() || validateInput(subject) || validateInput(message)){
                emailValue = email.editText?.text.toString()
                subjectValue = subject.editText?.text.toString()
                messageValue = message.editText?.text.toString()

                startActivity(intent)

                iSend=Intent(Intent.ACTION_SENDTO).apply{
                    data= Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL,arrayOf(email.editText?.text.toString()))
                    putExtra(Intent.EXTRA_SUBJECT, subject.editText?.text.toString())
                    putExtra(Intent.EXTRA_TEXT, message.editText?.text.toString())
                }

                try{
                    startActivity(iSend)
                    finish()
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        val newEmail = email.editText?.text.toString().trim { it <= ' ' }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

        return if (newEmail.isEmpty()) {
            email.error = "Required field"
            false
        } else if (!newEmail.matches(emailPattern)) {
            email.error = "Invalid email address"
            false
        } else {
            email.error = null
            email.isErrorEnabled = false
            true
        }
    }

    private fun validateInput(inputLayout: TextInputLayout): Boolean {
        val inputs = inputLayout.editText?.text.toString().trim { it <= ' ' }

        return if (inputs.isEmpty()) {
            inputLayout.error = "Required field"
            false
        } else {
            inputLayout.error = null
            inputLayout.isErrorEnabled = false
            true
        }
    }
}