package com.ablancomziar.billsmanager;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App extends Application {

    public static final String APP_TAG = "billM";

    private IInvoiceHandler invoiceHandler;
    private ITagHandler tagHandler;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
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
        invoiceHandler = new InvoiceHandler(this);
        tagHandler = new TagHandler(this);
    }

    public ITagHandler getTagHandler(){
        return tagHandler;
    }

    public IInvoiceHandler getInvoiceHandler(){
        return invoiceHandler;
    }

    public static String getAppTag() {
        return APP_TAG;
    }
}