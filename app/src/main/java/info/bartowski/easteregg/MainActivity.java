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
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import info.bartowski.easteregg.framework.Config;
import info.bartowski.easteregg.framework.Setting;
import info.bartowski.easteregg.framework.UpdateApp;
import info.bartowski.easteregg.framework.Utility;
import info.bartowski.easteregg.neko.NekoActivationActivity;

public class MainActivity extends AppCompatActivity {
    public static String IMEI = "000000000000000";
    public static String nickname = "";
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private long lastBackTime = 0;
    private long currentBackTime = 0;
    private EditText nicknameEt;
    private Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isPermissionGranted()) {
            IMEI = Utility.getDeviceId(this);
        } else {
            IMEI = "000000000000001";
        }

        setting = new Setting(this, LOG_TAG);
        nicknameEt = (EditText) findViewById(R.id.nickname_et);
        nickname = setting.getString("nickname");
        if (nickname.equals("")) {
            nickname = "路人甲";
        }
        nicknameEt.setText(nickname);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!nicknameEt.getText().toString().equals(nickname)) {
            nickname = nicknameEt.getText().toString();
            setting.putString("nickname", nickname);
        }
        ;
    }

    public void openEasterEgg(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.egg_choose_rg);
        Map<Integer, Class> targetMap = new HashMap<>();

        targetMap.put(R.id.egg_nekoland_rb, NekoActivationActivity.class);
        targetMap.put(R.id.egg_beanbag_rb, BeanBag.class);
        targetMap.put(R.id.egg_dessertcase_rb, DessertCase.class);
        targetMap.put(R.id.egg_mland_rb, MLandActivity.class);
        targetMap.put(R.id.egg_lland_rb, LLandActivity.class);
        targetMap.put(R.id.egg_nyandroid_rb, Nyandroid.class);
        targetMap.put(R.id.egg_zombie_rb, ZombieActivity.class);
        targetMap.put(R.id.egg_honeybee_rb, HoneybeeActivity.class);

        if(rg.getCheckedRadioButtonId() == R.id.egg_nekoland_rb){
            if(Build.VERSION.SDK_INT < 24){
                Toast.makeText(this,"Need Android Nougat",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent = new Intent(this, targetMap.get(rg.getCheckedRadioButtonId()));
        startActivity(intent);
    }

    public void checkVersion(View view) {
        if (isPermissionGranted()) {
            new UpdateApp(getApplicationContext()).execute();
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                            (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                    ) {
                System.out.println("Permission is granted");
                return true;
            } else {
                System.out.println("Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                }, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            System.out.println("Permission is granted");
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
            System.out.println("Permission: " + permissions[0] + " was " + grantResults[0]);
            System.out.println("Permission: " + permissions[1] + " was " + grantResults[1]);
            IMEI = Utility.getDeviceId(this);
        }else{
            this.finish();
        }
    }

    short tap = 0;

    public void openDebug(View view) {
        if (tap != 7) {
            tap++;
        } else {
            Config.DEBUG = !Config.DEBUG;
            Toast.makeText(this, "debug open", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            currentBackTime = System.currentTimeMillis();
            if (currentBackTime - lastBackTime > 2 * 1000) {
                Toast.makeText(this, "tap back again to exit", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
