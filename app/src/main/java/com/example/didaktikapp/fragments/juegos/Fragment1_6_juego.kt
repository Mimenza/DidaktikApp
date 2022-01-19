package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity6_Site
import kotlinx.android.synthetic.main.activity1_principal.*
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.android.synthetic.main.fragment1_6_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var vistaAnimada: TranslateAnimation

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */

class Fragment1_6_juego : Fragment() {
    private val thisJuegoId = 6
    private var param1: String? = null
    private var param2: String? = null
    private var sonido = false
    private var audio: MediaPlayer? = null
    private var firstTime: Boolean = true
    private lateinit var globalView: View

    private lateinit var playPauseButton: ImageView
    private lateinit var backwardButton: ImageView
    private lateinit var forwardButton: ImageView
    private var audioState: Boolean = false
    private var testAudioTemp: MediaPlayer? = null

    var respuestas = listOf(
        "sagardoaren",
        "guztia",
        "bizia",
        "kupelan",
        "prezioa",
        "estimazioa"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_6_juego, container, false)
        globalView = view
        val btnsiguiente: Button = view.findViewById(R.id.btnf1_6siguienteJuego)
        val btnsaltar: Button = view.findViewById(R.id.btnf1_6saltarjuego)
        val btnrepertir : Button = view.findViewById(R.id.btnf1_6repetirJuego)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_6_ajustes)
        //val buttonSonido: ImageButton = view.findViewById(R.id.btnf1_6_sonido)

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


        btnsiguiente.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_to_fragment2_6_minijuego)
        }
        ajustes.setOnClickListener() {
            if (audio?.isPlaying == false){

                (activity as Activity6_Site?)?.menuCheck()

            }
        }
//        buttonSonido.setOnClickListener() {
//           animacionVolumen(view)
//        }
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

    private fun prepararSpinners() {
        val spinner1Element: Spinner = globalView.findViewById(R.id.juego6_opcion1)
        val spinner1Opts = arrayOf("SELECT","sagardoaren", "otraopcion1")
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
        val spinner2Opts = arrayOf("SELECT","guztia", "otraopcion2")
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
        val spinner3Opts = arrayOf("SELECT","bizia", "otraopcion3")
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
        val spinner4Opts = arrayOf("SELECT","kupelan", "otraopcion4")
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
        val spinner5Opts = arrayOf("SELECT","prezioa", "opcion5")
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
        val spinner6Opts = arrayOf("SELECT","estimazioa", "otraopcion6")
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
            runBlocking {
                launch {
                    audio = MediaPlayer.create(context, R.raw.ongiaudioa6)
                    audio?.start()
                    audio?.setOnCompletionListener {
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (getView() != null) {

                                val btnsiguiente: Button = globalView.findViewById(R.id.btnf1_6siguienteJuego)
                                val btnrepetir : Button = globalView.findViewById(R.id.btnf1_6repetirJuego)

                                //sacamos el boton para el siguiente minijuego
                                btnsiguiente.isVisible = true
                                btnrepetir.isVisible = true



                                //sacamos el boton para el siguiente minijuego

                            }

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
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (getView() != null) {
                                //recargamos el juego

                                Navigation.findNavController(globalView)
                                    .navigate(R.id.action_fragment1_6_juego_self)

                            }
                        }, 1000)
                    }
                }
            }
        }
    }

    private fun makeBertsoControlVisible() {
        val layoutBertso: LinearLayout = globalView.findViewById(R.id.juego6_layoutBertso)
        val comprobarbtn: Button = globalView.findViewById(R.id.btn1_6_comprobar)
        playPauseButton.visibility = View.VISIBLE
        backwardButton.visibility = View.VISIBLE
        forwardButton.visibility = View.VISIBLE
        layoutBertso.visibility = View.VISIBLE
        comprobarbtn.visibility = View.VISIBLE

    }


    private fun animacionVolumen(view: View) {
        //Funcion para la animacion del icono del volumen mientras se reproduce el audio del bertso
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



    private fun startAudio(view: View) {
        //Funcion que reproduce el primer audio (bertso)

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

    private fun startAudio2(view: View) {
        //Funcion para el segundo audio(descripcion del juego)
        //disable set on click listener
        val fondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        fondo.setOnClickListener(null)

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

                        //btnVolumen.isVisible = true
                    }
                }
                typewriter(view)

            }
            //Variable para settear que el texto ya ha sido escrito y no es la primera vez
            firstTime = false
    }

    private fun typewriter(view: View) {

        val typeWriterView = view.findViewById(R.id.txtv1_6_explicacion) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.titulo))
        typeWriterView.setDelay(70)
    }

    private fun starAnimationfun(view: View) {
        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_6_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
            }
        }, 2000)

    }

    private fun exitAnimationfun(view: View) {

        //animacion salida de upelio
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        val upelio = view.findViewById(R.id.imgv1_6_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        val upelioAnimado = view.findViewById(R.id.imgv1_6_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //difuminado fondo gris y las letras
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                val txtAnimacion = view.findViewById(R.id.imgv1_6_fondo) as TextView
                val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                txtAnimacion.startAnimation(aniFade)
                txtAnimacion.isVisible = false
                //makeBertsoControlVisible()
            }
        }, 1000)


        //aparece el bertso
        Handler().postDelayed({
            if (getView() != null) {
                txtv1_6_explicacion.isVisible = false
                txtv1_6_bertso.isVisible = true

                val btn: Button = view.findViewById(R.id.btn1_6_comprobar)
                btn.isVisible = true
                makeBertsoControlVisible()
            }
        }, 2000)
    }

    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_6_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
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
            Fragment1_6_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        audio?.stop()
        testAudioTemp?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        audio?.pause()
        testAudioTemp?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        audio?.start()
        testAudioTemp?.start()
    }
}