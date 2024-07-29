package com.example.contact.mvvmarch

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.contact.roomdb.Database
import com.example.contact.roomdb.DbBuilder
import com.example.contact.roomdb.entity.Contact

class Repo(
    var context: Context
) {
    var database: Database? = null

    init {
        database = DbBuilder.getdb(context)
    }

    fun getData(): LiveData<List<Contact>>? {
        return  database?.contactDao()?.readContact()
    }

    fun insertData(contact: Contact): Long? {
        return database?.contactDao()?.createContact(contact)
    }
    fun deleteData(contact: Contact) {
        database?.contactDao()?.deleteContact(contact)
    }

    fun updateData(contact: Contact): Int? {
        return database?.contactDao()?.updateContact(contact)
    }


}