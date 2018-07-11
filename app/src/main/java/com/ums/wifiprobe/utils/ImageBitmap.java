package com.ums.wifiprobe.utils;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;

public class ImageBitmap {	
	
	private static ImageBitmap instance = null;

	public static ImageBitmap getInstance() {
		if (instance == null) {
			synchronized (ImageBitmap.class) {
				if (instance == null) {
					instance = new ImageBitmap();
				}
			}
		}

		return instance;
	}

	private ImageBitmap() {
	}
	
	private final int DEFAULT_SIZE = 150;

	public Bitmap get(String path){
		return get(DEFAULT_SIZE, path);
	}
	
	public Bitmap get(InputStream is){
		return get(DEFAULT_SIZE, is);
	}
	
	public Bitmap get(int resId, Resources res){
		return get(DEFAULT_SIZE, resId, res);
	}
	
	public Bitmap get(int size, String path) {
		if (size > 0 && !TextUtils.isEmpty(path)) {
			Bitmap tmpBmp = BitmapUtil.decodeSampledBitmapFromFile(path, size, size);
			return BitmapUtil.getSoftBmpRef(tmpBmp);
		}

		return null;
	}
	
	public Bitmap get(int size, InputStream is){
		if (size > 0 && is != null) {
			Bitmap tmpBmp = BitmapUtil.decodeSampledBitmapFromStream(is, size, size);
			return BitmapUtil.getSoftBmpRef(tmpBmp);
		}

		return null;
	}
	
	public Bitmap get(int size, int resId, Resources res){
		if(size > 0 && resId >= 0){
			Bitmap tmpBmp = BitmapUtil.decodeSampledBitmapFromResource(res,
					resId, size, size);
			return BitmapUtil.getSoftBmpRef(tmpBmp);
		}
		
		return null;
	}
	

}
