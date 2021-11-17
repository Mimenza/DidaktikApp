package com.example.didaktikapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.didaktikapp.R

class Activity6_Site : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity6_site)

        val newInt: Int?
        newInt = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getInt("numero")
        } else {
            savedInstanceState.getSerializable("numero") as Int?
        }

        val sharedPreferences = getSharedPreferences("site", 0)
        var editor = sharedPreferences.edit()
        editor.putString("numero", newInt.toString()).apply()

        println("NUMERO "+newInt)
    }
}