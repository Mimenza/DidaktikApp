package com.example.didaktikapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.fragment6_ayuda.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment6_ayuda.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment6_ayuda : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment6_ayuda, container, false)
        val tvDescripcion: TextView? = view?.findViewById<TextView>(R.id.txtv6_ayuda)
        val btnBackToGame: ImageView = view.findViewById(R.id.imgv6f_backtogame)
        //Recojemos el shared prefs

        btnBackToGame.setOnClickListener{
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment6_ayuda_to_fragment4_menu)
        }

        val sharedPreferences = this.activity?.getSharedPreferences("site", 0)
        val numero = sharedPreferences?.getString("numero", null)?.toInt()

        println(numero)
        var descripcionTitulo: String = ""


        when(numero){

            0->  {descripcionTitulo=resources.getString(R.string.ayudajuego1)}
            1->  {descripcionTitulo=resources.getString(R.string.ayudajuego2)}
            2->  {descripcionTitulo=resources.getString(R.string.ayudajuego3)}
            3->  {descripcionTitulo=resources.getString(R.string.ayudajuego4)}
            4->  {descripcionTitulo=resources.getString(R.string.ayudajuego5)}
            5->  {descripcionTitulo=resources.getString(R.string.ayudajuego6)}
            6->  {descripcionTitulo=resources.getString(R.string.ayudajuego7)}


        }

        if (tvDescripcion!=null){

            tvDescripcion.setText(descripcionTitulo)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment6_ayuda.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment6_ayuda().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}