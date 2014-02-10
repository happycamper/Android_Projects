package heffay.research.ftpupload;

import android.app.Activity;
import android.os.Bundle;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;

public class ftpupload extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Ftpconnect connector = new Ftpconnect();
        connector.execute();
        
        }
}