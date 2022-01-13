package com.example.didaktikapp.fragments.minijuegos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R

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
    private lateinit var siguiente: Button
    private lateinit var vaso: ImageView

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
        siguiente = view.findViewById(R.id.btnf2_5siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_5ajustes)
        vaso  = view.findViewById(R.id.imgv2_5vaso)

        siguiente.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment2_5_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment2_5_minijuego_to_fragment4_menu)
        }

        val manzana1: ImageView = view.findViewById(R.id.imgv2_5_applepiece1)
        val manzana2: ImageView = view.findViewById(R.id.imgv2_5_applepiece2)
        val manzana3: ImageView = view.findViewById(R.id.imgv2_5_applepiece3)
        val manzana4: ImageView = view.findViewById(R.id.imgv2_5_applepiece4)
        val manzana5: ImageView = view.findViewById(R.id.imgv2_5_applepiece5)
        val manzana6: ImageView = view.findViewById(R.id.imgv2_5_applepiece6)


        manzana1.setOnClickListener() { machacar(manzana1) }
        manzana2.setOnClickListener() { machacar(manzana2) }
        manzana3.setOnClickListener() { machacar(manzana3) }
        manzana4.setOnClickListener() { machacar(manzana4) }
        manzana5.setOnClickListener() { machacar(manzana5) }
        manzana6.setOnClickListener() { machacar(manzana6) }

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

            siguiente.isVisible = true
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
        changeVaso()
        acierto++



    }

    fun changeVaso(){

        println("acierto"+ acierto)

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