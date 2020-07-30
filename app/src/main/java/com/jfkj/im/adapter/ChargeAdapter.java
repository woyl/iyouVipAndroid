package com.jfkj.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.WebActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/23
 * </pre>
 */
public class ChargeAdapter extends RecyclerView.Adapter<ChargeAdapter.ViewHolder> {

//    private List<ChargeBean> mList = new ArrayList<>();
    private Context mContext;

    private ExChangeBean bean;
    onClick click;

    public ChargeAdapter(Context context) {
        this.mContext = context;

    }
    public void setBean(ExChangeBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    public void setClick(onClick click) {
        this.click = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_charge_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < bean.getData().size()) {
            ExChangeBean.DataBean dataBean = bean.getData().get(position);
            holder.mTvDiamond.setText(dataBean.getImoney() +"" );
            holder.mTvMoney.setText( dataBean.getIexchangemoney() + "元");
            holder.mTvDiamond.setVisibility(View.VISIBLE);
            holder.mTvMoney.setVisibility(View.VISIBLE);
            holder.mTvService.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   ToastUtils.showShortToast("需要充值" + dataBean.getIexchangemoney());
                    Intent intent = new Intent("paymoney");
                    intent.putExtra(Utils.MONEY, dataBean.getIexchangemoney() + "");
                    intent.putExtra("cexchangeid",dataBean.getCexchangeid() );
                    mContext.sendBroadcast(intent);
                    click.onclickitem(v,position);
                }

            });
        } else {
            holder.mTvDiamond.setVisibility(View.GONE);
            holder.mTvMoney.setVisibility(View.GONE);
            holder.mTvService.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> querymap = new HashMap<>();

                    querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                    querymap.put(Utils.OSNAME, Utils.ANDROID);
                    querymap.put(Utils.CHANNEL, Utils.ANDROID);
                    querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
                    querymap.put(Utils.DEVICEID, Utils.ANDROID);
                    querymap.put(Utils.REQTIME, AppUtils.getReqTime());

                    OkGo.<String>post(ApiStores.base_url + "/my/customerService")
                            .tag(this)
                            .headers(Utils.TOKEN, SPUtils.getInstance(mContext).getString(Utils.TOKEN))
                            .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                            .params(querymap)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    OkLogger.e(response.body());
                                    CustomerServiceBean customerServiceBean = GsonUtils.fromJson(response.body(), CustomerServiceBean.class);
                                    customerServiceBean.getData().getUrl();

                                    Intent uintent = new Intent(mContext, WebActivity.class);
                                    uintent.putExtra("title", "在线客服");
                                    uintent.putExtra("url", customerServiceBean.getData().getUrl());
                                    mContext.startActivity(uintent);
                                }
                            });
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return bean!=null?bean.getData().size() + 1:0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvDiamond;
        TextView mTvMoney;
        TextView mTvService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDiamond = itemView.findViewById(R.id.tv_diamond);
            mTvMoney = itemView.findViewById(R.id.tv_money);
            mTvService = itemView.findViewById(R.id.tv_service);
        }
    }
    public  interface onClick{
        public void onclickitem(View view,int positon);
    }
}
