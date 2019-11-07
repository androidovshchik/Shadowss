package domain.shadowss.local

import androidx.room.Dao
import androidx.room.Query
import domain.shadowss.models.TxtData

@Dao
interface TxtDao {

    @Query(
        """
        SELECT * FROM txt_data
    """
    )
    fun getAll(): List<TxtData>
}