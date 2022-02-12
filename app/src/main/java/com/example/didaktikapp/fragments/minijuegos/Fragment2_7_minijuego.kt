package com.example.didaktikapp.fragments.minijuegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.example.didaktikapp.Model.CustomAudioTest.audio
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity1_Principal
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.fragments.Fragment4_ajustes
import kotlinx.android.synthetic.main.fragment1_2_juego.*
import kotlinx.android.synthetic.main.fragment2_7_minijuego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Fragment2_7_minijuego : Fragment() {
    private lateinit var upelio1: ImageView                                     // upelio sin animacion
    private lateinit var upelio2: ImageView                                     // upelio para animacion
    private lateinit var fondoTutorial: TextView                                // fondo gris
    private lateinit var txtTutorial: TextView                                  // texto del tutorial
    private lateinit var vistaanimada: TranslateAnimation                       // variable para animacion
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment2_7_minijuego, container, false)
        // Inflate the layout for this fragment

        upelio1= view.findViewById(R.id.imgv2_7_upelio3)
        upelio2= view.findViewById(R.id.imgv2_7_upelio4)
        txtTutorial= view.findViewById(R.id.txtv2_7felicitacionesminijuego7)
        fondoTutorial= view.findViewById(R.id.txtv2_7fondogris2)
        Handler().postDelayed({
            if (getView() != null) {
                typewriter(view)
            }
        }, 2000)




        //Audio minijuego 7 felicitaciones
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.ongiaudioa7)
                audio?.start()
                audio?.setOnCompletionListener {
                    exitAnimationfun(view)
                    Handler().postDelayed({
                        activity?.let{
                            getActivity()?.finish()
                        }
                    }, 2000)

                }
            }
        }
        //animacion para la descripcion
        starAnimationfun(view)


        return view
    }

    /**
     * escribimos el texto de felicitaciones
     *
     * @param view
     */
    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv2_7felicitacionesminijuego7) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.findelosjuegos))
        typeWriterView.setDelay(70)
    }

    /**
     * Animacion de cierre del minijuego, generamos un cartel con un texto y dos botones
     *
     * @param view
     */
    private fun starAnimationfun(view: View) {
        // animacion fondo gris
        val txt_animacion = view.findViewById(R.id.txtv2_7fondogris2) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txt_animacion.startAnimation(aniFade)


        //animacion entrada upelio
        vistaanimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaanimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv2_7_upelio3) as ImageView
        upelio.startAnimation(vistaanimada)

        //llamamos a la animacion para animar a upelio
        Handler().postDelayed({
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
            }
        }, 2000)
    }

    /**
     * Animacion de hablar de upelio
     *
     * @param view
     */
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv2_7_upelio4) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.getBackground() as AnimationDrawable
        ani.start()
    }

    /**
     * Animacion de salida de la manzana
     *
     * @param view
     */
    private fun exitAnimationfun(view: View) {
        //si la intro se ha acabado

            //escondemos la manzanda de la animacion
            val upelioanimado = view.findViewById(R.id.imgv2_7_upelio4) as ImageView
            upelioanimado.isVisible = false

            //animacion salido upelio
            vistaanimada = TranslateAnimation(0f, 1000f, 0f, 0f)
            vistaanimada.duration = 2000

            //vistaanimada.fillAfter = true
            val upelio = view.findViewById(R.id.imgv2_7_upelio3) as ImageView
            upelio.startAnimation(vistaanimada)

            //animacion fondo gris
            Handler().postDelayed({
                if (getView() != null) {
                    val txt_animacion = view.findViewById(R.id.txtv2_7fondogris2) as TextView
                    val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                    txt_animacion.startAnimation(aniFade)
                    txtv2_7felicitacionesminijuego7.startAnimation(aniFade)
                    txtv2_7felicitacionesminijuego7.isVisible = false
                    txt_animacion.isVisible = false
                }
            }, 1000)

    }

    //AUDIO EVENTS FIX ON DESTROYING
    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        audio?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        audio?.start()
    }
}