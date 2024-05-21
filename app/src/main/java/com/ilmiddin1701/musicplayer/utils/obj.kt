@file:Suppress("DEPRECATION")

package com.ilmiddin1701.musicplayer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import com.ilmiddin1701.musicplayer.models.MusicData

object obj {
    var p: Int? = null
    var musicData: MusicData? = null
    var mediaPlayer: MediaPlayer? = null

    @SuppressLint("Range", "Recycle")
    fun Context.musicFiles(): MutableList<MusicData> {
        val list: MutableList<MusicData> = mutableListOf()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = this.contentResolver.query(
            uri,
            null,
            selection,
            null,
            sortOrder
        )
        if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val imageId: Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
            val authorId: Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
            do {
                val musicId: Long = cursor.getLong(id)
                val musicName: String = cursor.getString(title)
                var musicImage = ""
                if (imageId != -1) {
                    musicImage = cursor.getString(imageId)
                }
                val music: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val singer = cursor.getString(authorId)

                list.add(MusicData(musicId, musicName, musicImage, music, singer, 0))
            } while (cursor.moveToNext())
        }
        return list
    }
}