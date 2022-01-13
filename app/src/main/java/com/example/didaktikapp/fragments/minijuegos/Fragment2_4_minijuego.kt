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
    private var acierto:Int = 0
    private lateinit var txtAciertos:TextView
    private lateinit var siguiente: Button

    private lateinit var manzana1 : ImageView
    private lateinit var manzana2 : ImageView
    private lateinit var manzana3 : ImageView
    private lateinit var manzana4 : ImageView
    private lateinit var manzana5 : ImageView

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
         manzana1= view.findViewById(R.id.manzana1_minijuego4)
         manzana2= view.findViewById(R.id.manzana2_minijuego4)
         manzana3= view.findViewById(R.id.manzana3_minijuego4)
         manzana4= view.findViewById(R.id.manzana4_minijuego4)
         manzana5= view.findViewById(R.id.manzana5_minijuego4)

        siguiente.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }

        siguiente.visibility=View.GONE

       /* manzana1.setOnTouchListener(listener)
        manzana2.setOnTouchListener(listener)
        manzana3.setOnTouchListener(listener)
        manzana4.setOnTouchListener(listener)
        manzana5.setOnTouchListener(listener)*/
        txtAciertos = view.findViewById(R.id.manzanasAciertos4)


        view.setOnTouchListener(handleTouch)
        return view
    }


   /* @SuppressLint("ClickableViewAccessibility", "ResourceType")
    var listener = View.OnTouchListener { viewElement, motionEvent ->

        var touch = 1

              viewElement.resources.getDrawable(R.drawable.trozomanzana1)

        val aniFade2 = AnimationUtils.loadAnimation(context, R.anim.slice_down)
        viewElement.startAnimation(aniFade2)
        viewElement.isVisible = false

        acierto= acierto + touch

        txtAciertos.text = acierto.toString()

         checkProgress()
        true
    }*/



    @SuppressLint("ClickableViewAccessibility")
    private val handleTouch = OnTouchListener { v, event ->
        val x = event.x.toInt()
        val y = event.y.toInt()

        var entra:Boolean = false
        var sale:Boolean = false

        val manzana1Location = IntArray(2)
        manzana1.getLocationOnScreen(manzana1Location)

        var manzana1PosX = manzana1Location[0]
        var manzana1PosY = manzana1Location[1]
        var manzana1Width = manzana1.width
        var manzana1height = manzana1.height

        //punto1 x
        var PosX1 = manzana1PosX-(manzana1Width/2)

        //punto2 x
        var PosX2 = manzana1PosX+(manzana1Width/2)

        //punto1 y
        var PosY1 = manzana1PosY-(manzana1height/2)

        //punto2 y
        var PosY2 = manzana1PosY+(manzana1height/2)

                when (event.action) {
            MotionEvent.ACTION_MOVE ->{
                //mientras mueves el dedo
                if ((x>= PosX1 && x <= PosX2) && (y>= PosY1 && y <= PosY2)) {
                    println("SI")
                }
                else{
                    println("NO")
                }

            }
            MotionEvent.ACTION_UP ->{
                //cuando levantas el dedo
                 entra = false
                 sale = false

            }


        }

        true
    }
    fun checkProgress(){

        if(acierto==5){

            siguiente.isVisible = true
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

