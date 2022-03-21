package com.example.birthdaygreet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_birthday_greeting.*

class BirthdayActivityGreeting : AppCompatActivity() {

    //Create companion object , this is used to store static varibles

    companion object {
        const val NAME_EXTRA = "name_extra"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday_greeting)


        // getting the name that we put in the birthday card function on the main activity
        val name= intent.getStringExtra(NAME_EXTRA)
        // giving the text view [BirthdayGreeting] the parameters / string
        BirthdayGreeting.text = "Happy Birthday\n $name !"
    }
}

