package ebook.ken.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;


public class JsonHandler {

    public static final String BASE_URL = "http://mrkenitvnn.esy.es/api/includes/";
    public static final String BASE_URL_2 = "http://kensource-001-site1.1tempurl.com/";

    public static final String URL_LOAD_SECTION = BASE_URL + "load_section.php";

    private static final String
            TAG_ID = "_id",
            TAG_NAME = "name",
            TAG_AUTHOR = "author",
            TAG_DESCRIPTION = "description",
            TAG_COVER_PATH = "cover_path",
            TAG_FILE_PATH = "file_path",
            TAG_RATE = "rate",
            TAG_TOTAL_VIEW = "total_view",
            TAG_TOTAL_DOWNLOAD = "total_download",
            TAG_SECTION_ID = "section_id",

    SECTION_ID = "id",
            SECTION_NAME = "section_name";

    public static final int
            GET_SECTION = 0x0,
            GET_BOOK_BY_PAGE = 0x1,
            GET_BOOK_BY_SEARCH = 0x2,
            GET_BOOK_BY_SECTION = 0x3,

    TIME_OUT = 5000;

    /**
     * TODO get List Section
     */
    public static List<SectionOnline> getSectionOnline() {
        List<SectionOnline> result = null;
        try {
            SectionOnline section = null;
            String jsonResponse = "";

            // get json response
            jsonResponse = getJSONString(BASE_URL + "load_section.php", TIME_OUT);

            if (jsonResponse == null) {
                return null;
            }
            JSONArray jsa = new JSONArray(jsonResponse);
            result = new ArrayList<SectionOnline>();
            for (int i = 0; i < jsa.length(); i++) {

                // create new section object
                section = new SectionOnline();
                // create json object
                JSONObject jso = jsa.getJSONObject(i);

                // get and set value to object section
                section.setSectionId(Integer.parseInt(jso.getString(SECTION_ID)));
                section.setSectionName(jso.getString(SECTION_NAME));

                // add to list
                result.add(section);

                MZLog.d("Section: " + section.getSectionName());
            }// end-for

        } catch (JSONException ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }// end-try

        return result;
    }

    public static List<SectionOnline> makeSectionFromJsonArray(JSONArray jsa) {
        List<SectionOnline> result = null;
        try {
            SectionOnline section = null;
            result = new ArrayList<SectionOnline>();
            for (int i = 0; i < jsa.length(); i++) {

                // create new section object
                section = new SectionOnline();
                // create json object
                JSONObject jso = jsa.getJSONObject(i);

                // get and set value to object section
                section.setSectionId(Integer.parseInt(jso.getString(SECTION_ID)));
                section.setSectionName(jso.getString(SECTION_NAME));

                // add to list
                result.add(section);

                MZLog.d("Section: " + section.getSectionName());
            }// end-for

        } catch (JSONException ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }// end-try

        return result;
    }


    ////////////////////////////////////////////////////////////////////////////////
    // TODO get list Book

    public static List<BookOnline> getBookOnline(int type, String value) throws JSONException {

        List<BookOnline> result = new ArrayList<BookOnline>();
        String jsonResponse;

        switch (type) {

            case GET_BOOK_BY_PAGE:
                jsonResponse = getJSONString(BASE_URL + "load_book.php?page=" + value.trim(), TIME_OUT);
                MZLog.d(">>> ken <<<", "1. GET BOOK BY PAGE: " + jsonResponse);
                result = listBookByJson(jsonResponse);
                break;
            case GET_BOOK_BY_SEARCH:
                jsonResponse = getJSONString(BASE_URL + "load_book.php?book_name=" + value.trim(), TIME_OUT);
                MZLog.d(">>> ken <<<", "2. GET BY SEARCH : " + jsonResponse);
                result = listBookByJson(jsonResponse);
                break;
            case GET_BOOK_BY_SECTION:
                jsonResponse = getJSONString(BASE_URL + "load_book.php?section_id=" + value.trim(), TIME_OUT);
                Log.d(">>> ken <<<", "3. GET BY SECTION : " + jsonResponse);
                result = listBookByJson(jsonResponse);
                break;
        }// end-switch

        return result;
    }// end-func getBookOnline


    public static List<BookOnline> listBookFromJsonArray(JSONArray jsaList) throws JSONException {
        List<BookOnline> result = new ArrayList<BookOnline>();
        BookOnline item;
        // loop thought per json object
        for (int i = 0; i < jsaList.length(); i++) {

            // get json object
            JSONObject jso = jsaList.getJSONObject(i);

            //create instance
            item = new BookOnline();

            // set data to object
            item.setBookId(Integer.valueOf(jso.getString(TAG_ID)))
                    .setBookAuthor(jso.getString(TAG_AUTHOR))
                    .setBookName(jso.getString(TAG_NAME))
                    .setBookCoverPath(jso.getString(TAG_COVER_PATH))
                    .setBookFilePath(jso.getString(TAG_FILE_PATH));
            item.setBookDesciption(jso.getString(TAG_DESCRIPTION))
                    .setBookRate(Float.valueOf(jso.getString(TAG_RATE)));
            item.setBookTotalDownload(jso.getInt(TAG_TOTAL_DOWNLOAD));

            // add to result list
            result.add(item);

        }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // TODO get list book by json

    private static List<BookOnline> listBookByJson(String jsonStr) throws JSONException {

        List<BookOnline> result = null;
        BookOnline item;
        try {
            if (jsonStr != null) {

                result = new ArrayList<BookOnline>();

                // get list object json
                JSONArray jsaList = new JSONArray(jsonStr);

                // loop thought per json object
                for (int i = 0; i < jsaList.length(); i++) {

                    // get json object
                    JSONObject jso = jsaList.getJSONObject(i);

                    //create instance
                    item = new BookOnline();

                    // set data to object
                    item.setBookId(Integer.valueOf(jso.getString(TAG_ID)))
                            .setBookAuthor(jso.getString(TAG_AUTHOR))
                            .setBookName(jso.getString(TAG_NAME))
                            .setBookCoverPath(jso.getString(TAG_COVER_PATH))
                            .setBookFilePath(jso.getString(TAG_FILE_PATH));
                    item.setBookDesciption(jso.getString(TAG_DESCRIPTION))
                            .setBookRate(Float.valueOf(jso.getString(TAG_RATE)));
                    item.setBookTotalDownload(jso.getInt(TAG_TOTAL_DOWNLOAD));

                    // add to result list
                    result.add(item);

                }// end-for

                return result;
            }// end-if
        } catch (JSONException ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }// end-try

        return null;
    }// end-func listBookByJson


    ////////////////////////////////////////////////////////////////////////////////
    // TODO get json string

    public static String getJSONString(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            MZLog.d(Log.getStackTraceString(ex));
        } catch (IOException ex) {
            MZLog.d(Log.getStackTraceString(ex));
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    MZLog.d(Log.getStackTraceString(ex));
                }
            }
        }
        return null;
    }

}

