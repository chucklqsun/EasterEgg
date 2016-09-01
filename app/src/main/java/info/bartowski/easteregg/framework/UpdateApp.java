/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.bartowski.easteregg.framework;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;

import info.bartowski.easteregg.BuildConfig;

public class UpdateApp extends AsyncTask<Void, String, String> {
    private final String LOG_TAG = UpdateApp.class.getSimpleName();
    private static final int VERSION_SIZE = 5; //version use 5 bytes
    private boolean checkError = true;
    private Context context;

    public UpdateApp(Context context) {
        super();
        this.context = context;
    }

    protected String doInBackground(Void... voids) {
        String msg;
        DatagramPacket packet;
        Transfer trans = new Transfer();
        Package p = new Package();
        try {
            trans.init();

            byte type[] = new byte[]{Config.FUNC.CHECK_APP_VERSION};
            byte[] res_buf = p.build(type);
            packet = trans.send(res_buf, VERSION_SIZE);
            // display response
            p.resolve(packet);
            //System.out.println(Arrays.toString(p.getData()));
            String ver = byteArrayToVerFormat(p.getData());
            msg = ver;
            //success
            checkError = false;

        } catch (Exception e) {
            msg = e.getMessage();
        }

        trans.close();
        return msg;
    }

    protected void onPostExecute(String result) {
        Log.v(LOG_TAG, result);
        //show error
        if (checkError) {
            Toast.makeText(context, "update error:"+result, Toast.LENGTH_SHORT).show();
            return;
        }

        if(needUpdate(result)) {
            result = String.format("version %s is available, downloading...", result);
            downloadApk(result);
        }else{  //already new
            result = String.format("Already latest version");
        }
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    private void downloadApk(String version) {
        Uri uri = Uri.parse(Config.getAppSite()+ "download/app-debug.apk");
        DownloadManager.Request r = new DownloadManager.Request(uri);

        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-debug.apk");

        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        r.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        r.setAllowedOverRoaming(false);

        r.setTitle("EasterEgg");
        r.setDescription("Download Easter Egg Ver. " + version + ".");

        // Start download
        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    private boolean needUpdate(String result) {
        String curVer = BuildConfig.VERSION_NAME;
        curVer = curVer.replace(".", "");
        result = result.replace(".", "");
        Log.v(LOG_TAG, curVer + " -> " + result);
        return Integer.parseInt(curVer) < Integer.parseInt(result);
    }

    private String byteArrayToVerFormat(byte[] b) {
        //only use 3 of 5 : major/min/rev
        return b[0] + "." + b[1] + "." + b[2];
    }

}
