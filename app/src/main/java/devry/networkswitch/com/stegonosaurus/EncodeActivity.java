package devry.networkswitch.com.stegonosaurus;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import devry.networkswitch.com.stegonosaurus.imageutils.Model;
import dialogs.AddPromptDialog;
import dialogs.EncodeTextDialog;
import helper.AppConstant;

import static adapters.GridViewImageAdapter.decodeFile;


public class EncodeActivity extends ActionBarActivity {

    private Button buttonSubmit;
    private Button buttonChoose;

    private CheckBox checkText;
    private CheckBox checkImage;

    private ImageView imageThumbnail;

    private Bitmap image;
    private String imagePath;
    private int imageWidth;

    public File get_textfile() {
        return _textfile;
    }

    public void set_textfile(File _textfile) {
        this._textfile = _textfile;
    }

    private File _textfile;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        buttonSubmit =  (Button) findViewById(R.id.button_submit);
        buttonChoose =  (Button) findViewById(R.id.button_choose);

        checkText =  (CheckBox) findViewById(R.id.checkBox_text);
        checkImage =  (CheckBox) findViewById(R.id.checkBox_image);

        imageThumbnail = (ImageView) this.findViewById(R.id.imageView_thumbnail);

        this.buttonChoose.setEnabled(false);
        this.buttonSubmit.setEnabled(false);

        imagePath = getIntent().getStringExtra("imagePath");
        imageWidth = getIntent().getIntExtra("imageWidth" , 50);

        image = decodeFile( imagePath, imageWidth*2 , imageWidth*2
                );

        this.imageThumbnail.setImageBitmap(image);

        this.checkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkText.isChecked())
                {
                    checkImage.setChecked(false);
                    buttonChoose.setEnabled(true);
                }
                else
                {
                    checkImage.setChecked(false);
                    buttonChoose.setEnabled(false);
                }
            }
        });

        this.checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkImage.isChecked())
                {
                    checkText.setChecked(false);
                    buttonChoose.setEnabled(true);
                }
                else
                {
                    checkText.setChecked(false);
                    buttonChoose.setEnabled(false);
                }
            }
        });

        this.buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkImage.isChecked())
                {
                    encodeImage();

                }
                else
                {
                    encodeText();
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstant.textstuff == null)
                {
                    Toast.makeText(getTheParent(), "File read error occurred.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Model.modifyImage(imagePath, AppConstant.textstuff.getPath());
                   // Bitmap parseimg = decodeFile(AppConstant.currentlyModified, imageWidth, imageWidth);
                    //String path = (AppConstant.currentlyModified.toString());
                    //String encoded = encodeTobase64(parseimg);
//                    ParseFile file = new ParseFile("Modifiedimage" , path.getBytes());
//                    file.saveInBackground();
//                    ParseObject image = new ParseObject("Images");
//                    image.put("Image", file);
//                    image.put("UserId", ParseUser.getCurrentUser().get("ObjectId"));
//                    image.saveInBackground();
                }
            }
        });


    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



    public Activity getTheParent()
    {
        return this;
    }

    private void encodeText()
    {
        DialogFragment newFragment = new EncodeTextDialog();
        newFragment.show(this.getFragmentManager(), "Encode Stego");
        buttonSubmit.setEnabled(true);
    }

    private void encodeImage()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(String.valueOf(Environment.getExternalStorageDirectory()) + "/Download");
        intent.setDataAndType(uri, "image/png");
        startActivityForResult(Intent.createChooser(intent, "open folder"), 1);


        Toast.makeText(this, "Encoding image!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
      if( requestCode == 1) {
          if (resultCode == RESULT_OK) {
              // The user picked a contact.
              // The Intent's data Uri identifies which contact was selected.
              Uri selectedimage = data.getData();
              Toast.makeText(this,getRealPathFromURI(this, selectedimage ), Toast.LENGTH_SHORT).show();
              Model.modifyImage(imagePath, getRealPathFromURI(this, selectedimage ));

              // Do something with the contact here (bigger example below)
          }
      }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_cancel)
        {
            returnToMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnToMenu()
    {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
