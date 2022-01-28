package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Dialog
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
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.didaktikapp.Model.Constantsjuego7
import com.example.didaktikapp.Model.Preguntasjuego7
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.Utils
import kotlinx.android.synthetic.main.fragment1_7_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class Fragment1_7_juego : Fragment(), View.OnClickListener {
    private lateinit var preguntasjuego7: Preguntasjuego7
    private lateinit var progressBar : ProgressBar
    private lateinit var txtProgressBar : TextView
    private lateinit var question1 : TextView
    private lateinit var question1_answer1 : TextView
    private lateinit var question1_answer2 : TextView
    private lateinit var question1_answer3 : TextView
    private lateinit var btnSiguiente : Button
    private lateinit var vistaanimada:TranslateAnimation
    private lateinit var defaultImage: ImageView
    private var mCurrentPosition:Int=1
    private var mQuestionList: ArrayList<Preguntasjuego7>?= null
    private var mSelectedOptionPosition: Int= 0
    private var mCorrectAnswers: Int = 0
    private var audio: MediaPlayer? = null
    private  var REQUEST_CODE =200
    private var introFinished: Boolean = false
    private lateinit var btnInfoJuego: ImageButton
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_7_juego, container, false)
        //Inicializar vistas
        val button:Button = view.findViewById(R.id.btnf1_7siguiente)


        progressBar = view.findViewById(R.id.custom_progressBar)
        txtProgressBar =view.findViewById(R.id.txtv1_7_progreessbar)
        question1= view.findViewById(R.id.txtv1_7_pregunta1)
        question1_answer1= view.findViewById(R.id.txtv1_7_respuesta1)
        question1_answer2= view.findViewById(R.id.txtv1_7_respuesta2)
        question1_answer3= view.findViewById(R.id.txtv1_7_respuesta3)
        btnSiguiente= view.findViewById(R.id.btnf1_7siguiente)
        btnInfoJuego= view.findViewById((R.id.btn1_7_infojuego))

        val mapa: ImageButton = view.findViewById(R.id.btnf1_7_mapa)
        mapa.setOnClickListener {
            if (audio?.isPlaying == false){
                activity?.let{
                    val intent = Intent (it, Activity5_Mapa::class.java)
                    it.startActivity(intent)
                }
            }
        }
        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_7_juego_to_fragment1_7_juego_results)
        }

        btnInfoJuego.setOnClickListener(){
            showDialogInfo()
        }

        audiotutorial(view)

        //Creamos la pregunta y las respuestas segun la posicion
        setQuestion()

        //Cuando hacer click en la respuesta de la pregunta, lanzamos mensaje de error o de acierto
        question1_answer1!!.setOnClickListener(this)
        question1_answer2!!.setOnClickListener(this)
        question1_answer3!!.setOnClickListener(this)

        btnSiguiente!!.setOnClickListener(this)

        introFinished = false
        return view
    }



    fun audiotutorial(view:View){
        //Audio juego 7 tutorial
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego7audiotutorial)
                audio?.start()
                audio?.setOnCompletionListener {

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

        //desabilitar boton ajustes mientras esta el tutorial
        val buttonSiguiente = view.findViewById(R.id.btnf1_7siguiente) as Button
        buttonSiguiente.setEnabled(false)
        question1_answer1.isEnabled=false
        question1_answer2.isEnabled=false
        question1_answer3.isEnabled=false


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
        if (!introFinished) {
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
                    val buttonSiguiente = view.findViewById(R.id.btnf1_7siguiente) as Button
                    buttonSiguiente.isEnabled=true
                    question1_answer1.isEnabled=true
                    question1_answer2.isEnabled=true
                    question1_answer3.isEnabled=true
                    introFinished = true
                }
            }, 1000)
        }
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
            option.background= ContextCompat.getDrawable(requireContext(), R.drawable.juego2_default_option_border_bg)
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

                        val sharedPreferences =requireContext().getSharedPreferences("scoreGame7", 0)
                        var editor = sharedPreferences.edit()
                        editor.putString("totalQuestions", mQuestionList!!.size.toString()).apply()
                        editor.putString("correctAnswers", mCorrectAnswers.toString()).apply()
                        /*Nos redirecciona al fragment de resultados*/
                        view?.findNavController()?.navigate(R.id.action_fragment1_7_juego_to_fragment1_7_juego_results)
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
                        Utils.vibrarTelefono(requireContext())
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
                    question1_answer1.isEnabled = false
                    question1_answer2.isEnabled = false
                    question1_answer3.isEnabled = false
                }
            }
        }
    }



    fun showDialogInfo(){

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info_dialog)
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        val textInfo = dialog.findViewById<View>(R.id.txtv_infominijuego) as TextView
        var texto =""
        //Recojemos datos de shared preferences
        val sharedPreferences = this.activity?.getSharedPreferences("site", 0)
        val numero = sharedPreferences?.getString("numero", null)?.toInt()
        println(numero)
        when(numero){

            0->  {texto=resources.getString(R.string.ayudajuego1)}
            1->  {texto=resources.getString(R.string.ayudajuego2)}
            2->  {texto=resources.getString(R.string.ayudajuego3)}
            3->  {texto=resources.getString(R.string.ayudajuego4)}
            4->  {texto=resources.getString(R.string.ayudajuego5)}
            5->  {texto=resources.getString(R.string.ayudajuego6)}

        }
        println(texto)
        if (textInfo!=null){

            textInfo.setText(texto)
        }
    }
    //Cambiamos de color de fondo a la opcion en concreto

    fun answerView(answer: Int, drawableView:Int){
        //Cambiamos el fondo a cada respuesta
        when(answer){

            1->{
                question1_answer1.background=ContextCompat.getDrawable(requireContext(), drawableView)
            }

            2->{
                question1_answer2.background=ContextCompat.getDrawable(requireContext(), drawableView)
            }

            3->{
                question1_answer3.background=ContextCompat.getDrawable(requireContext(), drawableView)
            }
        }
    }


    //COLOR Y FONDO RESPUESTA SELECCIONADA
    private  fun selectedOptionView(tv:TextView, selectedOptiomNum:Int){

        defaultOptionsView()
        mSelectedOptionPosition= selectedOptiomNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background= ContextCompat.getDrawable(requireContext(), R.drawable.juego2_selected_option_border_bg)
    }

    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        audio?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        audio?.start()
    }
}