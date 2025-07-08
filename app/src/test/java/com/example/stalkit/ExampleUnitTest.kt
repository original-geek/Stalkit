package com.example.stalkit

import android.util.Log
import com.example.stalkit.data.login.LoginHelper
import com.example.stalkit.data.login.LoginStatus
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    @Test
    fun testParsing() {
        val s = "https://oauth.vk.com/blank.html#access_token=vk1.a.blah_blah-blah12345678ololololo&expires_in=98765&user_id=1234567"
        val res = LoginHelper.checkAuthUrl(s)
        val token = LoginHelper.parseVariable(s, "access_token")
        assertEquals("vk1.a.blah_blah-blah12345678ololololo", token)
        val expires = LoginHelper.parseVariable(s, "expires_in")
        assertEquals("98765", expires)
        val id = LoginHelper.parseVariable(s, "user_id")
        assertEquals("1234567", id)
        assertTrue(res is LoginStatus.Succeed)
        assertTrue((res as LoginStatus.Succeed).token.userId.toLongOrNull() != null)
    }

}