package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuInteractionListener {

    //sub class to handle individual menu data
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

    private MenuData[] MENU_DATA (){
        return new MenuData[]{
                new MenuData(R.drawable.add, getString(R.string.add),AddActivity.class),
                new MenuData(R.drawable.tags, getString(R.string.edit_tags),TagActivity.class),
                new MenuData(R.drawable.report, getString(R.string.saved_invoices),SavedActivity.class),
                new MenuData(R.drawable.chart, getString(R.string.stats),StatsActivity.class),
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.version)).setText(getString(R.string.version, BuildConfig.VERSION_NAME));

        if(savedInstanceState == null){

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for( MenuData data : MENU_DATA()){
                MenuItem fragment = MenuItem.newInstance(data.getId(),data.getName());
                fragmentTransaction.add(R.id.parent_menu,fragment);
            }

            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(String text) {
        Log.d(App.getAppTag(),text);
        for( MenuData data : MENU_DATA())
            if(data.getName().equals(text)){
                Intent t = new Intent(this,data.getCls());
                startActivity(t);
            }
    }

    // we need that so the list is not re-render when the screen rotate
    @Override
    public void onSaveInstanceState(Bundle outInstanceState) {
        super.onSaveInstanceState(outInstanceState);
        outInstanceState.putInt("dummy", 1);
    }
}
