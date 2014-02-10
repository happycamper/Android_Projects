package proj.seateasy.v1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class User_Authenticator {
	private Context mContext;
	private Account[] accounts;
	private AccountManager am;
	
	public User_Authenticator(Context context){
		this.mContext = context;
		am = AccountManager.get(mContext); // "this" references the current Context
		accounts = am.getAccountsByType("com.google");
	}
	
	public Account[] getAccounts(){
		Account[] return_accounts = accounts;
		return return_accounts;
	}
	
	
	

}
