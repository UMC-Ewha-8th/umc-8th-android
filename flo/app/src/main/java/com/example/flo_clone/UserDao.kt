package com.example.flo_clone

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flo_clone.User


@Dao
interface UserDao {
    @Insert
    fun insert(user : User)

    @Query("SELECT * FROM UserTable WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM UserTable")
    fun getAllUsers(): List<User>


}