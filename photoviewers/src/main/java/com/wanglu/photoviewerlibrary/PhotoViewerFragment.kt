package com.wanglu.photoviewerlibrary

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_picture.*
import java.text.SimpleDateFormat
import java.util.*

class
PhotoViewerFragment : BaseLazyFragment() {


    var exitListener: OnExitListener? = null
    var longClickListener: OnLongClickListener? = null

    private var mImgSize = IntArray(2)
    private var mExitLocation = IntArray(2)
    private var mInAnim = true
    private var mPicData = ""

    var onclick: OnClick? = null
    private var isShowView = false

    private var time: String? = null
    private var content: String? = null
    private var discussNum: String? = null
    private var paiseNum: String? = null
    private var isShow: Boolean? = null

    var onText : OnView?= null

    /**是否显示*/
    private var isLook = false

    private var all :Int?= null
    var currePage :Int? = null

//    private var constraintLayout: ConstraintLayout?= null
//    private var imageBack:ImageView?= null

    /**
     * 每次选中图片后设置图片信息
     */
    fun setData(imgSize: IntArray, exitLocation: IntArray, picData: String, inAnim: Boolean) {
        mImgSize = imgSize
        mExitLocation = exitLocation
        mInAnim = inAnim
        mPicData = picData
    }

    fun setData(time: String?, content: String?, paiseNum: String?, discussNum: String?, isShow: Boolean?, allNUm: Int?,currePage:Int?,constraintLayout: ConstraintLayout?,imageBack:ImageView?) {
        this.time = time
        this.content = content
        this.paiseNum = paiseNum
        this.discussNum = discussNum
        this.isShow = isShow
        this.all = allNUm
        this.currePage = currePage
//        this.constraintLayout = constraintLayout
//        this.imageBack = imageBack
    }

    fun showView(isShow: Boolean?) {
        this.isLook = isShow!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.item_picture, container, false)
    }

    interface OnExitListener {
        fun exit()
    }

    interface OnClick {
        fun onclick()
    }

    interface OnView{
        fun onView(curr:Int?)
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onLazyLoad() {


        if (PhotoViewer.mInterface != null) {
            PhotoViewer.mInterface!!.show(mIv, mPicData)
        } else {
            throw RuntimeException("请设置图片加载回调 ShowImageViewInterface")
        }

        var alpha = 1f  // 透明度
        mIv.setExitLocation(mExitLocation)
        mIv.setImgSize(mImgSize)
        mIv.setOnLongClickListener {
            if (longClickListener != null) {
                longClickListener!!.onLongClick(it)
            }
            true
        }

        // 循环查看是否添加上了图片
        Thread(Runnable {
            while (true) {
                if (mIv.drawable != null) {
                    activity!!.runOnUiThread {
                        loading.visibility = View.GONE
                    }
                    break
                }
                Thread.sleep(300)
            }
        }).start()

        var intAlpha = 255
        root.background.alpha = 250
        mIv.rootView = root
        mIv.setOnViewFingerUpListener {
            alpha = 1f
            intAlpha = 255
        }

        // 注册退出Activity 滑动大于一定距离后退出
        mIv.setExitListener {
            if (exitListener != null) {
                exitListener!!.exit()
            }
        }


        tv_date_time.text = "${Utils.getTodayDateTime3()} ${Utils.getTodayDateTime2()}"
        tv_content.text = content
        if (isShow!!) {
            val com = ContextCompat.getDrawable(context!!, R.mipmap.paise_red)
            com?.bounds = Rect(0, 0, com?.minimumWidth!!, com.minimumHeight)
            tv_dianzan.setCompoundDrawables(com, null, null, null)
        } else {
            val com = ContextCompat.getDrawable(context!!, R.mipmap.paise)
            com?.bounds = Rect(0, 0, com?.minimumWidth!!, com.minimumHeight)
            tv_dianzan.setCompoundDrawables(com, null, null, null)
        }
        tv_dianzan.text = paiseNum
        tv_discuss.text = discussNum

        img_back.setImageResource(R.mipmap.nav_icon_back_white)


        PhotoViewer.onCurr = object :OnView, PhotoViewer.OnView {
            override fun onView(curr: Int?) {
                currePage = curr

            }
        }

        tv_right_num.text = "${currePage}/${all}"
        // 添加点击进入时的动画
        if (mInAnim)
            mIv.post {

                val scaleOa = ObjectAnimator.ofFloat(mIv, "scale", mImgSize[0].toFloat() / mIv.width, 1f)
                val xOa = ObjectAnimator.ofFloat(mIv, "translationX", mExitLocation[0].toFloat() - mIv.width / 2, 0f)
                val yOa = ObjectAnimator.ofFloat(mIv, "translationY", mExitLocation[1].toFloat() - mIv.height / 2, 0f)

                val set = AnimatorSet()
                set.duration = 250
                set.playTogether(scaleOa, xOa, yOa)
                set.start()
            }


        root.isFocusableInTouchMode = true
        root.requestFocus()
        root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                mIv.exit()

                return@OnKeyListener true
            }
            false
        })

        mIv.setOnViewDragListener { dx, dy ->

            mIv.scrollBy((-dx).toInt(), (-dy).toInt())  // 移动图像
            alpha -= dy * 0.001f
            intAlpha -= (dy * 0.5).toInt()
            if (alpha > 1) alpha = 1f
            else if (alpha < 0) alpha = 0f
            if (intAlpha < 0) intAlpha = 0
            else if (intAlpha > 250) intAlpha = 250
            root.background.mutate().alpha = intAlpha    // 更改透明度

            if (alpha >= 0.6)
                mIv.attacher.scale = alpha   // 更改大小


        }

        if (isLook) {
            isShowView = !isShowView
            if (isShowView) {
                constraint_top.visibility = View.VISIBLE
                constraint_bt.visibility = View.VISIBLE
            } else {
                constraint_top?.visibility = View.GONE
                constraint_bt.visibility = View.GONE
            }
        } else {
            constraint_top?.visibility = View.GONE
            constraint_bt.visibility = View.GONE
        }

        mIv.setOnClickListener {
            if (isLook) {
                isShowView = !isShowView
                if (isShowView) {
                    constraint_top?.visibility = View.VISIBLE
                    constraint_bt.visibility = View.VISIBLE
                } else {
                    constraint_top?.visibility = View.GONE
                    constraint_bt.visibility = View.GONE
                }

            } else {
                mIv.exit()
            }
        }

        constraint_bt.setOnClickListener {
            onclick?.onclick()
        }


        img_back?.setOnClickListener {
            mIv.exit()
        }

    }

}