package domain.shadowss.local

import androidx.room.Database
import androidx.room.RoomDatabase
import domain.shadowss.models.TxtData

@Database(
    entities = [
        TxtData::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun baseDao(): BaseDao
}