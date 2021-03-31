package com.ablancomziar.billsmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static com.ablancomziar.billsmanager.App.APP_TAG;

class InvoiceHandler implements IInvoiceHandler{
    private List<Invoice> invoices;
    private App app;
    private static final String INVOICE_FILE_START = "BILLS_MANAGER_INVOICE_";
    private static final String FILENAME_INCOICES = "invoices8";

    public InvoiceHandler(App a){
        invoices = new ArrayList<>();
        app = a;
        handleInvoiceFile();
    }

    @Override
    public File writeInvoiceDownload(Invoice i, Activity activity) {
        App.verifyStoragePermissions(activity);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filename = INVOICE_FILE_START + i.getName() + ".html";
        File fileout = new File(folder, filename);
        try (FileOutputStream fos = new FileOutputStream(fileout)) {
            PrintStream ps = new PrintStream(fos);
            ps.print(i.toHTML(app,app.getTagHandler()));
            ps.close();
        } catch (FileNotFoundException e) {
            Log.e(APP_TAG,"File not found",e);
            return null;
        } catch (IOException e) {
            Log.e(APP_TAG,"Error I/O",e);
            return null;
        }
        return fileout;
    }

    @Override
    public List<Invoice> getInvoices() {
        return invoices;
    }

    @Override
    public void AddInvoice(Invoice i){
        this.invoices.add(i);
        saveInvoices();
    }

    private void saveInvoices(){
        try {
            FileOutputStream fos = app.openFileOutput(FILENAME_INCOICES, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            Log.println(Log.DEBUG, APP_TAG, "saved " + invoices);

            os.writeObject(invoices);
            os.close();
            fos.close();
            Log.println(Log.DEBUG, APP_TAG, "Write on file successfull");
        } catch (IOException e) {
            Log.println(Log.DEBUG, APP_TAG, "could not write on file !");
            e.printStackTrace();
        }
    }

    private void handleInvoiceFile(){
        Log.println(Log.DEBUG, APP_TAG, "INVOICE FILE!");
        File fileInvoices = new File(app.getFilesDir(), FILENAME_INCOICES);
        if (!fileInvoices.exists()) {
            try {
                fileInvoices.createNewFile();
            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot create file !");
            }
            Log.println(Log.DEBUG, APP_TAG, "create file !");
        } else {
            try {
                Log.println(Log.DEBUG, APP_TAG, "found file !");
                FileInputStream fis = app.openFileInput(FILENAME_INCOICES);
                ObjectInputStream is = new ObjectInputStream(fis);
                invoices = (List<Invoice>) is.readObject();
                Log.println(Log.DEBUG, APP_TAG, "retrieve " + invoices);
                is.close();
                fis.close();

            } catch (IOException e) {
                Log.println(Log.DEBUG, APP_TAG, "cannot open file !");
                e.printStackTrace();
            }  catch (Exception e) {
                Log.println(Log.DEBUG, APP_TAG, "Error reading file invoice !");
                e.printStackTrace();
            }

        }
    }
}