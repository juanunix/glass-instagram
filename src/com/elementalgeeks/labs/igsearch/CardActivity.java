package com.elementalgeeks.labs.igsearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class CardActivity extends FragmentActivity {
	/* TODO:
	 * 		-insert a card in Timeline to access photos 
	 * 			https://developers.google.com/glass/develop/gdk/ui/live-cards
	 * 		-menu on each photo to share link or media
	 * 			https://developers.google.com/glass/develop/gdk/ui/immersion-menus */
	
	public final static String INSTAGRAM_API_KEY = "fc8041d4af1544a2939c3f5a9a1ef8cf";
	public final static String BASE_API_URL = "https://api.instagram.com/v1/";
	private ArrayList<Image> imagesArray = new ArrayList<Image>();
	public static RequestQueue requestQueue;	
	
	public static String getRecentUrl(String tag){
		return BASE_API_URL + "tags/" + tag + "/media/recent?client_id=" + INSTAGRAM_API_KEY; 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		ArrayList<String> voiceResults = getIntent().getExtras()
		        .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
		requestQueue = Volley.newRequestQueue(this);
		setContentView(R.layout.activity_card);
		
		final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		final ImageAdapter adapter = new ImageAdapter(getSupportFragmentManager(), imagesArray);
		viewPager.setAdapter(adapter);
		
		String tag = "guatemala";
		if (voiceResults.size() > 0) {
			 tag = voiceResults.get(0);			
		}
		String url = getRecentUrl(tag);		
	    Response.Listener<JSONObject> successListener = 
	    		new Response.Listener<JSONObject>() {
		            @Override
		            public void onResponse(JSONObject response) {
		            	JSONArray imgData;
						try {
							imgData = response.getJSONArray("data");
							for (int i = 0; i < imgData.length(); i++) {
								JSONObject currentElement = imgData.getJSONObject(i);
								String url = currentElement.getJSONObject("images").getJSONObject("low_resolution").getString("url");
								String userName = currentElement.getJSONObject("user").getString("username");
								Image img = new Image();
								img.setImgUrl(url);
								img.setUserName(userName);
								imagesArray.add(img);	
								Log.e("ASDF","agregando imagen " + url);
								adapter.notifyDataSetChanged();
							}		
							
							ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
							progressBar.setVisibility(View.GONE);
							viewPager.setVisibility(View.VISIBLE);

						} catch (JSONException e) {
							e.printStackTrace();
						}
		            }
	    };
	    
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, 
															   url, 
															   null, 
															   successListener,
															   null);		
		requestQueue.add(jsObjRequest);	
		requestQueue.start();            
		
	}  
}
