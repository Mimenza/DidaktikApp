package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
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
class Fragment2_4_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    private lateinit var txtAciertos:TextView

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
        val view = inflater.inflate(R.layout.fragment2_4_minijuego, container, false)
        val button: Button = view.findViewById(R.id.btnf2_4siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_4ajustes)
        txtAciertos = view.findViewById((R.id.manzanasAciertos4))

        //manzanas
        val manzana1: ImageView= view.findViewById(R.id.manzana1_minijuego4)
        val manzana2: ImageView= view.findViewById(R.id.manzana2_minijuego4)
        val manzana3: ImageView= view.findViewById(R.id.manzana3_minijuego4)
        val manzana4: ImageView= view.findViewById(R.id.manzana4_minijuego4)
        val manzana5: ImageView= view.findViewById(R.id.manzana5_minijuego4)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_4_minijuego_to_fragment4_menu)
        }

        button.visibility=View.GONE


        prepairApples()



        return view
    }

   fun prepairApples(){



   }


    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
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