package com.example.contact.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contact.roomdb.dao.ContactDao
import com.example.contact.roomdb.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}