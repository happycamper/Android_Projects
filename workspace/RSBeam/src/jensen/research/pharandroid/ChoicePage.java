package jensen.research.pharandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoicePage extends Activity {
	
	private Button controller, tester;
	private Activity activity;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_page);
        
        activity = this;
        
        controller = (Button) findViewById(R.id.controller);
        tester = (Button) findViewById(R.id.tester);
        
        controller.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(activity, pharandroid.class);
				startActivity(i);
			}
        	
        });
        
        tester.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(activity,TesterPage.class);
				startActivity(i);
			}
        	
        });
        
	}

}
