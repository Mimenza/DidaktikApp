package com.example.didaktikapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity1_Principal
import android.app.Activity
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment4_menu2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment4_menu : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var fragment : Fragment?=null

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
        val view = inflater.inflate(R.layout.fragment4_menu2, container, false)
        val ajustes: Button = view.findViewById(R.id.btn4f_ajustes2)
        val ayuda: Button = view.findViewById(R.id.btn4f_ayuda2)
        val inicio: Button = view.findViewById(R.id.btn4f_inicio2)
        val siguiente: Button = view.findViewById(R.id.btn4f_siguiente2)

        //Navegacion a los framents
        ajustes.setOnClickListener{
            (activity as Activity6_Site)?.ajustesCheck()
        }

        ayuda.setOnClickListener{
            (activity as Activity6_Site)?.ayudaCheck()
        }

        //Navegacion a activities
        inicio.setOnClickListener(){
            val i = Intent(activity, Activity1_Principal::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }

        siguiente.setOnClickListener(){
            val i = Intent(activity, Activity5_Mapa::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)

        }

        return view
    }



}