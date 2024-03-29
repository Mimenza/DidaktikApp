package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import android.view.View.OnTouchListener
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.DbHandler


class Fragment2_4_minijuego : Fragment(), DbHandler.QueryResponseDone {
    private var acierto: Int = 1                                // numero de aciertos
    private lateinit var manzana1: ImageView                    // manzana que vamos a cortar 1
    private lateinit var manzana2: ImageView                    // manzana que vamos a cortar 2
    private lateinit var manzana3: ImageView                    // manzana que vamos a cortar 3
    private lateinit var manzana4: ImageView                    // manzana que vamos a cortar 4
    private lateinit var manzana5: ImageView                    // manzana que vamos a cortar 5
    private lateinit var manzanaSeleccionada: ImageView         // manzana que estamos cortando
    private lateinit var vistaAnimada:TranslateAnimation        // variable para animacion
    private lateinit var txtcartel: TextView                    // texto cartel del final
    private lateinit var cartel: ImageView                      // cartel del final
    private lateinit var btnsiguiente:Button                    // boton de siguiente
    private lateinit var btnrepetir:Button                      // boton de repetir
    private lateinit var layout: ConstraintLayout               //
    private lateinit var customLine: CustomLine                 // Linea de dibujado cuando cortamos 1
    private lateinit var customStroke: CustomLine               // Linea de dibujado cuando cortamos 2


    var entra: Boolean = false                                  // variable de si hemos empezado a cortar una manzana
    var sale: Boolean = false                                   // variable de si hemos salido de cortar una manzana
    var dentro: Boolean = false                                 // variable para saber si estamos dentro de una manzana
    var dejarCortar: Boolean = false                            // variable para dejarnos cortar la manzana
    var manzana1cortada:Boolean = false                         // variable para saber si hemos cortado la manzana 1
    var manzana2cortada:Boolean = false                         // variable para saber si hemos cortado la manzana 2
    var manzana3cortada:Boolean = false                         // variable para saber si hemos cortado la manzana 3
    var manzana4cortada:Boolean = false                         // variable para saber si hemos cortado la manzana 4
    var manzana5cortada:Boolean = false                         // variable para saber si hemos cortado la manzana 5

