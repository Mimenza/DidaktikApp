package com.example.didaktikapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity2LoginBinding
import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class Activity2_Login : AppCompatActivity() {
    private lateinit var binding:Activity2LoginBinding
    lateinit private var Usuario : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2_login)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        binding= Activity2LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase
        var db:FirebaseFirestore = FirebaseFirestore.getInstance()
        getLastIdUser(db)

    }


    private fun  getLastIdUser(db:FirebaseFirestore){

        var datos=""
        db.collection("Usuarios").get().
        addOnSuccessListener { documentos->

            for (documento in documentos){


               datos+="${documento.id}"

            }
          var last_id= datos.last().toString().toInt()

            //Botón alta usuario
            binding.btn1Hasi.setOnClickListener{

                guardardatos(db, last_id)
            }
        }

    }


    private fun guardardatos(db:FirebaseFirestore, last_id:Int){
        //Si los datos están rellenos




        if (binding.txt2Nombre.text.isNotBlank()){

            val dato= hashMapOf(
                "admin" to 0,
                "id" to "u"+((last_id+1).toString()),
                "nombre" to binding.txt2Nombre.text.toString(),
                 "puntuacion" to 0,
                 "ultimo_punto" to 0)


            db.collection("Usuarios").document(((last_id+1).toString())).set(dato)
                .addOnSuccessListener {

                    Toast.makeText(this, "Usuario insertado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
        }

    }
}