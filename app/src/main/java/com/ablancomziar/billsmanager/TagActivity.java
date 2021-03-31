package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TagActivity extends AppCompatActivity {

    private EditText editText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);


        
        editText = findViewById(R.id.add_edit_text);
        addButton = findViewById(R.id.add_tag_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(editText.getText().toString());
            }
        });

        final ITagHandler a = ((App) getApplication()).getTagHandler();
        List<ITag> tags = a.getTags();
        for (ITag tag : tags){
            addToList(tag,getLayoutInflater(),(ViewGroup)findViewById(R.id.tag_list));
            Log.println(Log.DEBUG,App.getAppTag(),tag.getName());
        }
    }

    private void addButton(String name){
        if (name.length() < 3){
            Toast.makeText(this, getString(R.string.too_short,name),
                    Toast.LENGTH_LONG).show();
            return;
        }
        final ITagHandler a = ((App) getApplication()).getTagHandler();
        ITag t = a.addTag(name);
        if(t == null){
            Toast.makeText(this, getString(R.string.name_already,name),
                    Toast.LENGTH_LONG).show();
            return;
        }
        editText.setText("");
        addToList(t,getLayoutInflater(),(ViewGroup)findViewById(R.id.tag_list));

    }

    private void addToList(ITag tag, LayoutInflater layoutInflater, ViewGroup parentLayout ){
        View view = layoutInflater.inflate(R.layout.tag_layout, parentLayout, false);

        // In order to get the view we have to use the new view with text_layout in it
        TextView textView = view.findViewById(R.id.tag_layout_text);
        textView.setText(tag.getName());

        TextView textViewId = view.findViewById(R.id.tag_layout_id);
        textViewId.setText(getString(R.string.tag_id,tag.getId()));

        if (tag.hasIcon()){
            ImageView img = view.findViewById(R.id.tagImageView);
            img.setImageDrawable(tag.getIcon());
            img.setColorFilter(tag.getColor());
        }
        parentLayout.addView(view);
    }
}
