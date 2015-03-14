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
        Parse.initialize(this, "d7jUJNqR3GutraiiZBBzuwji40eBZXfem5Ot01n6", "9HEqeJEPhU8yLaSL6xTp7rYEvQgDutxxzJAxEhGt");

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


    }
}
