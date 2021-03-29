package com.ablancomziar.billsmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class App extends Application {

    private static final String INVOICE_FILE_START = "BILLS_MANAGER_INVOICE_";
    private static String APP_TAG = "tagg";
    private static Context mContext;
    private static String filename = "test6";
    private static final String FILENAME_INCOICES = "invoices8";
    private List<ITag> tags;
    private List<Invoice> invoices;
    private List<CustomTag> customTags;
    private File file;
    private File fileInvoices;
    private int currentLastId;
    private static final int LAST_ID_DEFAULT = 10;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Vérifie si nous avons les droits d'écriture
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Aie, il faut les demander àl'utilisateur
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static List<String> readAllLines(File file) throws IOException {
        List<String> strs = new ArrayList<>();
        Scanner input=new Scanner(file);
        while(input.hasNextLine())
            strs.add(input.nextLine());
        return strs;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        // ============================ //

        invoices = new ArrayList<>();
        customTags = new ArrayList<>();
        file = new File(this.getFilesDir(), filename);
        Log.println(Log.DEBUG, APP_TAG, "TAG FILE!");
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
                List<String> lines = readAllLines(file);

                Log.println(Log.DEBUG, APP_TAG, "found file !");
                ITag[] tag = DefaultTag.getAllDefaultTag(this);
                tags = new ArrayList<>();
                for (int i = 0; i < tag.length; i++)
                    tags.add(tag[i]);
                currentLastId = Integer.valueOf(lines.get(0));
                Log.println(Log.DEBUG, APP_TAG, "current " + currentLastId);
                lines.remove(0);
                for(String line: lines){
                    String strs[] = line.split(",");
                    CustomTag t = new CustomTag(this,strs[1],Integer.valueOf(strs[0]),Integer.valueOf(strs[2]));
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

        // ---------------------------- //

        Log.println(Log.DEBUG, APP_TAG, "INVOICE FILE!");
        fileInvoices = new File(this.getFilesDir(), FILENAME_INCOICES);
        if (!fileInvoices.exists()) {
            try {
                fileInvoices.createNewFile();
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot create file !");
            }
            tags = new ArrayList<ITag>(Arrays.asList(DefaultTag.getAllDefaultTag(this)));
            Log.println(Log.DEBUG, APP_TAG, "create file !");
        } else {
            try {
                Log.println(Log.DEBUG, APP_TAG, "found file !");
                FileInputStream fis = openFileInput(FILENAME_INCOICES);
                ObjectInputStream is = new ObjectInputStream(fis);
                invoices = (List<Invoice>) is.readObject();
                is.close();
                fis.close();

            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot open file !");
                e.printStackTrace();
            }  catch (Exception e) {
                Log.println(Log.DEBUG, APP_TAG, "Error reading file !");
                e.printStackTrace();
            }

        }
        // ---------------------------- //
        mContext = this;
    }

    public static String getAppTag() {
        return APP_TAG;
    }

    public List<ITag> getTags() {
        return  (List<ITag>) (((ArrayList<ITag>)tags).clone());
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void AddInvoice(Invoice i){
        this.invoices.add(i);
        saveInvoices();
    }

    public ITag getTagById(int id){
        //tags.stream().filter(e -> e.getId() == id);
        for (ITag t : tags)
            if(t.getId() == id)
                return t;
        return null;
    }

    public ITag addTag(String name){
        currentLastId++;
        Random r = new Random();
        CustomTag t = new CustomTag(this,name,currentLastId, Color.rgb(r.nextInt(225),r.nextInt(225),r.nextInt(225)));
        for(ITag tag : tags){
            if (tag.getName().equals(t.getName()))
                return null;
        }
        this.customTags.add(t);
        this.tags.add(t);
        this.saveCustomTags();
        return t;
    }

    //todo
    @SuppressLint("NewApi")
    private void saveCustomTags(){
        List<String> s = new ArrayList<>();
        s.add(customTags.get(customTags.size()-1).getId() + "");
        for (CustomTag c : this.customTags)
            s.add(c.getId() + "," + c.getName() + "," + c.getColor());
        try {
            Files.write(Paths.get(file.getPath()),s);
            Log.println(Log.DEBUG, APP_TAG, "Write on file successfull : " + s);
        } catch (IOException e) {
            Log.println(Log.DEBUG, APP_TAG, "could not write on file !");
            e.printStackTrace();
        }
    }

    private void saveInvoices(){
        try {
            FileOutputStream fos = this.openFileOutput(FILENAME_INCOICES, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(invoices);
            os.close();
            fos.close();
            Log.println(Log.DEBUG, APP_TAG, "Write on file successfull");
        } catch (IOException e) {
            Log.println(Log.DEBUG, APP_TAG, "could not write on file !");
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }


    public File writeInvoiceDownload(Invoice i, Activity activity) {
        verifyStoragePermissions(activity);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filename = INVOICE_FILE_START + i.getName() + ".html";
        File fileout = new File(folder, filename);
        try (FileOutputStream fos = new FileOutputStream(fileout)) {
            PrintStream ps = new PrintStream(fos);
            ps.print(i.toHTML(this));
            ps.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG,"File not found",e);
            return null;
        } catch (IOException e) {
            Log.e(TAG,"Error I/O",e);
            return null;
        }
        return fileout;
    }


}