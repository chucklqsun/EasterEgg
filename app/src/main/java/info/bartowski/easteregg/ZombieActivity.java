/*
 * Copyright (C) 2010 The Android Open Source Project
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
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class ZombieActivity extends Activity {
    Toast mToast;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = MediaPlayer.create(this,R.raw.zombie);
        mPlayer.start();

        mToast = Toast.makeText(this, "Zombie art by Jack Larson", Toast.LENGTH_SHORT);

        ImageView content = new ImageView(this);
        content.setImageResource(R.drawable.zombie);
        content.setScaleType(ImageView.ScaleType.FIT_CENTER);

        setContentView(content);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mToast.show();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onResume(){
        super.onResume();
        mPlayer.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        mPlayer.pause();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPlayer.stop();
    }
}
