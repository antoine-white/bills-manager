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


    private Button displayAddressButton;
    private TextView labelNumAddress;
    private EditText numAddress;
    private TextView labelStreetAddress;
    private EditText streetAddress;
    private TextView labelCityAddress;
    private EditText cityAddress;
    private TextView labelStateAddress;
    private EditText stateAddress;
    private boolean isVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle(getString(R.string.addtitle));

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


        this.displayAddressButton = findViewById(R.id.DisplayAddressButton);
        this.numAddress = findViewById(R.id.EditNumAddress);
        this.labelNumAddress = findViewById(R.id.LabelNumAddress);
        this.streetAddress = findViewById(R.id.EditStreetAddress);
        this.labelStreetAddress = findViewById(R.id.LabelStreetAddress);
        this.cityAddress = findViewById(R.id.EditCityAddress);
        this.labelCityAddress = findViewById(R.id.LabelCityAddress);
        this.stateAddress = findViewById(R.id.EditStateAddress);
        this.labelStateAddress = findViewById(R.id.LabelStateAddress);
        this.numAddress.setVisibility(View.GONE);
        this.labelNumAddress.setVisibility(View.GONE);
        this.streetAddress.setVisibility(View.GONE);
        this.labelStreetAddress.setVisibility(View.GONE);
        this.cityAddress.setVisibility(View.GONE);
        this.labelCityAddress.setVisibility(View.GONE);
        this.stateAddress.setVisibility(View.GONE);
        this.labelStateAddress.setVisibility(View.GONE);
        this.isVisible = false;


        this.selectedList = new ArrayList<>();

        parseDate();

        spinner = findViewById(R.id.spinnerSelectTagForNewInvoice);

        selected = new ArrayList<>();

        final ITagHandler a = ((App) getApplication()).getTagHandler();

        available = a.getTags();

        setSpinnerAdapter();

        addTag.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
              String str = spinner.getSelectedItem().toString();
              for (ITag tag : available)
                  if(tag.getName().equals(str)){
                      selected.add(tag);
                      available.remove(tag);
                      setSpinnerAdapter();
                      displaySelectedTag();
                      return;
                  }
              }
        });


        displayAddressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setVisibilityAddress(v);
            }
        });


        final IInvoiceHandler invoiceHandler = ((App) getApplication()).getInvoiceHandler();

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

                    invoiceHandler.AddInvoice(invoice);

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


    private void displaySelectedTag(){
        ViewGroup group = findViewById(R.id.tags_selected);
        group.removeAllViews();
        for(ITag tag : selected)
            addToTagView(tag, getLayoutInflater(), group);
    }


    private void addToTagView(ITag tag, LayoutInflater layoutInflater, ViewGroup parentLayout ){
        View view = layoutInflater.inflate(R.layout.small_tag_layout, parentLayout, false);
        // In order to get the view we have to use the new view with text_layout in it
        TextView textView = view.findViewById(R.id.tag_layout_text);
        textView.setText(tag.getName());

        ImageButton button = view.findViewById(R.id.imageButton);
        final ITag _tag = tag;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.remove(_tag);
                displaySelectedTag();
                available.add(_tag);
                setSpinnerAdapter();
            }
        });

        if (tag.hasIcon()){
            ImageView img = view.findViewById(R.id.tagImageView);
            img.setImageDrawable(tag.getIcon());
            img.setColorFilter(tag.getColor());
        }

        parentLayout.addView(view);
    }


    private void addOptionalInfo(){
        addSpinnerPaymentType();
        if(!this.notesEditText.getText().toString().equals(""))
            invoice.addNotes(this.notesEditText.getText().toString());

        if(!this.personalNameEdit.getText().toString().equals(""))
            invoice.addInvoiceName(this.personalNameEdit.getText().toString());

        if((!this.numAddress.getText().toString().equals(""))&&
                (!this.streetAddress.getText().toString().equals(""))&&
                (!this.cityAddress.getText().toString().equals(""))&&
                (!this.stateAddress.getText().toString().equals("")))
        {
            int num = Integer.parseInt(this.numAddress.getText().toString());
            String street = this.streetAddress.getText().toString();
            String city = this.cityAddress.getText().toString();
            String state = this.stateAddress.getText().toString();
            Address a = new Address(num, street, city, state);
            invoice.addAddress(a);
        }
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


    private void setVisibilityAddress(View v){
        if(this.isVisible){
            this.numAddress.setVisibility(View.GONE);
            this.labelNumAddress.setVisibility(View.GONE);
            this.streetAddress.setVisibility(View.GONE);
            this.labelStreetAddress.setVisibility(View.GONE);
            this.cityAddress.setVisibility(View.GONE);
            this.labelCityAddress.setVisibility(View.GONE);
            this.stateAddress.setVisibility(View.GONE);
            this.labelStateAddress.setVisibility(View.GONE);
            this.displayAddressButton.setText(R.string.address_button);
            this.isVisible = false;
        }
        else {
            this.numAddress.setVisibility(View.VISIBLE);
            this.labelNumAddress.setVisibility(View.VISIBLE);
            this.streetAddress.setVisibility(View.VISIBLE);
            this.labelStreetAddress.setVisibility(View.VISIBLE);
            this.cityAddress.setVisibility(View.VISIBLE);
            this.labelCityAddress.setVisibility(View.VISIBLE);
            this.stateAddress.setVisibility(View.VISIBLE);
            this.labelStateAddress.setVisibility(View.VISIBLE);

            this.displayAddressButton.setText(R.string.address_button_hide);
            this.isVisible = true;
        }
    }

    

}