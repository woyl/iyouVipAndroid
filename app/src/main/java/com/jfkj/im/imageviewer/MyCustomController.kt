package com.jfkj.im.imageviewer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.iielse.imageviewer.ImageViewerActionViewModel
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.adapter.ItemType
import com.github.iielse.imageviewer.core.OverlayCustomizer
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.VHCustomizer
import com.github.iielse.imageviewer.core.ViewerCallback
import com.github.iielse.imageviewer.utils.inflate
import com.jfkj.im.Bean.MyPicPhoto
import com.jfkj.im.R
import com.jfkj.im.entity.MineInfo
import com.jfkj.im.ui.home.DynamicDetailActivity
import com.jfkj.im.utils.DataFormatUtils
import com.jfkj.im.utils.JumpUtil
import me.relex.circleindicator.CircleIndicator

class MyCustomController(private val activity: FragmentActivity) {
    private val viewerActions by lazy { ViewModelProvider(activity).get(ImageViewerActionViewModel::class.java) }

    private var indicatorDecor: View? = null
    private var overlayIndicator: TextView? = null
    private var preIndicator: TextView? = null
    private var nextIndicator: TextView? = null
    private var currentPosition = -1

    private var tv_right_num: TextView? = null

    private var dataInfo: MineInfo.DataBean.CircleArticleBean? = null
    private var allNUm: Int? = null

    private var indicator: CircleIndicator? = null
    private var type: Int? = null
    private var isShow = false
    private var top: RelativeLayout? = null
    private var ll_bt: LinearLayout? = null
    private var tv_content: TextView? = null


    fun setData(data: MineInfo.DataBean.CircleArticleBean?, allNUm: Int?) {
        this.dataInfo = data;
        this.allNUm = allNUm;
    }

    fun setType(type: Int) {
        this.type = type;
    }
    

