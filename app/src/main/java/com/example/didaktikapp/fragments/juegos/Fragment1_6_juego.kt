package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.core.text.HtmlCompat;
import android.widget.TextView;
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity1_principal.*
import kotlinx.android.synthetic.main.fragment1_6_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import android.widget.EditText


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */

class Fragment1_6_juego : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var sonido = false
    private var audio: MediaPlayer? = null
    private var firstTime: Boolean = true

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
        val button: Button = view.findViewById(R.id.btnf1_6_siguiente)

        val ajustes: ImageButton = view.findViewById(R.id.btnf1_6_ajustes)
        val buttonSonido: ImageButton = view.findViewById(R.id.btnf1_6_sonido)

        button.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_to_fragment2_6_minijuego)
        }
        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_6_juego_to_fragment4_menu)
        }
        buttonSonido.setOnClickListener() {
            startAudio(view)
        }

        //=====================================
        val blank = "&#x2009;&#x2009;&#x2009;&#x2009;&#x2009;"

        val bertsotxt : TextView = view.findViewById(R.id.txtv1_6_bertso)

        var input1 = blank + "<b>1</b>" + blank
        var input2 = blank + "<b>2</b>" + blank
        var input3 = blank + "<b>3</b>" + blank
        var input4 = blank + "<b>4</b>" + blank
        var input5 = blank + "<b>5</b>" + blank
        var input6 = blank + "<b>6</b>" + blank

        //=====================================
        var respuesta1=false
        var respuesta2=false
        var respuesta3=false
        var respuesta4=false
        var respuesta5=false
        var respuesta6=false
        //=====================================
        //funcion para rellenar el bertso
        insertHtml(bertsotxt, input1, input2, input3, input4, input5, input6)

        animacionVolumen(view)

        //JUEGO========================================================================

        var clickedButton = 0
        //arraylist con las respuestas correctas
        var respuestas = listOf(
            "sagardoaren",
            "guztia",
            "bizia",
            "kupelan",
            "prezioa",
            "estimazioa"
        )

        //declaramos los botones
        val input1btn : TextView = view.findViewById(R.id.txtv1_6_input1)
        val input2btn : TextView = view.findViewById(R.id.txtv1_6_input2)
        val input3btn : TextView = view.findViewById(R.id.txtv1_6_input3)
        val input4btn : TextView = view.findViewById(R.id.txtv1_6_input4)
        val input5btn : TextView = view.findViewById(R.id.txtv1_6_input5)
        val input6btn : TextView = view.findViewById(R.id.txtv1_6_input6)
        val comprobarbtn : Button = view.findViewById(R.id.btn1_6_comprobar)

        //declaramos los inputText
        val inputgeneral0 : EditText = view.findViewById(R.id.txtv1_6_inputgeneral0)


        //listeners para saber que input hemos clickado
        input1btn.setOnClickListener(){
            clickedButton = 1
            inputgeneral0.setHint("escribe respuesta 1")
        }
        input2btn.setOnClickListener(){
            clickedButton = 2
            inputgeneral0.setHint("escribe respuesta 2")
        }
        input3btn.setOnClickListener(){
            clickedButton = 3
            inputgeneral0.setHint("escribe respuesta 3")
        }
        input4btn.setOnClickListener(){
            clickedButton = 4
            inputgeneral0.setHint("escribe respuesta 4")
        }
        input5btn.setOnClickListener(){
            clickedButton = 5
            inputgeneral0.setHint("escribe respuesta 5")
        }
        input6btn.setOnClickListener(){
            clickedButton = 6
            inputgeneral0.setHint("escribe respuesta 6")
        }

        //=====================================
        //para comprobar si la respuesta es la correcta

        comprobarbtn.setOnClickListener(){
        var input:String

            if (!inputgeneral0.text.isBlank() || clickedButton != 0){

                var respuestaCorrecta = respuestas[clickedButton-1]
                var respuestaIntroducida = inputgeneral0.text.toString()

                respuestaCorrecta = respuestaCorrecta.replace("\\s".toRegex(), "")
                respuestaIntroducida = respuestaIntroducida.replace("\\s".toRegex(), "")

                if(respuestaCorrecta.equals(respuestaIntroducida)){
                    //respuesta correcta

                    //seteamos el valor correcto en los inputs
                        // guardamos que inputs hemos ya introducido correctamente

                    when (clickedButton) {
                        1 -> {input1 = respuestaCorrecta
                            respuesta1 = true}

                        2 -> {input2 = respuestaCorrecta
                            respuesta2 = true}

                        3 -> {input3 = respuestaCorrecta
                            respuesta3 = true}

                        4 -> {input4 = respuestaCorrecta
                            respuesta4 = true}

                        5 -> {input5 = respuestaCorrecta
                            respuesta5 = true}

                        6 -> {input6 = respuestaCorrecta
                        respuesta6 = true}
                    }

                    //volvemos a llamar a la funcion para actualizar los datos
                    insertHtml(bertsotxt, input1, input2, input3, input4, input5, input6)

                    //limpiamos el input text
                    inputgeneral0.setText("")

                    //checkeamos si hemos compleado toodo el juego o no
                    checkGameStatus(respuesta1,respuesta2,respuesta3,respuesta4,respuesta5,respuesta6,view)
                }
                else{
                    //respuesta incorrecta


                }

            }

        }

        //=====================================

        return view
    }

    private fun checkGameStatus( respuesta1: Boolean, respuesta2: Boolean, respuesta3: Boolean, respuesta4: Boolean, respuesta5: Boolean, respuesta6: Boolean, view:View) {

        if (respuesta1==true && respuesta2==true &&respuesta3==true &&respuesta4==true &&respuesta5==true &&respuesta6==true){

            println("EJERCICIO COMPLEADO")

            val btnsiguiente: Button = view.findViewById(R.id.btnf1_6_siguiente)

            //sacamos el boton para el siguiente minijuego
            btnsiguiente.isVisible=true
        }

    }

    private fun insertHtml(bertsotxt:TextView, input1:String, input2: String, input3: String, input4: String, input5: String, input6: String) {

        var htmlString = "Sagardotegiari<br/><br/>\n" +
                "\n" +
                "        Bedeinkatua izan dadila "+input1+" grazia<br/><br/>\n" +
                "\n" +
                "        Bai eta ere kupira gabe edaten duen "+input2+";<br/><br/>\n" +
                "\n" +
                "        Edari honek jende askori ematen dio "+input3+";,<br/><br/>\n" +
                "\n" +
                "        Hau edan gabe egotea da neretzat penitentzia.<br/><br/>\n" +
                "\n" +
                "        "+input4+" dagon sagardoak dit ematen tentazioa<br/><br/>\n" +
                "\n" +
                "        Prezisamente edan beharra daukat pertsekuzioa;<br/><br/>\n" +
                "\n" +
                "        Mila deabruz josirikako orain duen "+input5+"<br/><br/>\n" +
                "\n" +
                "        Zaleak asko geran medioz dauka "+input6

        val spanned = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT)

        val tvOutput = bertsotxt as TextView

        tvOutput.text = spanned
    }

    private fun animacionVolumen(view: View) {
        //Funcion para la animacion del icono del volumen mientras se reproduce el audio del bertso
        //Recogemos el icono del volumen y le añadimos la animacion
        val volumen: TextView = view.findViewById(R.id.txtv1_6_volumen)
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

        val fondo: TextView = view.findViewById(R.id.imgv1_6_fondo)
        //Cuando clickamos fuera de la foto, la musica y el fondo se van
        fondo.setOnClickListener() {

            imgv1_6_fondo.isVisible = false
            txtv1_6_volumen.isVisible = false
            audio?.stop()

            //lanzamos el segundo audio(bertso)
            startAudio2(view)
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
            volumen.isVisible = false
        }

        runBlocking() {
            launch {
                audio = MediaPlayer.create(
                    context, R.raw.bertsoa
                )
                audio?.setVolume(0.15F, 0.15F)
                audio?.start()

                audio?.setOnCompletionListener {
                    imgv1_6_fondo.isVisible = false
                    txtv1_6_volumen.isVisible = false
                    startAudio2(view)
                }
            }
        }
    }

    private fun startAudio2(view: View) {
        //Funcion para el segundo audio(descripcion del juego)

        //Si es la primera vez que se reproduce el berso si que se escuchara la descripcion, sino no.
        if (firstTime) {
            //reproducimo el audio
            runBlocking() {
                launch {
                    audio = MediaPlayer.create(
                        context, R.raw.kantu_kantajolasa
                    )
                    audio?.setVolume(0.15F, 0.15F)
                    audio?.start()

                    audio?.setOnCompletionListener {
                        //cuando el audio se termine escondemos el texto y sacamos el bertso
                        txtv1_6_explicacion.isVisible = false
                        txtv1_6_bertso.isVisible = true
                    }
                }
            }

            typewriter(view)

            //Variable para settear que el texto ya ha sido escrito
            firstTime = false
        }

        //hacemos que el boton de reproducir el audio sea visible al terminar el audio
        val reproducirAudio: ImageButton = view.findViewById(R.id.btnf1_6_sonido)
        reproducirAudio.isVisible = true
    }

    private fun typewriter(view: View) {

        val typeWriterView = view.findViewById(R.id.txtv1_6_explicacion) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.titulo))
        typeWriterView.setDelay(70)
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

    override fun onPause() {
        super.onPause()
        audio?.pause()
    }

    override fun onResume() {
        super.onResume()
        // TODO: preguntar si esta el audio empezado
        audio?.start()
    }
}