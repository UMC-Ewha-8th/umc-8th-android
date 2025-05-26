package com.example.flo_clone

import androidx.room.*

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


