package com.example.didaktikapp.fragments.minijuegos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_5_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var acierto: Int = 0

    private lateinit var vaso: ImageView
    private lateinit var vistaAnimada:TranslateAnimation

    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button

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
        val view = inflater.inflate(R.layout.fragment2_5_minijuego, container, false)

        val ajustes: ImageButton = view.findViewById(R.id.btnf2_5ajustes)
        vaso  = view.findViewById(R.id.imgv2_5vaso)
        cartel= view.findViewById((R.id.imgv2_5cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_5carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_5_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_5_siguiente)


        ajustes.setOnClickListener() {
            (activity as Activity6_Site?)?.menuCheck()
        }

        val manzana1: ImageView = view.findViewById(R.id.imgv2_5_applepiece1)
        val manzana2: ImageView = view.findViewById(R.id.imgv2_5_applepiece2)
        val manzana3: ImageView = view.findViewById(R.id.imgv2_5_applepiece3)
        val manzana4: ImageView = view.findViewById(R.id.imgv2_5_applepiece4)
        val manzana5: ImageView = view.findViewById(R.id.imgv2_5_applepiece5)



        manzana1.setOnClickListener() { machacar(manzana1) }
        manzana2.setOnClickListener() { machacar(manzana2) }
        manzana3.setOnClickListener() { machacar(manzana3) }
        manzana4.setOnClickListener() { machacar(manzana4) }
        manzana5.setOnClickListener() { machacar(manzana5) }


        return view
    }

    fun machacar(manzana: ImageView) {

        println(manzana.id)
        //declaramos variable a 0
        var pulsaciones: Int = 0
        zoom(manzana)
        println(pulsaciones)

        manzana.setOnClickListener() {
            if (pulsaciones < 4) {
                zoom(manzana)
                pulsaciones++
            }

            if (pulsaciones == 4) {
                disapear(manzana)

                checkProgress()
            }
        }


    }

    fun checkProgress() {
        println("check" + acierto)
        if (acierto == 5) {


            starAnimationfun()
        }
    }

    fun starAnimationfun(){

        //Diseñar cartel madera
        btnsiguiente.visibility = View.VISIBLE
        btnrepetir.visibility = View.VISIBLE
        cartel.visibility=View.VISIBLE
        txtcartel.visibility=View.VISIBLE



        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 1000

        cartel.startAnimation(vistaAnimada)
        btnrepetir.startAnimation(vistaAnimada)
        btnsiguiente.startAnimation(vistaAnimada)
        txtcartel.startAnimation(vistaAnimada)

        btnsiguiente.setOnClickListener(){
            val i = Intent(activity, Activity5_Mapa::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }

        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_5_minijuego_self)
        }

    }
    fun zoom(manzana: ImageView) {
        println("ZOOM")
        val zoom = AnimationUtils.loadAnimation(context, R.anim.zoom)
        manzana.startAnimation(zoom)
    }

    fun disapear(manzana: ImageView) {
        manzana.setOnClickListener() {}
        println("DISAPEAR")
        manzana.setImageResource(R.drawable.manchaverde)
        val disapear = AnimationUtils.loadAnimation(context, R.anim.disapear)
        manzana.startAnimation(disapear)
        manzana.isVisible = false
        changeVaso(manzana)
        acierto++

    }

    fun changeVaso(manzana: ImageView){
        manzana.setOnClickListener() {}

        when (acierto) {
            0 ->{vaso.setImageResource(R.drawable.vaso2) }
            1 ->{vaso.setImageResource(R.drawable.vaso3) }
            2 ->{vaso.setImageResource(R.drawable.vaso4) }
            3 ->{vaso.setImageResource(R.drawable.vaso5) }
            4 ->{vaso.setImageResource(R.drawable.vaso6) }

            else -> {
                print("x is neither 1 nor 2")
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
         * @return A new instance of fragment Fragment2_minijuego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2_5_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}