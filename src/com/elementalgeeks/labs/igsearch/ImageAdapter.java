package com.elementalgeeks.labs.igsearch;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImageAdapter extends FragmentPagerAdapter {
	private ArrayList<Image> dataArray;
	
    public ImageAdapter(FragmentManager fm, ArrayList<Image> dataArray) {
    	super(fm);
        this.dataArray = dataArray;        
    }
            
    @Override
    public Fragment getItem(int position) {
    	FragmentImage f = new FragmentImage();
    	Bundle args = new Bundle();
    	args.putString(FragmentImage.URL, dataArray.get(position).getImgUrl());
    	args.putString(FragmentImage.USERNAME, dataArray.get(position).getUserName());
    	f.setArguments(args);	
        return f;
    }
    
    @Override
    public int getCount() {
    	return dataArray.size();
    }
}
