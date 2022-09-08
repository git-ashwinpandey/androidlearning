package com.example.photonotes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.StringBufferInputStream

@Entity(tableName = "photo_notes_database")
data class PhotoNotes(


    @ColumnInfo(name = "photo_title")
    var title: String,

    @ColumnInfo(name = "photo_description")
    val description: String,

    @ColumnInfo(name = "photo_date")
    val date: String,

    val photo: ByteArray,

    @PrimaryKey(autoGenerate = true)
    var photoID: Long = 0L

)
{


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoNotes

        if (photoID != other.photoID) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (date != other.date) return false
        //if (!photo.contentEquals(other.photo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photoID.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + date.hashCode()
        //result = 31 * result + photo.contentHashCode()
        return result
    }
}