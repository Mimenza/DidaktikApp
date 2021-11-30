package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.android.synthetic.main.fragment1_3_2_juego.*
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
class Fragment1_3_2_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var globalView: View
    private var audio: MediaPlayer? = null
    private var viewActiva = false
    private lateinit var vistaAnimada: TranslateAnimation

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
        val view = inflater.inflate(R.layout.fragment1_3_2_juego, container, false)
        globalView = view
        val button: Button = view.findViewById(R.id.btnf1_3_2_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_3_2_ajustes)
        button.visibility=GONE


        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout)
        var newView: ImageView
        newView = ImageView(requireContext())
        newView.layoutParams.height = 200
        newView.layoutParams.width = 200
        newView.x = 300F
        newView.y = 500F
        newView.setBackgroundColor(Color.BLUE)
        //newView.setImageResource(R.drawable.sagarragorria)

        constraintLayoutFound.addView(newView)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_3_2_juego_to_fragment2_3_2_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_3_2_juego_to_fragment4_menu)
        }

        //Typewriter juego 4 tutorial
        Handler(Looper.getMainLooper()).postDelayed({
            typewriter(view)
        }, 2000)
        //Typewriter juego 4 tutorial fin
        audioTutorial(view)
        return view
    }

    fun audioTutorial(view: View){

        //Audio juego 4
        var audio: MediaPlayer
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego4_audiotutorial)
                audio.start()
                audio?.setOnCompletionListener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        //Llama a la funcion para la animacion de salida cuando el audio se termina
                        exitAnimationfun(view)
                    }, 1000)
                }
            }
            //animacion para la descripcion
            starAnimationfun(view)
        }
    }

    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_3_2_fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_3_2_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            upelio.isVisible = false
            talkAnimationfun(view)
        }, 2000)

    }
    private fun exitAnimationfun(view: View) {
        val upelioAnimado = view.findViewById(R.id.imgv1_3_2_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //Animacion upelio salido
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //VistaAnimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_3_2_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //Animacion fondo gris
        Handler(Looper.getMainLooper()).postDelayed({
            val txtAnimacion = view.findViewById(R.id.txtv1_3_2_fondogris) as TextView
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            txtAnimacion.startAnimation(aniFade)
            txtv1_3_2_tutorialjuego4.startAnimation(aniFade)
            txtv1_3_2_tutorialjuego4.isVisible = false
            txtAnimacion.isVisible = false
        }, 1000)
    }

    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_3_2_tutorialjuego4) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.juego4tutorial))
        typeWriterView.setDelay(65)
    }
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_3_2_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
    }


    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onStop() {
        audio?.stop()
        super.onStop()
    }



    fun startTimeCounter(view: View, timeInSeconds: Int) {
        object: CountDownTimer(50000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Each second create an element
            }
            override fun onFinish() {
                //Timer Finished + Might this should show win screen
            }
        }.start()
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
            Fragment1_3_2_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}