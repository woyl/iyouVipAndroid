package com.jfkj.im.imageviewer

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.jfkj.im.Bean.MyPicPhoto
import com.luck.picture.lib.tools.PictureFileUtils.saveBitmapFile
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import java.io.File

class MySimpleLoader : ImageLoader {
    override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        val it = (data as? MyPicPhoto?)?.url!!
        Glide.with(view).load(it)
                .override(view.width, view.height)
                .placeholder(view.drawable)
                .into(view)
    }

//    override fun load(subsamplingView: SubsamplingScaleImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
//        runOnIOThread {
//            val fileName = "data_subsampling_${data.id()}"
//            val file = File(appContext().cacheDir, fileName)
//            if (!file.exists()) {
//                val it = (data as? MyPicPhoto?)?.url!!
//
//                try {
//                    val s: Bitmap = Glide.with(appContext()).asBitmap().load(it).submit().get()
//                    saveBitmapFile(s, file)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    e.message?.let(::toast)
//                }
//            }
//
//            runOnUIThread {
//                subsamplingView.setImage(ImageSource.uri(Uri.fromFile(file)))
//            }
//        }
//    }
}
