package com.ilmiddin1701.musicplayer.models

class MusicData{
    var id: Long? = null
    var musicName: String? = null
    var musicImage: String? = null
    var music: String? = null
    var singer: String? = null
    var isPlaying: Int? = null

    constructor(
        id: Long?,
        musicName: String?,
        musicImage: String?,
        music: String?,
        singer: String?
    ) {
        this.id = id
        this.musicName = musicName
        this.musicImage = musicImage
        this.music = music
        this.singer = singer
    }

    constructor(
        id: Long?,
        musicName: String?,
        musicImage: String?,
        music: String?,
        singer: String?,
        isPlaying: Int?
    ) {
        this.id = id
        this.musicName = musicName
        this.musicImage = musicImage
        this.music = music
        this.singer = singer
        this.isPlaying = isPlaying
    }

    override fun toString(): String {
        return "MusicData(id=$id, musicName=$musicName, musicImage=$musicImage, music=$music, singer=$singer, isPlaying=$isPlaying)"
    }

}