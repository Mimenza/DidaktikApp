package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import android.util.Log
import android.view.View.OnTouchListener
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Rect
import android.media.Image
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
    private lateinit var txtAciertos: TextView
    private lateinit var siguiente: Button

    private lateinit var manzana1: ImageView
    private lateinit var manzana2: ImageView
    private lateinit var manzana3: ImageView
    private lateinit var manzana4: ImageView
    private lateinit var manzana5: ImageView
    private lateinit var manzanaSeleccionada: ImageView
    private lateinit var test : ImageView

    var entra: Boolean = false
    var sale: Boolean = false

    var dejarCortar: Boolean = false

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
        siguiente = view.findViewById(R.id.btnf2_4siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_4ajustes)
        txtAciertos = view.findViewById((R.id.manzanasAciertos4))

        //manzanas
        manzana1 = view.findViewById(R.id.manzana1_minijuego4)
        manzana2 = view.findViewById(R.id.manzana2_minijuego4)
        manzana3 = view.findViewById(R.id.manzana3_minijuego4)
        manzana4 = view.findViewById(R.id.manzana4_minijuego4)
        manzana5 = view.findViewById(R.id.manzana5_minijuego4)


        //test
         test = view.findViewById(R.id.manzanasAciertos4)
         manzanaSeleccionada = test

        siguiente.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }
        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }
        siguiente.visibility = View.GONE
        txtAciertos = view.findViewById(R.id.manzanasAciertos4)

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

                if ((x >= M1PosX1 && x <= M1PosX2) && (y >= M1PosY1 && y <= M1PosY2)) {

                    manzanaSeleccionada = manzana1
                }
                if ((x >= M2PosX1 && x <= M2PosX2) && (y >= M2PosY1 && y <= M2PosY2)) {

                    manzanaSeleccionada = manzana2
                }
                if ((x >= M3PosX1 && x <= M3PosX2) && (y >= M3PosY1 && y <= M3PosY2)) {

                    manzanaSeleccionada = manzana3
                }
                if ((x >= M4PosX1 && x <= M4PosX2) && (y >= M4PosY1 && y <= M4PosY2)) {

                    manzanaSeleccionada = manzana4
                }
                if ((x >= M5PosX1 && x <= M5PosX2) && (y >= M5PosY1 && y <= M5PosY2)) {

                    manzanaSeleccionada = manzana5
                }



                if ( manzanaSeleccionada != test) {
                    println(manzanaSeleccionada.id)

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
                    }
                    //seteamos la variable para que corte la manzana
                    dejarCortar = true
                }

                if (entra && sale) {
                    cortarManzana(manzanaSeleccionada)
                }

            }
            MotionEvent.ACTION_DOWN -> {
                //cuando aprietas el dedo
                entra = false
                sale = false
                dejarCortar = false
                manzanaSeleccionada = test
            }

            MotionEvent.ACTION_UP -> {
                //cuando levantas el dedo
                entra = false
                sale = false
                dejarCortar = false
                manzanaSeleccionada = test

            }


        }

        true
    }

    fun checkProgress() {

        if (acierto == 5) {

            siguiente.isVisible = true
        }
    }

    fun cortarManzana(manzana: ImageView) {
        //reseteamos las variables para la siguiente manzana
        entra = false
        sale = false
        //animacion
        val aniFade2 = AnimationUtils.loadAnimation(context, R.anim.slice_down)
        manzana.startAnimation(aniFade2)
        manzana.isVisible = false

        manzanaSeleccionada = test
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

