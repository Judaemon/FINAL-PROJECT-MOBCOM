package com.example.courseconsultationapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentMainActivity : AppCompatActivity() {
    private lateinit var  toggle: ActionBarDrawerToggle

    private lateinit var auth: FirebaseAuth

    private lateinit var userPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        auth = Firebase.auth

        userPref = getPreferences(MODE_PRIVATE) // Initialization of SharedPreference
        editor = userPref.edit()

        val drawerLayout: DrawerLayout = findViewById(R.id.my_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle=ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        toggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open,
            R.string.close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                updateNavigationViewHeader();
            }
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.itm_ViewProfile-> {
                    this.startActivity(Intent(this, UpdateProfileActivity::class.java))
//                    startActivity(Intent(this, ViewProfileActivity::class.java))
                }
                R.id.itm_CreateEvent-> {
                    this.startActivity(Intent(this, CreateEventActivity::class.java))
                }
                R.id.itm_PostQuery-> {
                    this.startActivity(Intent(this, PostQueryActivity::class.java))
                }
                R.id.itm_SendEmail-> {
                    this.startActivity(Intent(this, StudentSendEmailActivity::class.java))
                }
                R.id.itm_Logout-> {
                    Firebase.auth.signOut()
                    this.startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                R.id.itm_ViewEvents-> {
//                    this.startActivity(Intent(this, ViewQueriesActivity::class.java))
                }
                R.id.itm_ViewQuery-> {
                    this.startActivity(Intent(this, ViewQueriesActivity::class.java))
                }
                else-> Toast.makeText(applicationContext, "Error can't find item", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    private fun updateNavigationViewHeader() {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val v = navigationView.getHeaderView(0)

        val name = v.findViewById<TextView>(R.id.tv_name)
        name.text = "${userPref.getString("firstname", "").toString()} ${userPref.getString("lastname", "").toString()}"

        val email = v.findViewById<TextView>(R.id.tv_email)
        email.text = userPref.getString("email", "").toString()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser

        if(currentUser != null){
            val db = Firebase.firestore

            val df = db.collection("users").document(currentUser.uid)

            df.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")
                        //firstname
                        editor.putString("firstname", document.getString("firstname"))
                        editor.putString("lastname", document.getString("lastname"))
                        editor.putString("email", currentUser.email.toString())

                        editor.apply()
                        editor.commit()
                    } else {
                        Log.d(LoginActivity.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(LoginActivity.TAG, "get failed with ", exception)
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}