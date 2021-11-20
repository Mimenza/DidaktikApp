package com.example.didaktikapp.activities

import android.content.Intent
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
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity2_login)

        binding= Activity2LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase
        //var db:FirebaseFirestore = FirebaseFirestore.getInstance()
        //getLastIdUser(db)


        binding.btn1Hasi.setOnClickListener() {
            if (binding.txt2Nombre.text.isNotBlank()) {
                val querydb = DbHandler.getDbInstance().collection("Usuarios")
                val query = querydb.whereEqualTo("nombre", binding.txt2Nombre.text.toString())
                //val query = querydb
                .get()
                .addOnSuccessListener { rows ->
                    // FIXME TENER EN CUENTA rows.size dado que puede bailar un valor +1/-1
                    DbHandler.setLastUserId(rows.size())
                    println("******** FILAS: " + rows.size())
                    if (rows.size() > 0) {
                        querydb.get()
                            .addOnSuccessListener { rowsTotal ->
                                var vId:Int = rows.documents[0]["id"].toString().replace("u","").toInt()
                                var vNombre:String = rows.documents[0]["nombre"].toString()
                                var vAdmin:Int = rows.documents[0]["admin"].toString().toInt()
                                var vPuntuacion:Int = rows.documents[0]["puntuacion"].toString().toInt()
                                var vUltimoPto:Int = rows.documents[0]["ultimo_punto"].toString().toInt()
                                DbHandler.setUser(User(vId, vNombre, vAdmin, vPuntuacion, vUltimoPto))
                                DbHandler.setLastUserId(rowsTotal.size())
                                Toast.makeText(this, "Usuario Logeado correctamente", Toast.LENGTH_SHORT).show()
                            }
                        /*
                        var vId:Int = rows.documents[0]["id"].toString().replace("u","").toInt()
                        var vNombre:String = rows.documents[0]["nombre"].toString()
                        var vAdmin:Int = rows.documents[0]["admin"].toString().toInt()
                        var vPuntuacion:Int = rows.documents[0]["puntuacion"].toString().toInt()
                        var vUltimoPto:Int = rows.documents[0]["ultimo_punto"].toString().toInt()
                        DbHandler.setUser(User(vId, vNombre, vAdmin, vPuntuacion, vUltimoPto))
                        Toast.makeText(this, "Usuario Logeado correctamente", Toast.LENGTH_SHORT).show()

                         */
                    } else {
                        querydb.get()
                            .addOnSuccessListener { rowsTotal ->
                                val defaultData = hashMapOf(
                                    "admin" to 0,
                                    "id" to "u"+(rowsTotal.size() + 1),
                                    "nombre" to binding.txt2Nombre.text.toString(),
                                    "puntuacion" to 0,
                                    "ultimo_punto" to 0)
                                //querydb.document("Usuarios", ).set(defaultData)
                                querydb.document((rowsTotal.size()+1).toString()).set(defaultData)
                                DbHandler.setUser(User(rowsTotal.size()+1, binding.txt2Nombre.text.toString(), 0, 0, 0))
                                DbHandler.setLastUserId(rowsTotal.size() + 1)
                                Toast.makeText(this, "Usuario insertado correctamente", Toast.LENGTH_SHORT).show()
                            }

                    }
                    var i = Intent(this, Activity4_bienvenida::class.java)
                    startActivity(i)
                    this.overridePendingTransition(0, 0)


                }
                .addOnFailureListener { exception ->
                    val functionName = object{}.javaClass.enclosingMethod.name
                    println("[ERROR] Found at: ${functionName.toString()}")
                }
            }
        }

    }

    private fun handlerCallTest() {
        
    }



    /*
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
                    var i = Intent(this, Activity4_bienvenida::class.java)
                    startActivity(i)
                    this.overridePendingTransition(0, 0)
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
        }

    }
     */
}