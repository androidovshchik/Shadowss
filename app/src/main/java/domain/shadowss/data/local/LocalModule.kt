package domain.shadowss.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val localModule = Kodein.Module("local") {

    bind<Preferences>() with provider {
        Preferences(instance())
    }

    bind<Database>() with eagerSingleton {
        Room.databaseBuilder(instance(), Database::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
    }
}