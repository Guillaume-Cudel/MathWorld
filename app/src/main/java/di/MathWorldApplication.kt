package di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import repository.MathWorldRepository

class MathWorldApplication: Application() {

    private val scope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getAppDatabase(this, scope) }
    val repository by lazy { MathWorldRepository(database.classDAO()) }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}