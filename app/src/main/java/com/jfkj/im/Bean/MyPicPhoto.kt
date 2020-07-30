package com.jfkj.im.Bean

import android.os.Handler
import android.os.Looper
import com.github.iielse.imageviewer.core.Photo
import com.luck.picture.lib.entity.LocalMedia
import kotlin.math.max
import kotlin.math.min

data class MyPicPhoto(val id: Long, val url: String, val subsampling: Boolean = false) : Photo {
    override fun id() = id
    override fun subsampling() = subsampling

    val myData = mutableListOf<MyPicPhoto>()
    val mainHandler = Handler(Looper.getMainLooper())
//    val pageSize = 5

    fun setData(list: List<MyPicPhoto>) {
        myData.addAll(list);
    }

    fun queryBefore(id: Long, callback: (List<MyPicPhoto>) -> Unit) {
        val idx = id.toInt()
        mainHandler.postDelayed({
            if (idx < 0) {
                callback(emptyList())
                return@postDelayed
            }
            val result = myData.subList(0, idx)
            callback(result)
        }, 100)
    }

    fun queryAfter(id: Long, callback: (List<MyPicPhoto>) -> Unit) {
        val idx = id.toInt()
        mainHandler.postDelayed({
            if (idx < 0) {
                callback(emptyList())
                return@postDelayed
            }
            val result = myData.subList(idx + 1, myData.size)
            callback(result)
        }, 100)
    }


}





