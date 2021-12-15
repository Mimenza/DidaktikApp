package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
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
class Fragment2_1_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var globalView: View
    private lateinit var button: Button
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
        val view= inflater.inflate(R.layout.fragment2_1_minijuego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf2_1siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_1ajustes)

        button.visibility = View.GONE

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_1_minijuego_to_fragment4_menu)
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
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout_Minijuego1)
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

                            viewElement.visibility = View.GONE
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
                button.visibility = View.VISIBLE
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
            Fragment2_1_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}