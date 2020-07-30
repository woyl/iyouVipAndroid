package com.jfkj.im.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.jfkj.im.R
import com.jfkj.im.TIM.component.picture.imageEngine.impl.GlideEngine
import com.jfkj.im.TIM.redpack.group.GroupAddOrDelMeberActivity
import com.jfkj.im.TIM.redpack.group.GroupMemberInfoBean
import com.jfkj.im.adapter.CommonRecyclerAdapter
import com.jfkj.im.adapter.CommonRecyclerHolder
import com.jfkj.im.mvp.BaseActivtiy
import com.jfkj.im.ui.home.UserDetailActivity
import kotlinx.android.synthetic.main.activity_group_member.*
import kotlinx.android.synthetic.main.include_base_back_blacd_title.*
import java.util.ArrayList

class GroupMemberActivity : BaseActivtiy(), View.OnClickListener {

    private var datas : MutableList<GroupMemberInfoBean>?= null

    private var dataNo = mutableListOf<GroupMemberInfoBean>()
    object Nums {
        val ADD_TYPE = -100
        val DEL_TYPE = -101
    }

    private var isGroupOwner = false
    private var groupId :String?= null

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_member)
        fullScreen(this)
        datas = intent.getParcelableArrayListExtra("data")

        isGroupOwner = intent.getBooleanExtra("isGroupOwner",false)
        groupId = intent.getStringExtra("groupId")
        iv_title_back.setOnClickListener(this)

        datas?.let {
            if (it.size > 0) {
                tv_title.text = "俱乐部成员(${it.size})"
            } else {
                tv_title.text = "俱乐部成员"
            }
        }

        if (!isGroupOwner) {
            datas?.filterNot { it.type == Nums.ADD_TYPE && it.type == Nums.DEL_TYPE}?.forEach { dataNo.add(it) }
        } else {
            datas?.let {
                val groupBean1 = GroupMemberInfoBean()
                groupBean1.type = Nums.ADD_TYPE
                val groupBean = GroupMemberInfoBean()
                groupBean.type = Nums.DEL_TYPE
                dataNo.addAll(datas as ArrayList<GroupMemberInfoBean>)
                dataNo.add(groupBean1)
                dataNo.add(groupBean)
            }
        }

        recyc_list.layoutManager = GridLayoutManager(this,5)
        recyc_list.adapter = object :CommonRecyclerAdapter<GroupMemberInfoBean>(this, dataNo, R.layout.group_member_adpater){
            override fun convert(holder: CommonRecyclerHolder?, t: GroupMemberInfoBean?, position: Int) {
                holder?.let {
                    val memberIcon = holder.getView<ImageView>(R.id.group_member_icon)
                    val memberName = holder.getView<TextView>(R.id.group_member_name)
                    val tv_group_z = holder.getView<TextView>(R.id.tv_group_z)

                    t?.let {
                        memberName.text = t.nickName
                        if (t.isOwner == 1) {
                            tv_group_z.visibility = View.VISIBLE
                        } else {
                            tv_group_z.visibility = View.GONE
                        }
                        GlideEngine.loadImage(memberIcon, t.head, null)

                        holder.itemView.setOnClickListener {
                            val intent = Intent(mContext, UserDetailActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("userId", t.userId)
                            startActivity(intent)
                        }

                        if (it.type == Nums.DEL_TYPE) {
                            memberIcon.setImageResource(R.mipmap.club_icon_groupdelete_gray)
                            memberIcon.setOnClickListener {
                                val intent = Intent(mContext, GroupAddOrDelMeberActivity::class.java)
                                        .putExtra("add", false)
                                        .putExtra("groupId", groupId)
                                startActivity(intent)
                            }

                        }

                        if (it.type == Nums.ADD_TYPE) {
                            memberIcon.setOnClickListener {
                                val intent = Intent(mContext, GroupAddOrDelMeberActivity::class.java)
                                        .putExtra("add", true)
                                        .putExtra("groupId", groupId)
                                startActivity(intent)
                            }
                            memberIcon.setImageResource(R.mipmap.club_icon_groupadd_gray)

                        }
                    }

                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_title_back -> finish()
        }
    }
}