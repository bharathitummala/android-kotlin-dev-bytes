package com.example.android.devbyteviewer.repository

import android.widget.Switch
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.devbyteviewer.database.VideoDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository (private val database: VideoDatabase){
    val videos: LiveData<List<Video>> = database.videoDao.getVideos().map{
        it.asDomainModel()
    }
    suspend fun refreshVideos(){
        withContext(Dispatchers.IO){
            val playList = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playList.asDatabaseModel())
        }
    }
}