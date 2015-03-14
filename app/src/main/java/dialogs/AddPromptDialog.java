package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import devry.networkswitch.com.stegonosaurus.DecodeActivity;
import devry.networkswitch.com.stegonosaurus.EncodeActivity;
import devry.networkswitch.com.stegonosaurus.GenerateActivity;
import devry.networkswitch.com.stegonosaurus.R;

/**
 * Created by Sebastian Florez on 3/14/2015.
 */
public class AddPromptDialog extends DialogFragment {

    public static final String TAG = "BeBrave";
    private NotificationManager notificationManager;

    private String _imagePath;
    private int _imageWidth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        final AlertDialog dialog;
        final Button encodeButton, decodeButton, generateButton;

        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_prompt, null))
                .setTitle("Create Stego")
                .setCancelable(true)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        // Using inline OnClickListener below to perform input validation
                    }
                });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


        encodeButton = (Button) dialog.findViewById(R.id.button_encode);
        decodeButton = (Button) dialog.findViewById(R.id.button_decode);
        generateButton = (Button) dialog.findViewById(R.id.button_generate);

        _imagePath = getArguments().getString("imagePath");
        _imageWidth = getArguments().getInt("imageWidth");

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEncode();
            }
        });

        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDecode();
            }
        });


        // Disable cancel button, until the security PIN is filled
//        cancelButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        cancelButton.setEnabled(false);





        return dialog;
    }

    private void openGenerate()
    {
        Intent intent = new Intent(this.getActivity(), GenerateActivity.class);
        Bundle args = new Bundle();
        args.putString("imagePath", _imagePath);
        args.putInt("imageWidth", _imageWidth);
        intent.putExtras(args);
        this.startActivity(intent);
    }

    private void openDecode()
    {
        Intent intent = new Intent(this.getActivity(), DecodeActivity.class);
        Bundle args = new Bundle();
        args.putString("imagePath", _imagePath);
        args.putInt("imageWidth", _imageWidth);
        intent.putExtras(args);
        this.startActivity(intent);
    }

    private void openEncode()
    {
        Intent intent = new Intent(this.getActivity(), EncodeActivity.class);
        Bundle args = new Bundle();
        args.putString("imagePath", _imagePath);
        args.putInt("imageWidth", _imageWidth);
        intent.putExtras(args);
        this.startActivity(intent);
    }
}