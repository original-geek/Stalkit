package com.example.stalkit.data.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "LoginInfo")
data class LoginEntity(@PrimaryKey val token: String,
                       val expiration: Long,
                       val dateLoggedIn: Date,
                       val userId: String): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readLong(),
        Date(parcel.readLong()),
        parcel.readString() ?: ""
    ) {
    }

    fun isSessionExpired(): Boolean {
        return Date().time > expiration + dateLoggedIn.time
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
        parcel.writeLong(expiration)
        parcel.writeLong(dateLoggedIn.time)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginEntity> {
        override fun createFromParcel(parcel: Parcel): LoginEntity {
            return LoginEntity(parcel)
        }

        override fun newArray(size: Int): Array<LoginEntity?> {
            return arrayOfNulls(size)
        }
    }

}
