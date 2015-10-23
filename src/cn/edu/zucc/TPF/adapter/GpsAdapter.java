package cn.edu.zucc.TPF.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.AlarmDealBean;
import cn.edu.zucc.TPF.GpsApp.ItemActivity;
import cn.edu.zucc.TPF.GpsApp.R;

/**
 * Created by aqi on 15/9/26.
 */
public class GpsAdapter extends SimpleAdapter {
    private Button mDealBtn;
    private Context mContext;
    private AlarmDealBean mAlarmDeal;
    private List<Map<String, Object>> mNoticeList;

    public GpsAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,
                          List<Map<String, Object>> noticeList) {
        super(context, data, resource, from, to);
        this.mContext = context;
        this.mNoticeList = noticeList;
        mAlarmDeal = new AlarmDealBean();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int mPosition = position;
        convertView = super.getView(position, convertView, parent);



        mDealBtn = (Button) convertView.findViewById(R.id.deal);
        mDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemActivity.class);
                Bundle b = new Bundle();

                b.putCharSequence("liftid",mNoticeList.get(mPosition).get("liftid")+"");
                b.putCharSequence("time",mNoticeList.get(mPosition).get("unusualtime")+"");
                b.putCharSequence("address",mNoticeList.get(mPosition).get("address")+"");
                b.putCharSequence("latitude",mNoticeList.get(mPosition).get("latitude")+"");
                b.putCharSequence("longtitude",mNoticeList.get(mPosition).get("longtitude")+"");
                b.putCharSequence("warninglevel", mNoticeList.get(mPosition).get("warninglevel")+"");
                intent.putExtras(b);
                mContext.startActivity(intent);



            }
        });
        return convertView;
    }




}
