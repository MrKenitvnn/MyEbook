package ebook.ken.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static String DATABASE_NAME		= "ebookReader";
	private static int 	  DATABASE_VERSION	= 1;

	public static String 
					TABLE_EPUB_BOOK			= "tblEpubBook",
					epubBook_id				= "epubBookId",
					epubBook_idOnline		= "idOnline",
					epubBookName			= "epubBookName",
					epubBookAuthor			= "epubBookAuthor",
					epubBookCover			= "epubBookCover",
					epubBookFolder			= "epubBookFolder",
					epubBookFolderPath		= "epubBookFolderPath",
					epubBookNcxPath			= "epubBookNcxPath",
					epubBookOpfPath			= "epubBookOpfPath";

	public static String 
					TABLE_EPUB_BOOKMARK	= "tblEpubBookmark",
//					epubBook_id 		= "epubBookId";
					bookmarkComponentId = "bookmarkComponentId",
					bookmarkPercent 	= "bookmarkPercent";

	public static String
					TABLE_EPUB_CHAPTER	= "tblEpubChapter",
					chapterId			= "chapterId",
//					epubBook_id			= "epubBookId",
					chapterPath 		= "chapterPath",
					chapterSrc			= "chapterSrc",
					chapterTitle		= "chapterTitle";

	public static String
					TABLE_EPUB_FAVORITE = "tblEpubFavorite",
					favorite_id 		= "favoriteId";
//					epubBook_id 		= "epubBookId";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String tblEpubBook 
				= " CREATE TABLE " + TABLE_EPUB_BOOK + " ( "
				+ epubBook_id
				+ " Integer primary key autoincrement, "
				+ epubBook_idOnline
				+ " text, "
				+ epubBookName
				+ " text, "
				+ epubBookAuthor
				+ " text, "
				+ epubBookCover
				+ " text, "
				+ epubBookFolder
				+ " text, "
				+ epubBookFolderPath
				+ " text, "
				+ epubBookNcxPath
				+ " text, "
				+ epubBookOpfPath
				+ " text "
				+ ")";
		
		String tblBookmark
				= " CREATE TABLE " + TABLE_EPUB_BOOKMARK + " ( "
				+ epubBook_id
				+ " Integer primary key, "
				+ bookmarkComponentId
				+ " text, "
				+ bookmarkPercent
				+ " text "
				+ ")";
		
		String tblChapter
				= " CREATE TABLE " + TABLE_EPUB_CHAPTER + " ( "
				+ chapterId
				+ " Integer primary key autoincrement, "
				+ epubBook_id
				+ " integer, "
				+ chapterPath
				+ " text, "
				+ chapterSrc
				+ " text, "
				+ chapterTitle
				+ " text "
				+ ")";

		String tblFavorite 
				= " CREATE TABLE " + TABLE_EPUB_FAVORITE + " ( "
				+ favorite_id
				+ " Integer primary key autoincrement, "
				+ epubBook_id
				+ " integer "	
				+ ")";
		
		db.execSQL(tblEpubBook);
		db.execSQL(tblBookmark);
		db.execSQL(tblChapter);
		db.execSQL(tblFavorite);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("crop table if exists "+ TABLE_EPUB_BOOK);
		db.execSQL("crop table if exists "+ TABLE_EPUB_BOOKMARK);
        db.execSQL("crop table if exists "+ TABLE_EPUB_FAVORITE);
		db.execSQL("crop table if exists "+ TABLE_EPUB_CHAPTER);

		onCreate(db);
	}
}
