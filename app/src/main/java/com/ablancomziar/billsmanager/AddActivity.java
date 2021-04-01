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

    public static final String INVOICE_INTENT = "Invoice";

    private Spinner spinner;

    private List<ITag> selected;
    private List<ITag> available;


    private EditText amountEditText;
    private EditText editTextDate;
    private RadioButton debtor;
    private RadioButton creditor;
    private EditText destNameEdit;

    private Spinner spinnerPaymentTypes;

    private EditText personalNameEdit;
    private EditText notesEditText;


    private Calendar myCalendar = Calendar.getInstance();


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
        Button addButton = findViewById(R.id.AddInvoice);
        this.personalNameEdit = findViewById(R.id.PersonalNameEdit);
        this.notesEditText = findViewById(R.id.NotesEditText);
        ImageButton addTag = findViewById(R.id.AddButtonSelectTag);


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


        parseDate();

        spinner = findViewById(R.id.spinnerSelectTagForNewInvoice);

        selected = new ArrayList<>();

        final ITagHandler a = ((App) getApplication()).getTagHandler();

        available = a.getTags();

        setSpinnerAdapter();

        // Ce bouton sert à ajouter un tag à la facture
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


        //celui sert à visualiser les champs pour remplir l'adresse ou non
        displayAddressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setVisibilityAddress(v);
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            Bundle b = intent.getExtras();
            if (b != null) {
                Invoice i = (Invoice)b.get(INVOICE_INTENT);
                if (i!= null){
                    Log.v(App.getAppTag(), "Invoice to edit : " + i);
                    amountEditText.setText(i.getAmount()+"");
                    myCalendar = StatsActivity.toCalendar(i.getInvoiceDate());
                    updateLabel();
                    creditor.setChecked(i.isCredit());
                    debtor.setChecked(!i.isCredit());
                    destNameEdit.setText(i.getName());
                    if (i.getTags() != null){
                        for (int id : i.getTags()){
                            ITag t = a.getTagById(id);
                            selected.add(t);
                            available.remove(t);
                        }
                    }
                    if(i.getAddress() != null){
                        numAddress.setText(i.getAddress().getNum() + "");
                        streetAddress.setText(i.getAddress().getStreet());
                        cityAddress.setText(i.getAddress().getCity());
                        stateAddress.setText(i.getAddress().getState());
                    }
                    if(i.getNotes() != null)
                        notesEditText.setText(i.getNotes());
                    if(i.getPersonalName()!= null)
                        notesEditText.setText(i.getPersonalName());
                    if(i.getPayment()!= null)
                        spinnerPaymentTypes.setSelection(i.getPayment() == Payment.bankcard? 0 : i.getPayment() == Payment.bankcheck? 1 : 2);
                }
            }
        }

        final IInvoiceHandler invoiceHandler = ((App) getApplication()).getInvoiceHandler();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int requiredField = testRequiredField();
                // si les 4 champs obligatoires ne sont pas remplis
                if(requiredField != 4 ){
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.toastfieldrequired), Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    boolean iscredit = ! debtor.isChecked();


                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                    try {
                        Date date = sdf.parse(editTextDate.getText().toString());
                        Invoice invoice = new Invoice(Float.parseFloat(amountEditText.getText().toString()),date ,iscredit,destNameEdit.getText().toString());

                        // cette fonction ajoute les champs optionnels à la fonction
                        addOptionalInfo(invoice);

                        if (selected.size() > 0){
                            // ajoute les tags à la facture
                            int cpt = 0;
                            int[] tagIds = new int[selected.size()];
                            for(ITag tag : selected){
                                tagIds[cpt] = tag.getId();
                                Log.v(App.getAppTag(), selected.get(cpt).getName());
                                cpt ++;
                            }
                            invoice.addTagsId(tagIds);
                        } else {
                            invoice.addTagsId(new int[]{DefaultTag.ID_NO_LABEL});
                        }


                        invoiceHandler.AddInvoice(invoice);

                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.toastinvoicecompleted), Toast.LENGTH_SHORT);
                        toast.show();


                        //renvoie vers le menu
                        Intent activity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(activity);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    // actualise les tags dans le spinner
    private void setSpinnerAdapter(){
        ArrayList<String> spinnerArray = new ArrayList<>();
        for(ITag tag : available)
            spinnerArray.add(tag.getName());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    // vérifie si les 4 champs obligatoires sont remplis
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

    // crée le calendrier
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


    // affiche les tags selectionnés
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


    // vérifie et ajoute à la facture si des champs optionnels sont remplis
    private void addOptionalInfo(Invoice invoice){
        addSpinnerPaymentType(invoice);
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


    // ajoute le moyen de paiement

    private void addSpinnerPaymentType(Invoice invoice){
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

    // affichage du bouton adresse ou non

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