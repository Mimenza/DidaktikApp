package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.Model.DragnDropImageLevel
import com.example.didaktikapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_6_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var acierto: Int = 0
    private lateinit var globalView: View

    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var contadorCartel: TextView
    private lateinit var progressBar:ProgressBar

    private var tiempoCompletarComprobacion = 1000

//    val listaImagenes = listOf(
//        listOf(R.id.imgV2_3CleanApple1,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple2,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple3,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple4,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple5,R.id.imgv2_3cesta),
//    )

    private var manzanaList: MutableList<DragnDropImageLevel> = mutableListOf()

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
        val view = inflater.inflate(R.layout.fragment2_6_minijuego, container, false)
        globalView = view

        val ajustes: ImageButton = view.findViewById(R.id.btnf2_6ajustes)
        cartel= view.findViewById((R.id.imgv2_6cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_6carteltexto))
        contadorCartel= view.findViewById((R.id.txtv2_6contador))
        progressBar= view.findViewById((R.id.progressBar_minijuego6))

        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_6_minijuego_to_fragment4_menu)
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                generarVasonTarget()
            }
        })



        return view
    }

    private var timerActive: Boolean? = null
    private var updateHandler: Handler? = null

    private fun itemActionCallback(pItem: DragnDropImageLevel) {
        //pItem.acertado = true
        //pItem.origen.setImageResource(R.drawable.vaso1)
        pItem.nivel = pItem.nivel - 1
        when(pItem.nivel) {
            4 -> {
                pItem.origen.setImageResource(R.drawable.vaso5)
            }
            3 -> {
                pItem.origen.setImageResource(R.drawable.vaso4)
            }
            2 -> {
                pItem.origen.setImageResource(R.drawable.vaso3)
            }
            1 -> {
                pItem.origen.setImageResource(R.drawable.vaso2)
            }
            else -> {
                pItem.origen.setImageResource(R.drawable.vaso1)
                pItem.acertado = true
                vasoVaciado()
                pItem.origen.setOnTouchListener(null)
                pItem.origen.visibility = View.GONE
                pItem.corcho.setOnTouchListener(listener)
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImageLevel? = null
        itemInList = findItemByOrigen(viewElement)
        if (null == itemInList) {
            itemInList = findItemByCorcho(viewElement)
        }

       // if (itemInList != null && !itemInList.acertado) {
            viewElement.bringToFront()

            var objetivoEncontrado: View = itemInList!!.objetivo
            val location = IntArray(2)
            objetivoEncontrado.getLocationOnScreen(location);
            var posX = location[0]
            var posY = location[1]
            var sizeX = objetivoEncontrado.width
            var sizeY = objetivoEncontrado.height

            val action = motionEvent.action
            when(action) {
                MotionEvent.ACTION_MOVE -> {
                    viewElement.x = motionEvent.rawX - viewElement.width/2
                    viewElement.y = motionEvent.rawY - viewElement.height/2

                    if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {

                        if (!itemInList.acertado) {
                            if (updateHandler == null && itemInList.nivel > 0) {
                                updateHandler = Handler()
                                updateHandler?.postDelayed({
                                    itemActionCallback(itemInList)
                                    updateHandler?.removeCallbacksAndMessages(null)
                                    updateHandler = null
                                }, 1000)
                            }
                        } else {

                        }
                    } else {
                        if (!itemInList.acertado) {
                            if (updateHandler != null) {
                                updateHandler?.removeCallbacksAndMessages(null)
                                updateHandler = null
                            }
                        }

                    }


                        /*
                        //Aqui comprobamos si la manzana no esta limpia.
                        if (!itemInList.acertado) {
                            val aguaLocation = IntArray(2)
                            agua.getLocationOnScreen(aguaLocation);
                            //Posicion/tamaño del agua
                            var aguaPosX = aguaLocation[0]
                            var aguaPosY = aguaLocation[1]
                            var aguaSizeX = agua.width
                            var aguaSizeY = agua.height

                            // Si la imagen pasa por el agua, la manzana cambiara a limpia
                            if ( (viewElement.x + viewElement.width/2) >= aguaPosX && (viewElement.y + viewElement.height/2) >= aguaPosY && (viewElement.x + viewElement.width/2) <= aguaPosX+aguaSizeX && (viewElement.y + viewElement.height/2) <= aguaPosY+aguaSizeY) {

                                if (cleanManzanaTimer == null) {
                                    cleanManzanaTimer = true
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        if (getView() != null && cleanManzanaTimer != null) {
                                            if ( (viewElement.x + viewElement.width/2) >= aguaPosX && (viewElement.y + viewElement.height/2) >= aguaPosY && (viewElement.x + viewElement.width/2) <= aguaPosX+aguaSizeX && (viewElement.y + viewElement.height/2) <= aguaPosY+aguaSizeY) {
                                                itemInList.acertado = true
                                                itemInList.origen.setImageResource(R.drawable.sagarraberdea)
                                                cleanManzanaTimer = null
                                            }
                                        }
                                    }, tiempoCompletarComprobacion.toLong())
                                }
                            } else {
                                if (cleanManzanaTimer != null) {
                                    cleanManzanaTimer = null
                                }
                            }
                        }

                         */
                }
                MotionEvent.ACTION_UP -> {

                    if (itemInList.acertado) {
                        if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {
                            viewElement.setOnTouchListener(null)
                            viewElement.visibility = View.GONE
                            itemInList.objetivo.visibility = View.GONE
                            botellaLlena()
                        }
                    }

                    if (updateHandler != null) {
                        updateHandler?.removeCallbacksAndMessages(null)
                        updateHandler = null
                    }
                    /*
                    if (itemInList.acertado) {
                        viewElement.x = motionEvent.rawX - viewElement.width / 2
                        viewElement.y = motionEvent.rawY - viewElement.height / 2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        val location = IntArray(2)
                        objetivoEncontrado.getLocationOnScreen(location);
                        var posX = location[0]
                        var posY = location[1]
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height
                        if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {
                            viewElement.x = posX.toFloat()
                            viewElement.y = posY.toFloat()
                            viewElement.setOnTouchListener(null)
                        }
                    }
                    cleanManzanaTimer = null

                     */
                }
            }
        //}
        true
    }

    private fun generarVasonTarget() {
        // Generamos el vaso
        var imgVasoLleno: ImageView = generateDinamycImageElement(R.drawable.vaso6, (globalView.width - 800), (globalView.height/2+150), 250,250)

        // Generamos la Botella Vacia
        var imgBotellaVacia: ImageView = generateDinamycImageElement(R.drawable.botellallena, (globalView.width/2 - 200/2), (globalView.height/2 - 200/2), 500, 500)

        // Generamos el corcho de la botella
        var imgCorchoBotella: ImageView = generateDinamycImageElement(R.drawable.corchotest, (globalView.width - 400), (globalView.height - 800), 200, 200)

        manzanaList.add(DragnDropImageLevel(imgVasoLleno,imgBotellaVacia,imgCorchoBotella))
        imgVasoLleno.setOnTouchListener(listener)
    }

    //Metodo para generar Una imagen dentro del layout de manera dinamica.
    private fun generateDinamycImageElement(resourceInt: Int, posX: Int, posY: Int, sizeX: Int, sizeY: Int): ImageView  {
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout_Minijuego6)
        var imgElement: ImageView = ImageView(requireContext())
        constraintLayoutFound.addView(imgElement)
        imgElement.layoutParams.height = sizeX
        imgElement.layoutParams.width = sizeY
        imgElement.x = posX.toFloat()
        imgElement.y = posY.toFloat()
        imgElement.setImageResource(resourceInt)
        return imgElement
    }

    private fun vasoVaciado() {
        val txtVasosVacios: TextView = globalView.findViewById(R.id.minijuego6_txtvasosvacios)

        txtVasosVacios.text = "x"+(txtVasosVacios.text.toString().replace("x","").toInt() + 1).toString()
    }

    private fun botellaLlena() {
        val txtBotellasLlenas: TextView = globalView.findViewById(R.id.minijuego6_txtbotellasllenas)
        txtBotellasLlenas.text = "x"+(txtBotellasLlenas.text.toString().replace("x","").toInt() + 1).toString()
        generarVasonTarget()
        acierto++
        checkProgress()
    }

    private fun checkProgress(){

        if (acierto==5){
            starAnimationfun()
        }
    }

    fun starAnimationfun(){

        //Diseñar cartel madera
        contadorCartel.visibility=View.VISIBLE
        cartel.visibility=View.VISIBLE
        txtcartel.visibility=View.VISIBLE



        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 1000

        cartel.startAnimation(vistaAnimada)
        contadorCartel.startAnimation(vistaAnimada)
        txtcartel.startAnimation(vistaAnimada)

        object : CountDownTimer(5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                contadorCartel.text= (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {

                progressBar.isVisible=true
                contadorCartel.isVisible=false
                Handler().postDelayed({

                    view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment2_6_minijuego_to_fragment4_menu) }
                }, 2000)

            }
        }.start()

    }






    private fun findItemByOrigen(view: View): DragnDropImageLevel? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    private fun findItemByCorcho(view: View): DragnDropImageLevel? {
        for (item in manzanaList!!) {
            if (item.corcho == view) {
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
         * @return A new instance of fragment Fragment2_minijuego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2_6_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}