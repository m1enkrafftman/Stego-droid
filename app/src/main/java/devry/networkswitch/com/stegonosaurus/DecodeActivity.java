package devry.networkswitch.com.stegonosaurus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import devry.networkswitch.com.stegonosaurus.imageutils.Model;

import static adapters.GridViewImageAdapter.decodeFile;


public class DecodeActivity extends ActionBarActivity {

    private Bitmap image;
    private String imagePath;
    private int imageWidth;

    private Button decodeButton;

    private ImageView decodeThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        decodeThumbnail = (ImageView) findViewById(R.id.imageView_thumbnaildecode);
        decodeButton = (Button) findViewById(R.id.button_decode);

        imagePath = getIntent().getStringExtra("imagePath");
        imageWidth = getIntent().getIntExtra("imageWidth" , 50);

        image = decodeFile( imagePath, imageWidth*2 , imageWidth*2
        );

        decodeThumbnail.setImageBitmap(image);

        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = new Date();
                String outS = "/Download/stegoout"+now.toString();
                Model.pullDataFromImage(imagePath, String.valueOf(Environment.getExternalStorageDirectory()) + outS );
                Toast.makeText(getShit(), "File saved to: " + outS, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Activity getShit()
    {
        return this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decode, menu);

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
