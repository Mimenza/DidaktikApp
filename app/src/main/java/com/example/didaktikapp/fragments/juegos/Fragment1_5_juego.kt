package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment1_5_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */

//Objeto para gestionar facilmente los juegos que requieran drag and drop.
data class DragnDropImage(var origen: ImageView, var objetivo: ImageView, var acertado: Boolean = false)

class Fragment1_5_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var button: Button
    private lateinit var myLayout: View
    private lateinit var video: VideoView
    private var audio: MediaPlayer? = null
    private lateinit var globalView: View
    private lateinit var vistaanimada: TranslateAnimation


    val listaImagenes = listOf(
        listOf(R.id.img_gorro_move,R.id.img_gorro_destino),
        listOf(R.id.img_ropa_move,R.id.img_ropa_destino),
        listOf(R.id.img_guantedcha_move,R.id.img_guantedcha_destino),
        listOf(R.id.img_guanteizq_move,R.id.img_guanteizq_destino),
        listOf(R.id.img_botas_move,R.id.img_botas_destino),
        /*
        // OLD LIST (DONT REMOVE)
        listOf(R.id.imgOrigenCamisa,R.id.imgObjetivoCamisa),
        listOf(R.id.imgOrigenCinturon,R.id.imgObjetivoCinturon),
        listOf(R.id.imgOrigenGorro,R.id.imgObjetivoGorro),
        listOf(R.id.imgOrigenManzana,R.id.imgObjetivoManzana),
        listOf(R.id.imgOrigenZapatos,R.id.imgObjetivoZapatos)
         */
    )

    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    //private lateinit var objetivo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_5_juego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf1_5_siguiente)
        button.visibility = View.GONE
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_5_ajustes)
        val btnVerVideo: Button = view.findViewById(R.id.btnf1_5_siguienteavideo)
        val btnIrAJuego: Button = view.findViewById(R.id.btnf1_5_siguienteajuego)

        //myLayout = view.findViewById(R.id.mainlayout)
        myLayout = view.findViewById<ConstraintLayout>(R.id.mainlayout)



        btnIrAJuego.isVisible=false

        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = view.findViewById(vItemList[0])
            var vItemDestino: ImageView = view.findViewById(vItemList[1])
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            vItemDestino.setColorFilter(Color.argb(150, 0, 80, 200))
            vItemOrigen.setOnTouchListener(listener)
        }

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment2_5_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment4_menu)
        }

        btnVerVideo.setOnClickListener() {

            verVideo()

        }

        //Typewriter juego 5 tutorial
        Handler(Looper.getMainLooper()).postDelayed({
            typewriter(view)
        }, 2000)
        //Typewriter juego 5 tutorial fin


        audioTutorial(view)
        videoTutorial(view)

        return view
    }

    private fun audioTutorial(view: View){

        //Audio juego 5
        var audio: MediaPlayer
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego6audio)
                audio.start()

            }
            //animacion para la descripcion
            starAnimationfun(view)
        }
    }


    private fun videoTutorial(view: View)
    {
        //----------------VIDEO on start on destroy---------------------------
        video= view.findViewById(R.id.videoViewjuego5)
        video.isVisible=false
        //Creating MediaController
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(video)
        //specify the location of media file
        val uri: Uri =
            Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.juego5video)
        //Setting MediaController and URI, then starting the videoView
        video.setMediaController(mediaController)
        video.setVideoURI(uri)
        video.requestFocus()
        video.start()
        //----------------VIDEO FIN---------------------------


    }


    override fun onDestroy() {
        video.stopPlayback()
        audio?.stop()
        super.onDestroy()
    }

    override fun onStop() {
        video.stopPlayback()
        audio?.stop()
        super.onStop()
    }

    override fun onPause() {

        audio?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        // TODO: preguntar si esta el audio empezado
        audio?.start()
    }


    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_5tutorialjuego5) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.sagardantzajolasa))
        typeWriterView.setDelay(70)
    }

    private fun starAnimationfun(view: View) {
        // animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_5tutorialjuego5) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //animacion entrada upelio
        vistaanimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaanimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_5_upelio) as ImageView
        upelio.startAnimation(vistaanimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            upelio.isVisible = false
            talkAnimationfun(view)
        }, 2000)

    }

    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_5_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()

    }

    private fun verVideo(){

        //Ocultamos acciones menos el video
        imgv1_5_upelio.isVisible=false
        imgv1_5_upelio2.isVisible=false
        txtv1_5tutorialjuego5.isVisible=false
        videoViewjuego5.isVisible=true

        btnf1_5_siguienteavideo.isVisible=false
        btnf1_5_siguienteajuego.isVisible=true

        btnf1_5_siguienteajuego.setOnClickListener{

            exitAnimationfun()
        }

    }

    private fun exitAnimationfun() {

        //Ocultamos las acciones
        videoViewjuego5.isVisible=false
        btnf1_5_siguienteajuego.isVisible=false
        txtv1_5fondogris.isVisible=false
        btnf1_5_siguiente.isVisible=false


    }


    var opX: Float = 0F
    var opY: Float = 0F

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)
        if (itemInList != null) {
            if (!itemInList.acertado) {
                val action = motionEvent.action
                when(action) {
                    MotionEvent.ACTION_DOWN -> {
                        opX = viewElement.x
                        opY = viewElement.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                    }
                    MotionEvent.ACTION_UP -> {
                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        var posX = objetivoEncontrado.getLeft()
                        var posY = objetivoEncontrado.getTop()
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height

                        if ( (viewElement.x + viewElement.width/2) >= posX && (viewElement.y + viewElement.height/2) >= posY && (viewElement.x + viewElement.width/2) <= posX+sizeX && (viewElement.y + viewElement.height/2) <= posY+sizeY) {
                            //viewElement.visibility = View.GONE
                            //viewElement.x = opX
                            //viewElement.y = opY
                            viewElement.x = posX.toFloat()
                            viewElement.y = posY.toFloat()
                            //Utils.drawLine(globalView, requireContext(),opX,opY,posX.toFloat(),posY.toFloat(),15F, (0..255).random(),(0..255).random(),(0..255).random())
                            itemInList.acertado = true
                            if (juegoCompletado()) {
                                button.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "Bikain!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        true
    }

    private fun juegoCompletado(): Boolean {
        var finalizado: Boolean = true
        for (item in manzanaList!!) {
            if (!item.acertado) {
                finalizado = false
                break
            }
        }
        return finalizado
    }

    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
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
            Fragment1_5_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}


