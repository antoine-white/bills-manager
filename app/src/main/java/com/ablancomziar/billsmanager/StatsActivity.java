package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatsActivity extends AppCompatActivity {

    private static final int DEFAULT_YEAR = 2021;
    private static final int DEFAULT_MONTH = 0;
    private static final int DEFAULT_DAY = 1;

    private EditText edittext;
    private EditText endEditText;
    private Calendar startCalendar;
    private Calendar endCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, DEFAULT_YEAR);
        startCalendar.set(Calendar.MONTH, DEFAULT_MONTH);
        startCalendar.set(Calendar.DAY_OF_MONTH, DEFAULT_DAY);
        endCalendar = Calendar.getInstance();

        edittext = findViewById(R.id.start);
        final DatePickerDialog.OnDateSetListener start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(true);
            }
        };


        endEditText = findViewById(R.id.end);
        final DatePickerDialog.OnDateSetListener end = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(false);
            }
        };

        final Context ctx = this;

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ctx, start, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ctx, end, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate();
            }
        });

        updateLabel(true);
        updateLabel(false);
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static <T> Float getMaxValue(Map<T,Float> map){
        Float maxValue = Float.MIN_VALUE;
        for(T i : map.keySet())
            if(map.get(i) > maxValue)
                maxValue = map.get(i);
        return maxValue;
    }

    private void updateLabel(boolean start) {
        String myFormat = getString(R.string.format_date);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        if (start)
            edittext.setText(sdf.format(startCalendar.getTime()));
        else
            endEditText.setText(sdf.format(endCalendar.getTime()));
    }

    private void generate(){
        Log.d(App.getAppTag(),"got date start : " + startCalendar);
        Log.d(App.getAppTag(),"got date end : " + endCalendar);
        App a = (App) getApplication();
        List<Invoice> all = a.getInvoices();
        List<Invoice> btwDate = new ArrayList<>();
        for(Invoice i : all){
            Calendar c = toCalendar(i.getInvoiceDate());
            if(startCalendar.before(c) && endCalendar.after(c))
                btwDate.add(i);
        }
        Map<Integer,Float> values = getTagAndAmount(btwDate);
        updateGraphicView(values);

    }

    private Map<Integer,Float> getTagAndAmount(List<Invoice> invoices){
        Map<Integer,Float> res = new HashMap<>();
        for(Invoice i : invoices){
            for(int id : i.getTags()){
                if(res.containsKey(id)){
                    res.put(id,res.get(id) + i.getAmount());
                } else
                    res.put(id,i.getAmount());
            }
        }
        return res;
    }

    private void updateGraphicView(Map<Integer,Float> values){
        ViewGroup group = findViewById(R.id.graphics);
        group.removeAllViews();
        App a = (App) getApplication();
        Float max = getMaxValue(values);
        for(Map.Entry<Integer,Float> entry : values.entrySet())
            addIndividualGraphic(new Pair<>(a.getTagById(entry.getKey()),entry.getValue()),getLayoutInflater(),group , max);
    }

    private void addIndividualGraphic(Pair<ITag,Float> value, LayoutInflater layoutInflater, ViewGroup parentLayout, float max){
        View view = layoutInflater.inflate(R.layout.graphic_tag, parentLayout, false);

        // In order to get the view we have to use the new view with text_layout in it
        TextView textView = view.findViewById(R.id.tag_name_graph);
        textView.setText(value.first.getName());

        if (value.first.hasIcon()){
            ImageView img = view.findViewById(R.id.tagImageView);
            img.setImageDrawable(value.first.getIcon());
            img.setColorFilter(value.first.getColor());
        }
        parentLayout.addView(view);
    }
}
