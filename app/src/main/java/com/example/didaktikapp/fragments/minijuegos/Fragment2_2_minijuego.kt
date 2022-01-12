package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity7_juego2_results.*
import kotlinx.coroutines.NonCancellable.cancel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_2_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var globalView: View
    private lateinit var button: Button

    private lateinit var cesta: ImageView
    private lateinit var basurero: ImageView
    private lateinit var txtAciertos: TextView
    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    val duracionJuego: Int = 60 // Duracion en segundos
    val intervaloGeneracionManzanas = 3 //Duracion en segundos
    val aciertosRequeridos: Int = 5
    var aciertosActuales: Int = 0
    var minijuegoFinalizado: Boolean = false

    var numManzanaVerde = 0
    var numManzanaRoja = 0


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
        val view= inflater.inflate(R.layout.fragment2_2_minijuego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf2_2siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_2ajustes)

        cesta = view.findViewById((R.id.juegox_cesta))
        basurero = view.findViewById((R.id.juegox_basurero))
        txtAciertos = view.findViewById((R.id.manzanasAciertos))


        button.visibility = View.GONE

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_2_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_1_minijuego_to_fragment4_menu)
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
        imgManzanaGenerada.x = ((0..globalView.width - 200).random()).toFloat()
        imgManzanaGenerada.y = ((0..globalView.height/2 - 200).random()).toFloat()
        //imgManzanaGenerada.setBackgroundColor(Color.BLUE)



        var tipoManzana = (0..1).random()

        println(" tipo "+tipoManzana +" roja "+ numManzanaRoja +" verde "+ numManzanaVerde)
        var mznGnrDestino: ImageView

        if (tipoManzana == 1) {
            if(numManzanaRoja <=4){
                imgManzanaGenerada.setImageResource(R.drawable.sagarragorria)
                mznGnrDestino = basurero
                numManzanaRoja++
            }else{
                imgManzanaGenerada.setImageResource(R.drawable.sagarraberdea)
                mznGnrDestino = cesta
                numManzanaVerde++
            }

        }
        else if(tipoManzana == 0){
            if(numManzanaVerde <=4){
                imgManzanaGenerada.setImageResource(R.drawable.sagarraberdea)
                mznGnrDestino = cesta
                numManzanaVerde++
            }else{
                imgManzanaGenerada.setImageResource(R.drawable.sagarragorria)
                mznGnrDestino = basurero
                numManzanaRoja++
            }

        }

        else  {
            mznGnrDestino = basurero
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
                        //var objetivoEncontrado: View = itemInList!!.objetivo
                        // Basurero Vars
                        val basureroLocation = IntArray(2)
                        basurero.getLocationOnScreen(basureroLocation);
                        var basureroPosX = basureroLocation[0]
                        var basureroPosY = basureroLocation[1]
                        var basureroSizeX = cesta.width
                        var basureroSizeY = cesta.height
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
                        } else if ((viewElement.x + viewElement.width/2) >= basureroPosX && (viewElement.y + viewElement.height/2) >= basureroPosY && (viewElement.x + viewElement.width/2) <= basureroPosX+cestaSizeX && (viewElement.y + viewElement.height/2) <= basureroPosY+basureroSizeY) {
                            comprobarInsercionManzana(itemInList, basurero)
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
        if (aciertosActuales >= 5) {
            button.visibility = View.VISIBLE
            minijuegoFinalizado = true
            Toast.makeText(requireContext(), "ZORIONAK !!", Toast.LENGTH_SHORT).show()
            removeManzanasListener()
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
        object: CountDownTimer((duracionJuego*1000).toLong(), (intervaloGeneracionManzanas*10).toLong()) {
            override fun onTick(millisUntilFinished: Long) {

                println(numManzanaRoja !=5 || numManzanaVerde !=5)
                if(numManzanaRoja !=5 || numManzanaVerde !=5){
                    if (!minijuegoFinalizado) {
                        generarManzana()
                    }
                }
                else{this.cancel()}

            }
            override fun onFinish() {
                //removeListenerManzanas()
                //button.visibility = View.VISIBLE
                //Actualmente queremos que no dejen de aparecer manzanas
                //FIXME Tal vez se necesite limitar el numero de manzanas activas para evitar que se llene la pantalla de ellas

                if(numManzanaRoja !=5 || numManzanaVerde !=5){
                    startTimeCounter()
                }
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
         * @return A new instance of fragment Fragment2_minijuego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2_2_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}