package info.bartowski.easteregg.framework;/*
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

public class Record {
    private String devSerialNum;
    private String nickname;
    private int wkMax;
    private int historyMax;

    public Record(String dsn,String nn){
        devSerialNum = dsn;
        nickname = nn;
    }
    public void setWkMax(int wm){
        wkMax = wm;
    }
    public void setHistoryMax(int hm){
        historyMax = hm;
    }

    public String getDevSerialNum(){
        return devSerialNum;
    }

    public String getNickname(){
        return nickname;
    }

    public int getWkMax(){
        return wkMax;
    }

    public int getHistoryMax(){
        return historyMax;
    }
}