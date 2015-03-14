package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import devry.networkswitch.com.stegonosaurus.DecodeActivity;
import devry.networkswitch.com.stegonosaurus.EncodeActivity;
import devry.networkswitch.com.stegonosaurus.GenerateActivity;
import devry.networkswitch.com.stegonosaurus.R;
import devry.networkswitch.com.stegonosaurus.imageutils.Model;
import helper.AppConstant;

/**
 * Created by Sebastian Florez on 3/14/2015.
 */
public class EncodeTextDialog extends DialogFragment {

    public static final String TAG = "BeBrave";
    private NotificationManager notificationManager;

    private String _imagePath;
    private int _imageWidth;

    private EditText messageText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        final AlertDialog dialog;
        final Button encodeButton, decodeButton, generateButton;

        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_encode_text, null))
                .setTitle("Create Stego")
                .setCancelable(true)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try { saveText(); }
                        catch  (Exception e)
                        {
                            Toast.makeText(getActivity(), "Error writing to data path.", Toast.LENGTH_SHORT).show();
                        }
                        // Using inline OnClickListener below to perform input validation
                    }
                });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        //_imagePath = getArguments().getString("imagePath");
        //_imageWidth = getArguments().getInt("imageWidth");
        messageText = (EditText) dialog.findViewById(R.id.editText_bigmessage);



        // Disable cancel button, until the security PIN is filled
//        cancelButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        cancelButton.setEnabled(false);

        return dialog;
    }

    private void saveText() throws Exception {
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/Download/" + Model.getRandomTXTName());
        AppConstant.textstuff = file;
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(messageText.getText().toString());
        writer.close();
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

}
