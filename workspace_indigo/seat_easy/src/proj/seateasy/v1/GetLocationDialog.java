package proj.seateasy.v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;



public class GetLocationDialog extends DialogFragment {
	public Boolean choicetrue;
	private Context mContext;
	
	public GetLocationDialog(Context context){
		this.mContext = context;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        
        builder.setMessage(R.string.LocationToast);
               builder.setPositiveButton(R.string.LocationAllow, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       choicetrue = true;
                       SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                       SharedPreferences.Editor editor = settings.edit();
                       editor.putBoolean("pref_location", true);
                       editor.commit();
                       dialog.dismiss();
                   }
               });
               builder.setNegativeButton(R.string.LocationDisable, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       choicetrue = false;
                       //Toast.makeText(getActivity().getApplicationContext(), R.string.DisableReason, Toast.LENGTH_SHORT);
                     dialog.dismiss(); 
                     LocationWarning quick_warn = (LocationWarning) LocationWarning.newInstance();
                     quick_warn.show(getFragmentManager(),"LocationWarning");
                                         }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

