package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedActivity extends AppCompatActivity {

    private List<ITag> selected;
    private List<ITag> available;
    private List<Invoice> invoices;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        selected = new ArrayList<>();

        spinner = findViewById(R.id.spinner_tags);

        final IInvoiceHandler a = ((App) getApplication()).getInvoiceHandler();
        final ITagHandler tagHandler = ((App) getApplication()).getTagHandler();

        available = tagHandler.getTags();

        setSpinnerAdapter();
        Button b = findViewById(R.id.add_button_saved);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String str = spinner.getSelectedItem().toString();
                    Log.d(App.getAppTag(),"got tag : " + str);
                    for (ITag tag : available)
                        if(tag.getName().equals(str)){
                            selected.add(tag);
                            updateTagView();
                            available.remove(tag);
                            setSpinnerAdapter();
                            updateInvoiceView();
                            return;
                        }
                    throw new Exception("Could not find the tag in selected");
                } catch (Exception e){
                    Log.e(App.getAppTag(),"got error : " + e.getMessage());
                    Toast.makeText(v.getContext(), getString(R.string.could_not_add),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        invoices = a.getInvoices();
        updateInvoiceView();
    }

    private void setSpinnerAdapter(){
        ArrayList<String> spinnerArray = new ArrayList<>();
        for(ITag tag : available)
            spinnerArray.add(tag.getName());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void updateTagView(){
        ViewGroup group = findViewById(R.id.tags_view_small);
        group.removeAllViews();
        for(ITag tag : selected)
            addToTagView(tag,getLayoutInflater(),group);
    }

    private void addToTagView(ITag tag, LayoutInflater layoutInflater, ViewGroup parentLayout ){
        View view = layoutInflater.inflate(R.layout.small_tag_layout, parentLayout, false);

        TextView textView = view.findViewById(R.id.tag_layout_text);
        textView.setText(tag.getName());

        ImageButton button = view.findViewById(R.id.imageButton);
        final ITag _tag = tag;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.remove(_tag);
                updateTagView();
                available.add(_tag);
                setSpinnerAdapter();
                updateInvoiceView();
            }
        });

        if (tag.hasIcon()){
            ImageView img = view.findViewById(R.id.tagImageView);
            img.setImageDrawable(tag.getIcon());
            img.setColorFilter(tag.getColor());
        }

        parentLayout.addView(view);
    }

    private void updateInvoiceView(){
        ViewGroup group = findViewById(R.id.invoices);
        group.removeAllViews();
        Set<Invoice> toDisplay = new HashSet<>();
        for(Invoice i : invoices){
            if(selected.isEmpty())
                toDisplay.add(i);
            for(ITag tag : selected)
                if(i.getTags() != null)
                    for(int tagId : i.getTags())
                        if (tagId == tag.getId())
                            toDisplay.add(i);
        }
        for(Invoice i : toDisplay)
            addToInvoiceView(i, getLayoutInflater(), group);
    }

    private void addToInvoiceView(final Invoice invoice, LayoutInflater layoutInflater, ViewGroup parentLayout ){
        View view = layoutInflater.inflate(R.layout.invoice_layout, parentLayout, false);

        TextView date = view.findViewById(R.id.date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(invoice.getInvoiceDate());
        date.setText(getString(R.string.date,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.YEAR)));

        TextView amount = view.findViewById(R.id.amount);
        amount.setText(getString(R.string.amount,invoice.getAmount()));

        TextView textView = view.findViewById(R.id.name);
        textView.setText(invoice.getName());

        final IInvoiceHandler invoiceHandler = ((App) getApplication()).getInvoiceHandler();
        final SavedActivity s = this;

        ImageButton download = view.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = invoiceHandler.writeInvoiceDownload(invoice,SavedActivity.this);
                if (file != null)
                    Toast.makeText(v.getContext(), getString(R.string.download_succ),
                        Toast.LENGTH_LONG).show();
                else Toast.makeText(v.getContext(), getString(R.string.could_not_download),
                        Toast.LENGTH_LONG).show();
            }
        });

        ImageButton edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(s,AddActivity.class);
                intent.putExtra(AddActivity.INVOICE_INTENT,invoice);
                startActivity(intent);
            }
        });

        ImageButton see = view.findViewById(R.id.see);
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = invoiceHandler.writeInvoiceDownload(invoice,SavedActivity.this);
                if (file != null){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                    intent.setDataAndType(uri, "text/html");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
                else Toast.makeText(v.getContext(), getString(R.string.could_not_download),
                        Toast.LENGTH_LONG).show();
            }
        });

        ImageButton delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invoiceHandler.removeInvoice(invoice)){
                    Toast.makeText(v.getContext(), getString(R.string.delete_succ),
                            Toast.LENGTH_LONG).show();
                    updateInvoiceView();
                }
                else Toast.makeText(v.getContext(), getString(R.string.could_not_delete),
                        Toast.LENGTH_LONG).show();
            }
        });

        parentLayout.addView(view);
    }
}
