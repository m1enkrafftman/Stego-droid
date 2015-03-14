package devry.networkswitch.com.stegonosaurus;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Sebastian Florez on 3/13/2015.
 */
public class StegonosaurusApplication  extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "nDboBEDCxckDkgvND1nZJyWXLSNm4E8UhHZ3KcwY", "t3vDHxxS8HUh6XqEjebs7Zv5mlRgjSAbCHIMiNI2");

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


    }
}
