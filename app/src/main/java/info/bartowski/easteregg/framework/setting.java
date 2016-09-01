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

import android.content.Context;
import android.content.SharedPreferences;

public class Setting {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public Setting(Context context,String eggName){
        preferences = context.getSharedPreferences(eggName,context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void putInt(String key, int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public void putString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public int getInt(String key){
        return preferences.getInt(key,0);
    }

    public int getInt(String key,int value){
        return preferences.getInt(key,value);
    }

    public String getString(String key){
        return preferences.getString(key,"");
    }

}
