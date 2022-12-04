package com.example.testapplication.data.local_db

import androidx.room.*
import com.example.testapplication.domain.models.response.PeopleItemResponse

/**
 * Data Access Object for accessing people_list_table
 * plase check on Room Database standard usage
 */
@Dao
interface PeopleItemDao {
    @Query("Select * from people_list_table")
    fun loadAllUser() : List<PeopleItemResponse>?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers (movie : PeopleItemResponse)
    @Update
    fun updateUser (movie : PeopleItemResponse)
    @Delete
    fun deleteUser (movie : PeopleItemResponse)
    @Query("DELETE FROM people_list_table")
    fun nukeTable()
}