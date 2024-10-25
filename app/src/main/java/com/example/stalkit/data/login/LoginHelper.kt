package com.example.stalkit.data.login

import com.example.stalkit.data.db.LoginEntity
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern


interface LoginStatus {
    data class Succeed(val token: LoginEntity): LoginStatus
    object NotLoggedIn: LoginStatus
}

object LoginHelper {

    fun parseVariable(url: String, name: String): String? {
        val pattern = Pattern.compile("$name=([^&]*)")
        val matcher = pattern.matcher(url)
        if (matcher.find()) {
            return matcher.group(1)
        } else {
            return null
        }
    }

    private fun parseToken(url: String): LoginEntity? {
        val token = parseVariable(url, "access_token") ?: return null
        val expires = parseVariable(url, "expires_in")?.toLongOrNull() ?: return null
        val userId = parseVariable(url, "user_id") ?: return null
        return LoginEntity(token, expires * 1000, Date(), userId)
    }

    /*private fun parseToken(url: String): String? {
        var token: String? = null
        val TOKEN_ASSINGMENT = "access_token="
        val TOKEN_ASSINGMENT_LEN = TOKEN_ASSINGMENT.length
        val tokenInd = url.indexOf(TOKEN_ASSINGMENT)
        if (tokenInd != -1) {
            val s = url.substring(tokenInd + TOKEN_ASSINGMENT_LEN)
            val indOfNextVar = s.indexOf("&")
            if (indOfNextVar != -1) {
                token = s.substring(0, indOfNextVar)
            } else {
                token = s
            }
        }
        return token
    }*/

    fun checkAuthUrl(url: String): LoginStatus {
        return parseToken(url)?.let { LoginStatus.Succeed(it) } ?: LoginStatus.NotLoggedIn
    }

}