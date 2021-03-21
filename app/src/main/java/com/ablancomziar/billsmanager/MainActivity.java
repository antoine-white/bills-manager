package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App a = (App) getApplication();
        List<ITag> tags = a.getTags();
        for (ITag t : tags)
            Log.println(Log.DEBUG,App.getAppTag(),t.getName());
        a.addTag(new CustomTag("enfants"));
    }
}
