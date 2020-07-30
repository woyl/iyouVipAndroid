package com.jfkj.im.ui.activity

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jfkj.im.R
import com.jfkj.im.adapter.CommonRecyclerAdapter
import com.jfkj.im.adapter.CommonRecyclerHolder
import kotlinx.android.synthetic.main.activity_look_circle_pic.*
import java.util.ArrayList

class LookCirclePicActivity : AppCompatActivity() {

    private var adpter : CommonRecyclerAdapter<String> ?= null
    private var list = mutableListOf<String>()
    private var position = 0;
    private var activity : Activity?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = intent.getIntExtra("position",0)
        list = intent.getStringArrayListExtra("list")
        setContentView(R.layout.activity_look_circle_pic)
        activity = this
        val ori = LinearLayoutManager(this)
        ori.orientation = LinearLayoutManager.HORIZONTAL
//        rv.layoutManager = ori
//
//        adpter = object : CommonRecyclerAdapter<String>(this,list,R.layout.item_look_circle_pic){
//            override fun convert(holder: CommonRecyclerHolder?, t: String?, position: Int) {
//                val img = holder?.getView(R.id.img) as ImageView;
//                Glide.with(mContext).load(t).into(img)
//                holder.itemView.setOnClickListener {
//                    PhotoViewer
//                            .setData(list as ArrayList<String>)
//                            .setCurrentPage(position)
//                            .setImgContainer( rv)
//                            .setShowImageViewInterface(object : PhotoViewer.ShowImageViewInterface {
//                                override fun show(iv: ImageView, url: String) {
//
//                                    // 设置自己加载图片的框架来加载图片
//                                    Glide.with(iv.context).load(url).into(iv)
//                                }
//                            })
//                            .setOnLongClickListener(object : OnLongClickListener {
//                                override fun onLongClick(view: View) {
//                                    // 长按图片的逻辑
//                                }
//                            })
//                            .start(activity as LookCirclePicActivity)
//                }
//            }
//        }
//        rv.adapter = adpter
//
//        rv.setCloseViewListener {
////            finish()
//        }
    }
}
