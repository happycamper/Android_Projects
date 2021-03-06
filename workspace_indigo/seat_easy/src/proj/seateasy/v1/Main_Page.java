package proj.seateasy.v1;

import java.util.List;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Main_Page extends ListActivity {
		
		private RestaurantDBManager manager;
		private EasyCommunicator easy;
		private Messenger mMessenger;
		private ActionBar main_bar;
	    private List<Restaurant> restaurantList;
	    private RestaurantListAdapter adapter;
	    public User MAIN_USER;
	    
	    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
	        Intent i = getIntent();
	        String user = i.getStringExtra("name");
	        
	        UsersDBManager usersmanager = new UsersDBManager(this);
 		   
	          usersmanager.open();
	          MAIN_USER = usersmanager.getUser(user);
	          usersmanager.close();
	        
	        mMessenger = new Messenger(new IncomingHandler());
			easy = new EasyCommunicator(getApplicationContext(),mMessenger);
			
	        
	            manager = new RestaurantDBManager(this);
	        	manager.open();
	        	/*manager.deleteRestaurant("Chilis");
	        	manager.deleteRestaurant("Whataburger");
	        	manager.deleteRestaurant("Taco");
	        	manager.deleteRestaurant("Chicken");
	        	manager.deleteRestaurant("Cheddars");
	        	manager.deleteRestaurant("Fridays");*/
	        	manager.clearRestaurantTable();
	        	manager.addRestaurant(new Restaurant("Chilis"));
	        	manager.addRestaurant(new Restaurant("Whataburger"));
	        	manager.addRestaurant(new Restaurant("Fridays"));
	        	manager.addRestaurant(new Restaurant("Taco"));
	        	manager.addRestaurant(new Restaurant("Chicken"));
	        	manager.addRestaurant(new Restaurant("Cheddars"));
	       // Toast.makeText(getApplicationContext(), "Made It", Toast.LENGTH_LONG).show();
	        //for now, fill in data for restaurant list, this will come from database/server
	        
	        //for now, just show that we are going to snag the user's location
	        	
	        
	 }
	 
	 
	 @Override
	 protected void onStart(){
		 	super.onStart();
		 	easy.doBindService();
		//We can probably write some code that can use split action bar and a regular action bar
	        main_bar = getActionBar();
	        main_bar.setDisplayUseLogoEnabled(true);
	        main_bar.show();
	        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean locationOK = settings.getBoolean(Settings.PREF_LOCATION, false);
            if(!locationOK){
            	GetLocationDialog locationDialog = new GetLocationDialog(this.getApplicationContext());
            	locationDialog.show(getFragmentManager(), "location1");
            }
	        //String toSend = ("192.168.1.129/seat_easy/index.php?ALL_RESTAURANTS=true");
	       // easy.restaurantDBRequest(toSend);
	        updateRestaurants();
	        
	       
		 
	 }
	 
	 public void updateRestaurants(){
		 manager.open();
		 restaurantList = manager.getAllRestaurants();
	        adapter = new RestaurantListAdapter(this, restaurantList,getFragmentManager(),MAIN_USER);
	        setListAdapter(adapter);
	        manager.close();
		 
	 }
	 
	 @Override
	 protected void onStop(){
		 super.onStop();
		 easy.doUnbindService();
	 }
	 
	 @Override
	 protected void onDestroy(){
		 super.onDestroy();
		 easy.easyStopService();
	 }
	 
	 
	 //Override for the ActionBar
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	     MenuInflater inflater = getMenuInflater();
	     inflater.inflate(R.layout.main_menu, menu);
	     return true;
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     	case R.id.mainSearch:
             updateRestaurants();
             return true;
             
	         case R.id.menu_Home:
	             updateRestaurants();
	             return true;
	             
	         case R.id.menu_Settings:
	        	 Intent settings_intent = new Intent(this, Share.class);
	             startActivity(settings_intent);
	             return true;
	             
	         case R.id.menu_Share:
	        	 Intent share_intent = new Intent(this, Settings.class);
	             startActivity(share_intent);
	             return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }


	 /*public List<Restaurant> fillData(){
		 List<Restaurant> returnlist = new ArrayList<Restaurant>();
		 Restaurant A = new Restaurant("Chilis");
		 Restaurant B = new Restaurant("Zaxbys");
		 Restaurant C = new Restaurant("Chicken");
		 Restaurant D = new Restaurant("Whataburger");
		 
		 returnlist.add(A);
		 returnlist.add(B);
		 returnlist.add(C);
		 returnlist.add(D);
		 return returnlist;
	 }*/
	 
	 
	 
	 /*@Override
	 public void onStop(){
		 super.onStop();
		 
	 }
	 
	 @Override
	 public void onDestroy(){
		 easy.doUnbindService();
		 easy.easyStopService();
		 super.onDestroy();
	 }*/
	 
	 class IncomingHandler extends Handler { // Handler of incoming messages from clients.
		 String local;
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case Communication_Handler.MSG_SET_INT_VALUE:
	            	//we may not want to connect back to this
	                break;
	            case Communication_Handler.MSG_DECODE_RESTAURANT:
	            	//manager.open();
	            	//manager.addRestaurant(new Restaurant(msg.getData().getString("fromServer")));
	            	//manager.close();
	            	updateRestaurants();
	            	
	            default:
	                super.handleMessage(msg);
	            }
	        }
	    }
	 

}
