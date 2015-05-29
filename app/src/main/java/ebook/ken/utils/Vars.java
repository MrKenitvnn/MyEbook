package ebook.ken.utils;

import android.app.Application;

import java.util.List;

import ebook.ken.objects.BookFavorite;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;


public class Vars extends Application{
	
	// state fragment 
	public static final int 
					FAVORITES	= 0x0,
					BOOKS		= 0x1,
					STORE		= 0x2;
	
	// check view state in listView or gridView
	public static boolean isInListView = true;
	// check is in section view
	public static boolean isInSection  = false;

	// check first time open section activity

	
	// current fragment ViewPager
	public static int currentPage = BOOKS ;
	
	// current page number of book can get
	public static int bookCurrentPage = 0;
	
	// list data
	public static List<SectionOnline> listSection			= null;
	public static List<BookOnline> listBookOnlineFirstPage	= null;
	public static List<BookOnline> listBookBySection		= null;
	public static List<BookOffline> listBookOffline 		= null;
	public static BookOnline currentBookDetail				= null;
	public static SectionOnline currentSection				= null;

	public static List<BookOffline> listAllBookFavorites	= null;
	public static List<BookFavorite> listAllFavorites		= null;
	
	
}
