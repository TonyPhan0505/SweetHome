package com.example.sweethome;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class DeleteItemFragment extends DialogFragment{
    private OnFragmentInteractionListener listener;
    private ArrayList<Item> selectedItemsList; // to hold the selected items

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener= (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        public void onDeletePressed(ArrayList<Item> selectedItems);
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dele_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Button cancelButton = view.findViewById(R.id.cancel_delete);
        Button deleteButton = view.findViewById(R.id.delete);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // Handle delete button click
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeletePressed(selectedItemsList);
                }
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();

    }
    public void setSelectedItemsList(ArrayList<Item> selectedItems) {
        this.selectedItemsList = selectedItems;
    }
}
