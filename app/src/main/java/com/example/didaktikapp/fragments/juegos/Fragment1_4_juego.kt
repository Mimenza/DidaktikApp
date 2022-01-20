package com.example.didaktikapp.fragments.juegos
import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity6_Site
import java.util.*
import kotlin.collections.ArrayList
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.TextView
import android.media.MediaPlayer
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import com.example.didaktikapp.Model.clone
import kotlinx.android.synthetic.main.fragment1_4_juego.*
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
class Fragment1_4_juego : Fragment() {
    private val thisJuegoId = 4
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var layout: ConstraintLayout

    private lateinit var constraintFila1: View

    private var myLineTest: CustomLine? = null

    private var testWidth: Int? = null
    private var testHeight: Int? = null

    private var nFilas: Int = 13
    private var nCols: Int = 13

    private lateinit var globalView: View
    private lateinit var matrizMain: LinearLayout
    private lateinit var constraintMain: ConstraintLayout

    private var letterWeight = 1/nCols
    private var audio: MediaPlayer? = null
    private lateinit var vistaAnimada:TranslateAnimation

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
        val view = inflater.inflate(R.layout.fragment1_4_juego, container, false)
        globalView = view
        matrizMain = view.findViewById(R.id.matrizprincipal)
        constraintMain = view.findViewById(R.id.juego4_constraintMain)
        val button: Button = view.findViewById(R.id.btnf1_4_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_4_ajustes)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_4_juego_to_fragment2_4_minijuego)
        }
        ajustes.setOnClickListener(){
                (activity as Activity6_Site?)?.menuCheck()
        }



        //Typewriter juego 4 tutorial
       Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                typewriter(view)
            }
        }, 2000)




        //Animacion manzana al iniciar el juego
       starAnimationfun(view)


        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego4audiotutorial)
                audio?.start()

                audio?.setOnCompletionListener {

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (getView() != null) {
                            //Llama a la funcion para la animacion de salida cuando el audio se termina
                            exitAnimationfun(view)
                        }
                    }, 1000)
                }
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)


                //constraintFila1 = view.findViewById(R.id.fila1)
                //val childListFila1: ViewGroup = constraintFila1 as ViewGroup
                //println("******** CHILD COUNT: " + childListFila1.childCount)
                prepararJuego()
            }
        })

        return view
    }

    private var letras = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ"
    private var letraComodin = "•"
    private var palabraFormada: String = ""
    private var primeraLetraSeleccionada: TextView? = null
    private var drawingLine: CustomLine? = null
    private var letraPosVector: ArrayList<Int>? = null

    var letterList = arrayListOf<ArrayList<String>>(
//        arrayListOf("A","B","C","D","E"),
    )

    var letterObjectList = arrayListOf<ArrayList<TextView>>(
//        arrayListOf("A","B","C","D","E"),
    )


    private var arrayPlabrasCercanas = arrayListOf<ArrayList<Any>>(
        //arrayListOf(1,arrayListOf<Int>())
    )

    private var moveSelected: Any? = null
    private var ultimaPosFila: Int? = null
    private var ultimaPosCol: Int? = null
    private var drawStartX: Float? = null
    private var drawStartY: Float? = null


    private var palabrasPintadas = arrayListOf<String>()

    //private var palabras = arrayListOf<String>("TXISTULARIAK","SAGARDANTZA","TXALAPARTA","TRIKITIXA","ERAKUSKETA",
    //    "SALMENTA","DASTATZEA","BILKETA","GARBTIZEA")

    private var palabras = arrayListOf<String>("SAGARDANTZA", "SALMENTA", "TRIKITIXA", "TXALAPARTA", "GARBITZEA", "DASTATZEA")

    private var palabrasEncontradas = arrayListOf<String>()

    private var filasHuecosLibres = arrayListOf<Int>()
    private var colsHuecosLibres = arrayListOf<Int>()

    private fun prepararJuego() {
        //Primero generamos la matriz limpia
        var letrasAmount = 0
        for (i in 0 until nFilas) {
            var filaLetterList = arrayListOf<String>()
            letterList.add(filaLetterList)
            for (k in 0 until nCols) {
                filaLetterList.add(letraComodin)
                letrasAmount++
            }
        }
        //println("************ LETRAS AMOUNT: " + letrasAmount)
        //Log.d("this is my array", "arr: " + letterList);
        //Seleccionamos una palabra aleatoria de la lista
        //seleccionarPalabraAleatoria()
        //convertComodinToRandomLetters()
        escribirPalabrasModoSimple()
        // La pintamos sobre la sopa
        pintarSopa()
    }

    private fun escribirPalabrasModoSimple() {
        //Palabras Horizontales
        //#SAGARDANTZA
        letterList[0][2] = "S"
        letterList[0][3] = "A"
        letterList[0][4] = "G"
        letterList[0][5] = "A"
        letterList[0][6] = "R"
        letterList[0][7] = "D"
        letterList[0][8] = "A"
        letterList[0][9] = "N"
        letterList[0][10] = "T"
        letterList[0][11] = "Z"
        letterList[0][12] = "A"

        //SALMENTA
        letterList[11][3] = "S"
        letterList[11][4] = "A"
        letterList[11][5] = "L"
        letterList[11][6] = "M"
        letterList[11][7] = "E"
        letterList[11][8] = "N"
        letterList[11][9] = "T"
        letterList[11][10] = "A"

        //TRIKITIXA
        letterList[12][3] = "A"
        letterList[12][4] = "X"
        letterList[12][5] = "I"
        letterList[12][6] = "T"
        letterList[12][7] = "I"
        letterList[12][8] = "K"
        letterList[12][9] = "I"
        letterList[12][10] = "R"
        letterList[12][11] = "T"

        //palabras verticales
        letterList[3][1] = "T"
        letterList[4][1] = "X"
        letterList[5][1] = "A"
        letterList[6][1] = "L"
        letterList[7][1] = "A"
        letterList[8][1] = "P"
        letterList[9][1] = "A"
        letterList[10][1] = "R"
        letterList[11][1] = "T"
        letterList[12][1] = "A"

        letterList[2][11] = "G"
        letterList[3][11] = "A"
        letterList[4][11] = "R"
        letterList[5][11] = "B"
        letterList[6][11] = "I"
        letterList[7][11] = "T"
        letterList[8][11] = "Z"
        letterList[9][11] = "E"
        letterList[10][11] = "A"

        letterList[1][12] = "D"
        letterList[2][12] = "A"
        letterList[3][12] = "S"
        letterList[4][12] = "T"
        letterList[5][12] = "A"
        letterList[6][12] = "T"
        letterList[7][12] = "Z"
        letterList[8][12] = "E"
        letterList[9][12] = "A"


        //convert0ToRandomLetters()
        //#SALMENTZA
        //for (i in 3 until letterList[11].size-1) {
         //   letterList[11][i] = "SALMENTZA".get(i).toString()
        //}
    }

    private fun convertComodinToRandomLetters() {
        for (i in 0 until letterList.size) {
            for (k in 0 until letterList[i].size) {
                if (letterList[i][k].equals(letraComodin)) {
                    val letrasLength = letras.length
                    val randomLetter = (0 until letrasLength-1).random()
                    letterList[i][k] = letras.substring(randomLetter,randomLetter+1)
                }
            }
        }
    }

    private var palabraPreparada: String = ""

    private fun pintarSopa() {
        /*
        val filaRandom = (0 until filasHuecosLibres.size-1).random()
        for ((i, value) in letterList[filaRandom].withIndex()) {
            if (i <= palabraPreparada.length-1 ) {
                letterList[filaRandom][i] = palabraPreparada.get(i).toString()
            } else {
                //break
            }
        }

         */

        //PINTAR
        for (i in 0 until letterList.size) {
            var filaLetterList = arrayListOf<String>()
            var filaLetterElementList = arrayListOf<TextView>()
            //letterList.add(filaLetterList)
            letterObjectList.add(filaLetterElementList)
            var nuevaFila = LinearLayout(requireContext())
            val paramsLinear: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            paramsLinear.weight = 13.toFloat()
            nuevaFila.setLayoutParams(paramsLinear)
            nuevaFila.setOrientation(LinearLayout.HORIZONTAL)
            matrizMain.addView(nuevaFila)

            for (k in 0 until letterList[i].size) {
                val letrasLength = letras.length
                val randomLetter = (0 until letrasLength-1).random()
                var letraElement = TextView(requireContext())
                filaLetterElementList.add(letraElement)
                letraElement.id = k+(i*nCols)
                //println("********** GENERATED ID: " + letraElement.id)
                //letraElement.text = letras.substring(randomLetter,randomLetter+1)
                letraElement.text = letterList[i][k]
                //filaLetterList.add("0")
                letraElement.textSize = 25.toFloat()
                letraElement.width = nuevaFila.width/nCols
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                params.weight = 1.toFloat()
                params.setMargins(10,0,10,30)
                params.gravity = Gravity.CENTER
                letraElement.setLayoutParams(params)
                nuevaFila.addView(letraElement)
                letraElement.setOnTouchListener(listener)
                val rnd = Random()
                val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                //letraElement.setBackgroundColor(color)
                letraElement.gravity = Gravity.CENTER
                //println("*********************** ANCHO/ALTO: ${letraElement.width} / ${letraElement.getMeasuredHeight()}")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        val vLetra = viewElement as TextView
        val action = motionEvent.action
        when(action) {
            MotionEvent.ACTION_DOWN -> {
                if (primeraLetraSeleccionada == null) {
                    primeraLetraSeleccionada = vLetra
                    println("********** PRIMERA LETRA SELECCIONADA: " + vLetra.text.toString())
                    encontrarLetraEnMatriz()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (primeraLetraSeleccionada != null && moveSelected == null) {
                    selectMoveType(motionEvent.rawX, motionEvent.rawY)
                } else {
                    if (moveSelected != null) {

                        var choosenMove = moveSelected as ArrayList<Int>
                        if (letterObjectList.getOrNull(ultimaPosFila!!+choosenMove[0]) != null) {
                            if (letterObjectList[ultimaPosFila!!+choosenMove[0]].getOrNull(ultimaPosCol!!+choosenMove[1]) != null) {
                                val location = IntArray(2)
                                (letterObjectList[ultimaPosFila!!+choosenMove[0]][ultimaPosCol!!+choosenMove[1]]).getLocationOnScreen(location);
                                var palPosX = location[0]
                                var palPosY = location[1]
                                var palSizeX: Int = (letterObjectList[ultimaPosFila!!+choosenMove[0]][ultimaPosCol!!+choosenMove[1]]).width
                                var palSizeY: Int = (letterObjectList[ultimaPosFila!!+choosenMove[0]][ultimaPosCol!!+choosenMove[1]]).height
                                var dedoX = motionEvent.rawX
                                var dedoY = motionEvent.rawY
                                if (dedoX >= palPosX && dedoY >= palPosY && dedoX <= palPosX+palSizeX && dedoY <= palPosY+palSizeY) {
                                    println("******** NUEVA LETRA ENCONTRADA: " + letterObjectList[ultimaPosFila!!+choosenMove[0]][ultimaPosCol!!+choosenMove[1]].text.toString())
                                    limpiarLinea()
                                    drawingLine = CustomLine(requireContext(), drawStartX!!.toFloat(), drawStartY!!.toFloat(), (palPosX+palSizeX/2).toFloat(), (palPosY+palSizeY/2).toFloat(), 20F, 100, 40, 103, 220)
                                    constraintMain.addView(drawingLine)
                                    palabraFormada = palabraFormada+(letterObjectList[ultimaPosFila!!+choosenMove[0]][ultimaPosCol!!+choosenMove[1]].text.toString())
                                    println("*********** PALABRA ACTUAL: " + palabraFormada)
                                    ultimaPosFila = ultimaPosFila!! + choosenMove[0]
                                    ultimaPosCol = ultimaPosCol!! + choosenMove[1]
                                }

                            } else {
                                viewElement.setOnTouchListener(null)
                                reenableTouch(viewElement)
                                //viewElement.setOnTouchListener(this)
                            }
                        } else {
                            viewElement.setOnTouchListener(null)
                            reenableTouch(viewElement)
                            //viewElement.setOnTouchListener(listener)
                        }


                    }
                }
                //viewElement.x = motionEvent.rawX - viewElement.width/2
                //viewElement.y = motionEvent.rawY - viewElement.height/2

            }
            MotionEvent.ACTION_UP -> {
                comprobarPalabraEncontrada()
                palabraFormada = ""
                primeraLetraSeleccionada = null
                //letraPosVector = null
                arrayPlabrasCercanas.clear()
                moveSelected = null
                ultimaPosFila = null
                ultimaPosCol = null
                drawStartX = null
                drawStartY = null
            }
        }
        true
    }

    private fun reenableTouch(viewElement: View) {
        viewElement.setOnTouchListener(listener)
    }

    private fun encontrarLetraEnMatriz() {
        for ((i, fila) in letterObjectList.withIndex()) {
            for ((k, bLetra) in fila.withIndex()) {
                if (bLetra == primeraLetraSeleccionada) {
                    //ultimoMovimiento = arrayListOf<Int>(i,k)
                    ultimaPosFila = i
                    ultimaPosCol = k
                    val location = IntArray(2)
                    (bLetra as View).getLocationOnScreen(location);
                    var palPosX = location[0]
                    var palPosY = location[1]
                    var palSizeX: Int = (bLetra as View).width
                    var palSizeY: Int = (bLetra as View).height
                    drawStartX = palPosX.toFloat()+palSizeX/2
                    drawStartY = palPosY.toFloat()+palSizeY/2
                    palabraFormada = palabraFormada+(bLetra.text.toString())
                    println("********** LETRA ENCONTRADA EN FILA: ${i} COLUMNA: ${k}")
                    encontrarLetrasCercanas(i,k)
                    break
                }
            }
        }
    }

    private var movesTypesInMatrix = arrayListOf<ArrayList<Int>>(
        //Mirar foto para entender los movimentos posibles
        arrayListOf(0,1),
        arrayListOf(1,1),
        arrayListOf(1,0),
        arrayListOf(1,-1),
        arrayListOf(0,-1),
        arrayListOf(-1,-1),
        arrayListOf(-1,0),
        arrayListOf(-1,1),
    )

    private fun encontrarLetrasCercanas(pFila: Int, pCol: Int) {
        for ((i, move) in movesTypesInMatrix.withIndex()) {
            //Primero comprobamos que la siguiente/anterior fila exista
            if (letterList.getOrNull(pFila+move[0]) != null) {
                //Comprbamos que dentro de la fila exista la siguiente/anterior letra
                if (letterList[pFila+move[0]].getOrNull(pCol+move[1]) != null) {
                    //println("********* LETRA CERCANA ENCONTRADA: " + letterObjectList[pFila+move[0]][pCol+move[1]].text.toString())
                    //var arrayPalabraCercana = arrayListOf<Any>()
                    var datosPalabraCercana = arrayListOf<Any>(
                        letterObjectList[pFila+move[0]][pCol+move[1]],
                        move
                    )
                    arrayPlabrasCercanas.add(datosPalabraCercana)
                }
            }
        }
        //Log.d("this is my array", "arr: " + arrayPlabrasCercanas);
    }

    private fun selectMoveType(rawX: Float, rawY: Float) {
        if (moveSelected == null) {
            for ((i, palabraCercana) in arrayPlabrasCercanas.withIndex()) {
                val location = IntArray(2)
                (palabraCercana[0] as View).getLocationOnScreen(location);
                var palPosX = location[0]
                var palPosY = location[1]
                var palSizeX: Int = (palabraCercana[0] as View).width
                var palSizeY: Int = (palabraCercana[0] as View).height
                if (rawX >= palPosX && rawY >= palPosY && rawX <= palPosX+palSizeX && rawY <= palPosY+palSizeY) {
                    if (moveSelected == null) {
                        moveSelected = palabraCercana[1] as ArrayList<Int>
                        var choosenMove = palabraCercana[1] as ArrayList<Int>
                        ultimaPosFila = ultimaPosFila!! + choosenMove[0]
                        ultimaPosCol = ultimaPosCol!! + choosenMove[1]
                        palabraFormada = palabraFormada+((palabraCercana[0] as TextView).text.toString())

                        val location = IntArray(2)
                        (palabraCercana[0] as View).getLocationOnScreen(location);
                        var palPosX = location[0]
                        var palPosY = location[1]
                        var palSizeX: Int = (palabraCercana[0] as View).width
                        var palSizeY: Int = (palabraCercana[0] as View).height
                        limpiarLinea()
                        drawingLine = CustomLine(requireContext(), drawStartX!!.toFloat(), drawStartY!!.toFloat(), (palPosX+palSizeX/2).toFloat(), (palPosY+palSizeY/2).toFloat(), 20F, 100, 40, 103, 220)
                        constraintMain.addView(drawingLine)

                        println("******** MOVIMIENTO SELECCIONADO: ${choosenMove[0]} / ${choosenMove[1]} LETRA: ${(palabraCercana[0] as TextView).text.toString()}" )
                        println("*********** PALABRA ACTUAL: " + palabraFormada)
                    }
                }
            }
        }
    }

    private fun comprobarPalabraEncontrada() {

        for ((index, value) in palabras.withIndex()) {
            if (palabraFormada.length == value.length) {
                if (palabraFormada.equals(value)) {
                    if (!palabrasEncontradas.contains(palabraFormada)) {
                        palabrasEncontradas.add(value)
                        if (drawingLine != null) {
                            val nuevaDrawLine = drawingLine!!.clone()
                            constraintMain.addView(nuevaDrawLine)
                        }
                        if (palabrasEncontradas.size == palabras.size) {
                            Toast.makeText(requireContext(), "ZORIONAK!", Toast.LENGTH_SHORT).show()
                            val btnSiguiente: Button = globalView.findViewById(R.id.btnf1_4_siguiente)
                            btnSiguiente.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        limpiarLinea()
    }

    private fun limpiarLinea() {
        if (drawingLine!= null) {
            constraintMain.removeView(drawingLine)
            drawingLine = null
        }
    }

    private fun seleccionarPalabraAleatoria() {
        //val palabraRandom = (0 until palabras.size-1).random()
        //palabrasPintadas.add(palabras[palabraRandom])
        for ((index, value) in palabras.withIndex()) {
            if (!palabrasPintadas.contains(value)) {
                palabrasPintadas.add(value)
                palabraPreparada = value
                findHuecosFilasLibres()
                break
            }
        }

    }

    private fun tipoMovimiento() {

    }

    private fun findDiagDchaLibres() {

    }

    private fun findDiagIzqLibres() {

    }

    private fun findHuecosFilasLibres() {
        for ((i, value) in letterList.withIndex()) {
            var filaHuecosLibres = 0
            for ((k, letter) in letterList.withIndex()) {
                if (letter.equals("0")) {
                    filaHuecosLibres++
                    if (filaHuecosLibres >= palabraPreparada.length) {
                        break
                    }
                }
            }
            filasHuecosLibres.add(i)
        }
        println(Arrays.toString(filasHuecosLibres.toIntArray()))
    }

    private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_4tutorialjuego4) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.juego4tutorialtxt))
        typeWriterView.setDelay(65)
    }

    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_4fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)
        val buttonSiguiente = view.findViewById(R.id.btnf1_4_siguiente) as Button
        buttonSiguiente.isVisible=false

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_4_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
            }
        }, 2000)
    }


    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_4_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
    }


    private fun exitAnimationfun(view: View) {
        val upelioAnimado = view.findViewById(R.id.imgv1_4_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //Animacion upelio salido
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //VistaAnimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_4_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //Animacion fondo gris
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                val txtAnimacion = view.findViewById(R.id.txtv1_4fondogris) as TextView
                val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                txtAnimacion.startAnimation(aniFade)
                txtv1_4tutorialjuego4.startAnimation(aniFade)
                txtv1_4tutorialjuego4.isVisible = false
                txtAnimacion.isVisible = false
                val buttonSiguiente = view.findViewById(R.id.btnf1_4_siguiente) as Button
                buttonSiguiente.isVisible=true
            }
        }, 1000)
    }

    fun getConstraintChildCount(contraintView: View): ArrayList<View>? {
        //TODO TENER CUIDADO DE USAR ESTA FUNCION CON HIJOS QUE NO SON DIRECTOS (PODRIA SEGUIR INCREMENTANDO)
        val result = ArrayList<View>()
        val childList: ViewGroup = contraintView as ViewGroup
        // No se puede realizar un bucle a un viewgroup
        for (i in 0 until childList.childCount) {
            val child = childList.getChildAt(i)
            result.add(child)
        }
        return result
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
            Fragment1_4_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}