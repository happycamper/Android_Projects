package proj.seateasy.v1;

import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {
  private final Context context;
  private final List<Restaurant> list;
  private final FragmentManager manager;
  private int positionAdapter;
  private User mUser;

  public RestaurantListAdapter(Context context, List<Restaurant> Rlist,FragmentManager fmanager,User user) {
    super(context, R.layout.restaurant_list, Rlist);
    this.context = context;
    this.list = Rlist;
    this.manager = fmanager;
    this.mUser = user;
  }

  public class viewholder{
	  public TextView name;
	  public ImageButton promo;
	  public Button callAhead;
	  public Restaurant restaurant;
	  public Promotion promotion;
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.restaurant_list, parent, false);
    
    final viewholder holder = new viewholder();
    holder.name = (TextView) rowView.findViewById(R.id.RestaurantName);
    holder.callAhead = (Button) rowView.findViewById(R.id.CallAhead);
    holder.promo = (ImageButton) rowView.findViewById(R.id.Promotion);
    holder.name.setText(list.get(position).getName());
    positionAdapter = position;
    holder.restaurant = list.get(position);
    
    PromotionHandler ph = new PromotionHandler(holder.restaurant,mUser);
    	
    
   
    
    holder.callAhead.setOnClickListener(new View.OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		CallAheadForm2 newForm = new CallAheadForm2(context,holder.restaurant,mUser);
            newForm.show();
    	}
    });
    
    
    	holder.promo.setOnClickListener(new View.OnClickListener() {
    
    	@Override
    	public void onClick(View v) {
    		PromotionRedeem newForm = new PromotionRedeem(context, holder.restaurant, mUser);
            newForm.show(manager,"Promo");
    		
    	}
    });
    
    

    return rowView;
 
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
  
} 