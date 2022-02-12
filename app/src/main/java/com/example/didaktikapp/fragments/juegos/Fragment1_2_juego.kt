
package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
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
import androidx.navigation.findNavController
import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.Model.Preguntasjuego2
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.Utils
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.android.synthetic.main.fragment1_2_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class Fragment1_2_juego : Fragment(), View.OnClickListener {
    private lateinit var globalView: View//vista del fragment
    private lateinit var preguntasjuego2: Preguntasjuego2 //data class preguntas2
    private var mSelectedOptionPosition: Int = 0//posicion de la opción de la respuesta seleccionada
    private lateinit var progressBar: ProgressBar//progress bar de las preguntas
    private lateinit var txtProgressBar: TextView// Texto de posición del progress bar
    private lateinit var question1: TextView//La pregunta
    private lateinit var question1_answer1: TextView //Respuesta 1
    private lateinit var question1_answer2: TextView //Respuesta 2
    private lateinit var question1_answer3: TextView //Respuesta 3
    private lateinit var question1_answer4: TextView //Respuesta 4
    private lateinit var defaultImage: ImageView //Imagen por defecto de las preguntas
    private lateinit var upelio1: ImageView //upelio 1 para la animacion de la vista
    private lateinit var upelio2: ImageView //upelio 2 animacion grafica para que hable
    private lateinit var fondoTutorial: TextView //fondo negro para el tutorial
    private lateinit var txtTutorial: TextView //texto en el tutuorial
    private lateinit var btnSiguiente: Button //boton siguiente preguntas
    private var mCurrentPosition: Int = 1 //Posición inicial del usuario
    private var mQuestionList: ArrayList<Preguntasjuego2>? = null//Arraylist vacío de las pregunta 2
    private lateinit var vistaanimada: TranslateAnimation// Animación vista para la manzana
    private var mCorrectAnswers: Int = 0//respuestas correctas
    private var audio: MediaPlayer? = null
    private var REQUEST_CODE= 200 //request success foto
    private var introFinished: Boolean = false//Booleano para que la intro solo se muestre una vez el tutorial

    //variables para saltar el tutorial al hacer doble click en la pantalla
    private var doubleTabHandler: Handler? = null
    private var typeWriterHandler: Handler? = null
    private var exitAnimationHandler: Handler? = null
    private var talkAnimationHandler: Handler? = null
    private var fondoAnimationHandler: Handler? = null

    private lateinit var btnInfoJuego: ImageButton//icono de la información del juego
    private lateinit var mapa: ImageButton//icono del mapa

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_2_juego, container, false)
        globalView = view
        //Inicializar vistas
        progressBar = view.findViewById(R.id.custom_progressBar)
        txtProgressBar = view.findViewById(R.id.txtv1_2_progreessbar)
        question1 = view.findViewById(R.id.txtv1_2_pregunta1)
        question1_answer1 = view.findViewById(R.id.txtv1_2_respuesta1)
        question1_answer2 = view.findViewById(R.id.txtv1_2_respuesta2)
        question1_answer3 = view.findViewById(R.id.txtv1_2_respuesta3)
        question1_answer4 = view.findViewById(R.id.txtv1_2_respuesta4)
        btnSiguiente = view.findViewById(R.id.btnf1_2siguiente)
        defaultImage = view.findViewById(R.id.imgv1_2defaultimage)
        upelio1= view.findViewById(R.id.imgv1_2_upelio)
        upelio2= view.findViewById(R.id.imgv1_2_upelio2)
        txtTutorial= view.findViewById(R.id.txtv1_2tutorialjuego2)
        fondoTutorial= view.findViewById(R.id.txtv1_2fondogris)
        btnInfoJuego= view.findViewById((R.id.btn1_2_infojuego))
        mapa = view.findViewById(R.id.btnf1_2_mapa)

        val introFondo: TextView = view.findViewById(R.id.txtv1_2fondogris)
        introFondo.setOnClickListener() {
            if (null == doubleTabHandler) {
                doubleTabHandler = Handler()
                doubleTabHandler?.postDelayed({
                    doubleTabHandler?.removeCallbacksAndMessages(null)
                    doubleTabHandler = null
                }, 200)
            } else {
                endIntroManually()
            }
        }

        //Typewriter juego 2 tutorial
        typeWriterHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler = Handler()
        typeWriterHandler?.postDelayed({
            typewriter(view)
            typeWriterHandler?.removeCallbacksAndMessages(null)
            typeWriterHandler = null
        }, 2000)

        //Audio juego 2 tutorial
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego2audiotutorial)
                audio?.start()
                audio?.setOnCompletionListener {
                    exitAnimationHandler?.removeCallbacksAndMessages(null)
                    exitAnimationHandler = Handler()
                    exitAnimationHandler?.postDelayed({
                        exitAnimationfun(view)
                        activateBtn()
                        exitAnimationHandler?.removeCallbacksAndMessages(null)
                        exitAnimationHandler = null
                    }, 1000)
                }
            }
        }
        //animacion para la descripcion
        starAnimationfun(view)

        //Creamos la pregunta y las respuestas segun la posicion
        setQuestion()

        //Cuando hacer click en la respuesta de la pregunta, lanzamos mensaje de error o de acierto
        question1_answer1!!.setOnClickListener(this)
        question1_answer2!!.setOnClickListener(this)
        question1_answer3!!.setOnClickListener(this)
        question1_answer4!!.setOnClickListener(this)
        btnSiguiente!!.setOnClickListener(this)

        return view
    }

    /**
     * Carga y muestra el diálogo con la ayuda del minijuego
     *
     */
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

    /**
     * Crea la animación para el typewritter
     *
     * @param view
     */
    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_2tutorialjuego2) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.ezetzigarrijolasa))
        typeWriterView.setDelay(70)
    }

    /**
     * Función que usa la animación de la vista a la manzana, de la izquierda a la derecha
     * con el fondo en negro, y al acabar el audio,
     * desaparece la manzana y el fondo
     * @param view
     */
    private fun starAnimationfun(view: View) {
        // animacion fondo gris
        val txt_animacion = view.findViewById(R.id.txtv1_2fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txt_animacion.startAnimation(aniFade)


        val buttonSiguiente = view.findViewById(R.id.btnf1_2siguiente) as Button
        buttonSiguiente.setEnabled(false)

        question1_answer1.isEnabled = false
        question1_answer2.isEnabled = false
        question1_answer3.isEnabled = false
        question1_answer4.isEnabled = false

        //animacion entrada upelio
        vistaanimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaanimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_2_upelio) as ImageView
        upelio.startAnimation(vistaanimada)

        //llamamos a la animacion para animar a upelio
        val upelioStatic = view.findViewById(R.id.imgv1_2_upelio2) as ImageView
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler = Handler()
        talkAnimationHandler?.postDelayed({
            upelioStatic.isVisible = false
            talkAnimationfun(view)
            talkAnimationHandler?.removeCallbacksAndMessages(null)
            talkAnimationHandler = null
        }, 2000)
    }

    /**
     * Función que crea la animacion gráfica de la manzana
     *
     * @param view
     */
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_2_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.getBackground() as AnimationDrawable
        ani.start()
    }

    /**
     *  Función que usa la animación de la vista a la manzana
     * Cuando acaba la manzana de hablar upelio hace una vista animada a
     * la derecha y desaperece
     * @param view

     */
    private fun exitAnimationfun(view: View) {
        //si la intro se ha acabado
        if (!introFinished) {
            //escondemos la manzanda de la animacion
            val upelioanimado = view.findViewById(R.id.imgv1_2_upelio2) as ImageView
            upelioanimado.isVisible = false

            //animacion salido upelio
            vistaanimada = TranslateAnimation(0f, 1000f, 0f, 0f)
            vistaanimada.duration = 2000

            //vistaanimada.fillAfter = true
            val upelio = view.findViewById(R.id.imgv1_2_upelio) as ImageView
            upelio.startAnimation(vistaanimada)

            //animacion fondo gris
            fondoAnimationHandler?.removeCallbacksAndMessages(null)
            fondoAnimationHandler = Handler()
            fondoAnimationHandler?.postDelayed({
                introFinished = true
                val txt_animacion = view.findViewById(R.id.txtv1_2fondogris) as TextView
                val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                txt_animacion.startAnimation(aniFade)
                txtv1_2tutorialjuego2.startAnimation(aniFade)
                txtv1_2tutorialjuego2.isVisible = false
                txt_animacion.isVisible = false
                upelio.isVisible = false
                //Habilitar botones cuando desaparece la animacion

                val buttonSiguiente = view.findViewById(R.id.btnf1_2siguiente) as Button
                buttonSiguiente.isEnabled = true
                question1_answer1.isEnabled = true
                question1_answer2.isEnabled = true
                question1_answer3.isEnabled = true
                question1_answer4.isEnabled = true
            }, 1000)
        }
    }

    /**
     * Función que settea las preguntas, es decir, al dar al botón de siguiente
     * pregunta, según la posición en la que estés, saldrán nuevas preguntas y respuetas
     * para esa posición
     *
     */
    private fun setQuestion() {

        preguntasjuego2 = Preguntasjuego2()

        //Recojemos las preguntas del Constants juego2
        mQuestionList = Constantsjuego2.getQuestions()

        //Posición de la pregunta en el progress bar (0)
        val question: Preguntasjuego2 = mQuestionList!![mCurrentPosition - 1]
        defaultOptionsView()

        if (mCurrentPosition == mQuestionList!!.size) {
            btnSiguiente.text = "Amaitu"
        } else {
            btnSiguiente.text = "Bidali"
        }
        progressBar!!.progress = mCurrentPosition
        txtProgressBar!!.text = "$mCurrentPosition" + "/" + progressBar.max
        question1!!.text = question!!.question
        question1_answer1!!.text = question!!.optionOne
        question1_answer2!!.text = question!!.optionTwo
        question1_answer3!!.text = question!!.optionThree
        question1_answer4!!.text = question!!.optionFour
        defaultImage.setImageResource(R.drawable.img_interrogacion)
    }

    /**
     * COLOR Y FONDO POR DEFECTO DE LAS RESPUESTAS
     *
     */
    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        options.add(0, question1_answer1)
        options.add(1, question1_answer2)
        options.add(2, question1_answer3)
        options.add(3, question1_answer4)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(requireContext(), R.drawable.juego2_default_option_border_bg)
        }
    }

    /**
     *
     * Override de los botones de respuesta, según que respuesta ha clickado el usuario,
     * se mostrará la opción seleccionada con el borde en negro. Al clickar en siguiente,
     * saldrá la respuesta correcta y la incorrecta en caso de haberla fallarlo. Si la posición
     * del usuario es igual que la lista de preguntas, al dar a sigueinte, nos llevará a la pestaña
     * de resultados, si no es igual, setteara las preguntas y respuestas llamando a la función
     * setQuestion()
     *
     * @param v
     */
    override fun onClick(v: View?) {
        //Según el botón que hayamos clickado, pasamos la id a la función selectedOptionView
        when (v?.id) {
            R.id.txtv1_2_respuesta1 -> {
                selectedOptionView(txtv1_2_respuesta1, 1)
            }

            R.id.txtv1_2_respuesta2 -> {
                selectedOptionView(txtv1_2_respuesta2, 2)
            }

            R.id.txtv1_2_respuesta3 -> {
                selectedOptionView(txtv1_2_respuesta3, 3)
            }

            R.id.txtv1_2_respuesta4 -> {
                selectedOptionView(txtv1_2_respuesta4, 4)
            }

            R.id.btnf1_2siguiente -> {
                //El usuario no ha elegido la opcion, cuando elija nos movemos de posicion
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    //Habilitamos las respuestas al clickar la respuesta
                    question1_answer1.isEnabled = true
                    question1_answer2.isEnabled = true
                    question1_answer3.isEnabled = true
                    question1_answer4.isEnabled = true
                    //Si mi posicion es menor o igual que el tamaño de la lista, reseteamos la pregunta
                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val sharedPreferences =requireContext().getSharedPreferences("scoreGame2", 0)
                            var editor = sharedPreferences.edit()
                            editor.putString("totalQuestions", mQuestionList!!.size.toString()).apply()
                            editor.putString("correctAnswers", mCorrectAnswers.toString()).apply()
                            /*Nos redirecciona al fragment de resultados*/
                            view?.findNavController()?.navigate(R.id.action_fragment1_2_juego_to_fragment1_2_juego_results)
                        }
                    }
                } else {
                    /*Si ya ha clickado en la respuesta, cojemos la pregunta de la posicion en la que esta.
                      Si la respuesta correcta no es igual a la respuesta que ha elegido el usuario, sacamos mensaje de error.
                        sino, añadimos la respuesta correcta a la variable CorrectAnswers para despues mostrar los aciertos
                        al acabar el quiz. La respuesta correcta va fuera del if, ya que siempre saldrá independientemente de si
                        el usuario ha acertado o ha fallado*/
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        Utils.vibrarTelefono(requireContext())
                        answerView(
                            mSelectedOptionPosition,
                            R.drawable.juego2_error_option_border_bg  )

                    } else {
                        upelio1.isVisible=false
                        upelio2.isVisible=false
                        takePicture()

                        mCorrectAnswers++
                    }
                    question.correctAnswer?.let {
                        answerView(
                            it,
                            R.drawable.juego2_correct_option_border_bg
                        )
                    }
                    if (mCurrentPosition == mQuestionList!!.size) {
                        btnSiguiente.text = "Amaitu jokoa"
                    } else {
                        btnSiguiente.text = "Joan hurrengo galderara"
                    }

                    mSelectedOptionPosition = 0
                    //Deshabilitamos las respuestas al clickar la respuesta
                    question1_answer1.isEnabled = false
                    question1_answer2.isEnabled = false
                    question1_answer3.isEnabled = false
                    question1_answer4.isEnabled = false
                }
            }
        }
    }

    /**
     * Función para sacar foto
     *
     */
    fun takePicture() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }


    /**
     * Función para guardar foto
     *
     * @param requestCode si ha dado error o un success 200
     * @param resultCode
     * @param data si el usuario ha sacado la foto o no
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            imgv1_2defaultimage.setImageBitmap(data.extras?.get("data") as Bitmap)
            audio?.stop()
            return

        }
    }


    /**
     * Cambiamos de color de fondo a la opción en concreto
     *
     * @param answer la posición de la respuesta correcta
     * @param drawableView pone de el fondo de color verde la respuesta correcta
     */
    fun answerView(answer: Int, drawableView: Int) {
        //Cambiamos el fondo a cada respuesta
        when (answer) {

            1 -> {
                question1_answer1.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }

            2 -> {
                question1_answer2.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }

            3 -> {
                question1_answer3.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }

            4 -> {
                question1_answer4.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }
        }
    }


    /**
     * COLOR Y FONDO RESPUESTA SELECCIONADA
     *
     * @param tv textview de las respuesta que el usuario ha clickado
     * @param selectedOptiomNum posición del textview de la respuesta que el usuario ha clickado
     */
    private fun selectedOptionView(tv: TextView, selectedOptiomNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptiomNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.juego2_selected_option_border_bg)
    }

    /**
     * Función de los botones info juego e ir al mapa
     * el botón de ir al mapa se activa cuando el audio no está en play
     *
     */
    private fun activateBtn() {
        mapa.setOnClickListener {
            if (audio?.isPlaying == false){
                activity?.let{
                    activity?.finish()
                }
            }
        }

        btnInfoJuego.setOnClickListener(){
            showDialogInfo()
        }
    }

    /**
     * Función para terminar la intro manualmente
     *
     */
    fun endIntroManually() {
        if (introFinished) {
            return
        }
        doubleTabHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler?.removeCallbacksAndMessages(null)
        exitAnimationHandler?.removeCallbacksAndMessages(null)
        introFinished = true
        val upelio1 = globalView.findViewById(R.id.imgv1_2_upelio) as ImageView
        upelio1.clearAnimation()
        upelio1.visibility = View.GONE
        val upelio2 = globalView.findViewById(R.id.imgv1_2_upelio2) as ImageView
        upelio2.clearAnimation()
        upelio2.visibility = View.GONE
        val txtAnimacion = globalView.findViewById(R.id.txtv1_2fondogris) as TextView
        txtAnimacion.clearAnimation()
        val typeWriterElement = globalView.findViewById(R.id.txtv1_2tutorialjuego2) as TextView
        typeWriterElement.isVisible = false
        txtAnimacion.isVisible = false
        audio?.stop()
        val buttonSiguiente = globalView.findViewById(R.id.btnf1_2siguiente) as Button
        buttonSiguiente.isEnabled = true
        question1_answer1.isEnabled = true
        question1_answer2.isEnabled = true
        question1_answer3.isEnabled = true
        question1_answer4.isEnabled = true

        activateBtn()
    }

    /**
     * AUDIO EVENTS FIX ON DESTROYING
     *
     */
    override fun onDestroy() {
        audio?.stop()
        doubleTabHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler?.removeCallbacksAndMessages(null)
        exitAnimationHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        fondoAnimationHandler?.removeCallbacksAndMessages(null)
        doubleTabHandler = null
        typeWriterHandler = null
        exitAnimationHandler = null
        talkAnimationHandler = null
        fondoAnimationHandler = null
        super.onDestroy()
    }

    /**
     * AUDIO EVENTS ON PAUSE
     *
     */
    override fun onPause() {
        audio?.pause()
        super.onPause()
    }

    /**
     * AUDIO EVENTS ON RESUME
     *
     */
    override fun onResume() {
        super.onResume()
        audio?.start()
    }
}

