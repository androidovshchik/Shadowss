package domain.shadowss.local

import androidx.room.Database
import androidx.room.RoomDatabase
import domain.shadowss.BuildConfig
import domain.shadowss.models.TxtData

@Database(
    entities = [
        TxtData::class
    ],
    version = BuildConfig.DB_VERSION
)
abstract class Database : RoomDatabase() {

    abstract fun txtDao(): TxtDao
}