package jensen.research.pharandroid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.os.AsyncTask;
import android.widget.TextView;

public class Ftpconnectdata extends AsyncTask<Void, Void, String>{
	private String strFileContents;
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		FTPClient client = new FTPClient();
        
        try {
        client.connect("165.91.247.153");
         
        //
        // When login success the login method returns true.
        //
        boolean login = client.login("jjensen", "fdsaasdf");
         
        if (login) {
        	
        	client.setFileTransferMode(FTP.ASCII_FILE_TYPE);
        	client.enterLocalPassiveMode();
        	try{
        	//Bitmap bmp = BitmapFactory.decodeFile("/sdcard/1337058757221.jpg");
        	//ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	//bmp.compress(CompressFormat.JPEG, 100, bos);
        	//byte[] bitmapdata = bos.toByteArray();
        	//ByteArrayInputStream is = new ByteArrayInputStream(bitmapdata);
        		BufferedInputStream buffIn=null;
        		String [] filenames = client.listNames();

        		//newtext.setText(filenames[3]);
        		buffIn=new BufferedInputStream(client.retrieveFileStream(filenames[3]));
        		byte[] contents = new byte[1024];
        		           int bytesRead=0;
        		            strFileContents=null; 
        		           while( (bytesRead = buffIn.read(contents)) != -1){ 
        		               strFileContents = new String(contents, 0, bytesRead);               
        		           }
        		           
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	System.out.println("Login success...");
        boolean logout = client.logout();
        if (logout) {
        System.out.println("Logout from FTP server...");
        return strFileContents;
        }
        } else {
        System.out.println("Login fail...");
        }
         
        } catch (SocketException e) {
        e.printStackTrace();
        } finally {
        try {
        client.disconnect();
        } catch (IOException e) {
        e.printStackTrace();
    }
		return null;
	}
}
	
	

}
