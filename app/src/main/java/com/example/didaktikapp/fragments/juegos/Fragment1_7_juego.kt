package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.DbHandler
import com.example.reto01.Model.User
import kotlinx.android.synthetic.main.fragment1_7_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_7_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var globalView: View
    private var audio: MediaPlayer? = null
    private var viewActiva = false
    private lateinit var vistaAnimada: TranslateAnimation

    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    val duracionJuego: Int = 60 // Duracion en segundos
    val intervaloGeneracionManzanas = 3 //Duracion en segundos

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
        val view = inflater.inflate(R.layout.fragment1_7_juego, container, false)
        globalView = view
        val button: Button = view.findViewById(R.id.btnf1_7_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_7_ajustes)
        button.visibility=GONE

/*
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout)
        var newView: ImageView
        newView = ImageView(requireContext())
        newView.layoutParams.height = 200
        newView.layoutParams.width = 200
        newView.x = 300F
        newView.y = 500F
        newView.setBackgroundColor(Color.BLUE)


        //newView.setImageResource(R.drawable.sagarragorria)

        constraintLayoutFound.addView(newView)
        */

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_7_juego_to_fragment2_7_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_7_juego_to_fragment4_menu)
        }

        /*
        // COMENTADO PARA EL DESARROLLO DEL JUEGO
        //Typewriter juego 4 tutorial
        Handler(Looper.getMainLooper()).postDelayed({
            typewriter(view)
        }, 2000)
        //Typewriter juego 4 tutorial fin
        audioTutorial(view)
         */
        finalizarQuickIntro(view)
        return view
    }

    fun iniciarJuegoRecogerManzanas() {
        startTimeCounter()
    }

    fun finalizarQuickIntro(view: View) {
        exitAnimationfun(view)
        val img: ImageView = view.findViewById(R.id.imgv1_7_upelio) as ImageView
        img.visibility = GONE
        iniciarJuegoRecogerManzanas()
    }

    fun audioTutorial(view: View){

        //Audio juego 4
        var audio: MediaPlayer
        runBlocking() {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego7audiotutorial)
                audio.start()
                audio?.setOnCompletionListener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        //Llama a la funcion para la animacion de salida cuando el audio se termina
                        exitAnimationfun(view)
                    }, 1000)
                }
            }
            //animacion para la descripcion
            starAnimationfun(view)
        }
    }

    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_7_fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_7_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            upelio.isVisible = false
            talkAnimationfun(view)
        }, 2000)

    }
    private fun exitAnimationfun(view: View) {
        val upelioAnimado = view.findViewById(R.id.imgv1_7_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //Animacion upelio salido
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //VistaAnimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_7_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //Animacion fondo gris
        Handler(Looper.getMainLooper()).postDelayed({
            val txtAnimacion = view.findViewById(R.id.txtv1_7_fondogris) as TextView
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            txtAnimacion.startAnimation(aniFade)
            txtv1_7_tutorialjuego4.startAnimation(aniFade)
            txtv1_7_tutorialjuego4.isVisible = false
            txtAnimacion.isVisible = false
        }, 1000)
    }

    private fun typewriter(view: View) {
        /*
        val typeWriterView = view.findViewById(R.id.txtv1_7_tutorialjuego4) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.juego4tutorial))
        typeWriterView.setDelay(65)

         */
    }
    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_7_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
    }


    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onStop() {
        audio?.stop()
        super.onStop()
    }

    fun generarManzana() {
        var imgManzanaGenerada: ImageView = ImageView(requireContext())
        //newView = ImageView(requireContext())
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout)
        constraintLayoutFound.addView(imgManzanaGenerada)
        imgManzanaGenerada.layoutParams.height = 200
        imgManzanaGenerada.layoutParams.width = 200
      //  newView.x = 200F
       // newView.y = 200F
        imgManzanaGenerada.x = ((0..globalView.width - 200).random()).toFloat()
        imgManzanaGenerada.y = ((0..globalView.height/2 - 200).random()).toFloat()
        //imgManzanaGenerada.setBackgroundColor(Color.BLUE)


        var tipoManzana = (0..1).random()
        var mznGnrDestino: ImageView
        if (tipoManzana == 1) {
            imgManzanaGenerada.setImageResource(R.drawable.sagarragorria)
            mznGnrDestino = globalView.findViewById(R.id.juegox_basurero)
        } else {
            imgManzanaGenerada.setImageResource(R.drawable.sagarraberdea)
            mznGnrDestino = globalView.findViewById(R.id.juegox_cesta)
        }
        manzanaList!!.add(DragnDropImage(imgManzanaGenerada,mznGnrDestino))

        imgManzanaGenerada.setOnTouchListener(listener)

        //OPTIONAL TO DO
        //val newAnimation: AnimatorSet = AnimatorSet()
        //newAnimation.stop

    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)
        if (itemInList != null) {
            if (!itemInList.acertado) {
                viewElement.bringToFront()
                val action = motionEvent.action
                when(action) {
                    MotionEvent.ACTION_MOVE -> {
                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                    }
                    MotionEvent.ACTION_UP -> {

                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        val location = IntArray(2)
                        objetivoEncontrado.getLocationOnScreen(location);
                        var posX = location[0]
                        var posY = location[1]
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height
                        if ( (viewElement.x + viewElement.width/2) >= posX && (viewElement.y + viewElement.height/2) >= posY && (viewElement.x + viewElement.width/2) <= posX+sizeX && (viewElement.y + viewElement.height/2) <= posY+sizeY) {
                            viewElement.x = posX.toFloat()
                            viewElement.y = posY.toFloat()
                            itemInList.acertado = true

                            viewElement.visibility = GONE
                            viewElement.setOnTouchListener(null)
                            /*
                            sendToTopImagesNotFinished()
                            viewElement.setOnTouchListener(null)
                            if (puzzleCompletado()) {
                                //iniciarPreguntas()
                                var myUser: User = DbHandler.getUser()!!
                                myUser.puntuacion = myUser.puntuacion!! + 5
                                DbHandler().requestDbUserUpdate(this)
                                button.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "Bikain!", Toast.LENGTH_SHORT).show()
                            }

                             */
                        }
                    }
                }
            }
        }
        true
    }

    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    private fun removeListenerManzanas() {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                item.origen.setOnTouchListener(null)
            }
        }
    }

    //fun startTimeCounter(view: View, timeInSeconds: Int) {
    fun startTimeCounter() {
        object: CountDownTimer((duracionJuego*1000).toLong(), (intervaloGeneracionManzanas*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                generarManzana()
            }
            override fun onFinish() {
                removeListenerManzanas()
            }
        }.start()
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