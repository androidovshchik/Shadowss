package domain.shadowss.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "txt_data"
)
class TxtData : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "txt_id")
    var id: Long? = null

    @ColumnInfo(name = "txt_lang_id")
    lateinit var langId: String

    @ColumnInfo(name = "txt_type_id")
    lateinit var typeId: String

    @ColumnInfo(name = "txt_text_id")
    lateinit var textId: String

    @ColumnInfo(name = "txt_text")
    lateinit var text: String
}