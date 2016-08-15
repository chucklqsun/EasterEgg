/*
 * Copyright (C) 2015 The Android Open Source Project
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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MLandActivity extends Activity {
    MLand mLand;
    View minus;
    View plus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mland);
        minus = findViewById(R.id.player_minus_button);
        plus = findViewById(R.id.player_plus_button);

        plus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLand.addPlayer();
                        updateSplashPlayers();
                    }
                }
        );
        minus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLand.removePlayer();
                        updateSplashPlayers();
                    }
                }
        );

        mLand = (MLand) findViewById(R.id.world);
        mLand.setScoreFieldHolder((ViewGroup) findViewById(R.id.scores));
        final View welcome = findViewById(R.id.welcome);
        mLand.setSplash(welcome);
        final int numControllers = mLand.getGameControllers().size();
        if (numControllers > 0) {
            mLand.setupPlayers(numControllers);
        }


    }

    public void updateSplashPlayers() {
        final int N = mLand.getNumPlayers();

        if (N == 1) {
            minus.setVisibility(View.INVISIBLE);
            plus.setVisibility(View.VISIBLE);
            plus.requestFocus();
        } else if (N == mLand.MAX_PLAYERS) {
            minus.setVisibility(View.VISIBLE);
            plus.setVisibility(View.INVISIBLE);
            minus.requestFocus();
        } else {
            minus.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        mLand.stop();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mLand.onAttachedToWindow(); // resets and starts animation
        updateSplashPlayers();
        mLand.showSplash();
    }

    public void startButtonPressed(View v) {
        minus.setVisibility(View.INVISIBLE);
        plus.setVisibility(View.INVISIBLE);
        mLand.start(true);
    }
}
