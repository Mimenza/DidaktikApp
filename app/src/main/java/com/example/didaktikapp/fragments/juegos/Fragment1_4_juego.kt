package com.example.didaktikapp.fragments.juegos
import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R
import java.util.*
import kotlin.collections.ArrayList
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.TextView
import android.media.MediaPlayer
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.clone
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.DbHandler
import kotlinx.android.synthetic.main.fragment1_4_juego.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Fragment1_4_juego : Fragment(), DbHandler.QueryResponseDone {
    val thisJuegoId: Int = 4
    private lateinit var btnInfoJuego: ImageButton
    private var nFilas: Int = 12
    private var nCols: Int = 12

    private lateinit var globalView: View
    private lateinit var matrizMain: LinearLayout
    private lateinit var constraintMain: ConstraintLayout

    private var letterWeight = 1/nCols
    private var audio: MediaPlayer? = null
    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var  mapa: ImageButton
    private var introFinished: Boolean = false
    private var doubleTabHandler: Handler? = null
    private var typeWriterHandler: Handler? = null
    private var exitAnimationHandler: Handler? = null
    private var talkAnimationHandler: Handler? = null
    private var fondoAnimationHandler: Handler? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_4_juego, container, false)
        globalView = view
        introFinished = false
        matrizMain = view.findViewById(R.id.matrizprincipal)
        constraintMain = view.findViewById(R.id.juego4_constraintMain)
        val button: Button = view.findViewById(R.id.btnf1_4_siguiente)

        btnInfoJuego= view.findViewById((R.id.btn1_4_infojuego))

        mapa = view.findViewById(R.id.btnf1_4_mapa)

        button.setOnClickListener(){
            prepararPreguntas()
        }

        val introFondo: TextView = view.findViewById(R.id.txtv1_4fondogris)
        introFondo.setOnClickListener() {
            if (null == doubleTabHandler) {
                doubleTabHandler = Handler()
                doubleTabHandler?.postDelayed({
                    doubleTabHandler?.removeCallbacksAndMessages(null)
                    doubleTabHandler = null
                }, 200)
            } else {
                endIntroManually()
            }
        }

        //Typewriter juego 4 tutorial
        typeWriterHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler = Handler()
        typeWriterHandler?.postDelayed({
            typewriter(view)
            typeWriterHandler?.removeCallbacksAndMessages(null)
            typeWriterHandler = null
        }, 2000)

        //Animacion manzana al iniciar el juego
        starAnimationfun(view)
        playAudio(R.raw.juego4audiotutorial)



        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                prepararJuego()
            }
        })

        return view
    }

    private fun playAudio(audioResource: Int) {
        runBlocking {
            launch {
                audio?.stop()
                audio = MediaPlayer.create(context, audioResource)
                audio?.start()

                audio?.setOnCompletionListener {

                    exitAnimationHandler?.removeCallbacksAndMessages(null)
                    exitAnimationHandler = Handler()
                    exitAnimationHandler?.postDelayed({
                        exitAnimationfun(globalView)
                        activateBtn()
                        exitAnimationHandler?.removeCallbacksAndMessages(null)
                        exitAnimationHandler = null
                    }, 1000)
                }
            }
        }
    }

    // SOPA DE LETRAS VARAIBLES
    private var letras = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ"
    private var letraComodin = "•"
    private var palabraFormada: String = ""
    private var primeraLetraSeleccionada: TextView? = null
    private var drawingLine: CustomLine? = null
    var storedLines = arrayListOf<CustomLine>()
    var letterList = arrayListOf<ArrayList<String>>()
    var letterObjectList = arrayListOf<ArrayList<TextView>>()
    private var arrayPlabrasCercanas = arrayListOf<ArrayList<Any>>()
    private var moveSelected: Any? = null
    private var ultimaPosFila: Int? = null
    private var ultimaPosCol: Int? = null
    private var drawStartX: Float? = null
    private var drawStartY: Float? = null
    private var palabrasPintadas = arrayListOf<String>()
    private var palabras = arrayListOf<String>("SAGARDANTZA", "SALMENTA", "TRIKITIXA", "TXALAPARTA", "GARBITZEA", "DASTATZEA", "TXISTULARIAK", "BILKETA", "ERAKUSKETA")
    private var palabrasEncontradas = arrayListOf<String>()
    private var filasHuecosLibres = arrayListOf<Int>()
    private var palabraPreparada: String = ""

    //Variables Juego Verdadero/Falso
    private var respuestasCorrectas = arrayListOf<Int>(1,3,5,8,9,12,13,16,17) // Radio button index Respuestas correctas

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

    //Funciones Juego Verdadero / Flaso

    //Funcion que gestiona el juego de verdadero falso
    private fun prepararPreguntas() {
        playAudio(R.raw.juego4_egiagezurra)
        limpiarLineas()
        val sopaLayout: LinearLayout = globalView.findViewById(R.id.matrizprincipal)
        val btnComprobarPreguntas: Button = globalView.findViewById(R.id.juego4_comprobarPreguntas)
        val btnSiguiente: Button = globalView.findViewById(R.id.btnf1_4_siguiente)
        val scrollViewPreguntas: ScrollView = globalView.findViewById(R.id.juego4_scroll_preguntas)
        val txtPlabras: TextView = globalView.findViewById(R.id.juego4_sopa_palabras)
        btnSiguiente.visibility = View.GONE
        txtPlabras.visibility = View.GONE
        sopaLayout.visibility = View.GONE
        scrollViewPreguntas.visibility = View.VISIBLE
        btnComprobarPreguntas.visibility = View.VISIBLE

        val pregLayout: LinearLayout = globalView.findViewById(R.id.juego4_linearlayout_preguntas)

        //Comprobamos las respuestas mediante bucles para no tener que ir elemento por elemento
        btnComprobarPreguntas.setOnClickListener() {
            var vFila1: ArrayList<View>? = getChildLLinearLayoutCustom(pregLayout)
            var todosSeleccionados: Boolean = true
            var todosAcertados = true
            for ((index, value) in vFila1!!.withIndex()) {
                var valueElementCasted: RadioGroup = value as RadioGroup
                if (valueElementCasted.getCheckedRadioButtonId() != -1) {
                    if (valueElementCasted.getCheckedRadioButtonId() != respuestasCorrectas[index]) {
                        todosAcertados = false
                    }
                } else {
                    todosSeleccionados = false
                    break
                }
            }
            //Si todas estan seleccionadas, se pasara a comprobar que sean correctas las opciones seleccionadas
            // Caso contrario, se mostrara mensajes de error/fallo
            if (todosSeleccionados) {
                if (todosAcertados) {
                    DbHandler.userAumentarPuntuacion(10)
                    DbHandler.userActualizarUltimoPunto(thisJuegoId)
                    DbHandler().requestDbUserUpdate(this)
                    Toast.makeText(requireContext(), "ERES UN CRACK!", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        //llamamos a la animacion para animar a upelio
                        playAudio(R.raw.ongiaudioa)
                        Navigation.findNavController(it).navigate(R.id.action_fragment1_4_juego_to_fragment2_4_minijuego)
                    }, 1000)
                } else {
                    playAudio(R.raw.gaizkiaudioa)
                    Toast.makeText(requireContext(), "VAYA ! HAS FALLADO!", Toast.LENGTH_SHORT).show()
                }
            } else {
                playAudio(R.raw.gaizkiaudioa)
                Toast.makeText(requireContext(), "SELECCIONAD TODAS LAS OPCIONES!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //Con este metodo obtenemos los hijos directos de un elemento
    private fun getChildLLinearLayoutCustom(pLinearElement: View): ArrayList<View>? {
        val result = ArrayList<View>()
        val childList: ViewGroup = pLinearElement as ViewGroup
        for (i in 0 until childList.childCount) {
            val child = childList.getChildAt(i)
            if (child is RadioGroup) {
                result.add(child)
            }
        }
        return result
    }

    //-------------------------------------------------------------------------------

    // FUNCIONES PARA EL JUEGO DE LA SOPA DE LETRAS
    private fun prepararJuego() {
        //Primero generamos la matriz limpia de n filas n columnas
        for (i in 0 until nFilas) {
            var filaLetterList = arrayListOf<String>()
            letterList.add(filaLetterList)
            for (k in 0 until nCols) {
                filaLetterList.add(letraComodin)
            }
        }
        //Metodos para preparar la sopa de letras
        escribirPalabrasModoSimple()
        convertComodinToRandomLetters()
        actualizarPalabrasRestantes()
        voltearMatriz()
        pintarSopa()
    }

    //Este metodo actuaiza las palabras restantes de la sopa de letras
    private fun actualizarPalabrasRestantes() {
        val palabrasRestantesElement: TextView = globalView.findViewById(R.id.juego4_sopa_palabras)
        var palabrasRestantes = ""
        for ((i, palabra) in palabras.withIndex()) {
            if (!palabrasEncontradas.contains(palabra)) {
                if (!palabrasRestantes.isEmpty()) {
                    palabrasRestantes = palabrasRestantes + ", " + palabra
                } else {
                    palabrasRestantes = palabrasRestantes + palabra
                }

            }
        }
        palabrasRestantesElement.text = palabrasRestantes
        if (palabrasRestantes.isEmpty()) {
            val buttonSiguiente = globalView.findViewById(R.id.btnf1_4_siguiente) as Button
            buttonSiguiente.isVisible=true
        }
    }

    // Metodo para escribir las palabras sobre la matriz
    private fun escribirPalabrasModoSimple() {

        // #Palabra SAGARDANTZA
        letterList[0][1] = "S"
        letterList[0][2] = "A"
        letterList[0][3] = "G"
        letterList[0][4] = "A"
        letterList[0][5] = "R"
        letterList[0][6] = "D"
        letterList[0][7] = "A"
        letterList[0][8] = "N"
        letterList[0][9] = "T"
        letterList[0][10] = "Z"
        letterList[0][11] = "A"

        // #TXISTULARIAK
        letterList[0][0] = "K"
        letterList[1][1] = "A"
        letterList[2][2] = "I"
        letterList[3][3] = "R"
        letterList[4][4] = "A"
        letterList[5][5] = "L"
        letterList[6][6] = "U"
        letterList[7][7] = "T"
        letterList[8][8] = "S"
        letterList[9][9] = "I"
        letterList[10][10] = "X"
        letterList[11][11] = "T"

        // TXALAPARTA
        letterList[1][0] = "T"
        letterList[2][0] = "X"
        letterList[3][0] = "A"
        letterList[4][0] = "L"
        letterList[5][0] = "A"
        letterList[6][0] = "P"
        letterList[7][0] = "A"
        letterList[8][0] = "R"
        letterList[9][0] = "T"
        letterList[10][0] = "A"

        // BILKETA
        letterList[7][3] = "B"
        letterList[6][4] = "I"
        letterList[5][5] = "L"
        letterList[4][6] = "K"
        letterList[3][7] = "E"
        letterList[2][8] = "T"
        letterList[1][9] = "A"

        // ERAKUSKETA
        letterList[11][0] = "E"
        letterList[10][1] = "R"
        letterList[9][2] = "A"
        letterList[8][3] = "K"
        letterList[7][4] = "U"
        letterList[6][5] = "S"
        letterList[5][6] = "K"
        letterList[4][7] = "E"
        letterList[3][8] = "T"
        letterList[2][9] = "A"

        // SALMENTA
        letterList[10][2] = "S"
        letterList[10][3] = "A"
        letterList[10][4] = "L"
        letterList[10][5] = "M"
        letterList[10][6] = "E"
        letterList[10][7] = "N"
        letterList[10][8] = "T"
        letterList[10][9] = "A"

        // TRIKITIXA
        letterList[11][10] = "T"
        letterList[11][9] = "R"
        letterList[11][8] = "I"
        letterList[11][7] = "K"
        letterList[11][6] = "I"
        letterList[11][5] = "T"
        letterList[11][4] = "I"
        letterList[11][3] = "X"
        letterList[11][2] = "A"

        // GARBITZEA
        letterList[1][10] = "G"
        letterList[2][10] = "A"
        letterList[3][10] = "R"
        letterList[4][10] = "B"
        letterList[5][10] = "I"
        letterList[6][10] = "T"
        letterList[7][10] = "Z"
        letterList[8][10] = "E"
        letterList[9][10] = "A"

        // DASTATZEA
        letterList[1][11] = "D"
        letterList[2][11] = "A"
        letterList[3][11] = "S"
        letterList[4][11] = "T"
        letterList[5][11] = "A"
        letterList[6][11] = "T"
        letterList[7][11] = "Z"
        letterList[8][11] = "E"
        letterList[9][11] = "A"
    }

    //Metodo para convertir las letras que no formen parte de la palabra en letras aleatorias
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

    // Pintamos la sopa de letra sobre el layout
    private fun pintarSopa() {
        // Creamos n cantidad de linearLayouts(FILAS) que son los que tendran cada letrs
        for (i in 0 until letterList.size) {
            var filaLetterElementList = arrayListOf<TextView>()
            letterObjectList.add(filaLetterElementList)
            var nuevaFila = LinearLayout(requireContext())
            // Aplicamos los parametros del layout para que se ajuste al espacio del layout padre de estos linear
            val paramsLinear: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            paramsLinear.weight = nCols.toFloat()
            nuevaFila.setLayoutParams(paramsLinear)
            nuevaFila.setOrientation(LinearLayout.HORIZONTAL)
            matrizMain.addView(nuevaFila)
            // creamos m cantidad de letras (COLUMNAS) que estaran dentro de cada layout que hemos creado previamente
            for (k in 0 until letterList[i].size) {
                var letraElement = TextView(requireContext())
                filaLetterElementList.add(letraElement)
                letraElement.id = k+(i*nCols)
                letraElement.text = letterList[i][k]
                letraElement.textSize = 25.toFloat()
                letraElement.width = nuevaFila.width/nCols
                letraElement.gravity = Gravity.CENTER
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.weight = 1.toFloat()
                params.setMargins(10,10,10,10)
                params.gravity = Gravity.CENTER
                letraElement.setLayoutParams(params)
                nuevaFila.addView(letraElement)
                letraElement.setOnTouchListener(listener)
                letraElement.setBackgroundResource(R.drawable.roundedcorners)
                letraElement.setClipToOutline(true)
            }
        }
    }

    //Listener que esta a la escucha del click y del movimiento de las letras
    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        val vLetra = viewElement as TextView
        val action = motionEvent.action
        when(action) {
            MotionEvent.ACTION_DOWN -> {
                //Bajo esta condicion, obtenemos la primera letra seleccioanda
                if (primeraLetraSeleccionada == null) {
                    primeraLetraSeleccionada = vLetra
                    encontrarLetraEnMatriz()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (primeraLetraSeleccionada != null && moveSelected == null) {
                    //Si la direccion del movimiento no esta definida, se llama a este metodo
                    selectMoveType(motionEvent.rawX, motionEvent.rawY)
                } else {
                    // Si tenemos la direccion del movimiento se realizaran comprobaciones SOLO DE LA SIGUIENTE LETRA POSIBILE
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
                                    limpiarLinea()
                                    drawingLine = CustomLine(requireContext(), drawStartX!!.toFloat(), drawStartY!!.toFloat(), (palPosX+palSizeX/2).toFloat(), (palPosY+palSizeY/2).toFloat(), 20F, 100, 40, 103, 220)
                                    constraintMain.addView(drawingLine)
                                    palabraFormada = palabraFormada+(letterObjectList[ultimaPosFila!!+choosenMove[0]][ultimaPosCol!!+choosenMove[1]].text.toString())
                                    ultimaPosFila = ultimaPosFila!! + choosenMove[0]
                                    ultimaPosCol = ultimaPosCol!! + choosenMove[1]
                                }

                            } else {
                                viewElement.setOnTouchListener(null)
                                reenableTouch(viewElement)
                            }
                        } else {
                            viewElement.setOnTouchListener(null)
                            reenableTouch(viewElement)
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                // Si se levanta el dedo, se comprobara la palabra formada y se restableceran las variables
                comprobarPalabraEncontrada()
                palabraFormada = ""
                primeraLetraSeleccionada = null
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

    //Metodo creado para llamar a itself, dado que no puedes llamar al mismo metodo desde donde ejecutas la accion
    private fun reenableTouch(viewElement: View) {
        viewElement.setOnTouchListener(listener)
    }

    //Metodo para comprobar que elemento es de la matriz
    private fun encontrarLetraEnMatriz() {
        for ((i, fila) in letterObjectList.withIndex()) {
            for ((k, bLetra) in fila.withIndex()) {
                if (bLetra == primeraLetraSeleccionada) {
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
                    encontrarLetrasCercanas(i,k)
                    break
                }
            }
        }
    }

    // Variable para determinar que tipo de movimiento son posibles
    private var movesTypesInMatrix = arrayListOf<ArrayList<Int>>(
        // Lista de todos los movimientos que se presentan en el juego como vectores
        arrayListOf(0,1),
        arrayListOf(1,1),
        arrayListOf(1,0),
        arrayListOf(1,-1),
        arrayListOf(0,-1),
        arrayListOf(-1,-1),
        arrayListOf(-1,0),
        arrayListOf(-1,1),
    )

    /*
        El siguiente metodo encuentra las letras mas cercanas a la que se ha hecho click
        basandose en los movimientos posibles del metodo anterior
        Si la palabra da por valido que dicho movimiento es posible, se añadira a una lista de posibles
        movimientos para limitarlo solo a la segunda letra
     */

    private fun encontrarLetrasCercanas(pFila: Int, pCol: Int) {
        for ((i, move) in movesTypesInMatrix.withIndex()) {
            if (letterList.getOrNull(pFila+move[0]) != null) {
                if (letterList[pFila+move[0]].getOrNull(pCol+move[1]) != null) {
                    var datosPalabraCercana = arrayListOf<Any>(
                        letterObjectList[pFila+move[0]][pCol+move[1]],
                        move
                    )
                    arrayPlabrasCercanas.add(datosPalabraCercana)
                }
            }
        }
    }

    // Con este metodo vamos a determinar que tipo de movimiento se seguira tras tener seleccionadas 2 letras
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
                    }
                }
            }
        }
    }

    // Al llamar a este metodo, comprobaremos el valor de la variable de la 'Palabra Formada' con la lista de todas
    // las palabras
    private fun comprobarPalabraEncontrada() {
        for ((index, value) in palabras.withIndex()) {
            if (palabraFormada.length == value.length) {
                if (palabraFormada.equals(value)) {
                    if (!palabrasEncontradas.contains(palabraFormada)) {
                        palabrasEncontradas.add(value)
                        actualizarPalabrasRestantes()
                        if (drawingLine != null) {
                            val nuevaDrawLine = drawingLine!!.clone()
                            constraintMain.addView(nuevaDrawLine)
                            storedLines.add(nuevaDrawLine)
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

    // Mediante este metodo limpiaremos la linea previamente dibujada en caso de que se seleccione una nueva letra
    private fun limpiarLinea() {
        if (drawingLine!= null) {
            constraintMain.removeView(drawingLine)
            drawingLine = null
        }
    }

    // Metodo para limpiar todas las lineas de todas las palabras marcadas en la sopa de letras
    // Este metodo se usa para cambiar a la parte de verdadero / falso
    private fun limpiarLineas() {
        for ((index,value) in storedLines.withIndex()) {
            constraintMain.removeView(value)
        }
        storedLines.clear()
    }

    // Metodo para genrar nuevas posibilidades de la matriz (Actualmente hay un total de 4 posibilidades)
    private fun voltearMatriz() {
        val matrizOption = (1 until 4).random()
        when (matrizOption) {
            2 -> {
                //La matriz va a rotar en el eje vertical
                letterList.reverse()
            }
            3 -> {
                // La matriz girara en el eje horizontal
                for ((i, fila) in letterList.withIndex()) {
                    fila.reverse()
                }
            }
            4 -> {
                //La matriz va a rotar en el eje vertical y horizontal
                letterList.reverse()
                for ((i, fila) in letterList.withIndex()) {
                    fila.reverse()
                }
            }
        }
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
        val upelioStatic = view.findViewById(R.id.imgv1_4_upelio) as ImageView
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler = Handler()
        talkAnimationHandler?.postDelayed({
            upelioStatic.isVisible = false
            talkAnimationfun(view)
            talkAnimationHandler?.removeCallbacksAndMessages(null)
            talkAnimationHandler = null
        }, 2000)
    }

    private fun talkAnimationfun(view: View) {
        val upelio = view.findViewById(R.id.imgv1_4_upelio2) as ImageView
        upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = upelio.background as AnimationDrawable
        ani.start()
    }

    private fun exitAnimationfun(view: View) {
        if (introFinished) {
            return
        }
        introFinished = true
        val upelioAnimado = view.findViewById(R.id.imgv1_4_upelio2) as ImageView
        upelioAnimado.isVisible = false

        //Animacion upelio salido
        vistaAnimada = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaAnimada.duration = 2000

        //VistaAnimada.fillAfter = true
        val upelio = view.findViewById(R.id.imgv1_4_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //Animacion fondo gris
        fondoAnimationHandler?.removeCallbacksAndMessages(null)
        fondoAnimationHandler = Handler()
        fondoAnimationHandler?.postDelayed({
            introFinished = true
            val txtAnimacion = view.findViewById(R.id.txtv1_4fondogris) as TextView
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            txtAnimacion.startAnimation(aniFade)
            txtv1_4tutorialjuego4.startAnimation(aniFade)
            txtv1_4tutorialjuego4.isVisible = false
            txtAnimacion.isVisible = false
        }, 1000)
    }

    private fun activateBtn() {
        mapa.setOnClickListener {
            if (audio?.isPlaying == false){
                activity?.let{
                    getActivity()?.finish()
                }
            }
        }

        btnInfoJuego.setOnClickListener(){
            showDialogInfo()
        }
    }

    fun endIntroManually() {
        if (introFinished) {
            return
        }
        doubleTabHandler?.removeCallbacksAndMessages(null)
        introFinished = true
        val upelio1 = globalView.findViewById(R.id.imgv1_4_upelio) as ImageView
        upelio1.clearAnimation()
        upelio1.visibility = View.GONE
        val upelio2 = globalView.findViewById(R.id.imgv1_4_upelio2) as ImageView
        upelio2.clearAnimation()
        upelio2.visibility = View.GONE
        val txtAnimacion = globalView.findViewById(R.id.txtv1_4fondogris) as TextView
        txtAnimacion.clearAnimation()
        val typeWriterElement = globalView.findViewById(R.id.txtv1_4tutorialjuego4) as TextView
        typeWriterElement.isVisible = false
        txtAnimacion.isVisible = false
        audio?.stop()

        activateBtn()
    }

    override fun onDestroy() {
        audio?.stop()
        doubleTabHandler?.removeCallbacksAndMessages(null)
        typeWriterHandler?.removeCallbacksAndMessages(null)
        exitAnimationHandler?.removeCallbacksAndMessages(null)
        talkAnimationHandler?.removeCallbacksAndMessages(null)
        fondoAnimationHandler?.removeCallbacksAndMessages(null)
        doubleTabHandler = null
        typeWriterHandler = null
        exitAnimationHandler = null
        talkAnimationHandler = null
        fondoAnimationHandler = null
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

}