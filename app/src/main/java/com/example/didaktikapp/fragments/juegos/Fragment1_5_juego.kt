package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
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
import android.graphics.ColorMatrixColorFilter

import android.graphics.ColorMatrix
import android.view.*
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Fragment1_5_juego : Fragment(), DbHandler.QueryResponseDone {
    private val thisJuegoId = 5
    // TODO: Rename and change types of parameters
    private lateinit var btnInfoJuego: ImageButton
    private lateinit var button: Button
    private lateinit var myLayout: View
    private lateinit var video: VideoView
    private var audio: MediaPlayer? = null
    private lateinit var globalView: View
    private lateinit var vistaanimada: TranslateAnimation
    private lateinit var  mapa: ImageButton
    //After using View Listener, we can get the full width and height
    var totalWidth: Int = 0
    var totalHeight: Int = 0

    //We store the resources ID in a list to later create the element and apply then an event
    val listaImagenes = listOf(
        listOf(R.id.img_gorro_move,R.id.img_gorro_destino),
        listOf(R.id.img_ropa_move,R.id.img_ropa_destino),
        listOf(R.id.juego5_zp_manodcha_move,R.id.juego5_zp_manodcha),
        listOf(R.id.ijuego5_manoizq_move,R.id.juego5_manoizq_destino),
        listOf(R.id.img_botasizq_move,R.id.img_botasizq_destino),
        listOf(R.id.img_botasdcha_move,R.id.img_botasdcha_destino),
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_5_juego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf1_5_siguiente)
        button.visibility = View.GONE

        val btnVerVideo: Button = view.findViewById(R.id.btnf1_5_siguienteavideo)
        val btnIrAJuego: Button = view.findViewById(R.id.btnf1_5_siguienteajuego)
        myLayout = view.findViewById<ConstraintLayout>(R.id.mainlayout)
        btnInfoJuego= view.findViewById((R.id.btn1_5_infojuego))
        btnIrAJuego.isVisible=false

        //We set a Global View Listener since it is not fill up even with the events onCreatedView...etc
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                totalWidth = view.width
                totalHeight = view.height
                prepairVestimentaElements()
            }
        })
         mapa = view.findViewById(R.id.btnf1_5_mapa)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment2_5_minijuego)
        }

        btnVerVideo.setOnClickListener() {

            verVideo()
            audio?.stop()
        }

        //Typewriter juego 5 tutorial
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                typewriter(view)
            }
        }, 2000)
        //Typewriter juego 5 tutorial fin

        audioTutorial(view)
        videoTutorial(view)

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

    private fun prepairVestimentaElements() {
        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = globalView.findViewById(vItemList[0])
            var vItemDestino: ImageView = globalView.findViewById(vItemList[1])
            vItemOrigen.x = ((0..totalWidth - vItemDestino.width).random()).toFloat()
            vItemOrigen.y = ((0..totalHeight - vItemDestino.height).random()).toFloat()
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(matrix)
            vItemDestino.setColorFilter(filter)
            vItemDestino.setAlpha(255)
            vItemOrigen.setOnTouchListener(listener)
        }
    }

    private fun audioTutorial(view: View){

        //Audio juego 5
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego5audiotutorial)
                audio?.start()

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

    /*
    //Old Way to stop it
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
        audio?.start()
    }
     */


    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_5tutorialjuego5) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.sagardantzajolasatutorial))
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
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
            }
        }, 2000)

    }

    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_5_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()

    }

    private fun verVideo(){
        //dejamos que se pueda ver el video en landscape
        getActivity()?.setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)

        //Ocultamos acciones menos el video
        imgv1_5_upelio.isVisible=false
        imgv1_5_upelio2.isVisible=false
        txtv1_5tutorialjuego5.isVisible=false
        videoViewjuego5.isVisible=true

        btnf1_5_siguienteavideo.isVisible=false
        btnf1_5_siguienteajuego.isVisible=true

        btnf1_5_siguienteajuego.setOnClickListener{

            exitAnimationfun()

            activateBtn()
        }

    }


    private fun exitAnimationfun() {
//volvemos a ponerlo en portrait
        getActivity()?.setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        //Ocultamos las acciones
        videoViewjuego5.isVisible=false
        btnf1_5_siguienteajuego.isVisible=false
        txtv1_5fondogris.isVisible=false
        btnf1_5_siguiente.isVisible=false

        //Habilitamos las piezas de los juegos
        img_botasdcha_move.isVisible=true
        img_botasizq_move.isVisible=true
        img_gorro_move.isVisible=true
        juego5_zp_manodcha_move.isVisible=true
        ijuego5_manoizq_move.isVisible=true
        img_ropa_move.isVisible=true

    }

    var opX: Float = 0F
    var opY: Float = 0F

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)
        if (itemInList != null) {
            if (!itemInList.acertado) {
                viewElement.bringToFront()
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
                            sendToTopImagesNotFinished()
                            viewElement.setOnTouchListener(null)
                            if (juegoCompletado()) {
                                DbHandler.userAumentarPuntuacion(10)
                                DbHandler.userActualizarUltimoPunto(thisJuegoId)
                                DbHandler().requestDbUserUpdate(this)
                                audio?.stop()
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

    private fun sendToTopImagesNotFinished() {
        for (item in manzanaList!!) {
            if (!item.acertado) {
                item.origen.bringToFront()
            }
        }
    }

    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
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

    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        //video.stopPlayback()
        if (video != null && video.isPlaying) {
            video.pause()
        }
        audio?.pause()
        super.onPause()
    }

    override fun onResume() {
        if (video != null && !video.isPlaying) {
            video.resume()
        }
        super.onResume()
        audio?.start()
    }


}


