package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.activities.DbHandler
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Fragment1_3_juego : Fragment(), DbHandler.QueryResponseDone {
    private val thisJuegoId = 3

    private lateinit var vistaAnimada: TranslateAnimation
    private lateinit var globalView: View
    private lateinit var button: Button
    private lateinit var buttonSaltarJuego: Button
    private lateinit var buttonRepetir: Button
    private lateinit var btnInfoJuego: ImageButton
    private lateinit var preguntasLayout: LinearLayout
    private lateinit var  mapa: ImageButton
    private var audio: MediaPlayer? = null
    private var totalWidth: Int = 0
    private var totalHeight: Int = 0
    private var puzzleShowing: Boolean = false
    private var introFinished: Boolean = false
    private var doubleTabHandler: Handler? = null
    private var typeWriterHandler: Handler? = null
    private var exitAnimationHandler: Handler? = null
    private var talkAnimationHandler: Handler? = null
    private var fondoAnimationHandler: Handler? = null
    private var respuestaPreguntasjuego2: String = "zanpantzarrak"
    private var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    private val listaImagenes = listOf(
        listOf(R.id.puzzle_pieza_o_1, R.id.puzzle_pieza_d_1),
        listOf(R.id.puzzle_pieza_o_2, R.id.puzzle_pieza_d_2),
        listOf(R.id.puzzle_pieza_o_3, R.id.puzzle_pieza_d_3),
        listOf(R.id.puzzle_pieza_o_4, R.id.puzzle_pieza_d_4),
        listOf(R.id.puzzle_pieza_o_5, R.id.puzzle_pieza_d_5),
        listOf(R.id.puzzle_pieza_o_6, R.id.puzzle_pieza_d_6),
        listOf(R.id.puzzle_pieza_o_7, R.id.puzzle_pieza_d_7),
        listOf(R.id.puzzle_pieza_o_8, R.id.puzzle_pieza_d_8),
        listOf(R.id.puzzle_pieza_o_9, R.id.puzzle_pieza_d_9),
        listOf(R.id.puzzle_pieza_o_10, R.id.puzzle_pieza_d_10),
        listOf(R.id.puzzle_pieza_o_11, R.id.puzzle_pieza_d_11),
        listOf(R.id.puzzle_pieza_o_12, R.id.puzzle_pieza_d_12),
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_3_juego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf1_3_siguiente)
        buttonSaltarJuego = view.findViewById(R.id.btn_saltarjuego3)
        buttonRepetir = view.findViewById(R.id.btnf1_3_repetir)
        button.visibility = View.GONE
        buttonRepetir.visibility = View.GONE
        introFinished = false

        val btnComprobarRespuesta: Button = globalView.findViewById(R.id.juego3_btnComprobar)
        preguntasLayout = view.findViewById(R.id.juego3_preguntas_layout)
        btnInfoJuego= view.findViewById((R.id.btn1_3_infojuego))
        preguntasLayout.visibility = View.GONE

        mapa= view.findViewById(R.id.btnf1_3_mapa)

        btnComprobarRespuesta.setOnClickListener() {
            comprobarRespuestas()
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                totalWidth = view.width
                totalHeight = view.height

            }
        })

        button.setOnClickListener() {
            if (puzzleShowing) {
                DbHandler.userActualizarUltimoPunto(thisJuegoId)
                DbHandler().requestDbUserUpdate(this)
            } else {
                iniciarPreguntas()
            }
        }

        buttonSaltarJuego.setOnClickListener() {
            Navigation.findNavController(globalView)
                .navigate(R.id.action_fragment1_3_juego_to_fragment2_3_minijuego)
        }

        buttonRepetir.setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_fragment1_3_juego_self)
        }


        //Animacion manzana al iniciar el juego
        starAnimationfun(view)

        val introFondo: TextView = view.findViewById(R.id.txtv1_1fondogris)
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

        //Typewriter juego 1 tutorial
        typeWriterHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler = Handler()
        typeWriterHandler?.postDelayed({
            typewriter(view)
            typeWriterHandler?.removeCallbacksAndMessages(null)
            typeWriterHandler = null
        }, 2000)

        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego3audiotutorial)
                audio?.start()

                audio?.setOnCompletionListener {
                    prepairPuzzleElements()
                    showPhotos()
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


        return view

    }

    /**
     * Recogemos del shared preferences en que minijuego estamos y depende de cual sea muestra una
     * info de ayuda u otra
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
     * Hace que las imagenes del puzzle se vean
     */
    private fun showPhotos() {
        //recogemos las fotos
        val foto1: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_1)
        val foto2: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_2)
        val foto3: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_3)
        val foto4: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_4)
        val foto5: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_5)
        val foto6: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_6)
        val foto7: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_7)
        val foto8: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_8)
        val foto9: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_9)
        val foto10: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_10)
        val foto11: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_11)
        val foto12: ImageView = globalView.findViewById(R.id.puzzle_pieza_o_12)

        //enseñamos las fotos
        foto1.isVisible = true
        foto2.isVisible = true
        foto3.isVisible = true
        foto4.isVisible = true
        foto5.isVisible = true
        foto6.isVisible = true
        foto7.isVisible = true
        foto8.isVisible = true
        foto9.isVisible = true
        foto10.isVisible = true
        foto11.isVisible = true
        foto12.isVisible = true

    }

    /**
     * Una vez que le llega la respuesta de la DB pasa al minijuego
     */
    override fun responseDbUserUpdated(responde: Boolean) {
        Navigation.findNavController(globalView)
            .navigate(R.id.action_fragment1_3_juego_to_fragment2_3_minijuego)
    }

    /**
     * Prepara los elementos del puzzle
     */
    private fun prepairPuzzleElements() {
        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = globalView.findViewById(vItemList[0])
            var vItemDestino: ImageView = globalView.findViewById(vItemList[1])
            vItemOrigen.getLayoutParams().width = vItemDestino.width
            vItemOrigen.getLayoutParams().height = vItemDestino.height
            vItemOrigen.x = ((0..totalWidth - vItemDestino.width).random()).toFloat()
            vItemOrigen.y = ((0..totalHeight - vItemDestino.height).random()).toFloat()
            manzanaList!!.add(DragnDropImage(vItemOrigen, vItemDestino))
            //vItemDestino.setColorFilter(Color.argb(150, 0, 80, 200))
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(matrix)
            vItemDestino.setColorFilter(filter)
            vItemDestino.setAlpha(70)
            vItemOrigen.setOnTouchListener(listener)
        }
    }

    /**
     * Añade la opcion de arrastar la imagenes
     */
    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)
        if (itemInList != null) {
            if (!itemInList.acertado) {
                viewElement.bringToFront()
                val action = motionEvent.action
                when (action) {
                    MotionEvent.ACTION_MOVE -> {
                        viewElement.x = motionEvent.rawX - viewElement.width / 2
                        viewElement.y = motionEvent.rawY - viewElement.height / 2
                    }
                    MotionEvent.ACTION_UP -> {
                        viewElement.x = motionEvent.rawX - viewElement.width / 2
                        viewElement.y = motionEvent.rawY - viewElement.height / 2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        val location = IntArray(2)
                        objetivoEncontrado.getLocationOnScreen(location);
                        var posX = location[0]
                        var posY = location[1]
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height
                        if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {
                            viewElement.x = posX.toFloat()
                            viewElement.y = posY.toFloat()
                            itemInList.acertado = true
                            sendToTopImagesNotFinished()
                            viewElement.setOnTouchListener(null)
                            if (puzzleCompletado()) {
                                DbHandler.userAumentarPuntuacion(10)
                                audio?.stop()
                                audio = MediaPlayer.create(context, R.raw.ongiaudiogeneral)
                                audio?.start()
                                audio?.setOnCompletionListener {
                                    button.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }
        true
    }

    /**
     * Hace que el texto del juego se escriba letra por letra
     *
     * @param view la vista en la que se encuentra
     */
    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_1tutorialjuego1) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.juego3audiotutorialtxt))
        typeWriterView.setDelay(65)
    }

    /**
     * Muestra la manzana de la animacion con una transicion
     *
     * @param view la vista en la que se encuentra
     */
    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        val upelioStatic = view.findViewById(R.id.imgv1_1_upelio) as ImageView
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
     * Esconde la manzana de la animacion con una transicion
     *
     * @param view la vista en la que se encuentra
     */
    private fun exitAnimationfun(view: View) {
        if (introFinished) {
            return
        }
        val upelioAnimado = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //Animacion upelio salido
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //VistaAnimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //Animacion fondo gris
        fondoAnimationHandler?.removeCallbacksAndMessages(null)
        fondoAnimationHandler = Handler()
        fondoAnimationHandler?.postDelayed({
            introFinished = true
            val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            txtAnimacion.startAnimation(aniFade)
            txtv1_1tutorialjuego1.startAnimation(aniFade)
            txtv1_1tutorialjuego1.isVisible = false
            txtAnimacion.isVisible = false
            introFinished = true
        }, 1000)
    }

    /**
     * Anima la manzana como que habla
     *
     * @param view la vista en la que se encuentra
     */
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
    }

    /**
     * Inicia las preguntas que salen despues del puzzle
     */
    private fun iniciarPreguntas() {
        button.visibility = View.GONE
        puzzleShowing = true
        ocultarPuzzle()
        preguntasLayout.visibility = View.VISIBLE
    }

    /**
     * Comprueba las respuestas de despues del puzzle
     */
    private fun comprobarRespuestas() {
        val radio1: RadioButton = globalView.findViewById(R.id.pregunta1_respuesta_1)
        val radio2: RadioButton = globalView.findViewById(R.id.pregunta1_respuesta_2)
        val editTextRespuesta: EditText = globalView.findViewById(R.id.juego3_pregunta2_respuesta1)
        val btnComprobarRespuesta: Button = globalView.findViewById(R.id.juego3_btnComprobar)

        if (radio1.isChecked && editTextRespuesta.text.toString().trim().toLowerCase()
                .equals(respuestaPreguntasjuego2)
        ) {
            editTextRespuesta.isEnabled = false
            radio1.isEnabled = false
            radio2.isEnabled = false
            btnComprobarRespuesta.visibility = View.GONE
            button.visibility = View.VISIBLE
            buttonRepetir.visibility = View.VISIBLE
            audio?.stop()
            audio = MediaPlayer.create(context, R.raw.ongiaudiogeneral)
            audio?.start()
            hideKeyboard()
        } else {
            audio?.stop()
            audio = MediaPlayer.create(context, R.raw.gaizkiaudioa)
            audio?.start()
        }
    }

    /**
     * Esconde el teclado
     */
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    /**
     * Esconde el teclado
     */
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    /**
     * Esconde el teclado
     */
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Oculta el puzzle
     */
    private fun ocultarPuzzle() {
        for (item in manzanaList!!) {
            item.origen.visibility = View.GONE
            item.objetivo.visibility = View.GONE
        }
    }

    /**
     * Envia las imagenes al primer plano
     */
    private fun sendToTopImagesNotFinished() {
        for (item in manzanaList!!) {
            if (!item.acertado) {
                item.origen.bringToFront()
            }
        }
    }

    /**
     * Comprueba si el puzzle se ha completado
     *
     * @return el estado del puzzle
     */
    private fun puzzleCompletado(): Boolean {
        var finalizado: Boolean = true
        for (item in manzanaList!!) {
            if (!item.acertado) {
                finalizado = false
                break
            }
        }
        return finalizado
    }

    /**
     * Busca la view que le pasamos en el array manzanasList
     *
     * @param view la vista que tiene que buscar
     * @return la vista que se encuentra el el array manzanasList
     */
    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    /**
     * Añade funcionalidades al los botones (mapa/info)
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
     */
    fun endIntroManually() {
        if (introFinished) {
            return
        }
        doubleTabHandler?.removeCallbacksAndMessages(null)
        introFinished = true
        val upelio1 = globalView.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio1.clearAnimation()
        upelio1.visibility = View.GONE
        val upelio2 = globalView.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelio2.clearAnimation()
        upelio2.visibility = View.GONE
        val txtAnimacion = globalView.findViewById(R.id.txtv1_1fondogris) as TextView
        txtAnimacion.clearAnimation()
        val typeWriterElement = globalView.findViewById(R.id.txtv1_1tutorialjuego1) as TextView
        typeWriterElement.isVisible = false
        txtAnimacion.isVisible = false
        audio?.stop()
        prepairPuzzleElements()
        showPhotos()
        activateBtn()
    }

    /**
     * Al destuir el fragment termina con todos los elementos que puedan dar errores
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
     * Al pausar el fragment pausa el audio para que no de error
     */
    override fun onPause() {
        audio?.pause()
        super.onPause()
    }

    /**
     * Al reanudar el fragment resume el audio
     */
    override fun onResume() {
        super.onResume()
        audio?.start()
    }
}