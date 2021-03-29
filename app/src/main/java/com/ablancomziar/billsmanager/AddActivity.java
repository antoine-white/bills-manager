package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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


        parseDate();

        spinner = findViewById(R.id.spinnerSelectTagForNewInvoice);

        selected = new ArrayList<>();


        final App a = (App) getApplication();


        available = a.getTags();

        setSpinnerAdapter();


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
        boolean iscompleted;
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
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

    private void addOptionalInfo(){
        addSpinnerPaymentType();
        if(!this.notesEditText.equals(""))
            invoice.addNotes(this.notesEditText.getText().toString());

        if(!this.personalNameEdit.equals(""))
            invoice.addInvoiceName(this.personalNameEdit.getText().toString());
    }




}