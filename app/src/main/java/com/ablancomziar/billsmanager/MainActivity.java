package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuInteractionListener {

    class MenuData{
        private int id;
        private String name;
        private Class<?> cls;

        public MenuData(int id, String name, Class<?> cls) {
            this.id = id;
            this.name = name;
            this.cls = cls;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Class<?> getCls() {
            return cls;
        }
    }

    private final MenuData[] MENU_DATA = new MenuData[]{
            new MenuData(R.drawable.add, "Add",AddActivity.class),
            new MenuData(R.drawable.tags, "EditTags",TagActivity.class),
            // todo : better word !!
            new MenuData(R.drawable.report, "Previous ??",MainActivity.class),
            new MenuData(R.drawable.chart, "Chart",MainActivity.class),
            new MenuData(R.drawable.privacy, "Privacy policies",MainActivity.class),
    };

    static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.version)).setText(getString(R.string.version, BuildConfig.VERSION_NAME));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for( MenuData data : MENU_DATA){
            MenuItem fragment = MenuItem.newInstance(data.getId(),data.getName());
            fragmentTransaction.add(R.id.parent_menu,fragment);
        }

        fragmentTransaction.commit();
    }



    @Override
    public void onFragmentInteraction(String text) {
        Log.d(App.getAppTag(),text);
        for( MenuData data : MENU_DATA)
            if(data.getName().equals(text)){
                Intent t = new Intent(this,data.getCls());
                startActivity(t);
            }
    }
}