    fun init(builder: ImageViewerBuilder) {
        builder.setVHCustomizer(object : VHCustomizer {
            override fun initialize(type: Int, viewHolder: RecyclerView.ViewHolder) {
                (viewHolder.itemView as? ViewGroup?)?.let {
                    it.addView(it.inflate(R.layout.item_photo_custom_layout))

                    when (type) {
                        ItemType.SUBSAMPLING -> {
                            val imageView = it.findViewById<View>(R.id.subsamplingView)
                            imageView.setOnClickListener {
                                if (type == 1) {
                                    isShow = !isShow
                                    if (isShow) {
                                        Show()
                                    } else {
                                        Hide()
                                    }
                                } else {
                                    viewerActions.dismiss()
                                }

                            }
                        }
                        ItemType.PHOTO -> {
                            val imageView = it.findViewById<View>(R.id.photoView)
                            imageView.setOnClickListener {
                                if (type == 1) {
                                    isShow = !isShow
                                    if (isShow) {
                                        Show()
                                    } else {
                                        Hide()
                                    }
                                } else {
                                    viewerActions.dismiss()
                                }
                            }
                        }
                    }
                }
            }

            override fun bind(types: Int, data: Photo, viewHolder: RecyclerView.ViewHolder) {
                (viewHolder.itemView as? ViewGroup?)?.let {
                    val x = data as MyPicPhoto
//                    it.findViewById<TextView>(R.id.exText).text = "${x.id}"
                    val tv = it.findViewById<TextView>(R.id.exText)
                    if (type == 1) {
                        tv.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("cuserId", dataInfo?.cuserid)
                            bundle.putString("articleId", dataInfo?.articleId)
                            bundle.putString("startType", "1")
                            JumpUtil.overlay(activity, DynamicDetailActivity::class.java, bundle)
                        }
                    } else {
                        tv.visibility = View.GONE
                    }
                }
            }
        })
        builder.setOverlayCustomizer(object : OverlayCustomizer {
            @SuppressLint("SetTextI18n")
            override fun provideView(parent: ViewGroup): View? {
//                return parent.inflate(R.layout.layout_indicator).also {
//                    indicatorDecor = it.findViewById(R.id.indicatorDecor)
//                    overlayIndicator = it.findViewById(R.id.indicator)
//                    preIndicator = it.findViewById(R.id.pre)
//                    nextIndicator = it.findViewById(R.id.next)
//
//                    preIndicator?.setOnClickListener {
//                        viewerActions.setCurrentItem(currentPosition - 1)
//                    }
//                    nextIndicator?.setOnClickListener {
//                        viewerActions.setCurrentItem(currentPosition + 1)
//                    }
//
//                    it.findViewById<View>(R.id.dismiss).setOnClickListener {
//                        viewerActions.dismiss()
//                    }
//                }
                return parent.inflate(R.layout.layout_pic_look_parent).also {
                    tv_content = it.findViewById(R.id.tv_content)
                    val tv_dianzan = it.findViewById<TextView>(R.id.tv_dianzan)
                    val tv_discuss = it.findViewById<TextView>(R.id.tv_discuss)
                    val img_back = it.findViewById<ImageView>(R.id.img_back)
                    val tv_date_time = it.findViewById<TextView>(R.id.tv_date_time)
                    tv_right_num = it.findViewById(R.id.tv_right_num)
                    top = it.findViewById(R.id.top)
                    ll_bt = it.findViewById(R.id.ll_bt)
                    indicator = it.findViewById(R.id.indicator)
                    if (type == 1) {
                        Hide()
                        if (dataInfo != null) {
                            tv_content?.text = dataInfo?.content
                            if (dataInfo?.commenCount != 0) {
                                tv_dianzan.text = "${dataInfo?.praisecount}"
                            }
                            tv_discuss.text = "${dataInfo?.commenCount}"
                            if (dataInfo?.isPraise == "1") {
                                val com = ContextCompat.getDrawable(activity, R.mipmap.paise_red)
                                com?.bounds = Rect(0, 0, com?.minimumWidth!!, com.minimumHeight)
                                tv_dianzan.setCompoundDrawables(com, null, null, null)
                            } else {
                                val com = ContextCompat.getDrawable(activity, R.mipmap.paise)
                                com?.bounds = Rect(0, 0, com?.minimumWidth!!, com.minimumHeight)
                                tv_dianzan.setCompoundDrawables(com, null, null, null)
                            }
                        }

                        tv_date_time.text = "${DataFormatUtils.getTodayDateTime3()} ${DataFormatUtils.getTodayDateTime2()}"
                        img_back.setOnClickListener {
                            viewerActions.dismiss()
                        }
                    } else {
                        Hide()
                        indicator?.visibility = View.GONE
                    }
                }
            }
        })
        builder.setViewerCallback(object : ViewerCallback {
            override fun onRelease(viewHolder: RecyclerView.ViewHolder, view: View) {
//                viewHolder.itemView.findViewById<View>(R.id.customizeDecor)
//                        .animate().setDuration(200).alpha(0f).start()
//                indicatorDecor?.animate()?.setDuration(200)?.alpha(0f)?.start()
                viewerActions.dismiss()

//                val fl = img
//                val with = fl?.width
//                val height = fl?.height
//                val withs = fl?.measuredWidth
//                val heigths = fl?.measuredHeight
//
//                Log.e("msg", "...............$with")
//                Log.e("msg", "...............$height")
//                Log.e("msg", "...............$withs")
//                Log.e("msg", "...............$heigths")
//                val m = Matrix()
//
//                with?.toFloat()?.let { height?.toFloat()?.let { it1 -> m.postScale(it, it1) } }
//
//
//                val scaleOa = ObjectAnimator.ofFloat(this, "scale", with?.toFloat()!!)
//
//                val xOa = ObjectAnimator.ofFloat(this, "translationX", with / 2f )
//                val yOa = ObjectAnimator.ofFloat(this, "translationY", height!! /2f)
//
//                val set = AnimatorSet()
//                set.duration = 250
//                set.playTogether(scaleOa, xOa, yOa)
//                set.start()
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                currentPosition = position
//                overlayIndicator?.text = position.toString()
                if (type == 1) {
                    tv_right_num?.text = "${position + 1}/${allNUm}"
                } else {
                    indicator?.createIndicators(allNUm!!, position)
                }
            }

            override fun onClick(viewHolder: RecyclerView.ViewHolder, view: View) {
//                super.onClick(viewHolder, view)
                if (type == 1) {
                    isShow = !isShow
                    if (isShow) {
                        Show()
                    } else {
                        Hide()
                    }
                } else {
                    viewerActions.dismiss()
                }
            }
        })
    }

    private fun Hide() {
        top?.visibility = View.GONE
        ll_bt?.visibility = View.INVISIBLE
        tv_content?.visibility = View.INVISIBLE
        indicator?.visibility = View.GONE
    }

    private fun Show() {
        top?.visibility = View.VISIBLE
        ll_bt?.visibility = View.VISIBLE
        tv_content?.visibility = View.VISIBLE
        if (type == 0){
            if (allNUm!! > 1){
                indicator?.visibility = View.VISIBLE
            }
        }
    }
}