    private lateinit var btninfominijuego: ImageButton          // boton para ayuda de juego
    private var lastX:Float = 0F                                // coordinada par dibujado
    private var lastY:Float = 0F                                // coordinada par dibujado
    private val customLines = arrayListOf<CustomLine>()         // array para guardar los puntos de lineas 1
    private val customStrokes = arrayListOf<CustomLine>()       // array para guardar los puntos de lineas 2

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment2_4_minijuego, container, false)
        layout = view.findViewById(R.id.cl2_4minijuego)

        //manzanas
        manzana1 = view.findViewById(R.id.manzana1_minijuego4)
        manzana2 = view.findViewById(R.id.manzana2_minijuego4)
        manzana3 = view.findViewById(R.id.manzana3_minijuego4)
        manzana4 = view.findViewById(R.id.manzana4_minijuego4)
        manzana5 = view.findViewById(R.id.manzana5_minijuego4)

        cartel= view.findViewById((R.id.imgv2_4cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_4carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_4_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_4_siguiente)
        btninfominijuego= view.findViewById((R.id.btn2_4_infominijuego))

        val mapa: ImageButton = view.findViewById(R.id.btnf2_4_mapa)
        mapa.setOnClickListener {
            activity?.let{
                getActivity()?.finish()
            }

        }
        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }

        view.setOnTouchListener(handleTouch)
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private val handleTouch = OnTouchListener { v, event ->
        val x = event.x.toInt()
        val y = event.y.toInt();


        var manzana1PosX = manzana1.x
        var manzana1PosY = manzana1.y
        var manzana1Width = manzana1.width
        var manzana1height = manzana1.height

        var manzana2PosX = manzana2.x
        var manzana2PosY = manzana2.y
        var manzana2Width = manzana2.width
        var manzana2height = manzana2.height

        var manzana3PosX = manzana3.x
        var manzana3PosY = manzana3.y
        var manzana3Width = manzana3.width
        var manzana3height = manzana3.height

        var manzana4PosX = manzana4.x
        var manzana4PosY = manzana4.y
        var manzana4Width = manzana4.width
        var manzana4height = manzana4.height

        var manzana5PosX = manzana5.x
        var manzana5PosY = manzana5.y
        var manzana5Width = manzana5.width
        var manzana5height = manzana5.height


        // manzana1 punto1 x
        var M1PosX1 = manzana1PosX + 15

        // manzana1 punto2 x
        var M1PosX2 = manzana1PosX + (manzana1Width) - 15

        // manzana1 punto1 y
        var M1PosY1 = manzana1PosY + 15

        // manzana1 punto2 y
        var M1PosY2 = manzana1PosY + (manzana1height) - 15

        //-------------------------------------------------

        // manzana2 punto1 x
        var M2PosX1 = manzana2PosX + 15

        // manzana2 punto2 x
        var M2PosX2 = manzana2PosX + (manzana2Width) - 15

        // manzana2 punto1 y
        var M2PosY1 = manzana2PosY + 15

        // manzana2 punto2 y
        var M2PosY2 = manzana2PosY + (manzana2height) - 15

        //-------------------------------------------------

        // manzana3 punto1 x
        var M3PosX1 = manzana3PosX + 15

        // manzana3 punto2 x
        var M3PosX2 = manzana3PosX + (manzana3Width) - 15

        // manzana3 punto1 y
        var M3PosY1 = manzana3PosY + 15

        // manzana3 punto2 y
        var M3PosY2 = manzana3PosY + (manzana3height) - 15

        //-------------------------------------------------

        // manzana4 punto1 x
        var M4PosX1 = manzana4PosX + 15

        // manzana4 punto2 x
        var M4PosX2 = manzana4PosX + (manzana4Width) - 15

        // manzana4 punto1 y
        var M4PosY1 = manzana4PosY + 15

        // manzana4 punto2 y
        var M4PosY2 = manzana4PosY + (manzana4height) - 15

        //-------------------------------------------------

        // manzana5 punto1 x
        var M5PosX1 = manzana5PosX + 15

        // manzana5 punto2 x
        var M5PosX2 = manzana5PosX + (manzana5Width) - 15

        // manzana5 punto1 y
        var M5PosY1 = manzana5PosY + 15

        // manzana5 punto2 y
        var M5PosY2 = manzana5PosY + (manzana5height) - 15


        when (event.action) {

            MotionEvent.ACTION_MOVE -> {
                //mientras mueves el dedo

                if ((x >= M1PosX1 && x <= M1PosX2) && (y >= M1PosY1 && y <= M1PosY2) && !manzana1cortada) {
                    manzanaSeleccionada = manzana1
                    dentro = true
                } else if ((x >= M2PosX1 && x <= M2PosX2) && (y >= M2PosY1 && y <= M2PosY2) && !manzana2cortada ) {
                    manzanaSeleccionada = manzana2
                    dentro = true
                } else if ((x >= M3PosX1 && x <= M3PosX2) && (y >= M3PosY1 && y <= M3PosY2) && !manzana3cortada) {
                    manzanaSeleccionada = manzana3
                    dentro = true
                } else if ((x >= M4PosX1 && x <= M4PosX2) && (y >= M4PosY1 && y <= M4PosY2) && !manzana4cortada) {
                    manzanaSeleccionada = manzana4
                    dentro = true
                } else if ((x >= M5PosX1 && x <= M5PosX2) && (y >= M5PosY1 && y <= M5PosY2) && !manzana5cortada) {
                    manzanaSeleccionada = manzana5
                    dentro = true

                } else {
                    dentro = false
                }

                if (dentro == true) {

                    //el dedo esta dentro del area
                    if (!entra && dejarCortar) {
                        //seteamos la variable de que ha entrado
                        entra = true

                    }
                } else {
                    //el dedo no esta en el area
                    if (entra) {
                        //si el dedo estaba dentro significa que ha salido
                        sale = true
                        dentro = false

                    }
                    //seteamos la variable para que corte la manzana
                    dejarCortar = true
                }

                if (entra && sale) {
                    cortarManzana(manzanaSeleccionada)
                }

                customLine = CustomLine(requireContext(), lastX, lastY, x.toFloat(), y.toFloat(), 8F, 255, 255, 255, 255)
                customStroke = CustomLine(requireContext(), lastX, lastY, x.toFloat(), y.toFloat(), 20F, 128, 255, 255, 255)

                layout.addView(customLine)
                layout.addView(customStroke)

                customLines.add(customLine)
                customStrokes.add(customStroke)

                lastX = event.x
                lastY = event.y

                if(customLines.size > 7){
                    layout.removeView(customLines[0])
                    layout.removeView(customStrokes[0])

                    customLines.remove(customLines[0])
                    customStrokes.remove(customStrokes[0])
                }

            }

            MotionEvent.ACTION_DOWN -> {
                //cuando aprietas el dedo
                entra = false
                sale = false
                dejarCortar = false

                lastX = event.x
                lastY = event.y

            }

            MotionEvent.ACTION_UP -> {
                //cuando levantas el dedo
                entra = false
                sale = false
                dejarCortar = false
                dentro = false

                for(i in customLines){
                    layout.removeView(i)
                }

                for(i in customStrokes){
                    layout.removeView(i)
                }
            }
        }
        true
    }

    /**
     * Recogemos del shared preferences en que minijuego estamos y depende de cual sea muestra una
     * info de ayuda u otra
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

            0->  {texto=resources.getString(R.string.ayudaminijuego1)}
            1->  {texto=resources.getString(R.string.ayudaminijuego2)}
            2->  {texto=resources.getString(R.string.ayudaminijuego3)}
            3->  {texto=resources.getString(R.string.ayudaminijuego4)}
            4->  {texto=resources.getString(R.string.ayudaminijuego5)}
            5->  {texto=resources.getString(R.string.ayudaminijuego6)}

        }
        println(texto)
        if (textInfo!=null){

            textInfo.setText(texto)
        }
    }

    /**
     * Miramos si hemos cortado todas las manzanas par saber si hemos teminado el juego o no
     *
     */
    fun checkProgress() {
        //si se han cortado todas las manzanas aparece el boton
        if (acierto== 5) {
            DbHandler.userAumentarPuntuacion(5)
            DbHandler().requestDbUserUpdate(this)
            starAnimationfun()
        } else {

            acierto++

        }

    }

    /**
     * Animacion de cierre del minijuego, generamos un cartel con un texto y dos botones
     *
     */
    fun starAnimationfun(){

        //Diseñar cartel madera
        btnsiguiente.visibility = View.VISIBLE
        btnrepetir.visibility = View.VISIBLE
        cartel.visibility=View.VISIBLE
        txtcartel.visibility=View.VISIBLE



        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 1000

        cartel.startAnimation(vistaAnimada)
        btnrepetir.startAnimation(vistaAnimada)
        btnsiguiente.startAnimation(vistaAnimada)
        txtcartel.startAnimation(vistaAnimada)

        btnsiguiente.setOnClickListener(){
            getActivity()?.finish()
        }

        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_4_minijuego_self)
        }

    }

    /**
     * Cuando hemos cortado una manzana se ejecuta esta funcion, generamos una foto random y animacion
     *
     * @param manzana manzana que hemos cortado
     */
    fun cortarManzana(manzana: ImageView) {
        //reseteamos las variables para la siguiente manzana
        entra = false
        sale = false

        //animacion y foto random

        var random =(0.. 5). random()

        when (random) {

            0->{ manzana.setImageResource(R.drawable.trozomanzana1)}
            1->{ manzana.setImageResource(R.drawable.trozomanzana2)}
            2->{ manzana.setImageResource(R.drawable.trozomanzana3)}
            3->{ manzana.setImageResource(R.drawable.trozomanzana4)}
            4->{ manzana.setImageResource(R.drawable.trozomanzana5)}
            5->{ manzana.setImageResource(R.drawable.trozomanzana6)}
        }

        val aniFade2 = AnimationUtils.loadAnimation(context, R.anim.slice_down)
        manzana.startAnimation(aniFade2)
        manzana.isVisible = false

        checkProgress()
        //when para settear que manzana hemos cortado ya
        when (manzana) {
            manzana1 -> {manzana1cortada=true}
            manzana2 -> {manzana2cortada=true}
            manzana3 -> {manzana3cortada=true}
            manzana4 -> {manzana4cortada=true}
            manzana5 -> {manzana5cortada=true}

        }

    }

}

