package com.ablancomziar.billsmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuItem.OnMenuInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuItem extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "drawable_id";
    private static final String ARG_PARAM2 = "text";

    // TODO: Rename and change types of parameters
    private int drawableId;
    private String text;

    private OnMenuInteractionListener mListener;

    public MenuItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuItem.
     */
    // TODO: Rename and change types and number of parameters
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

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMenuInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String text);
    }
}
