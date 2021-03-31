package com.ablancomziar.billsmanager;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MenuItem extends Fragment {
    private static final String ARG_PARAM1 = "drawable_id";
    private static final String ARG_PARAM2 = "text";

    private int drawableId;
    private String text;

    private OnMenuInteractionListener mListener;

    public MenuItem() {
        // Required empty public constructor
    }

    public static MenuItem newInstance(int drId, String txt) {
        MenuItem fragment = new MenuItem();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, drId);
        args.putString(ARG_PARAM2, txt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drawableId = getArguments().getInt(ARG_PARAM1);
            text = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_item, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(drawableId);

        TextView textView = view.findViewById(R.id.text);
        textView.setText(text);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentPressed(v);
            }
        });
        return view;
    }

    public void onFragmentPressed(View v) {
        if (mListener != null) {
            mListener.onFragmentInteraction(text);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuInteractionListener) {
            mListener = (OnMenuInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnMenuInteractionListener {
        void onFragmentInteraction(String text);
    }
}
