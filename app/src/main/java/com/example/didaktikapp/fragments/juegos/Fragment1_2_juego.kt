package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.Model.Preguntasjuego2
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.fragment1_2_juego.*
import java.security.cert.PKIXRevocationChecker
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_2_juego : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var preguntasjuego2: Preguntasjuego2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_2_juego, container, false)
        val button:Button = view.findViewById(R.id.btnf1_2siguiente)
        val ajustes:ImageButton = view.findViewById(R.id.btnf1_2_ajustes)
        val progressBar: ProgressBar = view.findViewById(R.id.custom_progressBar)
        val txtvProgressBar: TextView = view.findViewById(R.id.txtv1_2_progreessbar)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_2_juego_to_fragment2_2_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_2_juego_to_fragment4_menu)
        }

        //Inicializar vistas
        preguntasjuego2= Preguntasjuego2()



        //Recojemos las preguntas del Constants juego2
        val questionList= Constantsjuego2.getQuestions()
        val currentPosition=1
        //Posición de la pregunta en el progress bar (0)
        val question: Preguntasjuego2 = questionList [currentPosition-1]
        progressBar!!.progress= currentPosition
        txtvProgressBar!!.text= "$currentPosition" + "/" + progressBar.max


       /* txtv1_2_pregunta1.text= preguntasjuego2!!.question
        txtv1_2_respuesta1.text= preguntasjuego2!!.optionOne
        txtv1_2_respuesta2.text= preguntasjuego2!!.optionTwo
        txtv1_2_respuesta3.text= preguntasjuego2!!.optionThree
        txtv1_2_respuesta4.text= preguntasjuego2!!.optionFour */


       defaultOptionsView()
        txtv1_2_respuesta1.setOnClickListener(this)



        return view
    }

    private fun defaultOptionsView(){

         val options= ArrayList<TextView>()
        options.add(0, txtv1_2_respuesta1)
        options.add(1, txtv1_2_respuesta2)
        options.add(2, txtv1_2_respuesta3)
        options.add(3, txtv1_2_respuesta4)

        for (option in options){

            option.setTextColor(Color.parseColor("#7A8090"))
            option.typeface= Typeface.DEFAULT
            option.background= ContextCompat.getDrawable(
                requireContext(), R.drawable.juego2_option_border_bg
            )
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment1_juego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1_2_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(p0:View?) {


    }

    private  fun selectedOptionView(textView:TextView, selectedOptiomNum:Int){

        defaultOptionsView()

    }
}