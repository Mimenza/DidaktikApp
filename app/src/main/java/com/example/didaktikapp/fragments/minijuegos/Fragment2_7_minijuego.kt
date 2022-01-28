package com.example.didaktikapp.fragments.minijuegos

import android.content.Intent
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
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.fragments.Fragment4_ajustes


class Fragment2_7_minijuego : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment2_7_minijuego, container, false)
        // Inflate the layout for this fragment

        val button: Button = view.findViewById(R.id.btnf2_7siguiente)
        val btnterminar: Button? = view?.findViewById(R.id.btn1_2_terminar)
        val btnRetry: Button? = view?.findViewById(R.id.btn7_1_saiatuberriro)
        val txtResult: TextView? = view?.findViewById(R.id.txtv7_1_result)
        val imgTrofeo: ImageView? = view?.findViewById(R.id.txtv7_1_imagen1)
        val scoreuser: TextView? = view?.findViewById(R.id.txtv7_1_scoreuser)
        val txtvminijuego: TextView? = view?.findViewById(R.id.txtv2_7minijuego7)


        val mapa: ImageButton = view.findViewById(R.id.btnf2_7_mapa)
        mapa.setOnClickListener {
            activity?.let{
                val intent = Intent (it, Activity5_Mapa::class.java)
                it.startActivity(intent)
            }

        }
        button.setOnClickListener(){

            //de activity(Resultsactivity) a fragment
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



}