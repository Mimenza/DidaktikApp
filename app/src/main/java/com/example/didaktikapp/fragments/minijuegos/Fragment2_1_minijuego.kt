package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site

class Fragment2_1_minijuego : Fragment() {
    private lateinit var globalView: View
    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var cesta: ImageView
    private lateinit var basurero: ImageView
    private lateinit var txtAciertos: TextView
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button

    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    val duracionJuego: Int = 60 // Duracion en segundos
    val intervaloGeneracionManzanas = 3 //Duracion en segundos
    val aciertosRequeridos: Int = 5
    var aciertosActuales: Int = 0
    var minijuegoFinalizado: Boolean = false
    var manzanasCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment2_1_minijuego, container, false)
        globalView = view

        val ajustes: ImageButton = view.findViewById(R.id.btnf2_1ajustes)

        cesta = view.findViewById((R.id.juegox_cesta))
        basurero = view.findViewById((R.id.juegox_basurero))
        txtAciertos = view.findViewById((R.id.manzanasAciertos))
        cartel= view.findViewById((R.id.imgv2_1cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_1carteltexto))


        btnrepetir = view.findViewById(R.id.btn2_1_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_1_siguiente)

        ajustes.setOnClickListener(){
            (activity as Activity6_Site?)?.menuCheck()
        }
        iniciarJuegoRecogerManzanas()
        return view
    }

    fun iniciarJuegoRecogerManzanas() {
        startTimeCounter()
    }

    fun generarManzana() {
        var imgManzanaGenerada: ImageView = ImageView(requireContext())
        //newView = ImageView(requireContext())
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout_Minijuego2)
        constraintLayoutFound.addView(imgManzanaGenerada)
        imgManzanaGenerada.layoutParams.height = 200
        imgManzanaGenerada.layoutParams.width = 200
        //  newView.x = 200F
        // newView.y = 200F
        imgManzanaGenerada.x = ((50..globalView.width - 250).random()).toFloat()
        imgManzanaGenerada.y = ((450..globalView.height - 600).random()).toFloat()
        //imgManzanaGenerada.setBackgroundColor(Color.BLUE)



        var tipoManzana = (0..1).random()
        var mznGnrDestino: ImageView
        if (tipoManzana == 1) {
            imgManzanaGenerada.setImageResource(R.drawable.manzanaroja)
            mznGnrDestino = cesta
        } else {
            imgManzanaGenerada.setImageResource(R.drawable.sagarraberdea)
            mznGnrDestino = cesta
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

                        // Cesta Vars
                        val cestaLocation = IntArray(2)
                        cesta.getLocationOnScreen(cestaLocation);
                        var cestaPosX = cestaLocation[0]
                        var cestaPosY = cestaLocation[1]
                        var cestaSizeX = cesta.width
                        var cestaSizeY = cesta.height

                        if ( (viewElement.x + viewElement.width/2) >= cestaPosX && (viewElement.y + viewElement.height/2) >= cestaPosY && (viewElement.x + viewElement.width/2) <= cestaPosX+cestaSizeX && (viewElement.y + viewElement.height/2) <= cestaPosY+cestaSizeY) {
                            comprobarInsercionManzana(itemInList, cesta)
                            itemInList.acertado = true

                            viewElement.visibility = View.GONE
                            viewElement.setOnTouchListener(null)
                        }
                    }
                }
            }
        }
        true
    }

    private fun comprobarInsercionManzana(item: DragnDropImage, objetivoInsertado: ImageView) {
        //item.acertado = true
        if (item.objetivo == objetivoInsertado) {
            aciertosActuales++
            txtAciertos.text = aciertosActuales.toString()
        }
        comprobarJuegoFinalizado()
    }

    private fun comprobarJuegoFinalizado() {
        if (aciertosActuales >= 10) {

            //Diseñar cartel madera
            starAnimationfun()
            minijuegoFinalizado = true
            Toast.makeText(requireContext(), "ZORIONAK !!", Toast.LENGTH_SHORT).show()
            removeManzanasListener()
        }
    }

    private fun starAnimationfun(){
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
            val i = Intent(activity, Activity5_Mapa::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }
        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_1_minijuego_self)
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

    private fun removeManzanasListener() {
        for (item in manzanaList!!) {
            item.origen.setOnTouchListener(null)
        }
    }

    //fun startTimeCounter(view: View, timeInSeconds: Int) {
    fun startTimeCounter() {
        object: CountDownTimer((duracionJuego*1000).toLong(), (intervaloGeneracionManzanas*750).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                if (!minijuegoFinalizado) {
                    if(manzanasCounter <= 9){
                        generarManzana()
                        manzanasCounter++
                    }
                }
            }
            override fun onFinish() {
                //removeListenerManzanas()
                //button.visibility = View.VISIBLE
                //Actualmente queremos que no dejen de aparecer manzanas
                startTimeCounter()
            }
        }.start()
    }
}
