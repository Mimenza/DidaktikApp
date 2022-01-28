package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_3_juego : Fragment(), DbHandler.QueryResponseDone {
    private val thisJuegoId = 3

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var vistaAnimada: TranslateAnimation
    private lateinit var globalView: View
    private lateinit var button: Button
    private lateinit var buttonSaltarJuego: Button
    private lateinit var buttonRepetir: Button
    private lateinit var btnInfoJuego: ImageButton
    private lateinit var preguntasLayout: LinearLayout
    private var audio: MediaPlayer? = null
    var totalWidth: Int = 0
    var totalHeight: Int = 0
    var puzzleShowing: Boolean = false

    val listaImagenes = listOf(
        listOf(R.id.puzzle_pieza_o_1, R.id.puzzle_pieza_d_1),
        listOf(R.id.puzzle_pieza_o_2, R.id.puzzle_pieza_d_2),
        listOf(R.id.puzzle_pieza_o_3, R.id.puzzle_pieza_d_3),
        listOf(R.id.puzzle_pieza_o_4, R.id.puzzle_pieza_d_4),
        listOf(R.id.puzzle_pieza_o_5, R.id.puzzle_pieza_d_5),
        listOf(R.id.puzzle_pieza_o_6, R.id.puzzle_pieza_d_6),
        listOf(R.id.puzzle_pieza_o_7, R.id.puzzle_pieza_d_7),
        listOf(R.id.puzzle_pieza_o_8, R.id.puzzle_pieza_d_8),
        listOf(R.id.puzzle_pieza_o_9, R.id.puzzle_pieza_d_9),
        listOf(R.id.puzzle_pieza_o_10, R.id.puzzle_pieza_d_10),
        listOf(R.id.puzzle_pieza_o_11, R.id.puzzle_pieza_d_11),
        listOf(R.id.puzzle_pieza_o_12, R.id.puzzle_pieza_d_12),
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
        val view = inflater.inflate(R.layout.fragment1_3_juego, container, false)
        globalView = view
        button = view.findViewById(R.id.btnf1_3_siguiente)
        buttonSaltarJuego = view.findViewById(R.id.btn_saltarjuego3)
        buttonRepetir = view.findViewById(R.id.btnf1_3_repetir)
        button.visibility = View.GONE
        buttonRepetir.visibility = View.GONE
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_3_ajustes)
        val btnComprobarRespuesta: Button = globalView.findViewById(R.id.juego3_btnComprobar)
        preguntasLayout = view.findViewById(R.id.juego3_preguntas_layout)
        btnInfoJuego= view.findViewById((R.id.btn1_3_infojuego))
        preguntasLayout.visibility = View.GONE

        btnComprobarRespuesta.setOnClickListener() {
            comprobarRespuestas()
        }
        btnInfoJuego.setOnClickListener(){
            showDialogInfo()
        }


        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                totalWidth = view.width
                totalHeight = view.height

            }
        })

        button.setOnClickListener() {
            if (puzzleShowing) {
                DbHandler.userActualizarUltimoPunto(thisJuegoId)
                DbHandler().requestDbUserUpdate(this)
            } else {
                iniciarPreguntas()
            }
        }

        buttonSaltarJuego.setOnClickListener() {
            Navigation.findNavController(globalView)
                .navigate(R.id.action_fragment1_3_juego_to_fragment2_3_minijuego)
        }



        buttonRepetir.setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_fragment1_3_juego_self)
        }


        //Animacion manzana al iniciar el juego
        starAnimationfun(view)

        //Typewriter juego 1 tutorial
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                typewriter(view)
            }
        }, 2000)

        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego3audiotutorial)
                audio?.start()

                audio?.setOnCompletionListener {
                    ajustes.setOnClickListener() {
                        //activamos el listener para los ajustes
                        (activity as Activity6_Site?)?.menuCheck()

                    }
                    prepairPuzzleElements()
                    showPhotos()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (getView() != null) {
                            //Llama a la funcion para la animacion de salida cuando el audio se termina
                            exitAnimationfun(view)
                        }
                    }, 1000)
                }
            }
        }


        return view

    }

    fun showDialogInfo(){

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info)
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

            0->  {texto=resources.getString(R.string.ayudajuego1)}
            1->  {texto=resources.getString(R.string.ayudajuego2)}
            2->  {texto=resources.getString(R.string.ayudajuego3)}
            3->  {texto=resources.getString(R.string.ayudajuego4)}
            4->  {texto=resources.getString(R.string.ayudajuego5)}
            5->  {texto=resources.getString(R.string.ayudajuego6)}

        }
        println(texto)
        if (textInfo!=null){

            textInfo.setText(texto)
        }
    }
    fun showPhotos() {
        //recogemos las fotos
        val foto1: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_1)
        val foto2: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_2)
        val foto3: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_3)
        val foto4: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_4)
        val foto5: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_5)
        val foto6: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_6)
        val foto7: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_7)
        val foto8: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_8)
        val foto9: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_9)
        val foto10: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_10)
        val foto11: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_11)
        val foto12: ImageView = requireView().findViewById(R.id.puzzle_pieza_o_12)

        //enseñamos las fotos
        foto1.isVisible = true
        foto2.isVisible = true
        foto3.isVisible = true
        foto4.isVisible = true
        foto5.isVisible = true
        foto6.isVisible = true
        foto7.isVisible = true
        foto8.isVisible = true
        foto9.isVisible = true
        foto10.isVisible = true
        foto11.isVisible = true
        foto12.isVisible = true

    }

    override fun responseDbUserUpdated(responde: Boolean) {
        Navigation.findNavController(globalView)
            .navigate(R.id.action_fragment1_3_juego_to_fragment2_3_minijuego)
    }

    fun prepairPuzzleElements() {
        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = globalView.findViewById(vItemList[0])
            var vItemDestino: ImageView = globalView.findViewById(vItemList[1])
            vItemOrigen.getLayoutParams().width = vItemDestino.width
            vItemOrigen.getLayoutParams().height = vItemDestino.height
            vItemOrigen.x = ((0..totalWidth - vItemDestino.width).random()).toFloat()
            vItemOrigen.y = ((0..totalHeight - vItemDestino.height).random()).toFloat()
            manzanaList!!.add(DragnDropImage(vItemOrigen, vItemDestino))
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
                when (action) {
                    MotionEvent.ACTION_MOVE -> {
                        viewElement.x = motionEvent.rawX - viewElement.width / 2
                        viewElement.y = motionEvent.rawY - viewElement.height / 2
                    }
                    MotionEvent.ACTION_UP -> {
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
                            itemInList.acertado = true
                            sendToTopImagesNotFinished()
                            viewElement.setOnTouchListener(null)
                            if (puzzleCompletado()) {
                                DbHandler.userAumentarPuntuacion(5)
                                //DbHandler().requestDbUserUpdate(this)
                                button.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "Bikain!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
        true
    }

    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_1tutorialjuego1) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.juego3audiotutorialtxt))
        typeWriterView.setDelay(65)
    }

    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
            }
        }, 2000)
    }

    private fun exitAnimationfun(view: View) {
        val upelioAnimado = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //Animacion upelio salido
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //VistaAnimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_1_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //Animacion fondo gris
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                val txtAnimacion = view.findViewById(R.id.txtv1_1fondogris) as TextView
                val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                txtAnimacion.startAnimation(aniFade)
                txtv1_1tutorialjuego1.startAnimation(aniFade)
                txtv1_1tutorialjuego1.isVisible = false
                txtAnimacion.isVisible = false
            }
        }, 1000)
    }

    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_1_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
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

        if (radio1.isChecked && editTextRespuesta.text.toString().trim().toLowerCase()
                .equals(respuestaPreguntasjuego2)
        ) {
            editTextRespuesta.isEnabled = false
            radio1.isEnabled = false
            radio2.isEnabled = false
            btnComprobarRespuesta.visibility = View.GONE
            button.visibility = View.VISIBLE
            buttonRepetir.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Zorionak, proba gainditu duzu !", Toast.LENGTH_SHORT)
                .show()
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
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
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

    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        audio?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        audio?.start()
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
            Fragment1_3_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}