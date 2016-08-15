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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import info.bartowski.easteregg.BuildConfig;

public class UpdateApp extends AsyncTask<Void, String, String> {
    private final String LOG_TAG = UpdateApp.class.getSimpleName();
    private static final int VERSION_SIZE = 5; //version use 5 bytes
    private Context context;

    public UpdateApp(Context context) {
        super();
        this.context = context;
    }

    protected String doInBackground(Void... voids) {
        String msg;
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);
            // send request
            Package p = new Package();
            byte type[] = new byte[]{Config.FUNC.CHECK_APP_VERSION};
            byte[] res_buf = p.build(type);

            InetAddress address = InetAddress.getByName(Config.HOST);
            DatagramPacket packet = new DatagramPacket(res_buf, res_buf.length, address, Config.PORT);
            socket.send(packet);

            // get response
            byte[] rev = new byte[Package.DATA_POS + VERSION_SIZE];
            packet = new DatagramPacket(rev, rev.length);
            socket.receive(packet);

            // display response
            p.resolve(packet);
            String ver = byteArrayToVerFormat(p.getData());
            msg = "version " + ver + " is available";

            socket.close();
        } catch (SocketTimeoutException e) {
            msg = "check timeout";
        } catch (IOException e) {
            msg = "network error";
        }
        return msg;
    }

    protected void onPostExecute(String result) {
        Log.v(LOG_TAG, result);
        if (!isNew(result)) {
            downloadApk();
        }
    }

    private void downloadApk() {
        Uri uri = Uri.parse(Config.Uri.APP_SITE + "download/app-debug.apk");
        DownloadManager.Request r = new DownloadManager.Request(uri);

        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-debug.apk");

        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        r.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        r.setAllowedOverRoaming(false);

        r.setTitle("EasterEgg");
        r.setDescription("Android Easter Egg Update.");

        // Start download
        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    private boolean isNew(String result) {
        return BuildConfig.VERSION_NAME == result;
    }

    private String byteArrayToVerFormat(byte[] b) {
        //only use 3 of 5 : major/min/rev
        return b[0] + "." + b[1] + "." + b[2];
    }

}
