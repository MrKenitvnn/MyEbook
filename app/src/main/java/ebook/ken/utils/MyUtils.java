package ebook.ken.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ebook.ken.activity.R;


public class MyUtils {
	
	////////////////////////////////////////////////////////////////////////////////
	// TODO change fragment
	
	public static void navigationToView(FragmentActivity activity, Fragment _fragment, int id_fragmentLayout) {
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		 transaction.replace(id_fragmentLayout, _fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	
	////////////////////////////////////////////////////////////////////////////////
	// TODO set ImageView in listView(or gridView) state
	
	public static void setImageIsListView(Activity activity, ImageView ivStyle){
		ivStyle.setImageDrawable(activity.getResources()
					 .getDrawable(R.drawable.icon_view_list));
	}

	
	public static void setImageIsGridView(Activity activity, ImageView ivStyle){
		ivStyle.setImageDrawable(activity.getResources()
					 .getDrawable(R.drawable.icon_view_grid));
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	// TODO check network
	
	public static boolean isOnline(Context context) {
		
	    ConnectivityManager cm = (ConnectivityManager) context
	    							.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo	   = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	    
	}
	

	////////////////////////////////////////////////////////////////////////////////
	// TODO download Image and set to bitmap
	
	public static Bitmap download_Image(String url) {
		
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (MalformedURLException e) {
			Log.d(">>> ken <<<", e.getMessage());
		} catch (IOException e) {
			Log.d(">>> ken <<<", e.getMessage());
		}
		return bitmap;
		
	}// end-func download_Image


	////////////////////////////////////////////////////////////////////////////////
	// TODO 
	
	
	

}
