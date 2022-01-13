package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity7_juego2_results.*
import kotlinx.coroutines.NonCancellable.cancel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_2_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var aciertoTxt:TextView
    private var acierto:Int = 0
    private lateinit var siguiente: Button
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
        val view= inflater.inflate(R.layout.fragment2_2_minijuego, container, false)

        val ajustes: ImageButton = view.findViewById(R.id.btnf2_2ajustes)
        siguiente = view.findViewById(R.id.btnf2_2siguiente)

        siguiente.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_2_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment2_1_minijuego_to_fragment4_menu)

        }

        val manzana1:ImageView = view.findViewById(R.id.imgv2_2_manzanav)
        val manzana2:ImageView = view.findViewById(R.id.imgv2_2_manzanav1)
        val manzana3:ImageView = view.findViewById(R.id.imgv2_2_manzanav2)
        val manzana4:ImageView = view.findViewById(R.id.imgv2_2_manzanav3)
        val manzana5:ImageView = view.findViewById(R.id.imgv2_2_manzanav4)

         aciertoTxt = view.findViewById(R.id.manzanasAciertos)

        manzana1.setOnClickListener(){desaparecer(manzana1)}
        manzana2.setOnClickListener(){desaparecer(manzana2)}
        manzana3.setOnClickListener(){desaparecer(manzana3)}
        manzana4.setOnClickListener(){desaparecer(manzana4)}
        manzana5.setOnClickListener(){desaparecer(manzana5)}
        return view
    }

    fun desaparecer(manzana:ImageView){

        val aniFade = AnimationUtils.loadAnimation(context, R.anim.disapear)
        manzana.startAnimation(aniFade)
        manzana.isVisible = false
        acierto++
        aciertoTxt.text = acierto.toString() + " / 5"

        //manzana.setOnClickListener(){}

        checkProgress()

    }

    fun checkProgress(){

        if(acierto==5){

            siguiente.isVisible = true
        }
    }
}