package com.example.flo_clone.ui.signin

import androidx.room.*
import com.example.flo_clone.data.entities.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM UserTable")
    fun getAllUsers(): List<User>
}


