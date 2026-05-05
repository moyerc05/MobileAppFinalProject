package edu.moravian.csci395.flashfocus

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.moravian.csci395.flashfocus.data.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("study.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}