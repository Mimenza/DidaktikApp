package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import android.widget.TextView
import com.example.didaktikapp.Model.CustomAudioTest.audio
import com.example.didaktikapp.activities.DbHandler


class Fragment2_1_minijuego : Fragment(), DbHandler.QueryResponseDone {
    private lateinit var globalView: View
    private lateinit var vistaAnimada:TranslateAnimation    //variable para la animacion
    private lateinit var cesta: ImageView                   //cesta para las manzanas
    private lateinit var basurero: ImageView                //basurero para la manzanas
    private lateinit var txtAciertos: TextView              //texto con numero de aciertos
    private lateinit var txtcartel: TextView                //texto cartel final
    private lateinit var cartel: ImageView                  //cartel final
    private lateinit var btnsiguiente:Button                //boton siguiente juego
    private lateinit var btnrepetir:Button                  //boton repetir juego
    private lateinit var btninfominijuego: ImageButton      //boton informacion juego


    var manzanaList: MutableList<DragnDropImage>? = mutableListOf() //
    val duracionJuego: Int = 60                                     // Duracion en segundos del juego
    val intervaloGeneracionManzanas = 3                             //Duracion en segundos generar manzanas
    var aciertosActuales: Int = 0                                   // numero de aciertos
    var minijuegoFinalizado: Boolean = false                        // variable de juego terminado
    var manzanasCounter = 0                                         // numero de manzanas generadas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment2_1_minijuego, container, false)
        globalView = view


        cesta = view.findViewById((R.id.juegox_cesta))
        basurero = view.findViewById((R.id.juegox_basurero))
        txtAciertos = view.findViewById((R.id.manzanasAciertos))
        cartel= view.findViewById((R.id.imgv2_1cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_1carteltexto))
        btninfominijuego= view.findViewById((R.id.btn2_1_infominijuego))



        btnrepetir = view.findViewById(R.id.btn2_1_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_1_siguiente)

        val mapa: ImageButton = view.findViewById(R.id.btnf2_1_mapa)
        mapa.setOnClickListener {
                activity?.let{
                    getActivity()?.finish()
                }
        }

        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }


        iniciarJuegoRecogerManzanas()
        return view
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
     * Inicia el minijuego llamando a una funcion
     *
     */
    fun iniciarJuegoRecogerManzanas() {
        startTimeCounter()
    }

    /**
     * Funcion que genera manzanas de manera aleatoria en el view(Rojas y verdes), las manzanzas se van añadiendo
     * a una mutable list, despues se les añade un setontouch listener
     *
     */
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
                        }else{
                            viewElement.x = ((50..globalView.width - 250).random()).toFloat()
                            viewElement.y = ((450..globalView.height - 600).random()).toFloat()
                        }
                    }
                }
            }
        }
        true
    }

    @SuppressLint("SetTextI18n")
            /**
             * Comprobamos si hemos dejado la manzana en el cesto o no
             *
             * @param item   manzana que estamos arrastrando en el momento
             * @param objetivoInsertado destino de la manzana que estamos arrastrando
             */
    private fun comprobarInsercionManzana(item: DragnDropImage, objetivoInsertado: ImageView) {
        //item.acertado = true
        if (item.objetivo == objetivoInsertado) {
            aciertosActuales++
            txtAciertos.text = aciertosActuales.toString() +"/10"
        }
        comprobarJuegoFinalizado()
    }

    /**
     * Comprobamos si hemos terminado de meter todas las manzanas
     *
     */
     private fun comprobarJuegoFinalizado() {
        if (aciertosActuales >= 10) {
            DbHandler.userAumentarPuntuacion(5)
            DbHandler().requestDbUserUpdate(this)
            //Diseñar cartel madera
            starAnimationfun()
            minijuegoFinalizado = true
            audio?.stop()
            audio = MediaPlayer.create(context, R.raw.ongiaudiogeneral)
            audio?.start()
            removeManzanasListener()

        }
    }

    /**
     * Animacion de cierre del minijuego, generamos un cartel con un texto y dos botones
     *
     */
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
            getActivity()?.finish()
        }
        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_1_minijuego_self)
        }

    }

    /**
     * TODO
     *
     * @param view
     * @return
     */
    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    /**
     * Cuando hemos dejado la manzana el el cesto no la podemos arrastrar mas
     *
     */
    private fun removeManzanasListener() {
        for (item in manzanaList!!) {
            item.origen.setOnTouchListener(null)
        }
    }

    /**
     * Generamos manzanas cada X tiempo siempre que no se hayan generado ya 10 manzanas
     *
     */
    fun startTimeCounter() {
        object: CountDownTimer((duracionJuego*1000).toLong(), (intervaloGeneracionManzanas*350).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                if (!minijuegoFinalizado) {
                    if(manzanasCounter <= 9){
                        if (getView() != null) {
                            generarManzana()
                            manzanasCounter++
                        }
                    }
                }
            }
            override fun onFinish() {
                startTimeCounter()
            }
        }.start()
    }
}
