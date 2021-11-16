package com.example.didaktikapp.activities.sites


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment3_info


class Activity6_1_Sagardoetxea : AppCompatActivity() {

    var bundle:Bundle=Bundle()
    lateinit var fragment:Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity6_1_sagardoetxea)
        getSupportActionBar()?.hide()


        //Poner  datos y pasar a fragment
        bundle.putString("titulo_juego1", resources.getString(R.string.titulo1_juego))
        bundle.putString("imagen_juego1",R.drawable.sagardoetxea.toString())
        bundle.putString("descripcion_juego1",resources.getString(R.string.text_1juego))

        fragment= Fragment3_info()
        fragment.arguments=bundle
        CargarFragment(fragment)


    }
    private fun CargarFragment(fragment: Fragment) {
        val transaccion=supportFragmentManager.beginTransaction()
        transaccion.replace(R.id.contenedor, fragment)
        transaccion.addToBackStack(null)
        transaccion.commit() }

}