package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

//     : Create a fake data source to act as a double to the real data source
var reminderList = mutableListOf<ReminderDTO>()

    private var getError = false

    fun setError(value: Boolean) {
        getError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {
            if(getError) {
                throw Exception("not found Reminders ")
            }
            Result.Success(ArrayList(reminderList))
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderList.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return try {
            val reminder = reminderList.find { it.id == id }
            if (getError || reminder == null) {
                throw Exception("Not found $id")
            } else {
                Result.Success(reminder)
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun deleteAllReminders() {
        reminderList.clear()
    }


}