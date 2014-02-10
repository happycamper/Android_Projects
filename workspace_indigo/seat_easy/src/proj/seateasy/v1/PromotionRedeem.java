package proj.seateasy.v1;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



public class PromotionRedeem extends DialogFragment {
	public Context mContext;
	public Boolean choicetrue;
	final EasyCommunicator easy;
	final Messenger mMessenger;
	public String name;
	public ProgressDialog pDialog;
	private URL url;
	private Bitmap bmImg;
	public View v;
	private ImageButton promo;
	private AlertDialog.Builder builder;
	private TextView redeemText;
	private Boolean downloadComplete;
	private String filename = "seat_easy";
	private Restaurant mRestaurant;
	private User mUser;
	private Promotion promotion;
	private PromotionHandler ph;
	
	public PromotionRedeem(final Context context, Restaurant restaurant, User user){
		this.mContext = context;
		mMessenger = new Messenger(new IncomingHandler());
		easy = new EasyCommunicator(mContext,mMessenger);
		this.mRestaurant = restaurant;
		this.mUser = user;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	 downloadComplete = false;
    	 easy.doBindService();
    	  promotion = new Promotion(mRestaurant,mUser);
    	  ph = new PromotionHandler();
    	 LayoutInflater inflater = getActivity().getLayoutInflater();
    	 
    	  v = inflater.inflate(R.layout.promotion_redeem, null);
    	  promo = (ImageButton) v.findViewById(R.id.Promotion);
    	  redeemText = (TextView) v.findViewById(R.id.Promotion_Accept);
    	  
    	  promo.setOnClickListener(new View.OnClickListener() {
          	@Override
          	public void onClick(View v) {
          		//save picture...
          		if(downloadComplete){
          		Toast.makeText(mContext.getApplicationContext(), "Promotion Saved", Toast.LENGTH_LONG).show();
          		ph.savePromotion(promotion);
          		}else{
          			Toast.makeText(mContext.getApplicationContext(), "Image not yet downloaded...", Toast.LENGTH_LONG).show();	
          		}
          	}
          });
    	  
    	  new loadSingleView().execute();
    	  
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    
        // Use the Builder class for convenient dialog construction
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
               builder.setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   String toSend = ("Promotion for: Restaurant");
                	   //easy.sendStringToService(toSend);
                       choicetrue = true;
                       
                       dialog.dismiss();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
    class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Communication_Handler.MSG_SET_INT_VALUE:
            	//we may not want to connect back to this
                break;
            case Communication_Handler.MSG_SET_STRING_VALUE:
            	//we may not want to connect back to this
            default:
                super.handleMessage(msg);
            }
        }
    }
    
    public class loadSingleView extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Connect to Server");
            pDialog.setMessage("This process can take a few seconds to a few minutes, depending on your Internet Connection Speed.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            redeemText.setText("Loading...");
        }
        @Override
        protected String doInBackground(String... args) {
            // updating UI from Background Thread
            try {  

                url = new URL("https://www.qrt.co/gen.cmqr?t_id=3&url=http://"+mRestaurant.getName()+".com");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();   
                conn.setDoInput(true);   
                conn.connect();     
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is); 
                downloadComplete = true;
            }
            catch (IOException e)
            {       
                e.printStackTrace();  
            }

            return null;   
        }
            @Override       
    protected void onPostExecute(String args) {
        // dismiss the dialog after getting all products

            	pDialog.dismiss();
            promo.setImageBitmap(bmImg);
            promotion.setBitmap(bmImg);
            redeemText.setText("Click to Redeem!");        


    }
    }
    
    private void savePicture(Bitmap bitmap){
    	try{
    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);

    	//you can create a new file name "test.jpg" in sdcard folder.
    	File f = new File(Environment.getExternalStorageDirectory()+ File.separator + filename+ File.separator);
    	f.mkdirs();
    	Calendar c = Calendar.getInstance();
    	String name = c.DATE+"_"+c.HOUR_OF_DAY+"_"+c.MINUTE+"_"+c.SECOND+"_"+mRestaurant.getName()+".png";
    	 f = new File(Environment.getExternalStorageDirectory()+ File.separator + filename+ File.separator + name);
    	f.createNewFile();
    	//write the bytes in file
    	FileOutputStream fo = new FileOutputStream(f);
    	fo.write(bytes.toByteArray());

    	// remember close de FileOutput
    	fo.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
}

