package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment4_menu
import com.example.didaktikapp.fragments.Fragment4_menu2
import com.example.didaktikapp.fragments.Fragment5_ajustes
import kotlinx.android.synthetic.main.fragment3_info.*

class Activity6_Site : AppCompatActivity() {

    private var fragment : String? = null
    var menuShowing: Boolean = false
    var ajustesShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity6_site)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

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

    override fun onResume() {

        cerrarAjustes()
        cerrarMenu()

        super.onResume()
    }
    fun showMenu() {
        if (menuShowing) {
            cerrarMenu()
            return
        }

        menuShowing = true
        fragment = "Fragment4"
        supportFragmentManager.beginTransaction().add(R.id.framelayout6_menu, Fragment4_menu2()!!).commit()


    }

    fun showAjustes() {
        if (ajustesShowing) {
            cerrarMenu()
            return
        }

        ajustesShowing = true
        fragment = "Fragment5"
        supportFragmentManager.beginTransaction().add(R.id.framelayout6_menu, Fragment5_ajustes()!!).commit()
    }

    override fun onBackPressed() {

        if(fragment == "Fragment5"){
            cerrarAjustes()
        }

        else if (fragment == "Fragment4") {
            cerrarMenu()
        }

        else {
            super.onBackPressed()
        }
    }

    private fun cerrarAjustes() {

        supportFragmentManager.beginTransaction().remove(Fragment5_ajustes()!!).commit();
        fragment = "Fragment4"
        ajustesShowing= false

    }

    private fun cerrarMenu() {

        supportFragmentManager.beginTransaction().remove(Fragment4_menu2()!!).commit();
        fragment = null
        menuShowing = false

    }

}