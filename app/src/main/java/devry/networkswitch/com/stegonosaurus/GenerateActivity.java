package devry.networkswitch.com.stegonosaurus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import static adapters.GridViewImageAdapter.decodeFile;


public class GenerateActivity extends ActionBarActivity {

    private Button generateButton;
    private ImageView imageThumbnail;

    private Bitmap image;
    private String imagePath;
    private int imageWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        generateButton = (Button) findViewById(R.id.button_generate);
        imageThumbnail = (ImageView) findViewById(R.id.imageView_thumbnail);

        imagePath = getIntent().getStringExtra("imagePath");
        imageWidth = getIntent().getIntExtra("imageWidth" , 50);

        image = decodeFile(imagePath, imageWidth, imageWidth
        );

        this.imageThumbnail.setImageBitmap(image);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generate, menu);
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
