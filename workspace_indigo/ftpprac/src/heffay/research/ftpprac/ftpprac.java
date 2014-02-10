package heffay.research.ftpprac;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ftpprac extends Activity {
    /** Called when the activity is first created. */
	
	public FTPClient mFTPClient;
	private static final String TAG = null;
	public String [] filenames;
	public TextView displaytext;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ftpConnect("192.168.1.131","jjensen","fdsaasdf",21);
    }
    
    public boolean ftpConnect(String host, String username,
            String password, int port)
{
try {
	TextView newtext = (TextView) findViewById(R.id.display);
	newtext.setText("Made it");
	
mFTPClient = new FTPClient();

// connecting to the host
mFTPClient.connect(host);

// now check the reply code, if positive mean connection success
if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
// login using username & password
	
boolean status = mFTPClient.login(username, password);

/* Set File Transfer Mode
*
* To avoid corruption issue you must specified a correct
* transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
* EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
* for transferring text, image, and compressed files.
*/
mFTPClient.setFileType(FTP.ASCII_FILE_TYPE);
BufferedInputStream buffIn=null;
filenames = mFTPClient.listNames();

//newtext.setText(filenames[3]);
buffIn=new BufferedInputStream(mFTPClient.retrieveFileStream(filenames[3]));
byte[] contents = new byte[1024];
           int bytesRead=0;
           String strFileContents=null; 
           while( (bytesRead = buffIn.read(contents)) != -1){ 
               strFileContents = new String(contents, 0, bytesRead);               
           }
          //newtext.setText(strFileContents);
//mFTPClient.remoteStore("/sdcard/1337058757221.jpg");
//newtext.setText(buffIn.toString());
mFTPClient.enterLocalPassiveMode();
//mFTPClient.storeFile("test.txt", buffIn);

//buffIn.close();
mFTPClient.logout();
mFTPClient.disconnect();
mFTPClient.enterLocalPassiveMode();

return status;
}
} catch(Exception e) {
Log.d(TAG, "Error: could not connect to host " + host );
}

return false;
}
    
}