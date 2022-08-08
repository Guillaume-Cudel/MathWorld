package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dao.ClassDAO
import model.Job
import model.Power
import model.RpgClass
import model.Student

@Database(entities = [RpgClass::class, Student::class, Job::class, Power::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun classDAO(): ClassDAO


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getAppDatabase(context: Context): AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class) {

                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "MathWorld"
                    ).build()
                }
            }
            return INSTANCE
        }

    }
}