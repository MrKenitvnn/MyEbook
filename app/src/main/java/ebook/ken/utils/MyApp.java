package ebook.ken.utils;

import android.app.Application;
import android.content.Context;
import java.util.List;
import ebook.ken.objects.BookFavorite;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;

public class MyApp extends Application {

    public static boolean isInListView = true;// check view state in listView or gridView

    // list data
    public static List<SectionOnline> listSection = null;
    public static List<BookOnline> listBookOnlineFirstPage = null;
    public static List<BookOnline> listBookBySection = null;
    public static List<BookOnline> listBookBySearch = null;
    public static BookOnline currentBookDetail = null;
    public static SectionOnline currentSection = null;

    public static List<BookOffline> listAllBookFavorites = null;
    public static List<BookFavorite> listAllFavorites = null;

    private static MyApp mInstance;

    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApp getInstance() {
        if (mInstance == null) {
            mInstance = new MyApp();
        }
        return mInstance;
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

}
