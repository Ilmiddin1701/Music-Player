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
        cursor?.use {
            val id = it.getColumnIndex(MediaStore.Audio.Media._ID)
            val title = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artist = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val data = it.getColumnIndex(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val musicId: Long = it.getLong(id)
                val musicName: String = it.getString(title)
                val singer: String = it.getString(artist)
                val music: String = it.getString(data)

                list.add(MusicData(musicId, musicName, "", music, singer, 0))
            }
        }
        return list
    }
}