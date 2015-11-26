package ebook.ken.utils;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ebook.ken.objects.BookFavorite;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;


public class MyApp extends Application{
	
	// state fragment 
	public static final int 
					FAVORITES	= 0x0,
					BOOKS		= 0x1,
					STORE		= 0x2;
	
	// check view state in listView or gridView
	public static boolean isInListView = true;
	// check is in section view
	public static boolean isInSection  = false;
	public static boolean isNoBack = false;

	// current fragment ViewPager
	public static int currentPage = BOOKS ;
	
	// current page number of book can get
	public static int bookCurrentPage = 0;
	
	// list data
	public static List<SectionOnline> listSection			= null;
	public static List<BookOnline> listBookOnlineFirstPage	= null;
	public static List<List<BookOnline>> listBookOfSection  = new ArrayList<List<BookOnline>>();;
	public static List<BookOnline> listBookBySection		= null;
	public static List<BookOffline> listBookOffline 		= null;
	public static BookOnline currentBookDetail				= null;
	public static SectionOnline currentSection				= null;

	public static List<BookOffline> listAllBookFavorites	= null;
	public static List<BookFavorite> listAllFavorites		= null;

	// gcm
	public static final String API_KEY = "AIzaSyCPUWdmOmTOMyaSpyVUTR4qAZbxQM19wfU";
	public static final String SENDER_ID = "634730422854";
	public static final String PRJ_NAME = "kencentermessage";

	public static final String SERVER_GCM ="";
	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Android Example";
	static final String DISPLAY_MESSAGE_ACTION = "com.androidexample.gcm.DISPLAY_MESSAGE";
	static final String EXTRA_MESSAGE = "message";


    private static MyApp mInstance;
	public MyApp() {};

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApp getInstance () {
        if (mInstance == null) {
            mInstance = new MyApp();
        }
        return mInstance;
    }

    public static Context getAppContext () {
        return getInstance().getApplicationContext();
    }
}
