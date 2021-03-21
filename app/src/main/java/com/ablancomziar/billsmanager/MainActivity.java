package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuInteractionListener {

    private static final Pair<Integer,String>[] MENU_DATA = new Pair[]{
            new Pair<>(R.drawable.add, "Add"),
            new Pair<>(R.drawable.tags, "EditTags"),
            // todo better word !!
            new Pair<>(R.drawable.report, "Previous ??"),
            new Pair<>(R.drawable.chart, "Chart"),
            new Pair<>(R.drawable.privacy, "Privacy policies"),
    };

    static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App a = (App) getApplication();
        List<ITag> tags = a.getTags();
        for (ITag t : tags)
            Log.println(Log.DEBUG,App.getAppTag(),t.getName());
        a.addTag(new CustomTag(this,"enfants"));



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for( Pair<Integer,String> data : MENU_DATA){
            MenuItem fragment = MenuItem.newInstance(data.first,data.second);
            fragmentTransaction.add(R.id.parent_menu,fragment);
        }

        fragmentTransaction.commit();
    }



    @Override
    public void onFragmentInteraction(String text) {
        Log.d("jj",text);
    }
}
