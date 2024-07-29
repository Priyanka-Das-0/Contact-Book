package com.example.contactapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.AddEditActivity
import com.example.contact.databinding.ContactItemBinding
import com.example.contact.roomdb.entity.Contact

class ContactAdapter(var contactList: List<Contact>,var context: Context): RecyclerView.Adapter<ContactAdapter.MyViewHolder>(){


    inner class MyViewHolder(var binding: ContactItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= ContactItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val contact= contactList[position]
        if (contact.profile  != null){

            var imageByte= contact.profile

            if (imageByte!=null){
                var image= BitmapFactory.decodeByteArray(imageByte,0,imageByte.size)
                holder.binding.profile.setImageBitmap(image)
                holder.binding.profile.visibility= View.VISIBLE
                holder.binding.sign.visibility= View.GONE


            }else{
                val splitName= contact.name?.split(" ")
                var sign: String=""
                splitName?.forEach {
                    if (it.isNotEmpty()){
                        sign += it[0]

                    }
                }
                holder.binding.sign.text=sign
                holder.binding.profile.visibility= View.GONE
                holder.binding.sign.visibility= View.VISIBLE
            }

        }else{
            val splitName= contact.name?.split(" ")
            var sign: String=""
            splitName?.forEach {
                if (it.isNotEmpty()){
                    sign += it[0]

                }
            }


            holder.binding.sign.text=sign
            holder.binding.sign.text=sign
            holder.binding.profile.visibility= View.GONE
            holder.binding.sign.visibility= View.VISIBLE
        }

        holder.binding.name.text=contact.name
        holder.binding.phone.text=contact.phoneNumber
        holder.binding.email.text=contact.email
        holder.itemView.setOnClickListener {


            context.startActivity(
                Intent(context, AddEditActivity::class.java).putExtra("FLAG", 1)
                    .putExtra("DATA", contact)
            )

        }
        holder.binding.profile.setOnClickListener {

        }
        makeAnimation(holder.itemView)

    }
    fun makeAnimation(view: View) {
        var anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 2000

//        anim.repeatCount=5
        view.startAnimation(anim)
    }
}