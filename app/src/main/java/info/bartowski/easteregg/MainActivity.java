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

        targetMap.put(R.id.egg_dessertcase_rb,DessertCase.class);
        targetMap.put(R.id.egg_mland_rb,MLandActivity.class);
        targetMap.put(R.id.egg_lland_rb,LLandActivity.class);

        Intent intent = new Intent(this, targetMap.get(rg.getCheckedRadioButtonId()));
        startActivity(intent);
    }
}
