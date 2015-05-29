package ebook.ken.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.JsonHandler;


public class FragmentBookStoreAdapter extends ArrayAdapter<BookOnline>{
	
	private ImageView ivCover;
	private TextView tvNameBookOnline, tvAuthorBookOnline;
	public ImageLoader imageLoader;


	////////////////////////////////////////////////////////////////////////////////
	// TODO 
	
	public FragmentBookStoreAdapter(Context context, List<BookOnline> listData) {
		super(context, R.layout.item_book_store, listData);
		imageLoader = new ImageLoader(context);
	}


	////////////////////////////////////////////////////////////////////////////////
	// TODO 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// get item by position
		BookOnline itemBook = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_book_store, parent, false);
		}
		
		ivCover				= (ImageView) convertView.findViewById(R.id.ivCoverBookStore);
		tvNameBookOnline	= (TextView) convertView.findViewById(R.id.tvNameBookStore);
		tvAuthorBookOnline	= (TextView) convertView.findViewById(R.id.tvAuthorBookOnline);
		
		tvNameBookOnline.setText(itemBook.getBookName());
		tvAuthorBookOnline.setText(itemBook.getBookAuthor());
		imageLoader.DisplayImage(JsonHandler.BASE_URL + itemBook.getBookCoverPath(), ivCover);
		
		return convertView;
	}

	////////////////////////////////////////////////////////////////////////////////
	
}
