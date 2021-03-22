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
    private static String filename = "test1";
    private List<ITag> tags;
    private List<CustomTag> customTags;
    private File file;
    private int currentLastId;
    private static final int LAST_ID_DEFAULT = 10;

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
            currentLastId = LAST_ID_DEFAULT;
        } else {
            try {
                // TODO: change to apply lower api version
                List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

                Log.println(Log.DEBUG, APP_TAG, "found file !");
                Log.println(Log.DEBUG, APP_TAG, "Got string first line : " + lines.get(0));
                for(String line: lines)
                    Log.println(Log.DEBUG, APP_TAG, line);

                ITag[] tag = DefaultTag.getAllDefaultTag(this);
                tags = new ArrayList<>();
                for (int i = 0; i < tag.length; i++)
                    tags.add(tag[i]);
                currentLastId = Integer.valueOf(lines.get(0));
                Log.println(Log.DEBUG, APP_TAG, "current " + currentLastId);
                lines.remove(0);
                for(String line: lines){
                    String strs[] = line.split(",");
                    CustomTag t = new CustomTag(this,strs[1],Integer.valueOf(strs[0]));
                    tags.add(t);
                    customTags.add(t);
                }
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot open file !");
                e.printStackTrace();
            }  catch (Exception e) {
                Log.println(Log.DEBUG, APP_TAG, "Error reading file !");
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

    public boolean addTag(String name){
        currentLastId++;
        CustomTag t = new CustomTag(this,name,currentLastId);
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
        s.add(customTags.get(customTags.size()-1).getId() + "");
        for (CustomTag c : this.customTags)
            s.add(c.getId() + "," + c.getName());
        try {
            Files.write(Paths.get(file.getPath()),s);
            Log.println(Log.DEBUG, APP_TAG, "Write on file successfull : " + s);
        } catch (IOException e) {
            Log.println(Log.DEBUG, APP_TAG, "could not write on file !");
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }
}