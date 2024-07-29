package com.example.contact.mvvmarch

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.livedata.core.R
import androidx.lifecycle.map
import com.example.contact.roomdb.entity.Contact

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var data: LiveData<List<Contact>>
    var repo:Repo

    var contactList = ArrayList<Contact>()
    init{

        repo = Repo(application)
        data = repo.getData()!!
       // repo.getData()?.map {
         //   contactList.add(it)


    }
    fun deleteContact(contact: Contact){
        repo.deleteData(contact)

    }

}