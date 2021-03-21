package com.ablancomziar.billsmanager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends Application {

    private static String APP_TAG = "tagg";
    private static Context mContext;
    private static String filename = "custom_tag";
    private List<ITag> tags;
    private List<CustomTag> customTags;
    private File file;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        customTags = new ArrayList<>();
        file = new File(this.getFilesDir(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot create file !");
            }
            tags = new ArrayList<ITag>(Arrays.asList(DefaultTag.getAllDefaultTag(this)));
            Log.println(Log.DEBUG, APP_TAG, "create file !");
        } else {
            try {
                // TODO: change to apply lower api version
                List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
                Log.println(Log.DEBUG, APP_TAG, "found file !");
                ITag[] tag = DefaultTag.getAllDefaultTag(this);
                tags = new ArrayList<>();
                for (int i = 0; i < tag.length; i++)
                    tags.add(tag[i]);
                for(String line: lines){
                    CustomTag t = new CustomTag(this,line);
                    tags.add(t);
                    customTags.add(t);
                }
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot open file !");
                e.printStackTrace();
            }

        }
        mContext = this;
    }

    public static String getAppTag() {
        return APP_TAG;
    }

    public List<ITag> getTags() {
        return tags;
    }

    public boolean addTag(CustomTag t){
        for(ITag tag : tags){
            if (tag.getName().equals(t.getName()))
                return false;
        }
        this.customTags.add(t);
        this.tags.add(t);
        this.saveCustomTags();
        return true;
    }

    @SuppressLint("NewApi")
    private void saveCustomTags(){
        List<String> s = new ArrayList<>();
        for (CustomTag c : this.customTags)
            s.add(c.getName());
        try {
            Files.write(Paths.get(file.getPath()),s);
        } catch (IOException e) {
            Log.println(Log.DEBUG, APP_TAG, "could not write on file !");
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }
}