package com.bonc.mobile.portal.search;

import java.util.Comparator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BaseAdapter<T> extends ArrayAdapter<T> {

	public BaseAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(T object) {
		// TODO Auto-generated method stub
		super.add(object);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}

	@Override
	public void sort(Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		super.sort(comparator);
	}

}
