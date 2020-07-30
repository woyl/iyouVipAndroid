package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.jfkj.im.R;
import com.jfkj.im.data.DataProvide;

import java.util.Calendar;
import java.util.List;


/**
 * @author Administrator
 */
public class SelecTimeDialog extends Dialog implements OnClickListener {

    private Context context;
    private CustomizeDialogListener listener;
    private View mView;
    private boolean mOneButton = false;
    private ArrayWheelAdapter arrayWheelAdapter;


    private String strMonth = "";

    private String strDay = "";

    private String strHour = "00:00上午";
    private List<String> day;

    private int dayIndex = 0;

    public interface CustomizeDialogListener {
        void onClick(View view , String time);
    }



    public SelecTimeDialog(Context context, int theme, CustomizeDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.layout_select_time_item, null);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 1.0f);//设置布局属性占满宽度
        //        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.BOTTOM;//设置dialog 居中
        window.setAttributes(params);
        initView();
    }

    private void initView() {

        mView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mView.findViewById(R.id.tv_submit).setOnClickListener(this);

        WheelView wv_month =  mView.findViewById(R.id.wv_month);
        WheelView wv_day  =  mView.findViewById(R.id.wv_day);
        WheelView wv_hour_minute  = mView.findViewById(R.id.wv_hour_minute);


        wv_month.setTextSize(14);
        wv_day.setTextSize(14);
        wv_hour_minute.setTextSize(14);

        //获取月
        List<String> month = DataProvide.getMonth();

        Calendar cal = Calendar.getInstance();
        int presentMonth = cal.get(Calendar.MONTH) + 1;

        strDay =  cal.get(Calendar.DAY_OF_MONTH) + "日";  //当前日

        strMonth =  presentMonth + "月";


        //获取日
        day = DataProvide.getDay(presentMonth);


        //获取时分
        List<String> hourMinute = DataProvide.getHourMinute();


        wv_month.setCyclic(false);
        wv_month.setAdapter(new ArrayWheelAdapter(month));
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                String s = month.get(index);
                // arrayWheelAdapter.refresh(    DataProvide.getDay(Integer.parseInt(s.substring(0,1))));

                day = DataProvide.getDay(Integer.parseInt(s.substring(0,1)));

                arrayWheelAdapter = new ArrayWheelAdapter(day);
                wv_day.setAdapter(arrayWheelAdapter);
                strMonth = month.get(index);
                strDay =  day.get(dayIndex);
            }
        });

        arrayWheelAdapter = new ArrayWheelAdapter(day);
        wv_day.setCyclic(false);
        wv_day.setAdapter(arrayWheelAdapter);
        wv_day.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                dayIndex = index;
                strDay = day.get(index);
            }
        });


        wv_hour_minute.setCyclic(false);
        wv_hour_minute.setAdapter(new ArrayWheelAdapter(hourMinute));
        wv_hour_minute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                strHour = hourMinute.get(index);
            }
        });


    }


    @Override
    public void onClick(View v) {
        listener.onClick(v,strMonth+" "   + strDay + " " + strHour);
    }
}
