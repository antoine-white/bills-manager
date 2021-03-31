package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pair;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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

        // starts with default first 2 dates
        updateLabel(true);
        updateLabel(false);
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static List<Pair<Integer,Float>> sort(Map<Integer,Float> map){
        List<Pair<Integer,Float>> l = new ArrayList<>();
        for(Map.Entry<Integer,Float> entry : map.entrySet())
            l.add(new Pair<>(entry.getKey(),entry.getValue()));
        List<Pair<Integer,Float>> res = new ArrayList<>();
        while(l.size() > 0){
            Pair<Integer, Float> max_ = max(l);
            l.remove(max_);
            res.add(max_);
        }
        return res;
    }

    private static Pair<Integer, Float> max(List<Pair<Integer, Float>> l) {
        Pair<Integer, Float> max = l.get(0);
        for (int i = 1; i < l.size(); i++) {
            if(l.get(i).second > max.second)
                max = l.get(i);
        }
        return max;
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
        final IInvoiceHandler a = ((App) getApplication()).getInvoiceHandler();
        List<Invoice> all = a.getInvoices();
        List<Invoice> btwDate = new ArrayList<>();
        for(Invoice i : all){
            Calendar c = toCalendar(i.getInvoiceDate());
            Log.d(App.getAppTag(),"is before : " + startCalendar.before(c));
            Log.d(App.getAppTag(),"is after : " + endCalendar.after(c));
            if(startCalendar.before(c) && endCalendar.after(c))
                btwDate.add(i);
        }
        Log.d(App.getAppTag(),"lll  : " + btwDate);
        Map<Integer,Float> values = getTagAndAmount(btwDate);
        Log.d(App.getAppTag(),"Values  : " + values);
        updateGraphicView(values);

    }

    private Map<Integer,Float> getTagAndAmount(List<Invoice> invoices){
        Map<Integer,Float> res = new HashMap<>();
        for(Invoice i : invoices){
            if(i.getTags() != null)
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
        final ITagHandler tagHandler = ((App) getApplication()).getTagHandler();
        List<Pair<Integer,Float>> l = sort(values);
        for(Pair<Integer,Float> p : l)
            addIndividualGraphic(new Pair<>(tagHandler.getTagById(p.first),p.second),getLayoutInflater(),group , l.get(0).second);
    }

    private void addIndividualGraphic(Pair<ITag,Float> value, LayoutInflater layoutInflater, ViewGroup parentLayout, float max){
        View view = layoutInflater.inflate(R.layout.graphic_tag, parentLayout, false);

        // In order to get the view we have to use the new view with text_layout in it
        TextView textView = view.findViewById(R.id.tag_name_graph);
        textView.setText(value.first.getName());


        TextView amount = view.findViewById(R.id.amount);
        amount.setText(getString(R.string.amount,value.second));


        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ObjectAnimator.ofInt(progressBar, "progress",
                0, (int)((value.second / max ) * 100f))
                .setDuration(2000)
                .start();

        // without animation but color uncomment this
        //progressBar.setProgress((int)((value.second / max ) * 100f));
        //progressBar.getProgressDrawable().setColorFilter(value.first.getColor(), PorterDuff.Mode.SRC_IN);

        if (value.first.hasIcon()){
            ImageView img = view.findViewById(R.id.imageViewTag);
            img.setImageDrawable(value.first.getIcon());
            img.setColorFilter(value.first.getColor());
        }
        parentLayout.addView(view);
    }
}
