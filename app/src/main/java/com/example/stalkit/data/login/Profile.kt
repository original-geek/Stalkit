package com.example.stalkit.data.login


data class UserInfoResponse(val response: List<Profile>)


data class Profile(val id: Int, val photo_400_orig: String, val first_name: String, val last_name: String) {

    fun getFullname() = first_name + " " + last_name

}