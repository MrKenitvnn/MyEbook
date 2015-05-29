package ebook.ken.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ebook.ken.objects.Chapter;

public class ChapterDAO {
	Database dbhelper;
	SQLiteDatabase db;

	public ChapterDAO(Context _context) {
		dbhelper = new Database(_context);
	}

	public void addListChapter(List<Chapter> lst) {
		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			for (Chapter item : lst) {
				values.put(Database.epubBook_id, item.getBookId());
				values.put(Database.chapterPath, item.getChapterPath());
				values.put(Database.chapterSrc, item.getChapterSrc());
				values.put(Database.chapterTitle, item.getChapterTitle());

				db.insert(Database.TABLE_EPUB_CHAPTER, null, values);
				Log.d(">>> ken <<<", "inserted a chapter ");
			}
		} finally {
			db.close();
		}
	}// end-func addListChapter


	public List<Chapter> getListChapterByBookId(int bookId) {

		List<Chapter> lst = new ArrayList<Chapter>();
		db = dbhelper.getReadableDatabase();
		String sql = "SELECT * FROM " + Database.TABLE_EPUB_CHAPTER
					+ " WHERE " + Database.epubBook_id + "=" + bookId;

		Cursor c = db.rawQuery(sql, null);
		try {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					Chapter chapter = new Chapter();

					chapter.setChapterPath(c.getString(c
							.getColumnIndex(Database.chapterPath)));
					chapter.setChapterSrc(c.getString(c
							.getColumnIndex(Database.chapterSrc)));
					chapter.setChapterTitle(c.getString(c
							.getColumnIndex(Database.chapterTitle)));

					lst.add(chapter);
					c.moveToNext();
				}// end-while
			}// end-if
		} finally {
			c.close();
			db.close();
		}// end-try

		return lst;
	}// end-func getListChapterByBookId


}
