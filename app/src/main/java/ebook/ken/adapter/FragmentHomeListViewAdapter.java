package ebook.ken.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import ebook.ken.activity.R;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.JsonHandler;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class FragmentHomeListViewAdapter extends BaseAdapter {

	private static List<BookOffline>	mLocations; // danh sách sách hiển thị
	private ArrayList<BookOffline>		arraylist;	// danh sách sách trung gian để lọc khi tìm kiếm
	private LayoutInflater			mInflater;
	private Book bookEpub;


	////////////////////////////////////////////////////////////////////////////////
	// TODO getters & setters

	public static List<BookOffline> getmLocations() {
		return mLocations;
	}

	public static void setmLocations(List<BookOffline> mLocations) {
		FragmentHomeListViewAdapter.mLocations = mLocations;
	}


	////////////////////////////////////////////////////////////////////////////////
	// TODO constructor

	public FragmentHomeListViewAdapter(Context context, List<BookOffline> locations) {
		mInflater		= (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLocations		= locations;
		this.arraylist	= new ArrayList<BookOffline>();
		this.arraylist	.addAll(locations);
	}// end-func


	////////////////////////////////////////////////////////////////////////////////
	// TODO implements methods

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
			view		= mInflater.inflate(R.layout.item_home_listview, parent, false);

			viewHolder.imageView 		= (ImageView) view.findViewById(R.id.ivBooksCoverList);
			viewHolder.tvEpubBookName 	= (TextView) view.findViewById(R.id.tvBooksName);
			viewHolder.tvEpubBookAuthor = (TextView) view.findViewById(R.id.tvBooksAuthor);
			viewHolder.cbFavorite		= (CheckBox) view.findViewById(R.id.cbBooksFavorite);

			view.setTag(viewHolder);
		} else if ( ((ViewHolder) convertView.getTag()).needInflate ){
			viewHolder	= new ViewHolder();
			view		= mInflater.inflate(R.layout.item_home_listview, parent, false);
			
			viewHolder.imageView		= (ImageView) view.findViewById(R.id.ivBooksCoverList);
			viewHolder.tvEpubBookName	= (TextView) view.findViewById(R.id.tvBooksName);
			viewHolder.tvEpubBookAuthor = (TextView) view.findViewById(R.id.tvBooksAuthor);
			viewHolder.cbFavorite		= (CheckBox) view.findViewById(R.id.cbBooksFavorite);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}// end-if
		
		// create an instance of EpubBook
		final BookOffline locationModel = mLocations.get(position);

		// find InputStream for book

		try {
			InputStream epubInputStream = new URL(FileHandler.ROOT_PATH + locationModel.getBookFilePath()).openStream();

			Book book = (new EpubReader()).readEpub(epubInputStream);

			byte[] asd = book.getCoverImage().getData();
			Bitmap bm = BitmapFactory.decodeByteArray(asd, 0, asd.length);

//			viewHolder.imageView.setImageBitmap( BitmapFactory.decodeStream(book.getCoverImage().getInputStream()) );
			viewHolder.imageView.setImageBitmap( bm );

		} catch (IOException ex) {
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		}

//		if (locationModel.getBookCoverPath() != null) { // when doesn't have cover
//			File imgFile = new File(locationModel.getEpubCover());
//			if (imgFile.exists()) {
//				Uri uri		= Uri.fromFile(imgFile);
//				viewHolder	.imageView.setImageURI(uri);
//			}// end-if
//		} else {
//			Drawable myDrawable = view.getResources().getDrawable(R.drawable.default_book_cover);
//			viewHolder.imageView.setImageDrawable(myDrawable);
//		}// end-if

		viewHolder.tvEpubBookName	.setText(locationModel.getBookName());
		viewHolder.tvEpubBookAuthor	.setText(locationModel.getBookAuthor());

//		// với mỗi cuốn sách, duyệt qua danh sách yêu thích để hiển thị checkbox
//		for (int i = 0; i < FragmentBooks.listFavorites.size(); i++) {
//			if (locationModel.getEpubBook_id() == FragmentBooks
//													.listFavorites		// danh sách yêu thích
//													.get(i)				// lấy ra phần tử
//													.getEpubBook_id())	// lấy ra id sách
//			{
//				viewHolder.cbFavorite.setChecked(true);
//			}// end-if
//		}// end-for
//
//		viewHolder.cbFavorite.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (viewHolder.cbFavorite.isChecked()) {
//					FragmentBooks.favoriteDao
//									.addEpubFavorite(locationModel.getEpubBook_id());
//				} else {
//					FragmentBooks.favoriteDao
//									.delEpubFavorite(locationModel.getEpubBook_id());
//				}// end-if
//			}// end-func
//		});

		return view;
	}// end-func getView


	////////////////////////////////////////////////////////////////////////////////
	// TODO function inner

	// event add a book
	public void eventAddNewBook(BookOffline wp) {
		if (wp != null) {
			mLocations.add(wp);
			notifyDataSetChanged();
		}
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


	////////////////////////////////////////////////////////////////////////////////
	// TODO class inner

	private static class ViewHolder {
		private ImageView imageView;	// hiển thị cover
		private TextView
				tvEpubBookName,			// tên sách
				tvEpubBookAuthor;		// tác giả
		private CheckBox cbFavorite;	// trạng thái yêu thích

		private boolean needInflate;
	}
}
