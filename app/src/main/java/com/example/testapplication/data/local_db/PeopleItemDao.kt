package vanrrtech.app.ajaib_app_sample.data.SQDb.github

import androidx.room.*
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemResponse

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