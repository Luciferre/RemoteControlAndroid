package com.gs.remotecontrol.bean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.remotecontrolandroid.R;

public class GridViewAdapter extends BaseAdapter{

	private Context mContext;
	private String[] descs;
	private int[] drables;
	
	public GridViewAdapter(Context mContext,String[] descs,int[] drables) {
		this.mContext=mContext;
		this.descs=descs;
		this.drables=drables;
	}
	@Override
	public int getCount() {
		return  descs.length;
	}

	@Override
	public Object getItem(int p) {
		return p;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null!=convertView){
			
		}else{
			convertView=View.inflate(mContext, R.layout.grid_view_item, null);
		}
		((TextView)convertView.findViewById(R.id.textViewId)).setText(descs[position]);
	    ((ImageView)convertView.findViewById(R.id.imageViewGridItem)).setImageResource(drables[position]);
		
	    
		return convertView;
	}
}
