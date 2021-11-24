package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList
import android.graphics.Paint

import android.graphics.Path

import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat


import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.coroutines.delay


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_1_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var paint = Paint()

    var canvas = Canvas()


    private val p1: List<Float> = ArrayList()
    private val p2: List<Float> = ArrayList()
    private val p3: List<Float> = ArrayList()
    private lateinit var vistaanimada: TranslateAnimation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_1_juego, container, false)
        val button: Button = view.findViewById(R.id.btnf1_1siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_1_ajustes)
        var audio: MediaPlayer

        button.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment2_1_minijuego)
        }

        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment4_menu)
        }

        //Juego
        val imagen1 :ImageView = view.findViewById(R.id.imgv1_1imagen1)
        val imagen2 :ImageView = view.findViewById(R.id.imgv1_1imagen2)
        val imagen3 :ImageView = view.findViewById(R.id.imgv1_1imagen3)
        val text1 :TextView = view.findViewById(R.id.txtv1_1azalpena1)
        val text2 :TextView = view.findViewById(R.id.txtv1_1azalpena2)
        val text3 :TextView = view.findViewById(R.id.txtv1_1azalpena3)

          imagen1.setOnTouchListener(OnTouchListener { v, event ->
              println("img1 " + event.x )
             true
          })


        val color = ContextCompat.getColor(requireContext(), R.color.black)
        paint.color = color
        paint.style= Paint.Style.FILL
        canvas.drawPaint( paint)


        //Path
        val fillPath = Path()
        fillPath.moveTo(imagen1.x, imagen1.y); // Your origin point
        println(imagen1.x)
        fillPath.lineTo(text3.x, text3.y); // First point
        canvas.drawPath(fillPath, paint)

        //Typewriter juego 1 tutorial
        Handler().postDelayed({
            typewriter(view)
        }, 2000)

        //Typewriter juego 1 tutorial fin

        //Audio juego 1
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego1audio)
                audio.start()
                audio.setOnCompletionListener {

                    Handler().postDelayed({
                        //llama a la funcion para la animacion de salida cuando el audio se termina
                        exitAnimationfun(view)
                    }, 1000)
                }
            }
        }
        //animacion para la descripcion
        starAnimationfun(view)




        return view

    }


    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_1tutorialjuego1) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.jositajositext))
        typeWriterView.setDelay(70)
    }

    private fun starAnimationfun(view:View) {
        // animacion fondo gris
        val txt_animacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context,R.anim.fade)
        txt_animacion.startAnimation(aniFade)

        //animacion entrada upelio
        vistaanimada= TranslateAnimation (-1000f,0f, 0f, 0f)
        vistaanimada.duration=2000
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaanimada)

        //llamamos a la animacion para animar a upelio
        Handler().postDelayed({
            upelio.isVisible=false
            talkAnimationfun(view)
        }, 2000)

    }

    private fun talkAnimationfun(view:View) {
        val upelio = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.getBackground() as AnimationDrawable
        ani.start()

    }

    private fun exitAnimationfun(view:View){
        //escondemos la manzanda de la animacion
        val upelioanimado = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelioanimado.isVisible = false

        //animacion salido upelio
        vistaanimada= TranslateAnimation (0f,1000f, 0f, 0f)
        vistaanimada.duration=2000

        //vistaanimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaanimada)

        //animacion fondo gris
        Handler().postDelayed({
            val txt_animacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
            val aniFade = AnimationUtils.loadAnimation(context,R.anim.fade_out)
            txt_animacion.startAnimation(aniFade)
            txtv1_1tutorialjuego1.startAnimation(aniFade)
            txtv1_1tutorialjuego1.isVisible=false
            txt_animacion.isVisible=false
        }, 1000)
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
            Fragment1_1_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}