package com.example.contact.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Contact(
    @PrimaryKey var id : Int?=null,
    var profile: ByteArray?=null,
    var name : String?=null,
    var phoneNumber : String?=null,
    var email : String?=null,
//    var date: Date
): Serializable

