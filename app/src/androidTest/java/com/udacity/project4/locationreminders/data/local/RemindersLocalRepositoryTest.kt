package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//     : Add testing implementation to the RemindersLocalRepository.kt

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @Before
    fun setup() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao())
    }

    @After
    fun cleanUp() = database.close()

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    //  : Replace with runBlockingTest once issue is resolved
    @Test
    fun testInsertRetrieveData() = runBlocking {

        val data = ReminderDTO(
            "title",
            "description",
            "location",
            7127.00,
            7217.00)

        repository.saveReminder(data)

        val result = repository.getReminder(data.id)

        result as Result.Success
         assertThat(result.data != null, CoreMatchers.`is`(true))

        val loadedData = result.data
         assertThat(loadedData.id, CoreMatchers.`is`(data.id))
         assertThat(loadedData.title, CoreMatchers.`is`(data.title))
         assertThat(loadedData.description, CoreMatchers.`is`(data.description))
         assertThat(loadedData.location, CoreMatchers.`is`(data.location))
         assertThat(loadedData.latitude, CoreMatchers.`is`(data.latitude))
         assertThat(loadedData.longitude, CoreMatchers.`is`(data.longitude))
    }

    @Test
    fun testDataNotFound_returnError() = runBlocking {
        val result = repository.getReminder("0000")
        val error =  (result is Result.Error)
        assertThat(error, CoreMatchers.`is`(true))
    }


}