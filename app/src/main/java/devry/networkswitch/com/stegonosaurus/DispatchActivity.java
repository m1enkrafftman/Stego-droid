package devry.networkswitch.com.stegonosaurus;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by Sebastian Florez on 3/13/2015.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return MainActivity.class;
    }
}