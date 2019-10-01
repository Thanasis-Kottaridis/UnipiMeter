package com.unipi.kottarido.unipimeter.unipimeter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogClass extends AppCompatDialogFragment {
    private EditText editText;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null );

        builder.setView(view)
                .setTitle("Set Speed Limit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Apply ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String UsersAnswer = editText.getText().toString();
                        listener.applyText(new String[]{UsersAnswer});
                    }
                });

        editText = view.findViewById(R.id.editText);

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            //throw new ClassCastException(context.toString() + "Must implement ExampleDialogListener");
        }
    }

    //ftiaxnw ena functional interface
    public interface DialogListener{
        void applyText(String [] Answers);
    }
}
