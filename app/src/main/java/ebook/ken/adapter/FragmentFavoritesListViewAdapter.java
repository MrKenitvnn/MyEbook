package ebook.ken.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import ebook.ken.activity.R;
import ebook.ken.dao.BookFavoriteDao;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookFavorite;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.JsonHandler;

public class FragmentFavoritesListViewAdapter extends BaseAdapter {
	private static class ViewHolder {
		private ImageView imageView;
		private TextView 
						tvEpubBookName,
						tvEpubBookAuthor;
		private CheckBox cbFavorite;
	}

	private LayoutInflater			mInflater;
	private BookFavoriteDao			favoriteDao;
	private List<BookOffline>		mLocations;
	private ArrayList<BookOffline> 	arraylist;
	private ArrayList<BookFavorite> listFavorites;
	public ImageLoader imageLoader;


	////////////////////////////////////////////////////////////////////////////////
	// TODO constructor

	public FragmentFavoritesListViewAdapter(Context context, List<BookOffline> locations) {
		mInflater			= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLocations		= locations;
		this.arraylist		= new ArrayList<BookOffline>();
		this.arraylist		.addAll(locations);
		this.favoriteDao	= new BookFavoriteDao(context);
		this.listFavorites	= new ArrayList<BookFavorite>();
		this.listFavorites	.addAll(favoriteDao.loadAllFavorites());
		imageLoader = new ImageLoader(context);
		// itemChecked = new boolean[locations.size()];
	}// end-func


	@Override
	public int getCount() {
		if (mLocations != null) {
			return mLocations.size();
		}
		return 0;
	}


	@Override
	public Object getItem(int position) {
		if (mLocations != null && position >= 0 && position < getCount()) {
			return mLocations.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mLocations != null && position >= 0 && position < getCount()) {
			return mLocations.get(position).getBookId();
		}
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			viewHolder	= new ViewHolder();
			view		= mInflater.inflate(R.layout.item_book_listview, parent, false);

			viewHolder.imageView 		= (ImageView) view.findViewById(R.id.ivBooksCoverList);
			viewHolder.tvEpubBookName 	= (TextView) view.findViewById(R.id.tvBooksName);
			viewHolder.tvEpubBookAuthor = (TextView) view.findViewById(R.id.tvBooksAuthor);
			viewHolder.cbFavorite		= (CheckBox) view.findViewById(R.id.cbBooksFavorite);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// create an instance of BookOffline
		final BookOffline locationModel = mLocations.get(position);

		// when doesn't have cover
		if (locationModel.getBookCoverPath() != null) {
			File imgFile = new File(locationModel.getBookCoverPath());
			if (imgFile.exists()) {
				Uri uri	  = Uri.fromFile(imgFile);
				viewHolder.imageView.setImageURI(uri);
			}
		} else {
			Drawable myDrawable = view.getResources().getDrawable(R.drawable.default_book_cover);
			viewHolder.imageView.setImageDrawable(myDrawable);
		}// end-if

		viewHolder.tvEpubBookName	.setText(locationModel.getBookName());
		viewHolder.tvEpubBookAuthor	.setText(locationModel.getBookAuthor());

		for (int i = 0; i < listFavorites.size(); i++) {
			if (locationModel.getBookId() == listFavorites.get(i).getBookOfflineId()) {
				viewHolder.cbFavorite.setChecked(true);
			}// end-if
		}// end-for

		viewHolder.cbFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (viewHolder.cbFavorite.isChecked()) {
//					favoriteDao.addBookFavorite(locationModel.getBookId());
					notifyDataSetChanged();
				} else {
					mLocations.remove(position); //remove book from list favorites
					notifyDataSetChanged();
					favoriteDao.delBookFavorite(locationModel.getBookId());
				}// end-if
			}// end-func onClick
		});
		return view;
	}
	// event del a book
	public void eventDelABook(int id) {
		mLocations.remove(id);
		notifyDataSetChanged();
	}


	////////////////////////////////////////////////////////////////////////////////
	// lọc khi nhập ký tự trên searchview

	public void filter(String charText) {
		charText	= charText.toLowerCase(Locale.getDefault());
		mLocations	.clear();
		if (charText.length() == 0) {
			mLocations.addAll(arraylist);
		} else {
			for (BookOffline wp : arraylist) {
				if (wp.getBookName().toLowerCase(Locale.getDefault()).contains(charText)
						|| wp.getBookAuthor().toLowerCase(Locale.getDefault()).contains(charText)) {
					mLocations.add(wp);
				}// end-if
			}// end-for
		}
		notifyDataSetChanged();
	}// end-func filter


	public void closeSearch() {
		// mLocations.addAll(arraylist);
		notifyDataSetChanged();
	}

}
