package com.ablancomziar.billsmanager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ablancomziar.billsmanager.App.APP_TAG;

class TagHandler implements ITagHandler{
    private List<ITag> tags;
    private List<CustomTag> customTags;
    private App app;
    public static final String FILENAME_TAGS = "test11";
    public static final int LAST_ID_DEFAULT = 10;
    private int currentLastId;

    public TagHandler(App a){
        app = a;
        handleTagFile();
    }

    private void handleTagFile() {
        customTags = new ArrayList<>();
        File file = new File(app.getFilesDir(), FILENAME_TAGS);
        Log.println(Log.DEBUG, APP_TAG, "TAG FILE!");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot create file !");
            }
            tags = new ArrayList<ITag>(Arrays.asList(DefaultTag.getAllDefaultTag(app)));
            Log.println(Log.DEBUG, APP_TAG, "create file !");
            currentLastId = LAST_ID_DEFAULT;
        } else {
            try {
                List<String> lines = app.readAllLines(file);
                Log.println(Log.DEBUG, APP_TAG, "found file !");
                ITag[] tag = DefaultTag.getAllDefaultTag(app);
                tags = new ArrayList<>();
                for (int i = 0; i < tag.length; i++)
                    tags.add(tag[i]);
                currentLastId = Integer.valueOf(lines.get(0));
                Log.println(Log.DEBUG, APP_TAG, "current " + currentLastId);
                lines.remove(0);
                for(String line: lines){
                    String strs[] = line.split(",");
                    CustomTag t = new CustomTag(app,strs[1],Integer.valueOf(strs[0]),Integer.valueOf(strs[2]));
                    tags.add(t);
                    customTags.add(t);
                }
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot open file !");
                e.printStackTrace();
            }  catch (Exception e) {
                Log.println(Log.DEBUG, APP_TAG, "Error reading file !" + e.getMessage());
                e.printStackTrace();
            }

        }

    }

    @Override
    public List<ITag> getTags() {
        return  (List<ITag>) (((ArrayList<ITag>)tags).clone());
    }



    @Override
    public ITag getTagById(int id){
        //tags.stream().filter(e -> e.getId() == id);
        for (ITag t : tags)
            if(t.getId() == id)
                return t;
        return null;
    }

    @Override
    public ITag addTag(String name){
        currentLastId++;
        Random r = new Random();
        CustomTag t = new CustomTag(app,name,currentLastId, Color.rgb(r.nextInt(225),r.nextInt(225),r.nextInt(225)));
        for(ITag tag : tags){
            if (tag.getName().equals(t.getName()))
                return null;
        }
        this.customTags.add(t);
        this.tags.add(t);
        this.saveCustomTags();
        return t;
    }


    private void saveCustomTags(){
        List<String> s = new ArrayList<>();
        s.add(customTags.get(customTags.size()-1).getId() + "\n");
        for (CustomTag c : this.customTags)
            s.add(c.getId() + "," + c.getName() + "," + c.getColor() + "\n");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(app.openFileOutput(FILENAME_TAGS, Context.MODE_PRIVATE));
            for(String str: s)
                outputStreamWriter.write(str);
            outputStreamWriter.close();
            Log.println(Log.DEBUG, APP_TAG, "Write on file successfull : " + s);
        } catch (IOException e) {
            Log.println(Log.DEBUG, APP_TAG, "could not write on file !");
            e.printStackTrace();
        }
    }

}