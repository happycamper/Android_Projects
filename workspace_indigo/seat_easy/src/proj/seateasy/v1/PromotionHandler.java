package proj.seateasy.v1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


public class PromotionHandler {
	
	private String FILE_NAME;
	private String DIRECTORY_NAME = "seat_easy";
	private String FILE_TYPE = ".png";
	private Bitmap bitmap;
	private ArrayList<String> promotionNameList;
	private Restaurant mRestaurant;
	private User mUser;
	
	public PromotionHandler(){}
	
	public PromotionHandler(Restaurant rest, User user){
		this.mUser = user;
		this.mRestaurant = rest;
	}

	public Promotion savePromotion(Promotion promo){
    	if(!getDIR().exists()){
    		createDIR();
    	}
    	promo.setFilePath(makeFile(promo));
    	promo.setDate(getDate());
    	if(saveFile(promo)){
    		promo.setSaved();
    	}
    	return promo;
    }
	
	public String getDate(){
		Calendar c =  Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(c.get(c.MONTH)));
		sb.append("_");
		sb.append(String.valueOf(c.get(c.DAY_OF_MONTH)));
		sb.append("_");
		sb.append(String.valueOf(c.get(c.YEAR)));
		sb.append("_");
		/*sb.append(String.valueOf(c.get(c.HOUR_OF_DAY)));
		sb.append("_");
		sb.append(String.valueOf(c.get(c.MINUTE)));
		sb.append("_");
		sb.append(String.valueOf(c.get(c.SECOND)));*/
		
		return sb.toString();
	}
	
	private File makeFile(Promotion promo){
		File filename = new File(getDIR().toString()+File.separator+getDate()+"_"+promo.getRestaurantName()+FILE_TYPE);
		return filename;
	}
	
	private void createDIR(){
		File f = getDIR();
    	f.mkdirs();
	}
	
	public File getDIR(){
		File f = new File(Environment.getExternalStorageDirectory()+ File.separator + DIRECTORY_NAME+ File.separator);
		return f;
	}
	
	private Boolean saveFile(Promotion promo){
		try{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    	promo.getBitmap().compress(Bitmap.CompressFormat.PNG, 40, bytes);
			File f = new File(promo.getFilePath());
			f.createNewFile();
			//write the bytes in file
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
			return true;
			}catch(IOException e){
				e.printStackTrace();
			}
		return false;
		}
	
	private File[] getAllPromotionNames(){
		File dir = getDIR();
		File[] files = dir.listFiles();
		return files;
	}
	
	public Boolean promotionExists(){
		Promotion promo = new Promotion(mRestaurant,mUser);
		File f = makeFile(promo);
		return f.exists();
	}
	
	public void removePromotion(){
		Promotion promo = new Promotion(mRestaurant,mUser);
		File f = makeFile(promo);
		f.delete();
	}
	
	public Promotion getPromotionWhenExists(){
		Promotion promo = new Promotion(mRestaurant,mUser);
		promo.setSaved();
		promo.setDate(getDate());
		File f = makeFile(promo);
		if(f.exists()){
			promo.setFilePath(f);
		
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(promo.getFilePath(), options);
			promo.setBitmap(bitmap);
			return promo;
		}else{
			return null;
		}
		
	}
	
	
	
	
}


