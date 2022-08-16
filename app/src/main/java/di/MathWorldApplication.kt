package di

import android.app.Application
import database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import repository.MathWorldRepository

class MathWorldApplication: Application() {

    val scope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getAppDatabase(this, scope) }
    val repository by lazy { MathWorldRepository(database.classDAO()) }
}