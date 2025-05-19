package com.example.flo_clone

import android.content.Context
import com.example.flo_clone.FloDatabase

class UserRepository(context: Context) {
    private val userDao = FloDatabase.getInstance(context)!!.userDao()

    fun getAllUsers(): List<User> {
        return userDao.getAll()
    }

    fun insert(user: User) {
        userDao.insert(user)
    }

    fun getUserByEmail(email: String): User? {
        return userDao.getUsers().firstOrNull { it.email == email }
    }

}