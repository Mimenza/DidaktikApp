package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.Model.Preguntasjuego2
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity7_Juego2_Results
import kotlinx.android.synthetic.main.fragment1_2_juego.*
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
    private var mCurrentPosition:Int=1
    private var mQuestionList: ArrayList<Preguntasjuego2>?= null
    private var mSelectedOptionPosition: Int= 0
    private lateinit var progressBar : ProgressBar
    private lateinit var txtProgressBar : TextView
    private lateinit var question1 : TextView
    private lateinit var question1_answer1 : TextView
    private lateinit var question1_answer2 : TextView
    private lateinit var question1_answer3 : TextView
    private lateinit var question1_answer4: TextView
    private lateinit var btnSiguiente : Button
    private  var mCorrectAnswers: Int = 0

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
        //Inicializar vistas
        val button:Button = view.findViewById(R.id.btnf1_2siguiente)
        val ajustes:ImageButton = view.findViewById(R.id.btnf1_2_ajustes)

            progressBar = view.findViewById(R.id.custom_progressBar)
            txtProgressBar =view.findViewById(R.id.txtv1_2_progreessbar)
            question1= view.findViewById(R.id.txtv1_2_pregunta1)
            question1_answer1= view.findViewById(R.id.txtv1_2_respuesta1)
            question1_answer2= view.findViewById(R.id.txtv1_2_respuesta2)
            question1_answer3= view.findViewById(R.id.txtv1_2_respuesta3)
            question1_answer4= view.findViewById(R.id.txtv1_2_respuesta4)
            btnSiguiente= view.findViewById(R.id.btnf1_2siguiente)


        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_2_juego_to_fragment2_2_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_2_juego_to_fragment4_menu)
        }


        setQuestion()


        //Cuando hacer click en la respuesta de la pregunta, lanzamos mensaje de error o de acierto
        question1_answer1!!.setOnClickListener(this)
        question1_answer2!!.setOnClickListener(this)
        question1_answer3!!.setOnClickListener(this)
        question1_answer4!!.setOnClickListener(this)
        btnSiguiente!!.setOnClickListener(this)


        return view
    }

    private fun setQuestion(){

        preguntasjuego2= Preguntasjuego2()

        //Recojemos las preguntas del Constants juego2
        mQuestionList= Constantsjuego2.getQuestions()

        //Posición de la pregunta en el progress bar (0)
        val question: Preguntasjuego2 = mQuestionList!! [mCurrentPosition-1]
        defaultOptionsView()

        if (mCurrentPosition==mQuestionList!!.size){

            btnSiguiente.text="Amaitu"
        }else{
            btnSiguiente.text="Bidali"
        }
        progressBar!!.progress= mCurrentPosition
        txtProgressBar!!.text= "$mCurrentPosition" + "/" + progressBar.max
        question1!!.text= question!!.question
        question1_answer1!!.text= question!!.optionOne
        question1_answer2!!.text= question!!.optionTwo
        question1_answer3!!.text= question!!.optionThree
        question1_answer4!!.text= question!!.optionFour

    }


    //COLOR Y FONDO POR DEFECTO DE LAS RESPUESTAS
    private fun defaultOptionsView(){

         val options= ArrayList<TextView>()
        options.add(0, question1_answer1)
        options.add(1, question1_answer2)
        options.add(2, question1_answer3)
        options.add(3, question1_answer4)

        for (option in options){

            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface= Typeface.DEFAULT
            option.background= ContextCompat.getDrawable(
                requireContext(), R.drawable.juego2_default_option_border_bg
            )
        }
    }

    override fun onClick(v:View?) {
     //Según el botón que hayamos clickado, pasamos la id a la función selectedOptionView
        when(v?.id){

            R.id.txtv1_2_respuesta1->{
                  selectedOptionView(txtv1_2_respuesta1, 1)
            }
            R.id.txtv1_2_respuesta2->{
                selectedOptionView(txtv1_2_respuesta2, 2)
            }
            R.id.txtv1_2_respuesta3->{
                selectedOptionView(txtv1_2_respuesta3, 3)
            }
            R.id.txtv1_2_respuesta4->{
                selectedOptionView(txtv1_2_respuesta4, 4)
            }
            R.id.btnf1_2siguiente->{
            //Si mi posicion inicial de la opcion es el 0
                if (mSelectedOptionPosition==0){
                    mCurrentPosition++
                    //Si mi posicion es menor o igual que el tamaño de la lista
                    when{
                        mCurrentPosition<=mQuestionList!!.size->{

                             setQuestion()
                        }else->{

                         /*Nos redirecciona a la activity de resultados,
                         recojemos las respuestas correctas y el total de preguntas
                        */
                            activity?.let{
                                val intent = Intent (it, Activity7_Juego2_Results::class.java)
                                intent.putExtra(Constantsjuego2.CORRECT_ANSWERS, mCorrectAnswers)
                                intent.putExtra(Constantsjuego2.TOTAL_QUESTIONS, mQuestionList!!.size)
                                it.startActivity(intent)
                                  }
                        }

                    }
                }else{

                    val question= mQuestionList?.get(mCurrentPosition-1)
                        if (question!!.correctAnswer!=mSelectedOptionPosition){

                            answerView(mSelectedOptionPosition, R.drawable.juego2_error_option_border_bg)
                        }else{
                            mCorrectAnswers++
                        }
                    question.correctAnswer?.let { answerView(it, R.drawable.juego2_correct_option_border_bg) }

                  if (mCurrentPosition==mQuestionList!!.size){

                       btnSiguiente.text="Amaitu jokoa"
                  }else{

                      btnSiguiente.text="Joan hurrengo galderara"
                  }

                    mSelectedOptionPosition=0
                }

            }

        }

    }

    //NOS MUESTRA LA RESPUESTA AL HACER CLICK EN SIGUIENTE

    fun answerView(answer: Int, drawableView:Int){
        //Cambiamos el fondo a cada respuesta
        when(answer){

            1->{

                question1_answer1.background=ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            2->{

                question1_answer2.background=ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }
            3->{

                question1_answer3.background=ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }

            4->{

                question1_answer4.background=ContextCompat.getDrawable(
                    requireContext(), drawableView
                )
            }



        }
    }


    //COLOR Y FONDO RESPUESTA SELECCIONADA
    private  fun selectedOptionView(tv:TextView, selectedOptiomNum:Int){

        defaultOptionsView()
        mSelectedOptionPosition= selectedOptiomNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background= ContextCompat.getDrawable(
            requireContext(), R.drawable.juego2_selected_option_border_bg)

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


}