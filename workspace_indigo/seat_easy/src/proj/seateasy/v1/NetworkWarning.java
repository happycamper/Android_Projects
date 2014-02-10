package proj.seateasy.v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;



public class NetworkWarning extends DialogFragment {
	public Boolean choicetrue;
	public Context mContext;
	public Class<?> mClass;
	
	public NetworkWarning(Context context, Class<?> downClass){
		this.mContext = context;
		this.mClass = downClass;
		
	}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.NetworkWarning);
               builder.setPositiveButton(R.string.Retry, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Intent start_main = new Intent(mContext, mClass);
              	      //Just go to main activity for now after going through email...
              	      startActivity(start_main);
                       choicetrue = true;
                       dialog.dismiss();
                   }
               });
               builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                       //Toast.makeText(getActivity().getApplicationContext(), R.string.DisableReason, Toast.LENGTH_SHORT);
                     dialog.dismiss(); 
                                         }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

