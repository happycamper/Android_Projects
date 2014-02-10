package proj.seateasy.v1;

import android.app.Activity;
import android.os.Bundle;

public class Share extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
		
		
	}

}
