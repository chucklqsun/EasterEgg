/*);
 * Copyright (C) 2012 The Android Open Source Project
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openEasterEgg(View view){
        RadioGroup rg = (RadioGroup) findViewById(R.id.egg_choose_rg);
        Map<Integer,Class> targetMap = new HashMap<>();

        targetMap.put(R.id.egg_beanbag_rb,BeanBag.class);
        targetMap.put(R.id.egg_dessertcase_rb,DessertCase.class);
        targetMap.put(R.id.egg_mland_rb,MLandActivity.class);
        targetMap.put(R.id.egg_lland_rb,LLandActivity.class);

        Intent intent = new Intent(this, targetMap.get(rg.getCheckedRadioButtonId()));
        startActivity(intent);
    }
}
