package com.ums.wifiprobe.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/***
 * 万能的ListView GridView 适配器
 * @author LiuChenShuo
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;
	protected MultiItemTypeSupport<T> mMultiItemSupport;
	
	public CommonAdapter(Context context, List<T> Datas, int itemLayoutId) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mDatas = Datas;
		mItemLayoutId = itemLayoutId;
	}
	
	public CommonAdapter(Context context, List<T> Datas, MultiItemTypeSupport<T> multiItemSupport) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mDatas = Datas;
		mItemLayoutId = 0;
		mMultiItemSupport = multiItemSupport;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		
		if(mMultiItemSupport != null){
			return mMultiItemSupport.getItemViewType(position, mDatas.get(position));
		}
		
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		
		if(mMultiItemSupport != null){
			return mMultiItemSupport.getViewTypeCount();
		}
		
		return super.getViewTypeCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		convert(viewHolder, getItem(position));		
		return viewHolder.getConvertView();
	}

	
	public abstract void convert(ViewHolder helper, T item);
	
	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent){
		if(mMultiItemSupport != null){
			return ViewHolder.get(mContext, convertView, parent, mMultiItemSupport.getLayoutId(position, mDatas.get(position)), position);
		} else{
			return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
		}		
	}
	
	public interface MultiItemTypeSupport<T>
	{
	    int getLayoutId(int position, T t);

	    int getViewTypeCount();

	    int getItemViewType(int position, T t);
	}
	
	public void add(T item) {
		mDatas.add(item);
        notifyDataSetChanged();
    }

	public void add(int location, T item){
		mDatas.add(location, item);
		notifyDataSetChanged();
	}
	
    public void addAll(List<T> items) {
    	mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public void set(T oldItem, T newItem) {
        set(mDatas.indexOf(oldItem), newItem);
    }

    public void set(int index, T item) {
    	mDatas.set(index, item);
        notifyDataSetChanged();
    }

    public void remove(T item) {
    	mDatas.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int index) {
    	mDatas.remove(index);
        notifyDataSetChanged();
    }

    public void removeAll(List<T> items){
    	mDatas.removeAll(items);
    	notifyDataSetChanged();
    }
    
    public void replaceAll(List<T> items) {
    	mDatas.clear();
    	mDatas.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 将指定项移至指定位置
     * @param item
     * @param index
     */
    public void moveTo(T item, int index){
    	mDatas.remove(item);
    	mDatas.add(index, item);
    	notifyDataSetChanged();
    }
    
    public boolean contains(T item) {
        return mDatas.contains(item);
    }

    /** Clear data list */
    public void clear() {
    	mDatas.clear();
        notifyDataSetChanged();
    }
}
