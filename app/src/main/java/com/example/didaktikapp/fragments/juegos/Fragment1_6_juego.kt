package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */

class Fragment1_6_juego : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var sonido = false
    private var audio: MediaPlayer? = null
    private var firstTime: Boolean = true

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
        val buttonSonido: ImageButton = view.findViewById(R.id.btnf1_6_sonido)

        button.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_to_fragment2_6_minijuego)
        }
        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_to_fragment4_menu)
        }
        buttonSonido.setOnClickListener() {
            startAudio(view)
        }

        animacionVolumen(view)
        return view
    }

    private fun animacionVolumen(view: View) {
        //Funcion para la animacion del icono del volumen mientras se reproduce el audio del bertso
        //Recogemos el icono del volumen y le añadimos la animacion
        val volumen: TextView = view.findViewById(R.id.txtv1_6_volumen)
        volumen.setBackgroundResource(R.drawable.animacion_volumen)
        val ani = volumen.getBackground() as AnimationDrawable
        ani.start()

        //Funcion para empezar audio bertso
        startAudio(view)

        //Variable para settear que el audio ya ha sido escuchado 1 vez
        sonido = true

        //Cuando clickamos sobre el icono del volumen el audio se para o sigue
        volumen.setOnClickListener() {
            //cuando clickas en la foto el audio para o sigue
            if (sonido == true) {
                volumen.setBackgroundResource(R.drawable.ic_volumenoff)
                sonido = false
                audio?.pause()
            } else {
                volumen.setBackgroundResource(R.drawable.animacion_volumen)
                val ani = volumen.getBackground() as AnimationDrawable
                ani.start()
                sonido = true
                audio?.start()
            }
        }

        val fondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        //Cuando clickamos fuera de la foto, la musica y el fondo se van
        fondo.setOnClickListener() {

            imgv1_6_fondo.isVisible = false
            txtv1_6_volumen.isVisible = false
            audio?.stop()

            //lanzamos el segundo audio(bertso)
            startAudio2(view)
        }
    }

    private fun startAudio(view: View) {
        //Funcion que reproduce el primer audio (bertso)

        //escondemos el boton de reproducir el audio mientras el audio ya se esta reproducciendo
        val reproducirAudio: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
        reproducirAudio.isVisible = false

        //Recogemos tanto el fondo gris como el icono del volumen
        val fondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        val volumen: TextView = view.findViewById(R.id.txtv1_6_volumen)

        //si no estan visibles sacamos el fondo y el icono del microfono
        if (!fondo.isVisible) {
            fondo.isVisible = true
            volumen.isVisible=false
        }

        runBlocking() {
            launch {
                audio = MediaPlayer.create(
                    context, R.raw.bertsoa
                )
                audio?.setVolume(0.15F, 0.15F)
                audio?.start()

                audio?.setOnCompletionListener {
                    imgv1_6_fondo.isVisible = false
                    txtv1_6_volumen.isVisible = false
                    startAudio2(view)
                }
            }
        }
    }

    private fun startAudio2(view: View) {
        //Funcion para el segundo audio(descripcion del juego)

        //Si es la primera vez que se reproduce el berso si que se escuchara la descripcion, sino no.
        if (firstTime) {
            //reproducimo el audio
            runBlocking() {
                launch {
                    audio = MediaPlayer.create(
                        context, R.raw.kantu_kantajolasa
                    )
                    audio?.setVolume(0.15F, 0.15F)
                    audio?.start()

                    audio?.setOnCompletionListener {
                        //cuando el audio se termine escondemos el texto y sacamos el bertso
                        txtv1_6_titulo.isVisible = false
                        txtv1_6_texto.isVisible=true
                    }
                }
            }

            typewriter(view)

            //Variable para settear que el texto ya ha sido escrito
            firstTime = false
        }

        //hacemos que el boton de reproducir el audio sea visible al terminar el audio
        val reproducirAudio: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
        reproducirAudio.isVisible = true
    }

    private fun typewriter(view: View) {

        val typeWriterView = view.findViewById(R.id.txtv1_6_titulo) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.titulo))
        typeWriterView.setDelay(70)
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

    override fun onPause() {
        super.onPause()
        audio?.pause()
    }

    override fun onResume() {
        super.onResume()
        // TODO: preguntar si esta el audio empezado
        audio?.start()
    }
}