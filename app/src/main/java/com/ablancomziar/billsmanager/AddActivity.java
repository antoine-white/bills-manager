package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddActivity extends AppCompatActivity {


    private Spinner spinner;

    private List<ITag> selected;
    private List<ITag> available;
    private List<Invoice> invoices;


    private EditText amountEditText;
    private EditText editTextDate;
    private RadioButton debtor;
    private RadioButton creditor;
    private EditText destNameEdit;

    private Spinner spinnerPaymentTypes;

    private EditText personalNameEdit;
    private EditText notesEditText;

    private Button addButton;

    private Invoice invoice;


    private ImageButton addTag;
    private ArrayList<ITag> selectedList;
    //private int[] tagIds;


    private  final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.amountEditText = findViewById(R.id.AmountEditText);
        this.editTextDate = findViewById(R.id.editTextDate);
        this.debtor = findViewById(R.id.radioButton);
        this.creditor = findViewById(R.id.radioButton2);
        this.destNameEdit = findViewById(R.id.DestNameEdit);
        this.spinnerPaymentTypes = findViewById(R.id.PaymentTypesSpinner);
        this.addButton = findViewById(R.id.AddInvoice);
        this.personalNameEdit = findViewById(R.id.PersonalNameEdit);
        this.notesEditText = findViewById(R.id.NotesEditText);
        this.addTag = findViewById(R.id.AddButtonSelectTag);

        this.selectedList = new ArrayList<>();

        parseDate();

        spinner = findViewById(R.id.spinnerSelectTagForNewInvoice);

        selected = new ArrayList<>();


        final App a = (App) getApplication();


        available = a.getTags();

        setSpinnerAdapter();

        addTag.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
              String str = spinner.getSelectedItem().toString();
              ArrayList<ITag> checklist = new ArrayList();
              for (ITag tag : available)
                  if(tag.getName().equals(str)){
                      selected.add(tag);
                      checklist.add(tag);
                      displaySelectedTag();
                      return;
                  }


              }
        });



        //spinner.setOnTouchListener(spinnerOnTouch);

        //spinner.setOnKeyListener(spinnerOnKey);




        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            int requiredField = testRequiredField();
            if(requiredField != 4 ){
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.toastfieldrequired), Toast.LENGTH_SHORT);
                toast.show();

            } else {
                boolean iscredit = true;
                if((debtor.isChecked()))
                    iscredit = false;


                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                try {
                    Date date = sdf.parse(editTextDate.getText().toString());
                    invoice = new Invoice(Float.parseFloat(amountEditText.getText().toString()),date ,iscredit,destNameEdit.getText().toString());
                    addOptionalInfo();

                    a.AddInvoice(invoice);


                    int cpt = 0;
                    int[] tagIds = new int[selected.size()];
                    for(ITag tag : selected){
                        tagIds[cpt] = tag.getId();
                        Log.v("test_tag_id_size", selected.get(cpt).getName());
                        cpt ++;
                    }
                    invoice.addTagsId(tagIds);

                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.toastinvoicecompleted), Toast.LENGTH_SHORT);
                    toast.show();
                    Intent activity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(activity);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            }
        });

    }


    private void setSpinnerAdapter(){
        ArrayList<String> spinnerArray = new ArrayList<>();
        for(ITag tag : available)
            spinnerArray.add(tag.getName());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    private int testRequiredField(){
        int count = 0;

        if(!this.amountEditText.getText().toString().equals(""))
            count ++;

        if(!this.editTextDate.getText().toString().equals(""))
            count ++;

        if((this.debtor.isChecked()) ||(this.creditor.isChecked()))
            count ++;
        if(!this.destNameEdit.getText().toString().equals(""))
            count ++;

        return count;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        this.editTextDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void parseDate(){

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void addSpinnerPaymentType(){
        int pos = spinnerPaymentTypes.getSelectedItemPosition();

        switch(pos){
            case 0 :
                    invoice.addPayment(Payment.bankcard);
                break;
            case 1:
                    invoice.addPayment(Payment.bankcheck);
                break;
            case 2:
                    invoice.addPayment(Payment.cash);
                break;
            default:
                break;
        }

    }

    private void displaySelectedTag(){

        ViewGroup group = findViewById(R.id.tags_selected);
        group.removeAllViews();
        boolean istwice = false;
        for(ITag tag : selected) {

/*
            for(int i = 0; i < checklist.size(); i++){
                if(!checklist.get(i).getName().equals(tag)){
                    Log.v("test_tag_selected", tag.getName());
                    addToTagView(tag, getLayoutInflater(), group);
                    checklist.add(tag);

                }
            }

*/
            if(this.selectedList.size() > 0 ){
                for (ITag tag2 : this.selectedList) {
                    if (tag2.getName().equals(tag)) {
                        istwice = true;
                    }
                }
            }
            if(this.selectedList.size() == 0){
                addToTagView(tag, getLayoutInflater(), group);
                this.selectedList.add(tag);
                Log.v("test_tag_added_size_0", selectedList.get(selectedList.size()-1).getName());
            }
            else if (!istwice){
                addToTagView(tag, getLayoutInflater(), group);
                this.selectedList.add(tag);
                Log.v("test_tag_added_size_0", selectedList.get(selectedList.size()-1).getName());
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),"Tag already selected", Toast.LENGTH_SHORT);
                toast.show();
            }

        }

    }


    private void addToTagView(ITag tag, LayoutInflater layoutInflater, ViewGroup parentLayout ){
        View view = layoutInflater.inflate(R.layout.tag_layout, parentLayout, false);
        // In order to get the view we have to use the new view with text_layout in it
        TextView textView = view.findViewById(R.id.tag_layout_text);
        textView.setText(tag.getName());


        ImageButton button = view.findViewById(R.id.imageButton);
        final ITag _tag = tag;
/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.remove(_tag);
                ViewGroup group = findViewById(R.id.tags_view_small);
                group.removeAllViews();
                for(ITag tag : selected)
                    addToTagView(tag,getLayoutInflater(),group);
                available.add(_tag);
                setSpinnerAdapter();

            }
        });
*/

        if (tag.hasIcon()){
            ImageView img = view.findViewById(R.id.tagImageView);
            img.setImageDrawable(tag.getIcon());
            img.setColorFilter(tag.getColor());
        }

        parentLayout.addView(view);
    }


    private void addOptionalInfo(){
        addSpinnerPaymentType();
        if(!this.notesEditText.equals(""))
            invoice.addNotes(this.notesEditText.getText().toString());

        if(!this.personalNameEdit.equals(""))
            invoice.addInvoiceName(this.personalNameEdit.getText().toString());


    }




}