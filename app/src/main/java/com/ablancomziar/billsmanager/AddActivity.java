package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {


    private Spinner spinner;

    private List<ITag> selected;
    private List<ITag> available;
    private List<Invoice> invoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        spinner = findViewById(R.id.spinnerSelectTagForNewInvoice);

        selected = new ArrayList<>();


        App a = (App) getApplication();


        available = a.getTags();

        setSpinnerAdapter();

    }



    private void setSpinnerAdapter(){
        ArrayList<String> spinnerArray = new ArrayList<>();
        for(ITag tag : available)
            spinnerArray.add(tag.getName());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
    }




    }