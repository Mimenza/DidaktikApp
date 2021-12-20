package com.example.didaktikapp.fragments.minijuegos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment4_menu
import com.example.didaktikapp.fragments.Fragment5_ajustes
import kotlinx.android.synthetic.main.activity7_juego2_results.*

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
    private var fragment :Fragment? = null
    var menuShowing: Boolean = false
    var ajustesShowing: Boolean = false


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
        val view = inflater.inflate(R.layout.fragment2_2_minijuego, container, false)
        val button: Button = view.findViewById(R.id.btnf2_2siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_2ajustes)
        val btnterminar: Button? = view?.findViewById(R.id.btn7_terminar)
        val btnRetry: Button? = view?.findViewById(R.id.btn7_saiatuberriro)
        val txtResult: TextView? = view?.findViewById(R.id.txtv7_result)
        val imgTrofeo: ImageView? = view?.findViewById(R.id.txtv1_2_respuesta1)
        val scoreuser: TextView? = view?.findViewById(R.id.txtv7_scoreuser)
        val txtvminijuego: TextView? = view?.findViewById(R.id.txtv2_2minijuego2)


        button.setOnClickListener(){

      //de activity(Resultsactivity) a fragment
            button.visibility = View.GONE
            showMenu()


            if (btnterminar != null) {
                btnterminar.isVisible=false
            }
            if (btnRetry != null) {
                btnRetry.isVisible=false
            }
            if (txtResult != null) {
                txtResult.isVisible=false
            }
            if (imgTrofeo != null) {
                imgTrofeo.isVisible=false
            }
            if (scoreuser != null) {
                scoreuser.isVisible=false
            }
            if (txtvminijuego != null) {
                txtvminijuego.isVisible=false
            }

        }

        ajustes.setOnClickListener(){
            showAjustes()
            button.visibility = View.GONE

            if (btnterminar != null) {
                btnterminar.isVisible=false
            }
            if (btnRetry != null) {
                btnRetry.isVisible=false
            }
            if (txtResult != null) {
                txtResult.isVisible=false
            }
            if (imgTrofeo != null) {
                imgTrofeo.isVisible=false
            }
            if (scoreuser != null) {
                scoreuser.isVisible=false
            }
            if (txtvminijuego != null) {
                txtvminijuego.isVisible=false
            }

        }
        return view
    }

    fun showMenu(){
        val fragManager:FragmentManager? = fragmentManager

        if (menuShowing) {
            return
        }
        menuShowing = true
        fragment = Fragment4_menu()
        fragManager?.beginTransaction()?.add(R.id.framelayout2menu, fragment!!)?.commit()


    }
    fun showAjustes(){
        val fragManager:FragmentManager? = fragmentManager

        if (ajustesShowing) {
            return
        }
        ajustesShowing = true
        fragment = Fragment5_ajustes()
        fragManager?.beginTransaction()?.add(R.id.framelayout2ajustes, fragment!!)?.commit()


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
            Fragment2_2_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}