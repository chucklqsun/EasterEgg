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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;
import android.support.graphics.drawable.VectorDrawableCompat;

public class Utility {
    public static Drawable getCompatDrawable(Context context, int vector_drawable_id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(vector_drawable_id, context.getTheme());
        } else {
            return VectorDrawableCompat.create(context.getResources(), vector_drawable_id, context.getTheme());
        }
    }

    public static void compatVibrate(Vibrator vb, long milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes mAudioAttrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME).build();
            vb.vibrate(milliseconds, mAudioAttrs);
        } else {
            vb.vibrate(milliseconds);
        }
    }
}
