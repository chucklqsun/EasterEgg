/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import info.bartowski.easteregg.framework.Config;
import info.bartowski.easteregg.framework.Scores;

public class LLandActivity extends Activity {
    MediaPlayer bgm;
    int curView = 1;
    private static final int VIEW_LAUNCH = 1;
    private static final int VIEW_WORLD  = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(bgm == null) {
            bgm = MediaPlayer.create(this, R.raw.lland_bgm);
            bgm.setLooping(true);
        }
        switch(curView) {
            case VIEW_LAUNCH:
                renderLaunch();
                break;
            case VIEW_WORLD:
                renderWorld();
                break;
            default:
        }
    }
    private void renderLaunch(){
        setContentView(R.layout.activity_lland_launch);
        if(bgm.isPlaying()){
            bgm.pause();
        }
        //update rank
        ListView wkRankLv = (ListView) findViewById(R.id.wk_rank_list);
        new Scores(getApplicationContext()).execute(Config.FUNC.GET_SCORES,Scores.DATA_TYPE_WKMAX,wkRankLv);
    }

    private void renderWorld(){
        setContentView(R.layout.lland);
        LLand world = (LLand) findViewById(R.id.world);
        world.setScoreField((TextView) findViewById(R.id.score),(TextView) findViewById(R.id.max_score));
        world.setSplash(findViewById(R.id.welcome));
        Log.v(LLand.TAG, "focus: " + world.requestFocus());

        if(!bgm.isPlaying()) {
            bgm.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            switch(curView) {
                case VIEW_WORLD:
                    renderLaunch();
                    curView = VIEW_LAUNCH;
                    break;
                default:
                    return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startGame(View view){
        renderWorld();
        curView = 2;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        bgm.stop();
        bgm.release();
    }
}
