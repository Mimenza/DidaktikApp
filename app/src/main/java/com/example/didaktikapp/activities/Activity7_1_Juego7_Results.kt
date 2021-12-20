package com.example.didaktikapp.activities


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.example.didaktikapp.Model.Constantsjuego7
import com.example.didaktikapp.R


import com.example.didaktikapp.fragments.juegos.Fragment1_7_juego
import com.example.didaktikapp.fragments.minijuegos.Fragment2_7_minijuego
import kotlinx.android.synthetic.main.activity7_1_juego7_results.*

import kotlinx.android.synthetic.main.activity7_juego2_results.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Activity7_1_Juego7_Results : AppCompatActivity() {
    private var fragment :Fragment? = null

    var minijuegoShowing: Boolean = false
    var juegoShowing: Boolean = false
    private lateinit var audio: MediaPlayer

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity7_1_juego7_results)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)



        //Recojemos los datos del intent en el fragment del juego 7
        val totalQuestions= intent.getIntExtra(Constantsjuego7.TOTAL_QUESTIONS, 0)
        val correctAnswers= intent.getIntExtra(Constantsjuego7.CORRECT_ANSWERS, 0)



        txtv7_1_scoreuser.text="Zure emaitza: $correctAnswers/$totalQuestions"


        //Boton finish que nos redirecciona al mapa


        if (correctAnswers>=6) {
            //Audio acierto
            runBlocking() {
                launch {
                    audio = MediaPlayer.create(this@Activity7_1_Juego7_Results, R.raw.ongiaudioa7)
                    audio.start()
                }
            }
            btn7_1_terminar.setOnClickListener {
                //Mostramos fragement minijuego
                showMinijuego7()
                //Ocultamos los botones
                ocultaracciones()
                audio.stop()
            }
            btn7_1_saiatuberriro.setOnClickListener {

                //Mostramos fragment juego7
                showFragmentJuego7()
                //Ocultamos los botones
                ocultaracciones()

            }
            btn7_1_saiatuberriro2.isVisible=false

        }else{
            //Audio error
            runBlocking() {
                launch {
                    audio = MediaPlayer.create(this@Activity7_1_Juego7_Results, R.raw.gaizkiaudioa)
                    audio.start()
                }
            }
            btn7_1_terminar.isVisible=false
            btn7_1_saiatuberriro.isVisible=false
            btn7_1_saiatuberriro2.setOnClickListener {

                //Mostramos fragment juego7
                showFragmentJuego7()
                //Ocultamos los botones
                ocultaracciones()

            }
        }
    }

    fun showFragmentJuego7(){

        if (juegoShowing) {
            return
        }
        juegoShowing = true
        fragment = Fragment1_7_juego()
        supportFragmentManager.beginTransaction().add(R.id.framelayout7menu, fragment!!).commit()

    }
    fun showMinijuego7(){

        if (minijuegoShowing) {
            return
        }
        minijuegoShowing = true
        fragment = Fragment2_7_minijuego()
        supportFragmentManager.beginTransaction().add(R.id.framelayout7menu, fragment!!).commit()

    }

    fun ocultaracciones(){
        btn7_1_terminar.isVisible=false
        btn7_1_saiatuberriro.isVisible=false
        txtv7_1_result.isVisible=false
        txtv7_1_imagen1.isVisible=false
        txtv7_1_scoreuser.isVisible=false
        btn7_1_saiatuberriro2.isVisible=false
    }
}