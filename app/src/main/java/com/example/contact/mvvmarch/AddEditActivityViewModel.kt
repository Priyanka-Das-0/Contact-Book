package com.example.contact.mvvmarch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.contact.roomdb.entity.Contact

class AddEditActivityViewModel(application: Application): AndroidViewModel(application)   {


    var repo: Repo
    init {

        repo = Repo(application)

    }
    fun storeData(contact: Contact,function :(i : Long?)-> Unit){
        var i= repo.insertData(contact)
        function(i)
    }
    fun updateData(contact: Contact, function: (i: Int?) -> Unit) {
        function(repo.updateData(contact))
    }
}