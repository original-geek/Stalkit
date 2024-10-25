package com.example.stalkit.data.entities

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.stalkit.data.network.entities.RemoteVideo
import com.google.gson.Gson


data class Video(val id: Int,
                 val player: String,
                 val thumb: String?,
                 val owner_id: Int): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(id)
        p0.writeString(player)
        p0.writeString(thumb)
        p0.writeInt(owner_id)
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }

}

fun RemoteVideo.asVideo(): Video {
    return Video(id, player, findThumbWithMaxSz(), owner_id)
}