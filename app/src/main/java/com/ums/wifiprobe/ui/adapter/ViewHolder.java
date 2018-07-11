package com.ums.wifiprobe.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ums.wifiprobe.utils.BitmapUtil;

/***
 * 通用的ViewHolder
 * @author LiuChenShuo
 *
 */
public class ViewHolder {

	private final SparseArray<View> mViews;
	private View mConvertView;
	private int mLayoutId;
	private int mPosition;	
	
	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mViews = new SparseArray<View>();
		mLayoutId = layoutId;
		mPosition = position;		
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		
		if(convertView == null){
			return new ViewHolder(context, parent, layoutId, position);
		}
		
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		
		if(viewHolder.mLayoutId != layoutId){
			return new ViewHolder(context, parent, layoutId, position);
		}
		
		viewHolder.mPosition = position;
		return viewHolder;
	}
	
	/**
	 * 通过控件的id获取对应的控件，如果没有则加入mViews
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		
		return (T) view;
	}
	
	public View getConvertView(){
		return mConvertView;
	}

	public int getLayoutId() {
		return mLayoutId;
	}

	public int getPosition() {
		return mPosition;
	}
	
	/** 
     * 为TextView设置字符串 
     *  
     * @param viewId 
     * @param text 
     * @return 
     */  
    public ViewHolder setText(int viewId, String text)  
    {  
        TextView view = getView(viewId);  
        view.setText(text);  
        return this;  
    }
    
    /**
     * 为TextView设置字符串
     * @param viewId
     * @param textId
     * @return
     */
    public ViewHolder setText(int viewId, int textId){
    	TextView view = getView(viewId);  
        view.setText(textId);  
        return this;
    }
    
    /**
     * 为TextView设置文本颜色
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setTextColor(int viewId, int color){
    	TextView view = getView(viewId);  
        view.setTextColor(color);  
        return this;
    }
    
    /** 
     * 为ImageView设置图片 
     *  
     * @param viewId 
     * @param drawableId 
     * @return 
     */  
    public ViewHolder setImageResource(int viewId, int drawableId)  
    {  
        ImageView view = getView(viewId);  
        view.setImageResource(drawableId);  
  
        return this;  
    }  
  
    /** 
     * 为ImageView设置图片 
     *  
     * @param viewId 
     * @param bm 
     * @return 
     */  
    public ViewHolder setImageBitmap(int viewId, Bitmap bm)  
    {  
        ImageView view = getView(viewId); 
        // 加入到弱引用
        bm = BitmapUtil.getSoftBmpRef(bm);
        view.setImageBitmap(bm);  
        return this;  
    }  

    /**
     * 为ImageView设置图片
     * @param viewId
     * @param drawable
     * @return
     */
    public ViewHolder setImageDrawable(int viewId, Drawable drawable){
    	ImageView view = getView(viewId);
    	view.setImageDrawable(drawable); 
        return this;
    }
    
    /**
     * 设置控件的背景
     * @param viewId
     * @param resid
     * @return
     */
    public ViewHolder setBackgroundResource(int viewId, int resid){
    	View view = getView(viewId);
    	view.setBackgroundResource(resid);
    	return this;
    }
    
    /**
     * 设置控件的背景颜色
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setBackgroundColor(int viewId, int color){
    	View view = getView(viewId);
    	view.setBackgroundColor(color);
    	return this;
    }
    
    /**
     * 设置控件的背景
     * @param viewId
     * @param drawable
     * @return
     */
    public ViewHolder setBackground(int viewId, Drawable drawable){
    	View view = getView(viewId);
    	view.setBackground(drawable);
    	return this;
    }
    
    public ViewHolder addView(int viewgroupId, View view){
    	ViewGroup viewGroup = getView(viewgroupId);
    	viewGroup.addView(view);
    	return this;
    }
    
    /**
     * 设置view的可见性
     * @param viewId
     * @param visibility
     * @return
     */
    public ViewHolder setVisible(int viewId, int visibility){
    	View view = getView(viewId);
    	view.setVisibility(visibility);
    	return this;
    }
    
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
