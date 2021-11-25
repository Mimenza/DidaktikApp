package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity2LoginBinding
import com.example.reto01.Model.User


class Activity2_Login : AppCompatActivity(), DbHandler.queryResponseDone {
    private lateinit var binding:Activity2LoginBinding
    lateinit private var Usuario : User
    private val dbHandlerInstance = DbHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity2_login)

        binding= Activity2LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1Hasi.setOnClickListener() {
            if (binding.txt2Nombre.text.isNotBlank()) {
                // Llamamos al metodo Login de la clase hibrida que gestiona queriess de la BD.
                dbHandlerInstance.requestDbUserLogin(binding.txt2Nombre.text.toString(), this)
                binding.progressBar.visibility = View.VISIBLE
                binding.btn1Hasi.isEnabled = false
            } else {
                Toast.makeText(this, "[ERROR] Escriba un nombre", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /*
        Generamos un metodo generico para iniciarlizar la siguiente actividad dado que lo llamamos
        desde diferentes metodos. En caso de necesitar hacer un cambio, no necesitaremos realizar el
        cambio en todos los metodos desde los que se llama
     */
    private fun startActivityBienvenida() {
        var bvIntent = Intent(this, Activity4_bienvenida::class.java)
        startActivity(bvIntent)
        this.overridePendingTransition(0, 0)
    }

    /*
        Overrideamos el callback  del login dado que FIREBASE usa funciones asincronas
     */
    override fun responseDbUserLogin(accountToRegister: String?) {
        if (accountToRegister != null) {
            /*
                Si el parametro que recibimos en este callback es distinto de Null llamamos
                al metodo de Contar usuarios para posteriormente registrar al usuario
                * NOTA: Se llama primero al metodo de contar usuarios dado que, la key de los registros
                *       En FIREBASE es personalizada y no auto-generada
             */
            dbHandlerInstance.requestDbUserCount(accountToRegister, this)
            return
        }
        Toast.makeText(this, "LOGEADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
        startActivityBienvenida()
    }

    /*
        Overrideamos el callback de Contar Usuarios para proceder a registrar el usuario
     */
    override fun responseDbUserCount(byRegister: String?, response: Int) {
        if (byRegister != null) {
            dbHandlerInstance.requestDbUserRegister(byRegister, this)
        }
    }

    /*
        Overrideamos el callback del Register para proseguir con instrucciones secuenciales.
     */
    override fun responseDbUserRegister(response: Boolean) {
        if (!response) {
            Toast.makeText(this, "[ERROR] No se ha podido registrar", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
        startActivityBienvenida()
    }


}