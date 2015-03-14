package devry.networkswitch.com.stegonosaurus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

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
    }

    private void encodeText()
    {
        Toast.makeText(this, "Encoding text!", Toast.LENGTH_SHORT).show();

    }

    private void encodeImage()
    {
        Toast.makeText(this, "Encoding image!", Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }
}
