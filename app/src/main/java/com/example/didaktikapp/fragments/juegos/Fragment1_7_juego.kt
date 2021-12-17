package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.Constantsjuego7
import com.example.didaktikapp.Model.Preguntasjuego7
import com.example.didaktikapp.R

import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.Activity7_1_Juego7_Results
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.android.synthetic.main.fragment1_2_juego.*

import kotlinx.android.synthetic.main.fragment1_7_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
class Fragment1_7_juego : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var preguntasjuego7: Preguntasjuego7
    private var mCurrentPosition:Int=1
    private var mQuestionList: ArrayList<Preguntasjuego7>?= null
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
    private lateinit var vistaanimada:TranslateAnimation

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
        val view = inflater.inflate(R.layout.fragment1_7_juego, container, false)
        //Inicializar vistas
        val button:Button = view.findViewById(R.id.btnf1_7siguiente)
        val ajustes:ImageButton = view.findViewById(R.id.btnf1_7_ajustes)
        var audio: MediaPlayer

        progressBar = view.findViewById(R.id.custom_progressBar)
        txtProgressBar =view.findViewById(R.id.txtv1_7_progreessbar)
        question1= view.findViewById(R.id.txtv1_7_pregunta1)
        question1_answer1= view.findViewById(R.id.txtv1_7_respuesta1)
        question1_answer2= view.findViewById(R.id.txtv1_7_respuesta2)
        question1_answer3= view.findViewById(R.id.txtv1_7_respuesta3)
        btnSiguiente= view.findViewById(R.id.btnf1_7siguiente)


        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_7_juego_to_fragment2_7_minijuego)
        }
        ajustes.setOnClickListener(){

            Navigation.findNavController(view).navigate(R.id.action_fragment1_7_juego_to_fragment4_menu)


                (activity as Activity6_Site?)?.menuCheck()


        }


        audiotutorial(view)

        //Creamos la pregunta y las respuestas segun la posicion
        setQuestion()


        //Cuando hacer click en la respuesta de la pregunta, lanzamos mensaje de error o de acierto
        question1_answer1!!.setOnClickListener(this)
        question1_answer2!!.setOnClickListener(this)
        question1_answer3!!.setOnClickListener(this)
        btnSiguiente!!.setOnClickListener(this)


        return view
    }



    fun audiotutorial(view:View){


        var audio: MediaPlayer
        //Audio juego 7 tutorial
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego7audiotutorial)
                audio.start()
                audio.setOnCompletionListener {

                    Handler().postDelayed({
                        if (getView() != null) {
                            //llama a la funcion para la animacion de salida cuando el audio se termina
                            exitAnimationfun(view)
                        }
                    }, 1000)
                }
            }
        }
        //animacion para la descripcion
        starAnimationfun(view)

    }
    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_7tutorialjuego7) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.belarriakadi))
        typeWriterView.setDelay(70)
    }
    private fun starAnimationfun(view: View) {
        // animacion fondo gris
        val txt_animacion = view.findViewById(R.id.txtv1_7fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txt_animacion.startAnimation(aniFade)

        //animacion entrada upelio
        vistaanimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaanimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_7_upelio) as ImageView
        upelio.startAnimation(vistaanimada)

        //llamamos a la animacion para animar a upelio
        Handler().postDelayed({
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
                typewriter(view)
            }
        }, 2000)

    }
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_7_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.getBackground() as AnimationDrawable
        ani.start()

    }

    private fun exitAnimationfun(view: View) {
        //escondemos la manzanda de la animacion
        val upelioanimado = view.findViewById(R.id.imgv1_7_upelio2) as ImageView
        upelioanimado.isVisible = false

        //animacion salido upelio
        vistaanimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaanimada.duration = 2000

        //vistaanimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_7_upelio) as ImageView
        upelio.startAnimation(vistaanimada)

        //animacion fondo gris
        Handler().postDelayed({
            if (getView() != null) {
                val txt_animacion = view.findViewById(R.id.txtv1_7fondogris) as TextView
                val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                txt_animacion.startAnimation(aniFade)
                txtv1_7tutorialjuego7.startAnimation(aniFade)
                txtv1_7tutorialjuego7.isVisible = false
                txt_animacion.isVisible = false
            }
        }, 1000)
    }

    private fun setQuestion(){

        preguntasjuego7= Preguntasjuego7()

        //Recojemos las preguntas del Constants juego2
        mQuestionList= Constantsjuego7.getQuestions()

        //Posición de la pregunta en el progress bar (0)
        val question: Preguntasjuego7 = mQuestionList!! [mCurrentPosition-1]
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


    }

    //COLOR Y FONDO POR DEFECTO DE LAS RESPUESTAS
    private fun defaultOptionsView(){

        val options= ArrayList<TextView>()
        options.add(0, question1_answer1)
        options.add(1, question1_answer2)
        options.add(2, question1_answer3)

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

            R.id.txtv1_7_respuesta1->{
                selectedOptionView(txtv1_7_respuesta1, 1)
            }
            R.id.txtv1_7_respuesta2->{
                selectedOptionView(txtv1_7_respuesta2, 2)
            }
            R.id.txtv1_7_respuesta3->{
                selectedOptionView(txtv1_7_respuesta3, 3)
            }

            R.id.btnf1_7siguiente->{
                //El usuario no ha elegido la opcion, cuando elija nos movemos de posicion
                if (mSelectedOptionPosition==0){
                    mCurrentPosition++
                    //Habilitamos las respuestas al clickar la respuesta
                    question1_answer1.isEnabled=true
                    question1_answer2.isEnabled=true
                    question1_answer3.isEnabled=true
                    //Si mi posicion es menor o igual que el tamaño de la lista, reseteamos la pregunta
                    when{
                        mCurrentPosition<=mQuestionList!!.size->{

                            setQuestion()

                        }else->{

                        /*Nos redirecciona a la activity de resultados,
                        recojemos las respuestas correctas y el total de preguntas
                       */
                        activity?.let{
                            val intent = Intent (it, Activity7_1_Juego7_Results::class.java)
                            intent.putExtra(Constantsjuego7.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constantsjuego7.TOTAL_QUESTIONS, mQuestionList!!.size)
                            it.startActivity(intent)
                        }
                    }

                    }
                }else{
                    /*Si ya ha clickado en la respuesta, cojemos la pregunta de la posicion en la que esta.
                      Si la respuesta correcta no es igual a la respuesta que ha elegido el usuario, sacamos mensaje de error.
                        sino, añadimos la respuesta correcta a la variable CorrectAnswers para despues mostrar los aciertos
                        al acabar el quiz. La respuesta correcta va fuera del if, ya que siempre saldrá independientemente de si
                        el usuario ha acertado o ha fallado*/
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
                    //Deshabilitamos las respuestas al clickar la respuesta
                    question1_answer1.isEnabled=false
                    question1_answer2.isEnabled=false
                    question1_answer3.isEnabled=false



                }

            }

        }

    }

    //Cambiamos de color de fondo a la opcion en concreto

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
            Fragment1_7_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}