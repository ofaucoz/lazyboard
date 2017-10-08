package com.ofaucoz.lazyboard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogCommandFragment extends DialogFragment {
    private EditText mInEditText;

    public DialogCommandFragment() {

    }

    public static DialogCommandFragment newInstance(int title, String command) {
        Log.d("dialog_constructor", "current command = " + command);
        DialogCommandFragment dialogCommandFragment = new DialogCommandFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("command", command);
        dialogCommandFragment.setArguments(args);
        return dialogCommandFragment;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        // Retrieve modules from the layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Context context = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_dialog, null, false);
        mInEditText = (EditText) view.findViewById(R.id.dialog_argument);

        // Create the dialog


        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(R.string.confirm_dialog,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainActivity) getActivity()).doPositiveClick(getArguments().getString("command"), mInEditText.getText().toString());
                    }
                }
        );
        builder.setNegativeButton(R.string.deny_dialog,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainActivity) getActivity()).doNegativeClick();
                    }
                }
        );


        return builder.create();
    }

}
