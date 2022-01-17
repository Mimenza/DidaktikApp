package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import android.view.View.OnTouchListener
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.didaktikapp.Model.CustomLine
import kotlinx.android.synthetic.main.fragment2_4_minijuego.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_4_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var acierto: Int = 0
    private lateinit var aciertostxt: TextView


    private lateinit var manzana1: ImageView
    private lateinit var manzana2: ImageView
    private lateinit var manzana3: ImageView
    private lateinit var manzana4: ImageView
    private lateinit var manzana5: ImageView
    private lateinit var manzanaSeleccionada: ImageView
    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var contadorCartel: TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var layout: ConstraintLayout
    private lateinit var customLine: CustomLine
    private lateinit var customStroke: CustomLine


    var entra: Boolean = false
    var sale: Boolean = false
    var dentro: Boolean = false
    var dejarCortar: Boolean = false
    var manzana1cortada:Boolean = false
    var manzana2cortada:Boolean = false
    var manzana3cortada:Boolean = false
    var manzana4cortada:Boolean = false
    var manzana5cortada:Boolean = false

    private var lastX:Float = 0F
    private var lastY:Float = 0F
    private val customLines = arrayListOf<CustomLine>()
    private val customStrokes = arrayListOf<CustomLine>()


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
        val view = inflater.inflate(R.layout.fragment2_4_minijuego, container, false)
        layout = view.findViewById(R.id.cl2_4minijuego)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_4ajustes)


        //manzanas
        manzana1 = view.findViewById(R.id.manzana1_minijuego4)
        manzana2 = view.findViewById(R.id.manzana2_minijuego4)
        manzana3 = view.findViewById(R.id.manzana3_minijuego4)
        manzana4 = view.findViewById(R.id.manzana4_minijuego4)
        manzana5 = view.findViewById(R.id.manzana5_minijuego4)

        cartel= view.findViewById((R.id.imgv2_4cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_4carteltexto))
        contadorCartel= view.findViewById((R.id.txtv2_4contador))
        progressBar= view.findViewById((R.id.progressBar_minijuego4))
        aciertostxt= view.findViewById(R.id.txt2_4_acierto)

        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }



        view.setOnTouchListener(handleTouch)
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private val handleTouch = OnTouchListener { v, event ->
        val x = event.x.toInt()
        val y = event.y.toInt();


        var manzana1PosX = manzana1.x
        var manzana1PosY = manzana1.y
        var manzana1Width = manzana1.width
        var manzana1height = manzana1.height

        var manzana2PosX = manzana2.x
        var manzana2PosY = manzana2.y
        var manzana2Width = manzana2.width
        var manzana2height = manzana2.height

        var manzana3PosX = manzana3.x
        var manzana3PosY = manzana3.y
        var manzana3Width = manzana3.width
        var manzana3height = manzana3.height

        var manzana4PosX = manzana4.x
        var manzana4PosY = manzana4.y
        var manzana4Width = manzana4.width
        var manzana4height = manzana4.height

        var manzana5PosX = manzana5.x
        var manzana5PosY = manzana5.y
        var manzana5Width = manzana5.width
        var manzana5height = manzana5.height


        // manzana1 punto1 x
        var M1PosX1 = manzana1PosX + 15

        // manzana1 punto2 x
        var M1PosX2 = manzana1PosX + (manzana1Width) - 15

        // manzana1 punto1 y
        var M1PosY1 = manzana1PosY + 15

        // manzana1 punto2 y
        var M1PosY2 = manzana1PosY + (manzana1height) - 15

        //-------------------------------------------------

        // manzana2 punto1 x
        var M2PosX1 = manzana2PosX + 15

        // manzana2 punto2 x
        var M2PosX2 = manzana2PosX + (manzana2Width) - 15

        // manzana2 punto1 y
        var M2PosY1 = manzana2PosY + 15

        // manzana2 punto2 y
        var M2PosY2 = manzana2PosY + (manzana2height) - 15

        //-------------------------------------------------

        // manzana3 punto1 x
        var M3PosX1 = manzana3PosX + 15

        // manzana3 punto2 x
        var M3PosX2 = manzana3PosX + (manzana3Width) - 15

        // manzana3 punto1 y
        var M3PosY1 = manzana3PosY + 15

        // manzana3 punto2 y
        var M3PosY2 = manzana3PosY + (manzana3height) - 15

        //-------------------------------------------------

        // manzana4 punto1 x
        var M4PosX1 = manzana4PosX + 15

        // manzana4 punto2 x
        var M4PosX2 = manzana4PosX + (manzana4Width) - 15

        // manzana4 punto1 y
        var M4PosY1 = manzana4PosY + 15

        // manzana4 punto2 y
        var M4PosY2 = manzana4PosY + (manzana4height) - 15

        //-------------------------------------------------

        // manzana5 punto1 x
        var M5PosX1 = manzana5PosX + 15

        // manzana5 punto2 x
        var M5PosX2 = manzana5PosX + (manzana5Width) - 15

        // manzana5 punto1 y
        var M5PosY1 = manzana5PosY + 15

        // manzana5 punto2 y
        var M5PosY2 = manzana5PosY + (manzana5height) - 15


        when (event.action) {

            MotionEvent.ACTION_MOVE -> {
                //mientras mueves el dedo

                if ((x >= M1PosX1 && x <= M1PosX2) && (y >= M1PosY1 && y <= M1PosY2) && !manzana1cortada) {
                    manzanaSeleccionada = manzana1
                    dentro = true
                } else if ((x >= M2PosX1 && x <= M2PosX2) && (y >= M2PosY1 && y <= M2PosY2) && !manzana2cortada ) {
                    manzanaSeleccionada = manzana2
                    dentro = true
                } else if ((x >= M3PosX1 && x <= M3PosX2) && (y >= M3PosY1 && y <= M3PosY2) && !manzana3cortada) {
                    manzanaSeleccionada = manzana3
                    dentro = true
                } else if ((x >= M4PosX1 && x <= M4PosX2) && (y >= M4PosY1 && y <= M4PosY2) && !manzana4cortada) {
                    manzanaSeleccionada = manzana4
                    dentro = true
                } else if ((x >= M5PosX1 && x <= M5PosX2) && (y >= M5PosY1 && y <= M5PosY2) && !manzana5cortada) {
                    manzanaSeleccionada = manzana5
                    dentro = true

                } else {
                    dentro = false
                }

                if (dentro == true) {

                    //el dedo esta dentro del area
                    if (!entra && dejarCortar) {
                        //seteamos la variable de que ha entrado
                        entra = true

                    }
                } else {
                    //el dedo no esta en el area
                    if (entra) {
                        //si el dedo estaba dentro significa que ha salido
                        sale = true
                        dentro = false

                    }
                    //seteamos la variable para que corte la manzana
                    dejarCortar = true
                }

                if (entra && sale) {
                    cortarManzana(manzanaSeleccionada)
                }

                customLine = CustomLine(requireContext(), lastX, lastY, x.toFloat(), y.toFloat(), 8F, 255, 255, 255, 255)
                customStroke = CustomLine(requireContext(), lastX, lastY, x.toFloat(), y.toFloat(), 20F, 128, 255, 255, 255)

                layout.addView(customLine)
                layout.addView(customStroke)

                customLines.add(customLine)
                customStrokes.add(customStroke)

                lastX = event.x
                lastY = event.y

                if(customLines.size > 7){
                    layout.removeView(customLines[0])
                    layout.removeView(customStrokes[0])

                    customLines.remove(customLines[0])
                    customStrokes.remove(customStrokes[0])
                }

            }

            MotionEvent.ACTION_DOWN -> {
                //cuando aprietas el dedo
                entra = false
                sale = false
                dejarCortar = false

                lastX = event.x
                lastY = event.y

            }

            MotionEvent.ACTION_UP -> {
                //cuando levantas el dedo
                entra = false
                sale = false
                dejarCortar = false
                dentro = false

                for(i in customLines){
                    layout.removeView(i)
                }

                for(i in customStrokes){
                    layout.removeView(i)
                }
            }
        }
        true
    }


    fun checkProgress() {
        //si se han cortado todas las manzanas aparece el boton
        if (acierto== 4) {

            starAnimationfun()
        } else {
            acierto++
            aciertostxt.text= acierto.toString()
        }

    }

    fun starAnimationfun(){

        //Diseñar cartel madera
        contadorCartel.visibility=View.VISIBLE
        cartel.visibility=View.VISIBLE
        txtcartel.visibility=View.VISIBLE



        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 1000

        cartel.startAnimation(vistaAnimada)
        contadorCartel.startAnimation(vistaAnimada)
        txtcartel.startAnimation(vistaAnimada)

        object : CountDownTimer(5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                contadorCartel.text= (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {

                progressBar.isVisible=true
                contadorCartel.isVisible=false
                Handler().postDelayed({

                    view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu) }
                }, 2000)

            }
        }.start()

    }

    fun cortarManzana(manzana: ImageView) {
        //reseteamos las variables para la siguiente manzana
        entra = false
        sale = false

        //animacion y foto random

        var random =(0.. 5). random()

        when (random) {

            0->{ manzana.setImageResource(R.drawable.trozomanzana1)}
            1->{ manzana.setImageResource(R.drawable.trozomanzana2)}
            2->{ manzana.setImageResource(R.drawable.trozomanzana3)}
            3->{ manzana.setImageResource(R.drawable.trozomanzana4)}
            4->{ manzana.setImageResource(R.drawable.trozomanzana5)}
            5->{ manzana.setImageResource(R.drawable.trozomanzana6)}
        }

        val aniFade2 = AnimationUtils.loadAnimation(context, R.anim.slice_down)
        manzana.startAnimation(aniFade2)
        manzana.isVisible = false

        checkProgress()
        //when para settear que manzana hemos cortado ya
        when (manzana) {
            manzana1 -> {manzana1cortada=true}
            manzana2 -> {manzana2cortada=true}
            manzana3 -> {manzana3cortada=true}
            manzana4 -> {manzana4cortada=true}
            manzana5 -> {manzana5cortada=true}

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment2_minijuego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2_4_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

