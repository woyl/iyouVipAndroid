package com.jfkj.im.imageviewer

import com.github.iielse.imageviewer.core.DataProvider
import com.github.iielse.imageviewer.core.Photo
import com.jfkj.im.Bean.MyPicPhoto

class MyTestDataProvider(private val initPhoto: Photo) : DataProvider {
    var i = 0;
    var j = 0;

    override fun loadInitial(): List<Photo> {
        return listOf(initPhoto)
    }

    override fun loadAfter(key: Long, callback: (List<Photo>) -> Unit) {
        if (i == 0){
            i++
            if (initPhoto is MyPicPhoto) {
                initPhoto.queryAfter(key, callback)
            }
        }

    }

    override fun loadBefore(key: Long, callback: (List<Photo>) -> Unit) {
        if (j == 0){
            j++
            if (initPhoto is MyPicPhoto) {
                initPhoto.queryBefore(key, callback)
            }
        }
    }
}

