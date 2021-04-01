package com.ablancomziar.billsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TagActivity extends AppCompatActivity {

    private static final int MIN_SIZE_NAME = 3;

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
            //Log.println(Log.DEBUG,App.getAppTag(),tag.getName());
        }
    }

    private void addButton(String name){
        if (name.length() < MIN_SIZE_NAME){
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

    private void addToList(final ITag tag, LayoutInflater layoutInflater, ViewGroup parentLayout ){
        final View view = layoutInflater.inflate(R.layout.tag_layout, parentLayout, false);

        TextView textView = view.findViewById(R.id.tag_layout_text);
        textView.setText(tag.getName());

        TextView textViewId = view.findViewById(R.id.tag_layout_id);
        textViewId.setText(getString(R.string.tag_id,tag.getId()));

        final ITagHandler tagHandler = ((App) getApplication()).getTagHandler();

        ImageButton delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag instanceof DefaultTag)
                    Toast.makeText(v.getContext(), getString(R.string.cannot_delete_default_tag),
                            Toast.LENGTH_LONG).show();
                else if (tagHandler.deleteTag(tag.getId())){
                    Toast.makeText(v.getContext(), getString(R.string.delete_succ_tag),
                            Toast.LENGTH_LONG).show();
                    ((ViewGroup)findViewById(R.id.tag_list)).removeView(view);
                }
                else Toast.makeText(v.getContext(), getString(R.string.could_not_delete_tag),
                        Toast.LENGTH_LONG).show();
            }
        });

        if (tag.hasIcon()){
            ImageView img = view.findViewById(R.id.tagImageView);
            img.setImageDrawable(tag.getIcon());
            img.setColorFilter(tag.getColor());
        }

        parentLayout.addView(view);
    }
}
