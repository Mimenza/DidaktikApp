package com.example.didaktikapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.didaktikapp.R
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment3_info.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment3_info : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // private lateinit var button_back:ImageView
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

        val view = inflater.inflate(R.layout.fragment3_info, container, false)

     val button :Button= view.findViewById(R.id.btn3f_jugar)

        button.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_fragment3_info_to_fragment1_1_juego)

        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments!=null)
        {
            val titulo= getArguments()?.getString("titulo_juego")
            println(titulo)
            val descripcion= getArguments()?.getString("descripcion")
            val etTitulo= view?.findViewById<TextView>(R.id.txtv3f_nombrezona)
            val etDescripcion= view?.findViewById<TextView>(R.id.txtv3f_textozona)
            if (etTitulo != null) {
                etTitulo.setText(titulo.toString())
            }
            if (etDescripcion != null) {
                etDescripcion.setText(descripcion.toString())
            }

        }}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment3_info.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment3_info().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}