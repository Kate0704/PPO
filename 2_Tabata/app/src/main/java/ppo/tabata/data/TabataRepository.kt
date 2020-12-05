package ppo.tabata.data

import androidx.lifecycle.LiveData

class TabataRepository(private val tabataDao : TabataDAO) {
    val allTabatas : LiveData<List<TabataEntity>> = tabataDao.getTabatas()

    suspend fun insertTabata(tabata: TabataEntity){
        tabataDao.insertTabata(tabata)
    }
    suspend fun updateTabata(tabata: TabataEntity){
        tabataDao.updateTabata(tabata)
    }
    suspend fun deleteTabata(tabata: TabataEntity){
        tabataDao.deleteTabata(tabata)
    }
    suspend fun clear(){
        tabataDao.clear()
    }

}