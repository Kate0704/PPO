package ppo.tabata.utility

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ppo.tabata.data.TabataDatabase
import ppo.tabata.data.TabataRepository

class TabataApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { TabataDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TabataRepository(database.tabataDao()) }
}
