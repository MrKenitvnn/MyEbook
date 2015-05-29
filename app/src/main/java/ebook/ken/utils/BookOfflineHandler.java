package ebook.ken.utils;

import android.content.Context;
import android.util.Log;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.Chapter;

/**
 * Created by admin on 5/28/2015.
 */
public class BookOfflineHandler {

    private static String
                        bookName,
                        bookAuthor,

                        chapterTitle,
                        chapterSrc,
                        chapterPath,
                        chapterFile,
                        chapterPlayOrder;

    private static BookOffline book;


    ////////////////////////////////////////////////////////////////////////////////

    public static BookOffline epubBookData(String contentFilePath) {

        book = new BookOffline();
        File contentFile = new File(contentFilePath);

        try {

            Document contentDoc = Jsoup.parse(contentFile, "UTF-8");

            bookName	= contentDoc.getElementsByTag("dc:title").first().text();
            bookAuthor	= contentDoc.getElementsByTag("dc:creator").first().text();

            book.setBookName(bookName).setBookAuthor(bookAuthor);
            book.setBookOpfPath(contentFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }// end-try

        return book;
    }


    ////////////////////////////////////////////////////////////////////////////////

    public static List<Chapter> listEpubChapterData(BookOffline book) {
        String tocNcxPath = book.getBookNcxPath();
        String book_id = book.getBookFolder();
        String epubFileFolder = book.getBookFolderPath();
        List<Chapter> lstChapter = new ArrayList<Chapter>();
        File tocFile = new File(tocNcxPath);

        try {
            Document tocDoc = Jsoup.parse(tocFile, "UTF-8");

            Elements navPoint = tocDoc.select("navPoint");

            for (Element el : navPoint) {
                chapterTitle = el.getElementsByTag("navLabel").first().text();
                chapterSrc	 = el.getElementsByTag("content").attr("src").toString().replace("\\", "/").trim();
                chapterPlayOrder	= el.attr("playOrder").toString();
                chapterFile			= FileHandler.getLastTokenizer(chapterSrc, "/");
                chapterPath			= FileHandler.getChapterPath(new File(epubFileFolder), chapterFile);

                lstChapter.add(new Chapter().setBookId(Integer.parseInt(book_id))
                                            .setChapterTitle(chapterTitle)
                                            .setChapterSrc(chapterSrc)
                                            .setChapterPath(chapterPath));

                Log.d(">>> ken <<<", "chapter : " + chapterTitle);

            }// end-for
        } catch (IOException e) {
            e.printStackTrace();
        }// end-try

        return lstChapter;

    }


    ////////////////////////////////////////////////////////////////////////////////

    public static String getChapterComponent(String chapterPath) {

        String result = "";
        HtmlCleaner htmCleaner = new HtmlCleaner();
        TagNode baseResultNode = null;
        try {
            TagNode chanNode = null;
            baseResultNode	= htmCleaner.clean(new File(chapterPath));

            chanNode		= baseResultNode.findElementByName("body", false);
            result			= htmCleaner.getInnerHtml(chanNode).trim();

        } catch (IOException e) {
            e.printStackTrace();
        }// end-try

        return result;
    }

}
