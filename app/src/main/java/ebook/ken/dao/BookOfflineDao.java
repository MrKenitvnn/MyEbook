package ebook.ken.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ebook.ken.objects.BookOffline;


public class BookOfflineDao {

	private static Database dbhelper;
	private static SQLiteDatabase db;

	public BookOfflineDao(Context context) {
		dbhelper = new Database(context);
	}


	////////////////////////////////////////////////////////////////////////////////

	public void addBookOffline(BookOffline _epubBook) {

		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Database.epubBook_idOnline, _epubBook.getBookIdOnline());
			values.put(Database.epubBookName, _epubBook.getBookName());
			values.put(Database.epubBookAuthor, _epubBook.getBookAuthor());
			values.put(Database.epubBookCover, _epubBook.getBookCoverPath());
			values.put(Database.epubBookFolder, _epubBook.getBookFolder());
			values.put(Database.epubBookFolderPath, _epubBook.getBookFolderPath());
			values.put(Database.epubBookNcxPath, _epubBook.getBookNcxPath());
			values.put(Database.epubBookOpfPath, _epubBook.getBookOpfPath());

			db.insert(Database.TABLE_EPUB_BOOK, null, values);
		} finally {
			db.close();
		}

	}// end-func addBookOffline


	////////////////////////////////////////////////////////////////////////////////

	public int deleteBookOffline(int id) {

		int result = -1;
		db = dbhelper.getWritableDatabase();
		try {
			result = db.delete(Database.TABLE_EPUB_BOOK, Database.epubBook_id
					+ "=" + id, null);
		} catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		}finally {
			db.close();
		}
		return result;

	}// end-func deleteBookOffline


	////////////////////////////////////////////////////////////////////////////////

	public BookOffline getBookOfflineById(int id) {

		db = dbhelper.getReadableDatabase();

		String sql = "select * from " + Database.TABLE_EPUB_BOOK + " where "
					+ Database.TABLE_EPUB_BOOK + "." + Database.epubBook_id + " = " + id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		BookOffline epubBook = new BookOffline();

		try {
			while (!c.isAfterLast()) {
				epubBook.setBookId(c.getInt(c
						.getColumnIndex(Database.epubBook_id)));
				epubBook.setBookIdOnline(c.getInt(c
						.getColumnIndex(Database.epubBook_idOnline)));
				epubBook.setBookName(c.getString(c
						.getColumnIndex(Database.epubBookName)));
				epubBook.setBookAuthor(c.getString(c
						.getColumnIndex(Database.epubBookAuthor)));
				epubBook.setBookCoverPath(c.getString(c
						.getColumnIndex(Database.epubBookCover)));
				epubBook.setBookFolder(c.getString(c
						.getColumnIndex(Database.epubBookFolder)));
				epubBook.setBookFolderPath(c.getString(c
						.getColumnIndex(Database.epubBookFolderPath)));
				epubBook.setBookOpfPath(c.getString(c
						.getColumnIndex(Database.epubBookOpfPath)));
				epubBook.setBookNcxPath(c.getString(c
						.getColumnIndex(Database.epubBookNcxPath)));

				c.moveToNext();
			}
		} catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		} finally {
			c.close();
			db.close();
		}

		return epubBook;

	}// end-func getBookOfflineById


	////////////////////////////////////////////////////////////////////////////////

	public boolean checkBookOfflineByIdOnline(int id) {

		db = dbhelper.getReadableDatabase();
		String sql = "select * from " + Database.TABLE_EPUB_BOOK + " where "
					+ Database.TABLE_EPUB_BOOK + "." + Database.epubBook_idOnline + " = " + id;

		Cursor c = db.rawQuery(sql, null);
		try {
			c.moveToFirst();
			if( c!=null && c.getCount()>0 ){
				return true;
			}
		} catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
			return false;
		} finally {
			c.close();
			db.close();
		}
		return false;

	}// end-func checkBookOfflineByIdOnline


	////////////////////////////////////////////////////////////////////////////////

	public List<BookOffline> loadAllBookOffline() {

		// get permission read database
		db = dbhelper.getReadableDatabase();

		// list result
		List<BookOffline> list = new ArrayList<BookOffline>();

		// query
		String sql = "select * from " + Database.TABLE_EPUB_BOOK;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		try {
			while (!c.isAfterLast()) {
				BookOffline epubBook = new BookOffline();
				epubBook.setBookId(Integer.parseInt(c.getString(c
						.getColumnIndex(Database.epubBook_id))));
				epubBook.setBookName(c.getString(c
						.getColumnIndex(Database.epubBookName)));
				epubBook.setBookAuthor(c.getString(c
						.getColumnIndex(Database.epubBookAuthor)));
				epubBook.setBookCoverPath(c.getString(c
						.getColumnIndex(Database.epubBookCover)));
				epubBook.setBookIdOnline(c.getInt(c
						.getColumnIndex(Database.epubBook_idOnline)));

				epubBook.setBookFolder(c.getString(c
						.getColumnIndex(Database.epubBookFolder)));
				epubBook.setBookFolderPath(c.getString(c
						.getColumnIndex(Database.epubBookFolderPath)));
				epubBook.setBookOpfPath(c.getString(c
						.getColumnIndex(Database.epubBookOpfPath)));
				epubBook.setBookNcxPath(c.getString(c
						.getColumnIndex(Database.epubBookNcxPath)));

				list.add(epubBook);
				c.moveToNext();

			}// end-while
		} finally {
			c.close();
			db.close();
		}// end-try
		return list;

	}// end-func loadAllBookOffline


	////////////////////////////////////////////////////////////////////////////////

	public int getLastId() {

		int id = 0;
		db = dbhelper.getWritableDatabase();

		String query = "select MAX(" + Database.epubBook_id + ") from "
				+ Database.TABLE_EPUB_BOOK;

		Cursor c = db.rawQuery(query, null);
		try {
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				id = c.getInt(0);
			}
		} finally {
			c.close();
			db.close();
		}

		return id;

	}// end-func getLastId


	////////////////////////////////////////////////////////////////////////////////


}
