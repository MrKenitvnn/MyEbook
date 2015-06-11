package ebook.ken.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ebook.ken.dao.BookOfflineDao;
import ebook.ken.dao.BookmarkDao;
import ebook.ken.dao.ChapterDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.Bookmark;
import ebook.ken.objects.Chapter;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.JsoupParse;

public class ReadingActivity extends Activity {
	static int book_id;
	private String
			webData = "",
			script = "",
			script2 = "",
			bookData = "";

	private WebView webview;
	private BookmarkDao bookmarkDAO;
	public 	static BookOfflineDao bookDAO;
	private static ChapterDao chapterDao;
	private static List<Chapter> listChapter;
	private Bookmark mBookmark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readding);

		bookDAO 	= new BookOfflineDao(getApplicationContext());
		bookmarkDAO = new BookmarkDao(getApplicationContext());
		chapterDao = new ChapterDao(getApplicationContext());

		// init controls
		webview			= (WebView) findViewById(R.id.webkit);

		// async task read book
		new ReadTask().execute();

	} // end-func onCreate

	@Override
	protected void onPause() {
		super.onPause();
		webview.loadUrl("javascript:getComponentId()");
	} // end-func onPause

	// get asset
	public String getAsset(String file_name) {
		// đọc file script từ assets
		StringBuilder stbScript = new StringBuilder();
		InputStream isScript;
		try {
			isScript = getAssets().open(file_name);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					isScript, "UTF-8"));
			String str;

			while ((str = br.readLine()) != null) {
				stbScript.append(str + "\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stbScript.toString();
	}// end-func getAsset

	@JavascriptInterface
	public void setData(String componentId, String percentId) {
		if (mBookmark != null) {
			mBookmark.setComponentId(componentId).setPercent(percentId);
			bookmarkDAO.editEpubBookmark(mBookmark);
			Log.d("mylog edit", componentId + " - " + percentId);
		} else {
			mBookmark = new Bookmark(book_id, componentId, percentId);
			bookmarkDAO.addEpubBookmark(mBookmark);
			Log.d("mylog add", componentId + " - " + percentId);
		}
	}// end-func setData

	// event

	View.OnClickListener bookMarkEvent = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			webview.loadUrl("javascript:getComponentId()");
		}
	};// end-event bookMarkEvent

	// end-event
	
