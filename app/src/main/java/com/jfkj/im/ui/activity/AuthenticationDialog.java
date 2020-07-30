package com.jfkj.im.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.citypick.CityBean;
import com.jfkj.im.citypick.DistrictBean;
import com.jfkj.im.citypick.JDCityConfig;
import com.jfkj.im.citypick.JDCityPicker;
import com.jfkj.im.citypick.OnCityItemClickListener;
import com.jfkj.im.citypick.ProvinceBean;
import com.jfkj.im.data.DataProvide;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.CityDialog;
import com.jfkj.im.ui.dialog.PickDialog;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class AuthenticationDialog extends Dialog {
    TextView tv_height, tv_weight, tv_education, tv_hometown,tv_delete;
    EditText et_describe;
    RelativeLayout rl_height, rl_weight, rl_education, rl_hometown;
    public JDCityConfig.ShowType mWheelType = JDCityConfig.ShowType.PRO_CITY;
    private JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();
    private JDCityPicker cityPicker;
    private CityDialog mCityDialog = null;
     Activity activity;
    public AuthenticationDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    Button btn_commit;
    String proData = null;
    String cityData = null;
    private String hometown;
    Map<String, String> map = new HashMap<>();
    private PickDialog weightDialog = null;
    Map<String,String> headmap=new HashMap<>();
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogauthentication);
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        tv_education = findViewById(R.id.tv_education);
        tv_hometown = findViewById(R.id.tv_hometown);
        et_describe = findViewById(R.id.et_describe);
        tv_delete= findViewById(R.id.tv_delete);
        rl_height = findViewById(R.id.rl_height);
        rl_weight = findViewById(R.id.rl_weight);
        rl_education = findViewById(R.id.rl_education);
        rl_hometown = findViewById(R.id.rl_hometown);
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mCityDialog = new CityDialog(getContext());
        cityPicker = new JDCityPicker();
        cityPicker.init(activity);
        cityPicker.setConfig(jdCityConfig);
        btn_commit = findViewById(R.id.btn_commit);
        headmap.put(Utils.TOKEN, SPUtils.getInstance(getContext()).getString(Utils.TOKEN));
        tv_delete.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.post()
                        .tag(getContext())
                        .url(ApiStores.base_url+"/my/editorIndividual")
                        .headers(headmap)
                        .params(map)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    ToastUtils.showLongToast(jsonObject.getString("message"));
                                    if(jsonObject.getString("code").equals("200")){
                                       // UIn
                                        dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        et_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(et_describe.getText().toString().trim())){
                    map.put("signature",et_describe.getText().toString().trim());
                }
                if (map.size() ==8) {
                    btn_commit.setAlpha(1.0f);
                    btn_commit.setEnabled(true);
                } else {
                    btn_commit.setAlpha(0.5f);
                    btn_commit.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rl_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightDialog = new PickDialog(getContext(), R.style.dialogstyle, "选择身高", DataProvide.getHeightList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        map.put("Height", pickStr);
                        tv_height.setText(pickStr);
                        tv_height.setTextColor(Color.parseColor("#999999"));
                        if (map.size() == 8) {
                            btn_commit.setAlpha(1.0f);
                            btn_commit.setEnabled(true);
                        } else {
                            btn_commit.setAlpha(0.5f);
                            btn_commit.setEnabled(false);
                        }
                    }
                });
                weightDialog.show();

            }
        });
        rl_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightDialog = new PickDialog(getContext(), R.style.dialogstyle, "选择体重", DataProvide.getWeightList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        //todo 提交参数
                        ToastUtils.showShortToast("选择了" + pickStr);
                        map.put("weight", pickStr);
                        tv_weight.setText(pickStr);
                        tv_weight.setTextColor(Color.parseColor("#999999"));
                        if (map.size() ==8) {
                            btn_commit.setAlpha(1.0f);
                            btn_commit.setEnabled(true);
                        } else {
                            btn_commit.setAlpha(0.5f);
                            btn_commit.setEnabled(false);
                        }
                    }
                });
                weightDialog.show();
            }
        });
        rl_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightDialog = new PickDialog(getContext(), R.style.dialogstyle, "选择学历", DataProvide.getEducationList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        map.put("educational", pickStr);
                        tv_education.setText(pickStr);
                        tv_education.setTextColor(Color.parseColor("#999999"));
                        if (map.size() ==8) {
                            btn_commit.setAlpha(1.0f);
                            btn_commit.setEnabled(true);
                        } else {
                            btn_commit.setAlpha(0.5f);
                            btn_commit.setEnabled(false);
                        }
                    }
                });
                weightDialog.show();
            }
        });
        rl_hometown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mWheelType = JDCityConfig.ShowType.PRO_CITY;
                jdCityConfig.setShowType(mWheelType);
                cityPicker.showCityPicker();

                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        if (province != null) {
                            proData = "name:  " + province.getName() + "   id:  " + province.getId();
                            proData = province.getName();
                        }
                        if (city != null) {
                            cityData = "name:  " + city.getName() + "   id:  " + city.getId();
                            cityData = city.getName();
                        }
                        String districtData = null;
                        if (district != null) {
                            districtData = "name:  " + district.getName() + "   id:  " + district.getId();
                        }
                        if (mWheelType == JDCityConfig.ShowType.PRO_CITY_DIS) {
                            ToastUtils.showShortToast(proData + cityData);
                        } else {
                            ToastUtils.showShortToast(proData + cityData);
                        }
                        hometown = proData + "·" + cityData;
                        map.put("hometown", hometown);
                        show();
                        tv_hometown.setText(hometown);
                        tv_hometown.setTextColor(Color.parseColor("#999999"));
                        if (map.size() ==8) {
                            btn_commit.setAlpha(1.0f);
                            btn_commit.setEnabled(true);
                        } else {
                            btn_commit.setAlpha(0.5f);
                            btn_commit.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });

    }
}
