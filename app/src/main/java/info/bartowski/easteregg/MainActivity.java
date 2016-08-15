/*);
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

package info.bartowski.easteregg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

import info.bartowski.easteregg.framework.UpdateApp;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openEasterEgg(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.egg_choose_rg);
        Map<Integer, Class> targetMap = new HashMap<>();

        targetMap.put(R.id.egg_beanbag_rb, BeanBag.class);
        targetMap.put(R.id.egg_dessertcase_rb, DessertCase.class);
        targetMap.put(R.id.egg_mland_rb, MLandActivity.class);
        targetMap.put(R.id.egg_lland_rb, LLandActivity.class);
        targetMap.put(R.id.egg_nyandroid_rb, Nyandroid.class);
        targetMap.put(R.id.egg_zombie_rb, ZombieActivity.class);
        targetMap.put(R.id.egg_honeybee_rb, HoneybeeActivity.class);

        Intent intent = new Intent(this, targetMap.get(rg.getCheckedRadioButtonId()));
        startActivity(intent);
    }

    public void checkVersion(View view) {
        if (isStoragePermissionGranted()) {
            new UpdateApp(getApplicationContext()).execute();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG, "Permission is granted");
                return true;
            } else {
                Log.v(LOG_TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(LOG_TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            new UpdateApp(getApplicationContext()).execute();
        }
    }
}
