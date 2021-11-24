package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.media.MediaPlayer
import android.media.MediaPlayer.create
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity1_principal.*
import kotlinx.android.synthetic.main.fragment1_6_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */

data class draggableImg(var origen: ImageView, var destino: ImageView)

class Fragment1_6_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var sonido = false
    private var audio: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_6_juego, container, false)
        val button: Button = view.findViewById(R.id.btnf1_6_siguiente)

        val ajustes: ImageButton = view.findViewById(R.id.btnf1_6_ajustes)
        val buttonSonido :ImageButton = view.findViewById(R.id.btnf1_6_sonido)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_6_juego_to_fragment2_6_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_6_juego_to_fragment4_menu)
        }
        buttonSonido.setOnClickListener(){
            startAudio(view)
        }
        animacionVolumen(view)
        return view
    }

    private fun animacionVolumen(view:View) {
        val volumen:TextView = view.findViewById(R.id.txtv1_6_volumen)
        volumen.setBackgroundResource(R.drawable.animacion_volumen)
        val ani = volumen.getBackground() as AnimationDrawable
        ani.start()

        startAudio(view)

        sonido = true

        volumen.setOnClickListener(){
            //cuando clickas en la foto el audio para o sigue
            if(sonido == true){
                volumen.setBackgroundResource(R.drawable.ic_volumenoff)
                sonido = false
                audio?.pause()
            }
            else{
                volumen.setBackgroundResource(R.drawable.animacion_volumen)
                val ani = volumen.getBackground() as AnimationDrawable
                ani.start()
                sonido = true
                audio?.start()
            }

        }
        val fondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        fondo.setOnClickListener(){
            //cuando clickamos fuera de la foto, la musica y el fondo se van
            imgv1_6_fondo.isVisible = false
            txtv1_6_volumen.isVisible = false
            audio?.stop()
        }
    }

    private fun startAudio(view:View) {

        val fondo : TextView = view.findViewById(R.id.imgv1_6_fondo)
        val volumen:TextView = view.findViewById(R.id.txtv1_6_volumen)
        if (!fondo.isVisible){
            fondo.isVisible = true
            volumen.isVisible = true
        }

        runBlocking() {
            launch {
                audio = MediaPlayer.create(
                    context,R.raw.bertsoa
                )
                audio?.setVolume(0.15F, 0.15F)
                audio?.start()

                audio?.setOnCompletionListener {
                    imgv1_6_fondo.isVisible = false
                    txtv1_6_volumen.isVisible = false
                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment1_juego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1_6_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}