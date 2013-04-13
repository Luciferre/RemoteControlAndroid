package com.gs.remotecontrol.bean;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.remotecontrolandroid.R;

public class AdapterCharactor extends BaseAdapter{
	private List<Character> mArrayListCharacters;
	private Context mContext = null;

	public AdapterCharactor(Context mContext,List<Character> arrayListCharactor) {
		
		this.mArrayListCharacters = arrayListCharactor;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mArrayListCharacters.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View viewItem = null;
		if (null != convertView) {
			viewItem = convertView;
		} else {
			viewItem = View.inflate(this.mContext, R.layout.list_view_item,null);
		}
		TextView tv = (TextView) viewItem.findViewById(R.id.textViewChracter);
		tv.setText(mArrayListCharacters.get(position).toString());
		return viewItem;
	}

}
