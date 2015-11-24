/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ebook.ken.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ebook.ken.activity.R;
import ebook.ken.utils.MZLog;

import static ebook.ken.gcm.QuickstartPreferences.SENT_TOKEN_TO_SERVER;

public class RegistrationIntentService extends IntentService {
    SharedPreferences sharedPreferences;

    public RegistrationIntentService() {
        super("RegIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            boolean state = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (!state) {
                MZLog.d("1- on Handle Intent + " + getClass().getName());
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                MZLog.d(token);
                // [END get_token]

                // TODO: send registration to server
                sendRegistrationToServer(token);
                MZLog.d("2- DONE Handle Intent + " + getClass().getName());
            }
        } catch (Exception ex) {
            MZLog.d("Failed to complete token refresh");
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    /**
     * TODO send any registration to my app's servers.
     */
    private void sendRegistrationToServer(String token) {
        try {
            int result = sendPost(token);
            if (result != 0) {
                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                sharedPreferences.edit().putInt("user_id", result).apply();
                MZLog.d("Send to server: oke");
            }
        } catch (Exception e) {
            MZLog.d(" -- error while send POST to gcm_register");
            e.printStackTrace();
        }
    }

    // HTTP POST request
    private int sendPost(String regId) throws Exception {

        String url = "http://mrkenitvnn.esy.es/api/includes/gcm_register.php";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        /*con.setRequestProperty("User-Agent", USER_AGENT);*/
        /*con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");*/

        String urlParameters = "regId=" + regId;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        /*System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);*/

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        MZLog.d("response: " + response.toString());
        return Integer.parseInt(response.toString());
    }
}
