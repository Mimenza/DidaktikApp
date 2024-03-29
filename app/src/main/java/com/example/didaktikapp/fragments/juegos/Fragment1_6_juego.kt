package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.DbHandler
import kotlinx.android.synthetic.main.fragment1_6_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Fragment1_6_juego : Fragment(), DbHandler.QueryResponseDone {
    private val thisJuegoId = 6
    private var param1: String? = null
    private var param2: String? = null
    private var sonido = false
    private var audio: MediaPlayer? = null
    private var firstTime: Boolean = true
    private lateinit var globalView: View
    private lateinit var btnInfoJuego: ImageButton
    private lateinit var playPauseButton: ImageView
    private lateinit var backwardButton: ImageView
    private lateinit var forwardButton: ImageView
    private var audioState: Boolean = false
    private var testAudioTemp: MediaPlayer? = null
    private lateinit var  mapa: ImageButton
    private var introFinished: Boolean = false
    private var doubleTabHandler: Handler? = null
    private var activarBtnsHanler: Handler? = null
    private var recargarJuegoHandler: Handler? = null
    private var bertsoHandler: Handler? = null
    private var talkAnimationHandler: Handler? = null
    private var fondoAnimationHandler: Handler? = null
    private lateinit var vistaAnimada: TranslateAnimation

    var respuestas = listOf(
        "sagardoaren",
        "guztia",
        "bizia",
        "kupelan",
        "prezioa",
        "estimazioa")

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_6_juego, container, false)
        globalView = view
        introFinished = false
        val btnsiguiente: Button = view.findViewById(R.id.btnf1_6siguienteJuego)
        val btnsaltar: Button = view.findViewById(R.id.btnf1_6saltarjuego)
        val btnrepertir : Button = view.findViewById(R.id.btnf1_6repetirJuego)

        //val buttonSonido: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
        btnInfoJuego= view.findViewById((R.id.btn1_6_infojuego))
        btnsaltar.setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_fragment1_6_juego_to_fragment2_6_minijuego)
        }

        playPauseButton = view.findViewById(R.id.juego6playpause)
        backwardButton = view.findViewById(R.id.juego6backward)
        forwardButton = view.findViewById(R.id.juego6forward)

        playPauseButton.setOnClickListener() {
            if (testAudioTemp == null) {
                testAudioTemp = MediaPlayer.create(context, R.raw.bertsoa)
                testAudioTemp?.setVolume(0.5F, 0.5F)
                testAudioTemp?.start()
                audioState = true
                playPauseButton.setImageResource(R.drawable.pausebutton)
                testAudioTemp?.setOnCompletionListener {
                    audioState = false
                    testAudioTemp = null
                    playPauseButton.setImageResource(R.drawable.playbutton)
                }
            } else {
                if (!audioState) {
                    testAudioTemp?.start()
                    audioState = true
                    playPauseButton.setImageResource(R.drawable.pausebutton)
                } else {
                    audioState = false
                    playPauseButton.setImageResource(R.drawable.playbutton)
                    testAudioTemp?.pause()
                    testAudioTemp?.currentPosition
                    testAudioTemp?.duration
                }
            }

        }

        var forbackwardTime: Int = 5000 // 5 segundos

        forwardButton.setOnClickListener() {
            if (testAudioTemp != null) {
                var bertsoAudioDuration: Int? = testAudioTemp?.duration
                var currentAudioPosition: Int? = testAudioTemp?.currentPosition
                var newPosition: Int? = (currentAudioPosition!! + forbackwardTime)
                if (newPosition!! < bertsoAudioDuration!!) {
                    testAudioTemp?.pause()
                    testAudioTemp?.seekTo(newPosition)
                    testAudioTemp?.start()
                }
            }
        }

        backwardButton.setOnClickListener() {
            if (testAudioTemp != null) {
                var bertsoAudioDuration: Int? = testAudioTemp?.duration
                var currentAudioPosition: Int? = testAudioTemp?.currentPosition
                var newPosition: Int? = (currentAudioPosition!! - forbackwardTime)
                testAudioTemp?.pause()
                if (newPosition!! > 0) {
                    testAudioTemp?.seekTo(newPosition)
                } else {
                    testAudioTemp?.seekTo(0)
                }
                testAudioTemp?.start()
            }
        }
        mapa = view.findViewById(R.id.btnf1_6_mapa)

        btnsiguiente.setOnClickListener() {
            DbHandler.userAumentarPuntuacion(10)
            DbHandler.userActualizarUltimoPunto(thisJuegoId)
            DbHandler().requestDbUserUpdate(this)
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_to_fragment2_6_minijuego)
        }

        btnrepertir.setOnClickListener(){
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_self)
        }

        animacionVolumen(view)

        //JUEGO========================================================================

        var clickedButton = 1

        //declaramos los botones
        val comprobarbtn: Button = view.findViewById(R.id.btn1_6_comprobar)

        comprobarbtn.setOnClickListener() {
            comprobarRespuestas()
        }

        //=====================================
        prepararSpinners()
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
     * Prepara los spinners del bertso
     */
    private fun prepararSpinners() {
        val spinner1Element: Spinner = globalView.findViewById(R.id.juego6_opcion1)
        val spinner1Opts = arrayOf("__________▼","sagardoaren", "ardiaren", "ibaiaren", "basoaren")
        val spinnerAdapter1: ArrayAdapter<String> = object: ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner1Opts){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                var v: View? = null
                if (position === 0) {
                    val tv = TextView(context)
                    tv.height = 0
                    tv.visibility = View.GONE
                    v = tv
                } else {
                    v = super.getDropDownView(position, null, parent)
                }
                parent.setVerticalScrollBarEnabled(false)
                return v!!

            }
        }
        spinner1Element.adapter = spinnerAdapter1


        val spinner2Element: Spinner = globalView.findViewById(R.id.juego6_opcion2)
        val spinner2Opts = arrayOf("__________▼","edaria","guztia", "eskaria", "bidaria")
        val spinnerAdapter2: ArrayAdapter<String> = object: ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner2Opts){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                var v: View? = null
                if (position === 0) {
                    val tv = TextView(context)
                    tv.height = 0
                    tv.visibility = View.GONE
                    v = tv
                } else {
                    v = super.getDropDownView(position, null, parent)
                }
                parent.setVerticalScrollBarEnabled(false)
                return v!!

            }
        }
        spinner2Element.adapter = spinnerAdapter2

        val spinner3Element: Spinner = globalView.findViewById(R.id.juego6_opcion3)
        val spinner3Opts = arrayOf("__________▼","basatia","azkuria", "errekoia", "bizia")
        val spinnerAdapter3: ArrayAdapter<String> = object: ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner3Opts){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                var v: View? = null
                if (position === 0) {
                    val tv = TextView(context)
                    tv.height = 0
                    tv.visibility = View.GONE
                    v = tv
                } else {
                    v = super.getDropDownView(position, null, parent)
                }
                parent.setVerticalScrollBarEnabled(false)
                return v!!

            }
        }
        spinner3Element.adapter = spinnerAdapter3

        val spinner4Element: Spinner = globalView.findViewById(R.id.juego6_opcion4)
        val spinner4Opts = arrayOf("__________▼","kupelan","amaitzian","atzerkijan", "arean")
        val spinnerAdapter4: ArrayAdapter<String> = object: ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner4Opts){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                var v: View? = null
                if (position === 0) {
                    val tv = TextView(context)
                    tv.height = 0
                    tv.visibility = View.GONE
                    v = tv
                } else {
                    v = super.getDropDownView(position, null, parent)
                }
                parent.setVerticalScrollBarEnabled(false)
                return v!!

            }
        }
        spinner4Element.adapter = spinnerAdapter4

        val spinner5Element: Spinner = globalView.findViewById(R.id.juego6_opcion5)
        val spinner5Opts = arrayOf("__________▼","prezioa", "opioa", "lekzioa", "ilusioa")
        val spinnerAdapter5: ArrayAdapter<String> = object: ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner5Opts){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                var v: View? = null
                if (position === 0) {
                    val tv = TextView(context)
                    tv.height = 0
                    tv.visibility = View.GONE
                    v = tv
                } else {
                    v = super.getDropDownView(position, null, parent)
                }
                parent.setVerticalScrollBarEnabled(false)
                return v!!

            }
        }
        spinner5Element.adapter = spinnerAdapter5

        val spinner6Element: Spinner = globalView.findViewById(R.id.juego6_opcion6)
        val spinner6Opts = arrayOf("__________▼","pertzepzioa","estimazioa", "pentsioa", "ekuazioa")
        val spinnerAdapter6: ArrayAdapter<String> = object: ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner6Opts){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                var v: View? = null
                if (position === 0) {
                    val tv = TextView(context)
                    tv.height = 0
                    tv.visibility = View.GONE
                    v = tv
                } else {
                    v = super.getDropDownView(position, null, parent)
                }
                parent.setVerticalScrollBarEnabled(false)
                return v!!

            }
        }
        spinner6Element.adapter = spinnerAdapter6
    }

    /**
     * Comprueba las respuestas del bertso
     */
    private fun comprobarRespuestas() {
        val spinner1Element: Spinner = globalView.findViewById(R.id.juego6_opcion1)
        val spinner2Element: Spinner = globalView.findViewById(R.id.juego6_opcion2)
        val spinner3Element: Spinner = globalView.findViewById(R.id.juego6_opcion3)
        val spinner4Element: Spinner = globalView.findViewById(R.id.juego6_opcion4)
        val spinner5Element: Spinner = globalView.findViewById(R.id.juego6_opcion5)
        val spinner6Element: Spinner = globalView.findViewById(R.id.juego6_opcion6)
        if (spinner1Element.selectedItem.toString().lowercase().equals(respuestas[0]) &&
                spinner2Element.selectedItem.toString().lowercase().equals(respuestas[1]) &&
                spinner3Element.selectedItem.toString().lowercase().equals(respuestas[2]) &&
                spinner4Element.selectedItem.toString().lowercase().equals(respuestas[3]) &&
                spinner5Element.selectedItem.toString().lowercase().equals(respuestas[4]) &&
                spinner6Element.selectedItem.toString().lowercase().equals(respuestas[5])) {
            val comprobarbtn: Button = globalView.findViewById(R.id.btn1_6_comprobar)
            comprobarbtn.visibility = View.GONE
            playPauseButton.setOnClickListener(null)
            backwardButton.setOnClickListener(null)
            forwardButton.setOnClickListener(null)
            if(testAudioTemp?.isPlaying == true){
                testAudioTemp?.stop()
            }

            runBlocking {
                launch {
                    audio = MediaPlayer.create(context, R.raw.ongiaudioa6)
                    audio?.start()
                    audio?.setOnCompletionListener {

                        activarBtnsHanler?.removeCallbacksAndMessages(null)
                        activarBtnsHanler = Handler()
                        activarBtnsHanler?.postDelayed({
                            val btnsiguiente: Button = globalView.findViewById(R.id.btnf1_6siguienteJuego)
                            val btnrepetir : Button = globalView.findViewById(R.id.btnf1_6repetirJuego)

                            //sacamos el boton para el siguiente minijuego
                            btnsiguiente.isVisible = true
                            btnrepetir.isVisible = true
                            activarBtnsHanler?.removeCallbacksAndMessages(null)
                            activarBtnsHanler = null
                        }, 1000)
                    }
                }
            }
        } else {
            runBlocking {
                launch {
                    audio = MediaPlayer.create(context, R.raw.gaizkiaudioa)
                    audio?.start()
                    audio?.setOnCompletionListener {
                        recargarJuegoHandler?.removeCallbacksAndMessages(null)
                        recargarJuegoHandler = Handler()
                        recargarJuegoHandler?.postDelayed({
                            Navigation.findNavController(globalView)
                                .navigate(R.id.action_fragment1_6_juego_self)
                            recargarJuegoHandler?.removeCallbacksAndMessages(null)
                            recargarJuegoHandler = null
                        }, 1000)
                    }
                }
            }
        }
    }

    /**
    * Hace los controles del audio visibles
    */
    private fun makeBertsoControlVisible() {
        val layoutBertso: LinearLayout = globalView.findViewById(R.id.juego6_layoutBertso)
        val comprobarbtn: Button = globalView.findViewById(R.id.btn1_6_comprobar)
        playPauseButton.visibility = View.VISIBLE
        backwardButton.visibility = View.VISIBLE
        forwardButton.visibility = View.VISIBLE
        layoutBertso.visibility = View.VISIBLE
        comprobarbtn.visibility = View.VISIBLE

    }

    /**
     * Hace la animacion del icono del volumen mientras se reproduce el audio del bertso
     */
    private fun animacionVolumen(view: View) {
        //Recogemos el icono del volumen y le añadimos la animacion
        val volumen: TextView = view.findViewById(R.id.txtv1_6_volumen)
        volumen.isVisible=true

        volumen.setBackgroundResource(R.drawable.animacion_volumen)
        val ani = volumen.getBackground() as AnimationDrawable
        ani.start()

        //Funcion para empezar audio bertso
        startAudio(view)

        //Variable para settear que el audio ya ha sido escuchado 1 vez
        sonido = true

        //Cuando clickamos sobre el icono del volumen el audio se para o sigue
        volumen.setOnClickListener() {
            //cuando clickas en la foto el audio para o sigue
            if (sonido == true) {
                volumen.setBackgroundResource(R.drawable.ic_volumenoff)
                sonido = false
                audio?.pause()
            } else {
                volumen.setBackgroundResource(R.drawable.animacion_volumen)
                val ani = volumen.getBackground() as AnimationDrawable
                ani.start()
                sonido = true
                audio?.start()
            }
        }

    }

    /**
     * Reproduce el primer audio (bertso)
     */
    private fun startAudio(view: View) {
        //escondemos el boton de reproducir el audio mientras el audio ya se esta reproducciendo
        val reproducirAudio: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
        reproducirAudio.isVisible = false

        //Recogemos tanto el fondo gris como el icono del volumen
        val fondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        val volumen: TextView = view.findViewById(R.id.txtv1_6_volumen)

        //si no estan visibles sacamos el fondo y el icono del microfono
        if (!fondo.isVisible) {
            fondo.isVisible = true
            volumen.isVisible = true
        }

        runBlocking() {
            launch {
                audio = MediaPlayer.create(
                    context, R.raw.bertsoa
                )
                audio?.setVolume(0.5F, 0.5F)
                audio?.start()

                audio?.setOnCompletionListener {
                    txtv1_6_volumen.isVisible = false

                    if(firstTime){
                        startAudio2(view)
                    }else{
                        //difuminado fondo gris y las letras

                            val txtAnimacion = view.findViewById(R.id.imgv1_6_fondo) as TextView
                            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                            txtAnimacion.startAnimation(aniFade)
                            txtAnimacion.isVisible = false

                        //hacemos que el boton de reproducir el audio sea visible al terminar el audio
                        val reproducirAudio: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
                        reproducirAudio.isVisible = true
                        }

                }
            }
        }

        //Cuando clickamos fuera de la foto, la musica se van
        fondo.setOnClickListener() {
            txtv1_6_volumen.isVisible = false

            audio?.stop()

            if(firstTime) {
                //lanzamos el segundo audio(bertso)
                startAudio2(view)
            }
            else{
                //difuminado fondo gris y las letras

                    val txtAnimacion = view.findViewById(R.id.imgv1_6_fondo) as TextView
                    val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                    txtAnimacion.startAnimation(aniFade)
                    txtAnimacion.isVisible = false

                //hacemos que el boton de reproducir el audio sea visible al terminar el audio
                val reproducirAudio: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
                reproducirAudio.isVisible = true

            }
        }
    }

    /**
     * Reproduce el segundo audio(descripcion del juego)
     */
    private fun startAudio2(view: View) {
        val introFondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        introFondo.setOnClickListener(null)

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
        //disable set on click listener


        val btnVolumen : ImageButton = view.findViewById(R.id.btnf1_6_sonido)
        btnVolumen.isVisible = false

            //reproducimo el audio
            runBlocking() {
                launch {
                    starAnimationfun(view)

                    audio = MediaPlayer.create(
                        context, R.raw.juego6audiotutorial
                    )
                    // audio?.setVolume(0.15F, 0.15F)
                    audio?.start()

                    audio?.setOnCompletionListener {
                        //cuando el audio se termine escondemos el texto y sacamos el bertso y los inputs
                        exitAnimationfun(view)

                        activateBtn()
                    }
                }
                typewriter(view)

            }
            //Variable para settear que el texto ya ha sido escrito y no es la primera vez
            firstTime = false


    }

    /**
     * Hace que el texto del juego se escriba letra por letra
     *
     * @param view la vista en la que se encuentra
     */
    private fun typewriter(view: View) {

        val typeWriterView = view.findViewById(R.id.txtv1_6_explicacion) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.titulo))
        typeWriterView.setDelay(70)
    }

    /**
     * Muestra la manzana de la animacion con una transicion
     *
     * @param view la vista en la que se encuentra
     */
    private fun starAnimationfun(view: View) {
        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_6_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler = Handler()
        talkAnimationHandler?.postDelayed({
            upelio.isVisible = false
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
        doubleTabHandler?.removeCallbacksAndMessages(null)
        introFinished = true
        //animacion salida de upelio
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        val upelio = view.findViewById(R.id.imgv1_6_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        val upelioAnimado = view.findViewById(R.id.imgv1_6_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //difuminado fondo gris y las letras
        fondoAnimationHandler?.removeCallbacksAndMessages(null)
        fondoAnimationHandler = Handler()
        fondoAnimationHandler?.postDelayed({
            val txtAnimacion = view.findViewById(R.id.imgv1_6_fondo) as TextView
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            txtAnimacion.startAnimation(aniFade)
            txtAnimacion.isVisible = false
        }, 1000)

        //aparece el bertso
        bertsoHandler?.removeCallbacksAndMessages(null)
        bertsoHandler = Handler()
        bertsoHandler?.postDelayed({
            txtv1_6_explicacion.isVisible = false
            txtv1_6_bertso.isVisible = true

            val btn: Button = view.findViewById(R.id.btn1_6_comprobar)
            btn.isVisible = true
            makeBertsoControlVisible()
        }, 2000)
    }

    /**
     * Anima la manzana como que habla
     *
     * @param view la vista en la que se encuentra
     */
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_6_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
    }

    /**
     * Añade funcionalidades al los botones (mapa/info)
     */
    private fun activateBtn() {
        mapa.setOnClickListener {
            if (audio?.isPlaying == false){
                activity?.let{
                    val intent = Intent (it, Activity5_Mapa::class.java)
                    it.startActivity(intent)
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
    private fun endIntroManually() {
        if (introFinished) {
            return
        }
        doubleTabHandler?.removeCallbacksAndMessages(null)
        introFinished = true
        val upelio1 = globalView.findViewById(R.id.imgv1_6_upelio) as ImageView
        upelio1.clearAnimation()
        upelio1.visibility = View.GONE
        val upelio2 = globalView.findViewById(R.id.imgv1_6_upelio2) as ImageView
        upelio2.clearAnimation()
        upelio2.visibility = View.GONE
        val txtAnimacion = globalView.findViewById(R.id.imgv1_6_fondo) as TextView
        txtAnimacion.clearAnimation()
        val typeWriterElement = globalView.findViewById(R.id.txtv1_6_explicacion) as TextView
        typeWriterElement.isVisible = false
        txtAnimacion.isVisible = false
        audio?.stop()
        val btn: Button = globalView.findViewById(R.id.btn1_6_comprobar)
        btn.isVisible = true
        makeBertsoControlVisible()
        activateBtn()
    }

    /**
     * Al destuir el fragment termina con todos los elementos que puedan dar errores
     */
    override fun onDestroy() {
        doubleTabHandler?.removeCallbacksAndMessages(null)
        activarBtnsHanler?.removeCallbacksAndMessages(null)
        recargarJuegoHandler?.removeCallbacksAndMessages(null)
        bertsoHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        fondoAnimationHandler?.removeCallbacksAndMessages(null)

        doubleTabHandler = null
        activarBtnsHanler = null
        recargarJuegoHandler = null
        bertsoHandler = null
        talkAnimationHandler = null
        fondoAnimationHandler = null
        audio?.stop()
        testAudioTemp?.stop()
        super.onDestroy()
    }

    /**
     * Al pausar el fragment pausa el audio para que no de error
     */
    override fun onPause() {
        audio?.pause()
        testAudioTemp?.pause()
        super.onPause()
    }

    /**
     * Al reanudar el fragment resume el audio
     */
    override fun onResume() {
        super.onResume()
        audio?.start()
        testAudioTemp?.start()
    }
}