package jensen.research.pharandroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.os.AsyncTask;

public class Ftpconnect extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		FTPClient client = new FTPClient();
        
        try {
        client.connect("165.91.247.153");
         
        //
        // When login success the login method returns true.
        //
        boolean login = client.login("jjensen", "fdsaasdf");
         
        if (login) {
        	client.setFileType(FTP.BINARY_FILE_TYPE);
        	client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        	client.enterLocalPassiveMode();
        	try{
        	//Bitmap bmp = BitmapFactory.decodeFile("/sdcard/1337058757221.jpg");
        	//ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	//bmp.compress(CompressFormat.JPEG, 100, bos);
        	//byte[] bitmapdata = bos.toByteArray();
        	//ByteArrayInputStream is = new ByteArrayInputStream(bitmapdata);
        	File f = new File("/sdcard/2.jpg");
        	
        	
        	FileInputStream fstream = new FileInputStream(f);
        	
        	client.storeFile("other.jpg", fstream);
        	fstream.close();
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	System.out.println("Login success...");
        boolean logout = client.logout();
        if (logout) {
        System.out.println("Logout from FTP server...");
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
