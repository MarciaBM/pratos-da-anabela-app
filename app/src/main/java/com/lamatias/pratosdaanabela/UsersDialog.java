package com.lamatias.pratosdaanabela;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class UsersDialog extends DialogFragment implements Serializable {

    private DialogClickListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            callback = (DialogClickListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException (context.toString ( )
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ( ));
        // Get the layout inflater
        final LayoutInflater inflater = getActivity ( ).getLayoutInflater ( );
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate (R.layout.info_users, null);

        callback.getUsers (view);
        builder.setView (view);
        builder.setTitle ("Comido por:");
        builder.setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create ( );
    }

    public interface DialogClickListener{
        void getUsers(View view);
    }
}
