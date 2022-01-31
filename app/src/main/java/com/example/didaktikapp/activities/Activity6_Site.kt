package com.example.didaktikapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.didaktikapp.R


class Activity6_Site : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val sharedPreferences = this.getSharedPreferences("com.example.didaktikapp_preferences", 0)
        val darkStatus = sharedPreferences?.getInt("io.github.manuelernesto.DARK_STATUS",0)

        if(darkStatus == 0){
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.activity6_site)

        val newInt: Int? = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getInt("numero")
        } else {
            savedInstanceState.getSerializable("numero") as Int?
        }

        val sharedPreferences2 = getSharedPreferences("site", 0)
        val editor = sharedPreferences2.edit()
        editor.putString("numero", newInt.toString()).apply()
    }

}