////////////////////////////////////////////////////////////////////////////////
	
	private class ReadTask extends AsyncTask<String, Long, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				ReadingActivity.this);

		// can use UI thread here
		protected void onPreExecute() {
			// startingMillis = System.currentTimeMillis();
			dialog.setMessage("Loading..");
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected Void doInBackground(final String... args) {
			try {
				// Nhận dữ liệu truyền sang
				BookOffline book = (BookOffline) getIntent().getExtras().getSerializable("BOOK");
				book_id = Integer.parseInt(book.getBookFolder());

				// base data
				listChapter = new ArrayList<Chapter>();

				listChapter	.addAll(chapterDao.getListChapterByBookId(book_id));
				mBookmark 	= bookmarkDAO.getEpubBookmarkById(book_id);

				// đọc file script từ assets
				script = getAsset("finalebookscript.js");
				script2 = getAsset("finalebookscript2.js");

				// book data
				bookData = "var bookData = {" 
							+ "getComponents: function () {"
							+ "return [";
				int i = 0;
				for (Chapter item : listChapter) {
					bookData += "'" + item.getChapterSrc().trim() + "'";
					bookData += i < listChapter.size() - 1 ? "," : "";
					i++;
				}
				bookData += "];" 
							+ "}," 
							+ "getContents: function () {"
							+ "return [";
				i = 0;
				for (Chapter item : listChapter) {
					bookData += "{title:\"" + item.getChapterTitle().trim()+ "\",";
					bookData += "src: \"" + item.getChapterSrc().trim() + "\"}";
					bookData += i < listChapter.size() - 1 ? "," : "";
					i++;
				}
				bookData += "]" 
							+ "},"
							+ "getComponent: function (componentId) {" 
							+ "return {";
				i = 0;
				for (Chapter item : listChapter) {
					bookData += "'" + item.getChapterSrc().trim() + "':";
					bookData += "'"
							 + JsoupParse
							 	.getChapterComponent(item.getChapterPath())
							 	.replace("\n", "").replace("<a", "<p")
								.replace("<a>", "<p>").trim() + "'";
					bookData += i < listChapter.size() - 1 ? "," : "";

					i++;
				}

				bookData += "}[componentId];" 
						 + "},"
						 + "getMetaData: function(key) {" + "return {"
						 + "title: \"" + book.getBookName() + "\","
						 + "creator: \"" + book.getBookAuthor() + "\""
						 + "}[key];" 
						 + "}" 
						 + "} ";

				// end book data

				// start - web data
				webData = "<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"
						+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">"
						+ "<meta"
						+ "name=\"viewport\""
						+ "content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\""
						+ "/>"
						+ "<title></title>"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/monocore.css\" />"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/monoctrl.css\" />";

				webData += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/finalebookstyle.css\" />"
						+ "<script type=\"text/javascript\" src=\"file:///android_asset/monocore.js\"></script>"
						+ "<script type=\"text/javascript\" src=\"file:///android_asset/monoctrl.js\"></script>"
						+ "<script>" // script start
						+ bookData 
						+ script;

				if (mBookmark != null) {
					webData += " var locus = {componentId:'"
							+ mBookmark.getComponentId() + "',percent:'"
							+ mBookmark.getPercent() + "'};";
					webData += "\n reader.moveTo(locus);";
				}

				webData += script2 
						+ " </script>" // end script
						+ "</head>";

				webData += "" + "<body>" + "<div id=\"readerBg\">"
						+ "<div class=\"board\"></div>"
						+ "<div class=\"jacket\"></div>"
						+ "<div class=\"dummyPage\"></div>"
						+ "<div class=\"dummyPage\"></div>"
						+ "<div class=\"dummyPage\"></div>"
						+ "<div class=\"dummyPage\"></div>"
						+ "<div class=\"dummyPage\"></div>"
						+ "<div class=\"dummyPage\"></div>" 
						+ "</div>"
						+ "<div id=\"readerCntr\">"
						+ "<div class=\"reader\" id=\"reader\"></div>"
						+ "</div>";

				webData += "<script type=\"text/javascript\" src=\"file:///android_asset/finalebookscript.js\"></script>";
				webData += "</body>" 
						+ "</html>";
				// ghi vào file của folder data trong sdcard
				FileHandler.writeData(webData, "index.html");

			} catch (Exception e) {
				Log.v("slow-job being done", e.getMessage());
			}
			return null;
		}
		
		@JavascriptInterface
		public void setData(String componentId, String percentId) {
			if (mBookmark != null) {
				mBookmark	.setComponentId(componentId).setPercent(percentId);
				bookmarkDAO	.editEpubBookmark(mBookmark);
				Log.d("mylog edit", componentId + " - " + percentId);
			} else {
				mBookmark = new Bookmark(book_id, componentId, percentId);
				bookmarkDAO.addEpubBookmark(mBookmark);
				Log.d("mylog add", componentId + " - " + percentId);
			}
		}// end-func setData

		// load sách vào webview
		@SuppressWarnings("deprecation")
		protected void onPostExecute(final Void unused) {
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setAllowFileAccessFromFileURLs(true);
			webview.setWebViewClient(new WebViewClient());
			webview.setWebChromeClient(new WebChromeClient());
			webview.addJavascriptInterface(this, "android");
			webview.requestFocusFromTouch();
			// these settings speed up page load into the webview
			webview.getSettings().setRenderPriority(RenderPriority.HIGH);
			webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			webview.requestFocus(View.FOCUS_DOWN);

			webview.loadUrl("file://" + FileHandler.ROOT_PATH
					+ FileHandler.DATA_FOLDER + "/index.html");
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}// end-asynctask ReadTask
}
