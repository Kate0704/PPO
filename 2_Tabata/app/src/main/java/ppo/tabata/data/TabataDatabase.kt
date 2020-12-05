package ppo.tabata.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ppo.tabata.R

@Database(entities = [TabataEntity::class], version = 1, exportSchema = false)
abstract class TabataDatabase : RoomDatabase() {

    abstract fun tabataDao(): TabataDAO

    companion object { @Volatile private var INSTANCE: TabataDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TabataDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TabataDatabase::class.java,
                    context.getString(R.string.db_name)
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(TabataDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }

    private class TabataDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.tabataDao())
                }
            }
        }

        suspend fun populateDatabase(tabataDao: TabataDAO) {
            tabataDao.clear()

            val tabata = TabataEntity(
                1, "Test tabata", "#007372", 5,
                5, 5, 3, 2, 5)
            tabataDao.insertTabata(tabata)
        }
    }

}
