package com.example.didaktikapp.fragments.juegos

import com.example.didaktikapp.R
import `in`.codeshuffle.typewriterview.TypeWriterView
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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity7_Juego2_Results
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
class Fragment1_1_juego : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var vistaAnimada: TranslateAnimation

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
        val view = inflater.inflate(R.layout.fragment1_1_juego, container, false)
        val button: Button = view.findViewById(R.id.btnf1_1saltartutorial)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_1_ajustes)


        img1 = view.findViewById(R.id.imgv1_1imagen1)
        img2 = view.findViewById(R.id.imgv1_1imagen2)
        img3 = view.findViewById(R.id.imgv1_1imagen3)

        txtv1 = view.findViewById(R.id.txtv1_1azalpena1)
        txtv2 = view.findViewById(R.id.txtv1_1azalpena2)
        txtv3 = view.findViewById(R.id.txtv1_1azalpena3)

        button.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment2_1_minijuego)
        }

        ajustes.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment4_menu)
        }



        //Typewriter juego 1 tutorial
        Handler(Looper.getMainLooper()).postDelayed({
            typewriter(view)
        }, 2000)
        //Typewriter juego 1 tutorial fin

        //Audio juego 1
        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego1audio)
                audio?.start()
                audio?.setOnCompletionListener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        //Llama a la funcion para la animacion de salida cuando el audio se termina
                        exitAnimationfun(view)
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
                pressedImg = 1

                //Mira que no hayas conseguido el punto 1
                if (!point1) {
                    //Si no tiene el borde se lo pone y si no se lo quita
                    border1 = if (!border1) {
                        setBorder(img1, "black")
                        true
                    } else {
                        unsetBorder(img1)
                        false
                    }
                }

                //Quita los bordes a las imagenes 2 y 3 comprobando que no los haya conseguido
                if (!point2) {
                    unsetBorder(img2)
                    border2 = false
                }

                if (!point3) {
                    unsetBorder(img3)
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
                        unsetBorder(img2)
                        false
                    }
                }

                //Quita los bordes a las imagenes 1 y 3 comprobando que no los haya conseguido
                if (!point1) {
                    unsetBorder(img1)
                    border1 = false
                }

                if (!point3) {
                    unsetBorder(img3)
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
                        unsetBorder(img3)
                        false
                    }
                }

                //Quita los bordes a las imagenes 1 y 2 comprobando que no los haya conseguido
                if (!point1) {
                    unsetBorder(img1)
                    border1 = false
                }

                if (!point2) {
                    unsetBorder(img2)
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
                        checkProgress(view, progress)
                    }
                } else {
                    when (pressedImg) {
                        1 -> setBorder(img1, "red")
                        2 -> setBorder(img2, "red")
                        3 -> setBorder(img3, "red")
                    }
                    setBorder(txtv1, "red")

                    runBlocking {
                        launch {
                            audio = MediaPlayer.create(context, R.raw.juego1_gaizki)
                            audio?.start()
                            audio?.setOnCompletionListener {
                                resetGame()
                            }
                        }
                    }
                }
            }
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
                        checkProgress(view, progress)
                    }
                } else {
                    when (pressedImg) {
                        1 -> setBorder(img1, "red")
                        2 -> setBorder(img2, "red")
                        3 -> setBorder(img3, "red")
                    }
                    setBorder(txtv2, "red")

                    runBlocking {
                        launch {
                            audio = MediaPlayer.create(context, R.raw.juego1_gaizki)
                            audio?.start()
                            audio?.setOnCompletionListener {
                                resetGame()
                            }
                        }
                    }
                }
            }
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
                        checkProgress(view, progress)
                    }
                } else {
                    when (pressedImg) {
                        1 -> setBorder(img1, "red")
                        2 -> setBorder(img2, "red")
                        3 -> setBorder(img3, "red")
                    }
                    setBorder(txtv3, "red")

                    runBlocking {
                        launch {
                            audio = MediaPlayer.create(context, R.raw.juego1_gaizki)
                            audio?.start()
                            audio?.setOnCompletionListener {
                                resetGame()
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_6_titulo) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.titulo))
        typeWriterView.setDelay(70)
    }

    private fun starAnimationfun(view: View) {
        // animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            upelio.isVisible = false
            talkAnimationfun(view)
        }, 2000)

    }

    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()

    }

    private fun exitAnimationfun(view: View) {
        //escondemos la manzanda de la animacion
        val upelioanimado = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelioanimado.isVisible = false

        //animacion salido upelio
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //vistaanimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //animacion fondo gris
        Handler(Looper.getMainLooper()).postDelayed({
            val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            txtAnimacion.startAnimation(aniFade)
            txtv1_1tutorialjuego1.startAnimation(aniFade)
            txtv1_1tutorialjuego1.isVisible = false
            txtAnimacion.isVisible = false
        }, 1000)
    }

    /**
     * Coloca bordes de colores a una ImageView
     * @param img la ImageView a la que se le pone el borde
     * @param color el color del borde
     */
    private fun setBorder(img: ImageView, color: String) {
        var colorId = 0
        when (color) {
            "black" -> colorId = R.drawable.bg_border_black
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
            "black" -> colorId = R.drawable.bg_border_black
            "green" -> colorId = R.drawable.bg_border_green
            "red" -> colorId = R.drawable.bg_border_red
        }

        txtv.background = context?.let { it ->
            ContextCompat.getDrawable(it, colorId)
        }
    }

    /**
     * Quita el borde a una ImageView
     * @param img la imagen a la que se le quita el borde
     */
    private fun unsetBorder(img: ImageView) {
        img.setBackgroundResource(0)
    }

    /**
     * Quita el borde a un TextView
     * @param txtv el TextView al que se le quita el borde
     */
    private fun unsetBorder(txtv: TextView) {
        txtv.setBackgroundResource(0)
    }

    /**
     * Vacia todas las variable para resetear el juego
     */
    private fun resetGame() {
        progress = 0
        pressedImg = 0

        unsetBorder(img1)
        unsetBorder(img2)
        unsetBorder(img3)

        unsetBorder(txtv1)
        unsetBorder(txtv2)
        unsetBorder(txtv3)

        point1 = false
        point2 = false
        point3 = false

        border1 = false
        border2 = false
        border3 = false
    }

    /**
     * Comprueba el progreso del juego
     * @param view la vista en la que se encuentra
     * @param progress el progeso de la partida
     */
    private fun checkProgress(view: View, progress: Int) {
        if (progress == 3) {
            runBlocking {
                launch {
                    audio = MediaPlayer.create(context, R.raw.juego1_ongi)
                    audio?.start()
                    audio?.setOnCompletionListener {
                        val btnNext: Button = view.findViewById(R.id.btnf1_1saltartutorial)

                        btnNext.isVisible = true
                    }
                }
            }
        }
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
            Fragment1_1_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}