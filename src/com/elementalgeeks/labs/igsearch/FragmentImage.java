package com.elementalgeeks.labs.igsearch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;

public class FragmentImage extends Fragment {	
    public static final String URL = "url";
    public static final String USERNAME = "username";

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_image, container, false);
    	
    	Bundle args = getArguments();
    	String url = args.getString(URL);
    	NetworkImageView img = (NetworkImageView) rootView.findViewById(R.id.img);
    	TextView txt = (TextView) rootView.findViewById(R.id.txtName);
    	img.setImageUrl(url, new ImageLoader(CardActivity.requestQueue, new BitmapLRUCache()));
    	txt.setText(args.getString(USERNAME));
        return rootView;
    }
         
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	            int reqWidth, int reqHeight) {
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	    
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	
	    return inSampleSize;
	 }        
	
	    
	private class BitmapLRUCache extends LruCache<String, Bitmap> implements ImageCache {
	    private static final int CACHE_SIZE_BYTES = 4 * 1024 * 1024; // 4 MB
	    
	    public BitmapLRUCache() {
	            super(CACHE_SIZE_BYTES);
	    }
	            
	    @Override
	    protected int sizeOf(String key, Bitmap value) {
	            return value.getRowBytes() * value.getHeight();
	    }
	    
	    @Override
	    public Bitmap getBitmap(String url) {
	            return get(url);
	    }
	
	    @Override
	    public void putBitmap(String url, Bitmap bitmap) {
	            put(url, bitmap);
	    }
	 
	}       
}
