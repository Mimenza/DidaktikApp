package com.example.didaktikapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.didaktikapp.R
import com.example.reto01.Model.User
import kotlinx.android.synthetic.main.itemuserrecycler.view.*

class UsersRecyclerAdapter(private val listUsers: List<User>,  val context:Context) : RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder>(){
    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType:Int
    ):UsersRecyclerAdapter.UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemuserrecycler, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:UsersRecyclerAdapter.UserViewHolder, i:Int) {
        var usuario = listUsers[i]


        holder.textViewName.text = usuario.nombre
        holder.textViewPuntuacion.text = usuario.puntuacion.toString()
        holder.textViewLastPuntuacion.text = usuario.ultima_puntuacion.toString()
    }

    override fun getItemCount():Int {
        return listUsers.size
    }
    inner class UserViewHolder(view:View) : RecyclerView.ViewHolder(view) {


        var cardUser:CardView
        var textViewName:TextView
        var textViewPuntuacion:TextView
        var textViewLastPuntuacion:TextView


        init {
            textViewName = view.txtv_nombre as TextView
            textViewPuntuacion = view.txtv_puntuacion as TextView
            textViewLastPuntuacion = view.txtv_ultimopunto as TextView
            cardUser = view.cardUser as CardView



        }
    }
}