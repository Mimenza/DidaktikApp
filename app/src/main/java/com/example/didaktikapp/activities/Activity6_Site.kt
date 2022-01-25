package com.example.didaktikapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment4_menu
import com.example.didaktikapp.fragments.Fragment5_ajustes
import com.example.didaktikapp.fragments.Fragment6_ayuda
import kotlinx.android.synthetic.main.activity6_site.*

class Activity6_Site : AppCompatActivity() {

    private var fragment : String? = null
    private var ayudaFirst:Boolean = true
    private var menuFirst:Boolean = true
    private var ajustesFirst:Boolean = true
    private var menuShowing: Boolean = false
    private var ajustesShowing: Boolean = false

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

    fun menuCheck(){
        if(menuFirst){
            addMenu()
            menuFirst = false
        }else{
            showMenu()
        }
    }

    fun ayudaCheck(){

        if(ayudaFirst){
            addAyuda()
            ayudaFirst = false
        }else{
            showAyuda()
        }
    }

    fun ajustesCheck(){

        if(ajustesFirst){
            addAjustes()
            ajustesFirst = false
        }else{
            showAjustes()
        }
    }

    private fun addAyuda() {
        //añadimos el fragment de ajustes

        ayudaFirst = true
        fragment = "Fragment6"
        supportFragmentManager.beginTransaction().add(R.id.framelayout6_ayuda, Fragment6_ayuda())
            .addToBackStack(null).commit()
        showAyuda()
    }

    private fun showAyuda() {
        //enseñamos el container del fragment de ajutes

        ayudaFirst = true
        fragment = "Fragment6"
        framelayout6_ayuda.isVisible=true

        /* supportFragmentManager.beginTransaction().show(Fragment5_ajustes()!!).commit()*/
    }

    private fun addMenu() {
        //añadimos el fragment del menu

        menuShowing = true
        fragment = "Fragment4"
        supportFragmentManager.beginTransaction().add(R.id.framelayout6_menu, Fragment4_menu())
            .addToBackStack(null).commit()

        showMenu()
    }

    private fun showMenu() {
        //enseñamos el container del fragment del menu

        menuShowing = true
        fragment = "Fragment4"

        framelayout6_menu.isVisible=true
        /*supportFragmentManager.beginTransaction().show(Fragment4_menu2()!!).commit()*/
    }

    private fun addAjustes() {
        //añadimos el fragment de ajustes

        ajustesShowing = true
        fragment = "Fragment5"
        supportFragmentManager.beginTransaction().add(R.id.framelayout6_ajustes, Fragment5_ajustes())
            .addToBackStack(null).commit()
        showAjustes()
    }

    private fun showAjustes() {
        //enseñamos el container del fragment de ajutes

        ajustesShowing = true
        fragment = "Fragment5"
        framelayout6_ajustes.isVisible=true
       /* supportFragmentManager.beginTransaction().show(Fragment5_ajustes()!!).commit()*/
    }

    private fun cerrarAjustes() {
        //escondemos el container del fragment del menu

        framelayout6_ajustes.isVisible=false
        /*supportFragmentManager.beginTransaction().hide(Fragment5_ajustes()!!).commit();*/
        fragment = "Fragment4"
        ajustesShowing= false

    }

    fun cerrarAyuda() {
        //escondemos el container del fragment del menu

        framelayout6_ayuda.isVisible=false
        /*supportFragmentManager.beginTransaction().hide(Fragment5_ajustes()!!).commit();*/
        fragment = "Fragment4"
        ajustesShowing= false

    }

    private fun cerrarMenu() {
        //escondemos el container del fragment de ajustes

        framelayout6_menu.isVisible=false
        /*supportFragmentManager.beginTransaction().hide(Fragment4_menu2()!!).commit();*/
        fragment = null
        menuShowing = false

    }

    override fun onBackPressed() {
        println(fragment)
        when (fragment) {
            "Fragment4" -> { cerrarMenu()
                println("cerrar menu")}
            "Fragment5" -> { cerrarAjustes()
                println("cerrar ajustes")}
            "Fragment6" -> {  cerrarAyuda()
                println("cerrar ayuda")}
            else -> {
                super.onBackPressed()
                println("atras")
            }
        }
    }
}
