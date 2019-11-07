package domain.shadowss.models

import androidx.room.*
import org.joda.time.DateTime
import java.io.File
import java.io.Serializable

@Entity(
    tableName = "photo_events",
    foreignKeys = [
        ForeignKey(
            entity = AccessToken::class,
            parentColumns = ["t_id"],
            childColumns = ["pe_token_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TxtData::class,
            parentColumns = ["pe_id"],
            childColumns = ["pe_related_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["pe_token_id"]),
        Index(value = ["pe_related_id"])
    ]
)
class TxtData() : Serializable, SendEvent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pe_id")
    override var id: Long? = null

    /**
     * May be changed
     */
    @ColumnInfo(name = "pe_token_id")
    override var tokenId = 0L

    @ColumnInfo(name = "pe_kp_id")
    var kpId: Int? = null

    @ColumnInfo(name = "pe_linked_id")
    var linkedId: Int? = null

    /**
     * It's value is not [kpId] but it is [id] of parent platform
     */
    @ColumnInfo(name = "pe_related_id")
    var relatedId: Long? = null

    /**
     * Normally it will never change
     */
    @ColumnInfo(name = "pe_type")
    var type = -1

    /**
     * Normally it will never change
     */
    @ColumnInfo(name = "pe_path")
    lateinit var path: String

    @ColumnInfo(name = "pe_latitude")
    var latitude = 0.0

    @ColumnInfo(name = "pe_longitude")
    var longitude = 0.0

    /**
     * [ru.iqsolution.tkoonline.PATTERN_DATETIME]
     */
    @ColumnInfo(name = "pe_when_time")
    lateinit var whenTime: DateTime

    /**
     * This is ready only after platform's clean event was triggered
     */
    @ColumnInfo(name = "pe_ready")
    var ready = false

    @ColumnInfo(name = "pe_sent")
    override var sent = false

    constructor(typeId: Int) : this() {
        type = typeId
        // required for initialization only
        path = ""
        whenTime = DateTime.now()
    }

    constructor(kp: Int, typeId: Int) : this(typeId) {
        kpId = kp
    }

    fun toFile() = File(path)
}