package ebook.ken.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ebook.ken.objects.BookFavorite;
import ebook.ken.objects.BookOffline;


public class BookFavoriteDao {

	Database dbhelper;
	SQLiteDatabase db;


	////////////////////////////////////////////////////////////////////////////////

	public BookFavoriteDao(Context _context) {
		dbhelper = new Database(_context);
	}


	////////////////////////////////////////////////////////////////////////////////

	public void addBookFavorite(int _epub_id) {
		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put(Database.epubBook_id, _epub_id);

			db.insert(Database.TABLE_EPUB_FAVORITE, null, values);

		} finally {
			db.close();
		}

	}// end-func addBookFavorite


	////////////////////////////////////////////////////////////////////////////////

	public int delBookFavorite(int id) {
		int result = -1;

		db = dbhelper.getWritableDatabase();
		try {
			result = db.delete(Database.TABLE_EPUB_FAVORITE,
								Database.epubBook_id + "=?",
								new String[] { String.valueOf(id) });
		} finally {
			db.close();
		}
		return result;
	}// end-func deleteBookFavorite


	////////////////////////////////////////////////////////////////////////////////

	public List<BookOffline> loadAllBookOfFavorites() {
		List<BookOffline> list = new ArrayList<BookOffline>();
		db = dbhelper.getWritableDatabase();

		String sql = "SELECT " 
					+ "b." + Database.epubBook_id + ", " 
					+ "b." + Database.epubBookName + ", " 
					+ "b." + Database.epubBookAuthor + ", " 
					+ "b." + Database.epubBookFolderPath + ", "
					+ "b." + Database.epubBookCover 
					+ " FROM "
					+ Database.TABLE_EPUB_FAVORITE + " AS a" 
					+ " LEFT JOIN "
					+ Database.TABLE_EPUB_BOOK + " AS b " 
					+ " ON " 
					+ "a." + Database.epubBook_id 
					+ "=" 
					+ "b." + Database.epubBook_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		try {
			while (!c.isAfterLast()) {
				BookOffline epubBook = new BookOffline();

				epubBook.setBookId(c.getInt(c
						.getColumnIndex(Database.epubBook_id)));
				epubBook.setBookName(c.getString(c
						.getColumnIndex(Database.epubBookName)));
				epubBook.setBookAuthor(c.getString(c
						.getColumnIndex(Database.epubBookAuthor)));
				epubBook.setBookCoverPath(c.getString(c
						.getColumnIndex(Database.epubBookCover)));
				epubBook.setBookFolderPath(c.getString(c
						.getColumnIndex(Database.epubBookFolderPath)));

				list.add(epubBook);
				c.moveToNext();
			}// end-while
		} finally {
			c.close();
			db.close();
		}// end-try

		return list;
	}// end-func loadAllBookOfFavorites


	////////////////////////////////////////////////////////////////////////////////

	public List<BookFavorite> loadAllFavorites() {
		List<BookFavorite> list = new ArrayList<BookFavorite>();
		db = dbhelper.getWritableDatabase();

		String sql = "SELECT " + Database.epubBook_id 
					+ " FROM "
					+ Database.TABLE_EPUB_FAVORITE;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		try {
			while (!c.isAfterLast()) {
				BookFavorite epubBook = new BookFavorite();

				epubBook.setBookOfflineId(Integer.parseInt(c.getString(c
						.getColumnIndex(Database.epubBook_id))));

				list.add(epubBook);
				c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}
		return list;
	}// end-func loadAllFavorites


	////////////////////////////////////////////////////////////////////////////////

}
