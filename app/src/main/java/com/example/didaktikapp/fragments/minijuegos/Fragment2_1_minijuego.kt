package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import com.example.didaktikapp.activities.DbHandler


class Fragment2_1_minijuego : Fragment(), DbHandler.QueryResponseDone {
    private lateinit var globalView: View
    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var cesta: ImageView
    private lateinit var basurero: ImageView
    private lateinit var txtAciertos: TextView
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button
    private lateinit var btninfominijuego: ImageButton



    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    val duracionJuego: Int = 60 // Duracion en segundos
    val intervaloGeneracionManzanas = 3 //Duracion en segundos
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
                    val intent = Intent (it, Activity5_Mapa::class.java)
                    it.startActivity(intent)
                }
        }


        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }


        iniciarJuegoRecogerManzanas()
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
    private fun comprobarInsercionManzana(item: DragnDropImage, objetivoInsertado: ImageView) {
        //item.acertado = true
        if (item.objetivo == objetivoInsertado) {
            aciertosActuales++
            txtAciertos.text = aciertosActuales.toString() +"/10"
        }
        comprobarJuegoFinalizado()
    }

    private fun comprobarJuegoFinalizado() {
        if (aciertosActuales >= 10) {
            DbHandler.userAumentarPuntuacion(5)
            DbHandler().requestDbUserUpdate(this)
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
        object: CountDownTimer((duracionJuego*1000).toLong(), (intervaloGeneracionManzanas*350).toLong()) {
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
