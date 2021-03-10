package com.app.weatherapp.data.local.alarmData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {

        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): AlarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AlarmDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.alarmDao())
                }
            }
        }


        suspend fun populateDatabase(alarmDao: AlarmDao) {
            alarmDao.deleteAll()

            var alarm =
                Alarm(hour = 14, minute = 22)
            alarmDao.insert(alarm)
            alarm = Alarm(hour = 5, minute = 30)
            alarmDao.insert(alarm)
        }
    }
}