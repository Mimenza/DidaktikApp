package com.example.didaktikapp.fragments.juegos

import com.example.didaktikapp.R
import `in`.codeshuffle.typewriterview.TypeWriterView
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.Fragment
import android.os.Handler
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import android.graphics.drawable.AnimationDrawable
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler
import com.example.didaktikapp.activities.Utils
import kotlinx.coroutines.runBlocking
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_1_juego : Fragment(), DbHandler.QueryResponseDone {
    private val thisJuegoId = 1

    private lateinit var globalView: View
    private lateinit var vistaAnimada: TranslateAnimation
    private lateinit var layout: ConstraintLayout
    private lateinit var customLine: CustomLine
    private lateinit var btnInfoJuego: ImageButton
    private val customLines = arrayListOf<CustomLine>()
    private var introFinished: Boolean = false
    private var doubleTabHandler: Handler? = null
    private var typeWriterHandler: Handler? = null
    private var exitAnimationHandler: Handler? = null
    private var talkAnimationHandler: Handler? = null
    private var fondoAnimationHandler: Handler? = null

    private var audio: MediaPlayer? = null

    private var progress: Int = 0
    private var pressedImg: Int = 0

    private var point1: Boolean = false
    private var point2: Boolean = false
    private var point3: Boolean = false

    private var border1: Boolean = false
    private var border2: Boolean = false
    private var border3: Boolean = false

    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var img3: ImageView

    private lateinit var txtv1: TextView
    private lateinit var txtv2: TextView
    private lateinit var txtv3: TextView
    private lateinit var  mapa: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_1_juego, container, false)
        globalView = view
        layout = view.findViewById(R.id.cl1_1juego)
        introFinished = false



        img1 = view.findViewById(R.id.imgv1_1imagen1)
        img2 = view.findViewById(R.id.imgv1_1imagen2)
        img3 = view.findViewById(R.id.imgv1_1imagen3)

        txtv1 = view.findViewById(R.id.txtv1_1azalpena1)
        txtv2 = view.findViewById(R.id.txtv1_1azalpena2)
        txtv3 = view.findViewById(R.id.txtv1_1azalpena3)
        btnInfoJuego= view.findViewById((R.id.btn1_1_infojuego))
        val button: Button = view.findViewById(R.id.btnf1_1siguienteJuego)
        val buttonAgain: Button = view.findViewById(R.id.btnf1_1repetirJuego)
        mapa = view.findViewById(R.id.btnf1_1_mapa)

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

        button.setOnClickListener {
            DbHandler.userActualizarUltimoPunto(thisJuegoId)
            DbHandler().requestDbUserUpdate(this)
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment2_1_minijuego)
        }

        buttonAgain.setOnClickListener() {
            DbHandler.userActualizarUltimoPunto(thisJuegoId)
            DbHandler().requestDbUserUpdate(this)
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_self)
        }

        println(audio?.isPlaying == false)

        //Typewriter juego 1 tutorial
        typeWriterHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler = Handler()
        typeWriterHandler?.postDelayed({
            typewriter(view)
            typeWriterHandler?.removeCallbacksAndMessages(null)
            typeWriterHandler = null
        }, 2000)


        //Typewriter juego 1 tutorial fin

        //Audio juego 1

        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego1audiotutorial)
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
        //Animacion para la descripcion
        starAnimationfun(view)

        img1.setOnClickListener {
            //Comprueba que no hay ningun audio reproduciendose
            if (audio?.isPlaying == false) {
                //Guarda en que imagen has clickado
                if (pressedImg != 1) {
                    pressedImg = 1
                } else {
                    pressedImg = 0
                }

                //Mira que no hayas conseguido el punto 1
                if (!point1) {
                    //Si no tiene el borde se lo pone y si no se lo quita
                    border1 = if (!border1) {
                        setBorder(img1, "black")
                        true
                    } else {
                        img1.setBackgroundResource(0)
                        false
                    }
                }

                //Quita los bordes a las imagenes 2 y 3 comprobando que no los haya conseguido
                if (!point2) {
                    img2.setBackgroundResource(0)
                    border2 = false
                }

                if (!point3) {
                    img3.setBackgroundResource(0)
                    border3 = false
                }
            }
        }

        img2.setOnClickListener {
            //Comprueba que no hay ningun audio reproduciendose
            if (audio?.isPlaying == false) {
                //Guarda en que imagen has clickado
                pressedImg = 2

                //Mira que no hayas conseguido el punto 2
                if (!point2) {
                    //Si no tiene el borde se lo pone y si no se lo quita
                    border2 = if (!border2) {
                        setBorder(img2, "black")
                        true
                    } else {
                        img2.setBackgroundResource(0)
                        false
                    }
                }

                //Quita los bordes a las imagenes 1 y 3 comprobando que no los haya conseguido
                if (!point1) {
                    img1.setBackgroundResource(0)
                    border1 = false
                }

                if (!point3) {
                    img3.setBackgroundResource(0)
                    border3 = false
                }
            }
        }

        img3.setOnClickListener {
            //Comprueba que no hay ningun audio reproduciendose
            if (audio?.isPlaying == false) {
                //Guarda en que imagen has clickado
                pressedImg = 3

                //Mira que no hayas conseguido el punto 3
                if (!point3) {
                    //Si no tiene el borde se lo pone y si no se lo quita
                    border3 = if (!border3) {
                        setBorder(img3, "black")
                        true
                    } else {
                        img3.setBackgroundResource(0)
                        false
                    }
                }

                //Quita los bordes a las imagenes 1 y 2 comprobando que no los haya conseguido
                if (!point1) {
                    img1.setBackgroundResource(0)
                    border1 = false
                }

                if (!point2) {
                    img2.setBackgroundResource(0)
                    border2 = false
                }
            }
        }

        txtv1.setOnClickListener {
            //Comprueba que no hay ningun audio reproduciendose
            if (audio?.isPlaying == false) {
                if (pressedImg == 2) {
                    if (!point2) {
                        progress++
                        point2 = true
                        setBorder(img2, "green")
                        setBorder(txtv1, "green")
                        setLine(img2, txtv1, "green")
                        checkProgress(view, progress)
                    }
                } else if (pressedImg != 0) {
                    when (pressedImg) {
                        1 -> {
                            setBorder(img1, "red")
                            setLine(img1, txtv1, "red")
                        }
                        2 -> {
                            setBorder(img2, "red")
                            setLine(img2, txtv1, "red")
                        }
                        3 -> {
                            setBorder(img3, "red")
                            setLine(img3, txtv1, "red")
                        }
                    }

                    Utils.vibrarTelefono(requireContext())
                    setBorder(txtv1, "red")

                    runBlocking {
                        launch {
                            audio = MediaPlayer.create(context, R.raw.gaizkiaudioa)
                            audio?.start()
                            audio?.setOnCompletionListener {
                                resetGame()
                            }
                        }
                    }
                }
            }
            pressedImg = 0
        }

        txtv2.setOnClickListener {
            //Comprueba que no hay ningun audio reproduciendose
            if (audio?.isPlaying == false) {
                if (pressedImg == 3) {
                    if (!point3) {
                        progress++
                        point3 = true
                        setBorder(img3, "green")
                        setBorder(txtv2, "green")
                        setLine(img3, txtv2, "green")
                        checkProgress(view, progress)
                    }
                } else if (pressedImg != 0) {
                    when (pressedImg) {
                        1 -> {
                            setBorder(img1, "red")
                            setLine(img1, txtv2, "red")
                        }
                        2 -> {
                            setBorder(img2, "red")
                            setLine(img2, txtv2, "red")
                        }
                        3 -> {
                            setBorder(img3, "red")
                            setLine(img3, txtv2, "red")
                        }
                    }

                    Utils.vibrarTelefono(requireContext())
                    setBorder(txtv2, "red")

                    runBlocking {
                        launch {
                            audio = MediaPlayer.create(context, R.raw.gaizkiaudioa)
                            audio?.start()
                            audio?.setOnCompletionListener {
                                resetGame()
                            }
                        }
                    }
                }
            }
            pressedImg = 0
        }

        txtv3.setOnClickListener {
            //Comprueba que no hay ningun audio reproduciendose
            if (audio?.isPlaying == false) {

                if (pressedImg == 1) {
                    if (!point1) {
                        progress++
                        point1 = true
                        setBorder(img1, "green")
                        setBorder(txtv3, "green")
                        setLine(img1, txtv3, "green")
                        checkProgress(view, progress)
                    }
                } else if (pressedImg != 0) {
                    when (pressedImg) {
                        1 -> {
                            setBorder(img1, "red")
                            setLine(img1, txtv3, "red")
                        }
                        2 -> {
                            setBorder(img2, "red")
                            setLine(img2, txtv3, "red")
                        }
                        3 -> {
                            setBorder(img3, "red")
                            setLine(img3, txtv3, "red")
                        }
                    }

                    Utils.vibrarTelefono(requireContext())
                    setBorder(txtv3, "red")

                    runBlocking {
                        launch {
                            audio = MediaPlayer.create(context, R.raw.gaizkiaudioa)
                            audio?.start()
                            audio?.setOnCompletionListener {
                                resetGame()
                            }
                        }
                    }
                }
            }
            pressedImg = 0
        }
        return view
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

    /**
     * Dibuja una linea entre una ImgView y un TextView
     * @param img la ImageView donde empieza la linea
     * @param txtv el TextView donde termina la linea
     * @param color el color de la linea
     */
    private fun setLine(img: ImageView?, txtv: TextView?, color: String) {
        val startX: Float = img!!.x + img.width
        val startY: Float = img.y + img.height / 2

        val endX: Float = txtv!!.x
        val endY: Float = txtv.y + txtv.height / 2

        when (color) {
            "green" ->
                customLine =
                    CustomLine(requireContext(), startX -7, startY -7, endX +7, endY +7, 15F, 255, 162, 224, 23
                    )
            "red" ->
                customLine =
                    CustomLine(requireContext(), startX -7, startY-7, endX+7, endY+7, 15F, 255, 224, 56, 23)
        }
        customLines.add(customLine)
        layout.addView(customLine)
    }

    /**
     * Hace que el texto del juego se escriba letra por letra
     * @param view la vista en la que se encuentra
     */
    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_1tutorialjuego1) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.jositajositext))
        typeWriterView.setDelay(65)
    }

    /**
     * Muestra la manzana de la animacion con una transicion
     * @param view la vista en la que se encuentra
     */

    private var upelioStatic: ImageView? = null
    private var upelioTalking: ImageView? = null

    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        upelioStatic = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelioStatic?.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler = Handler()
        talkAnimationHandler?.postDelayed({
            upelioStatic?.isVisible = false
            talkAnimationfun(view)
            talkAnimationHandler?.removeCallbacksAndMessages(null)
            talkAnimationHandler = null
        }, 2000)


    }

    /**
     * Esconde la manzana de la animacion con una transicion
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
            fondoAnimationHandler?.removeCallbacksAndMessages(null)
            fondoAnimationHandler = null
        }, 1000)

    }

    /**
     * Anima la manzana como que habla
     * @param view la vista en la que se encuentra
     */

    private fun talkAnimationfun(view: View) {
        upelioTalking = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelioTalking?.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelioTalking?.background as AnimationDrawable
        ani.start()
    }

     fun endIntroManually() {
        if (introFinished) {
            return
        }
        doubleTabHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler?.removeCallbacksAndMessages(null)
        exitAnimationHandler?.removeCallbacksAndMessages(null)
        introFinished = true
        val upelio1 = globalView.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio1.clearAnimation()
        upelio1.visibility = View.GONE
        val upelio2 = globalView.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelio2.clearAnimation()
        upelio2.visibility = View.GONE
        val txtAnimacion = globalView.findViewById(R.id.txtv1_1fondogris) as TextView
        txtAnimacion.clearAnimation()
        txtv1_1tutorialjuego1.isVisible = false
        txtAnimacion.isVisible = false
        audio?.stop()

         activateBtn()
    }

    private fun activateBtn() {
        mapa.setOnClickListener {
            if (audio?.isPlaying == false){
                activity?.let{
                    getActivity()?.finish()
                }
            }
        }

        btnInfoJuego.setOnClickListener(){
            showDialogInfo()
        }
    }

    /**
     * Coloca bordes de colores a una ImageView
     * @param img la ImageView a la que se le pone el borde
     * @param color el color del borde
     */
    private fun setBorder(img: ImageView, color: String) {
        var colorId = 0
        when (color) {
            "black" -> colorId = R.drawable.bg_border_selected
            "green" -> colorId = R.drawable.bg_border_green
            "red" -> colorId = R.drawable.bg_border_red
        }

        img.background = context?.let { it ->
            ContextCompat.getDrawable(it, colorId)
        }
    }

    /**
     * Coloca bordes de colores a un TextView
     * @param txtv el TextView al que se le pone el borde
     * @param color el color del borde
     */
    private fun setBorder(txtv: TextView, color: String) {
        var colorId = 0
        when (color) {
            "black" -> colorId = R.drawable.bg_border_selected
            "green" -> colorId = R.drawable.bg_border_green
            "red" -> colorId = R.drawable.bg_border_red
        }

        txtv.background = context?.let { it ->
            ContextCompat.getDrawable(it, colorId)
        }
    }

    /**
     * Resetea el juego vaciando las variables y borrando las lineas
     */
    private fun resetGame() {
        progress = 0
        pressedImg = 0

        img1.setBackgroundResource(0)
        img2.setBackgroundResource(0)
        img3.setBackgroundResource(0)

        txtv1.setBackgroundResource(0)
        txtv2.setBackgroundResource(0)
        txtv3.setBackgroundResource(0)

        point1 = false
        point2 = false
        point3 = false

        border1 = false
        border2 = false
        border3 = false

        for (i in 0 until customLines.size) {
            layout.removeView(customLines[i])
        }
    }

    /**
     * Comprueba el progreso del juego
     * @param view la vista en la que se encuentra
     * @param progress el progeso de la partida
     */
    private fun checkProgress(view: View, progress: Int) {
        if (progress >= 3) {
            DbHandler.userAumentarPuntuacion(10)
            runBlocking {
                launch {
                    audio = MediaPlayer.create(context, R.raw.juego1ongi)
                    audio?.start()
                    imgv1_1osoongi.isVisible=true
                    imgv1_1osoongi2.isVisible=true
                    imgv1_1osoongi3.isVisible=true
                    imgv1_1osoongi4.isVisible=true
                    audio?.setOnCompletionListener {
                        val btnNext: Button = view.findViewById(R.id.btnf1_1siguienteJuego)
                        val btnAgain: Button = view.findViewById(R.id.btnf1_1repetirJuego)
                        btnNext.isVisible = true
                        btnAgain.isVisible = true
                        imgv1_1osoongi.isVisible=false
                        imgv1_1osoongi2.isVisible=false
                        imgv1_1osoongi3.isVisible=false
                        imgv1_1osoongi4.isVisible=false

                    }
                }
            }
        }
    }

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

    override fun onPause() {
        audio?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        audio?.start()
    }

}