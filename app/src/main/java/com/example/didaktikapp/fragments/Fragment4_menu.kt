package com.example.didaktikapp.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity1_Principal

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment4_menu.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment4_menu : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment4_menu, container, false)
        val ajustes: Button = view.findViewById(R.id.btn4f_ajustes)
        val ayuda: Button = view.findViewById(R.id.btn4f_ayuda)
        val inicio: Button = view.findViewById(R.id.btn4f_inicio)
        val repetir: Button = view.findViewById(R.id.btn4f_repetir)
        val siguiente: Button = view.findViewById(R.id.btn4f_siguiente)

        //Navegacion a los framents
        ajustes.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_fragment4_menu_to_fragment5_ajustes)
        }

        ayuda.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_fragment4_menu_to_fragment6_ayuda)
        }

        //Navegacion a activities
        /*inicio.setOnClickListener(){
            val intent = Intent(this, Activity1_Principal::class.java)

            startActivity(intent)

        }*/

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment4_menu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment4_menu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}