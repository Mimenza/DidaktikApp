package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_3_1_juego : Fragment(), DbHandler.queryResponseDone {
    private val thisJuegoId = 3
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var globalView: View
    private lateinit var button: Button
    private lateinit var preguntasLayout: LinearLayout
    var totalWidth: Int = 0
    var totalHeight: Int = 0
    var puzzleShowing: Boolean = false

    val listaImagenes = listOf(
        listOf(R.id.puzzle_pieza_o_1,R.id.puzzle_pieza_d_1),
        listOf(R.id.puzzle_pieza_o_2,R.id.puzzle_pieza_d_2),
        listOf(R.id.puzzle_pieza_o_3,R.id.puzzle_pieza_d_3),
        listOf(R.id.puzzle_pieza_o_4,R.id.puzzle_pieza_d_4),
        listOf(R.id.puzzle_pieza_o_5,R.id.puzzle_pieza_d_5),
        listOf(R.id.puzzle_pieza_o_6,R.id.puzzle_pieza_d_6),
        listOf(R.id.puzzle_pieza_o_7,R.id.puzzle_pieza_d_7),
        listOf(R.id.puzzle_pieza_o_8,R.id.puzzle_pieza_d_8),
        listOf(R.id.puzzle_pieza_o_9,R.id.puzzle_pieza_d_9),
        listOf(R.id.puzzle_pieza_o_10,R.id.puzzle_pieza_d_10),
        listOf(R.id.puzzle_pieza_o_11,R.id.puzzle_pieza_d_11),
        listOf(R.id.puzzle_pieza_o_12,R.id.puzzle_pieza_d_12),
        //listOf(R.id.puzzle_pieza_otest1,R.id.puzzle_pieza_otest2),
    )

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
        val view = inflater.inflate(R.layout.fragment1_3_1_juego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf1_3_1_siguiente)
        button.visibility = View.GONE
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_3_1_ajustes)
        val btnComprobarRespuesta: Button = globalView.findViewById(R.id.juego3_btnComprobar)
        preguntasLayout = view.findViewById(R.id.juego3_preguntas_layout)

        preguntasLayout.visibility =  View.GONE

        btnComprobarRespuesta.setOnClickListener() {
            comprobarRespuestas()
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                totalWidth = view.width
                totalHeight = view.height
                prepairPuzzleElements()
            }
        })

        button.setOnClickListener(){
            if (puzzleShowing) {
                DbHandler.userActualizarUltimoPunto(thisJuegoId)
                DbHandler().requestDbUserUpdate(this)
            }else {
                iniciarPreguntas()
            }
        }
        ajustes.setOnClickListener(){

                        (activity as Activity6_Site?)?.menuCheck()

        }
        return view
    }

    override fun responseDbUserUpdated(responde: Boolean) {
        Navigation.findNavController(globalView).navigate(R.id.action_fragment1_3_1_juego_to_fragment2_3_1_minijuego)
    }

    fun prepairPuzzleElements() {
        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = globalView.findViewById(vItemList[0])
            var vItemDestino: ImageView = globalView.findViewById(vItemList[1])
            vItemOrigen.getLayoutParams().width = vItemDestino.width
            vItemOrigen.getLayoutParams().height = vItemDestino.height
            vItemOrigen.x = ((0..totalWidth - vItemDestino.width).random()).toFloat()
            vItemOrigen.y = ((0..totalHeight - vItemDestino.height).random()).toFloat()
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            //vItemDestino.setColorFilter(Color.argb(150, 0, 80, 200))
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(matrix)
            vItemDestino.setColorFilter(filter)
            vItemDestino.setAlpha(70)
            vItemOrigen.setOnTouchListener(listener)
        }
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
                            sendToTopImagesNotFinished()
                            viewElement.setOnTouchListener(null)
                            if (puzzleCompletado()) {
                                DbHandler.userAumentarPuntuacion(5)
                                //DbHandler().requestDbUserUpdate(this)
                                button.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "Bikain!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        true
    }

    private fun iniciarPreguntas() {
        button.visibility = View.GONE
        puzzleShowing = true
        ocultarPuzzle()
        preguntasLayout.visibility = View.VISIBLE
    }

    private var respuestaPreguntasjuego2: String = "zanpantzarrak"

    private fun comprobarRespuestas() {
        val radio1: RadioButton = globalView.findViewById(R.id.pregunta1_respuesta_1)
        val radio2: RadioButton = globalView.findViewById(R.id.pregunta1_respuesta_2)
        val editTextRespuesta: EditText = globalView.findViewById(R.id.juego3_pregunta2_respuesta1)
        val btnComprobarRespuesta: Button = globalView.findViewById(R.id.juego3_btnComprobar)

        if (radio1.isChecked && editTextRespuesta.text.toString().trim().toLowerCase().equals(respuestaPreguntasjuego2)) {
            editTextRespuesta.isEnabled = false
            radio1.isEnabled = false
            radio2.isEnabled = false
            btnComprobarRespuesta.visibility = View.GONE
            button.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Zorionak, proba gainditu duzu !", Toast.LENGTH_SHORT).show()
            hideKeyboard()
        } else {
            Toast.makeText(requireContext(), "Saiatu berriro", Toast.LENGTH_SHORT).show()
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun ocultarPuzzle() {
        for (item in manzanaList!!) {
            item.origen.visibility = View.GONE
            item.objetivo.visibility = View.GONE
        }
    }

    private fun sendToTopImagesNotFinished() {
        for (item in manzanaList!!) {
            if (!item.acertado) {
                item.origen.bringToFront()
            }
        }
    }

    private fun puzzleCompletado(): Boolean {
        var finalizado: Boolean = true
        for (item in manzanaList!!) {
            if (!item.acertado) {
                finalizado = false
                break
            }
        }
        return finalizado
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
         * @return A new instance of fragment Fragment1_juego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1_3_1_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}