package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var agua:ImageView

var manzanaList: MutableList<DragnDropImage>? = mutableListOf()

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_3_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var aciertosActuales: Int = 0
    private lateinit var globalView: View

    private var suciedadOrigen: IntArray? = null
    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button

    val listaImagenes = listOf(
        listOf(R.id.imgV2_suciedad1,R.id.minijuego3_basurero),
        listOf(R.id.imgV2_suciedad2,R.id.minijuego3_basurero),
        listOf(R.id.imgV2_suciedad3,R.id.minijuego3_basurero),
        listOf(R.id.imgV2_suciedad4,R.id.minijuego3_basurero),
        listOf(R.id.imgV2_suciedad5,R.id.minijuego3_basurero),
    )
    private lateinit var aciertosTxt:TextView

    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()

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
        val view= inflater.inflate(R.layout.fragment2_3_minijuego, container, false)
        globalView = view

        val ajustes: ImageButton = view.findViewById(R.id.btnf2_3_1ajustes)
        cartel= view.findViewById((R.id.imgv2_3cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_3carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_3_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_3_siguiente)


        ajustes.setOnClickListener(){
            (activity as Activity6_Site?)?.menuCheck()
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                prepairDirts()
            }
        })

        return view
    }
    fun prepairDirts() {
        aciertosActuales = 0
        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = globalView.findViewById(vItemList[0])
            var vItemDestino: ImageView = globalView.findViewById(vItemList[1])
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            vItemOrigen.setOnTouchListener(listener)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)
        if (itemInList != null) {
            if (null == suciedadOrigen) {
                suciedadOrigen = IntArray(2)
                itemInList.origen.getLocationOnScreen(suciedadOrigen);
                //Posicion/tamaño del agua
                var suciedadOrigenX = suciedadOrigen!![0]
                var suciedadOrigenY = suciedadOrigen!![1]
                println("******** SUCIEDAD ORIGEN: " + suciedadOrigenX + " / " + suciedadOrigenY)
            }
            //if (!itemInList.acertado) {
                viewElement.bringToFront()
                val action = motionEvent.action
                when(action) {
                    MotionEvent.ACTION_MOVE -> {
                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                    }
                MotionEvent.ACTION_UP -> {
                    viewElement.x = motionEvent.rawX - viewElement.width / 2
                    viewElement.y = motionEvent.rawY - viewElement.height / 2
                    var objetivoEncontrado: View = itemInList!!.objetivo
                    val location = IntArray(2)
                    objetivoEncontrado.getLocationOnScreen(location)
                    var posX = location[0]
                    var posY = location[1]
                    var sizeX = objetivoEncontrado.width
                    var sizeY = objetivoEncontrado.height
                    if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {
                        viewElement.x = posX.toFloat()
                        viewElement.y = posY.toFloat()
                        viewElement.setOnTouchListener(null)
                        itemInList.acertado = true
                        viewElement.isVisible = false
                        manzanaLimpia()
                        checkJuegoFinalizado()
                    } else {
                        //println("******** SUCIEDAD DEFAULT: " +  suciedadOrigenX + " / " +suciedadOrigenY)
                        viewElement.x = suciedadOrigen!![0].toFloat()
                        viewElement.y = suciedadOrigen!![1].toFloat()

                    }
                    suciedadOrigen = null
                }
            }
        }
        true
    }

    private fun manzanaLimpia() {
        aciertosActuales++
        val txtPuntuacion: TextView = globalView.findViewById(R.id.manzanasAciertos2)
        txtPuntuacion.text = aciertosActuales.toString()
    }

    private fun checkJuegoFinalizado() {
        if (aciertosActuales >= 5) {
            starAnimationfun()
        }
    }
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
            val i = Intent(activity, Activity5_Mapa::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }

        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_3_minijuego_self)
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
            Fragment2_3_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}