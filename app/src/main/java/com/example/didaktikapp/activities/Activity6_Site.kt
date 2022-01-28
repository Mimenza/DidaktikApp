package com.example.didaktikapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.didaktikapp.R


class Activity6_Site : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity6_site)

        val newInt: Int? = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getInt("numero")
        } else {
            savedInstanceState.getSerializable("numero") as Int?
        }

        val sharedPreferences = getSharedPreferences("site", 0)
        val editor = sharedPreferences.edit()
        editor.putString("numero", newInt.toString()).apply()
    }

}
