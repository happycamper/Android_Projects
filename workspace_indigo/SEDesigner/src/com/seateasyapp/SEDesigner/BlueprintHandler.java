package com.seateasyapp.SEDesigner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class BlueprintHandler {

	private String FILE_NAME;
	private String DIRECTORY_NAME = "seat_easy";
	private String FILE_TYPE = ".xml";
	private Bitmap bitmap;
	private ArrayList<String> promotionNameList;
	private Context mContext;
	public static String TABLE_TYPE = "Type";
	public static String TABLE_WIDTH = "Width";
	public static String TABLE_HEIGHT = "Height";
	public static String TABLE_SEATSX = "SeatsX";
	public static String TABLE_NUMBER = "TableNumber";
	public static String TABLE_ANCHORX = "AnchorX";
	public static String TABLE_ANCHORY = "AnchorY";
	public static String TABLE_ID = "Id";
	public static String ROTATION = "Rotation";
	
	public BlueprintHandler(Context context){
		mContext = context;
	}
	
	
	

	public String getDate() {
		Calendar c = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(c.get(c.MONTH)));
		sb.append("_");
		sb.append(String.valueOf(c.get(c.DAY_OF_MONTH)));
		sb.append("_");
		sb.append(String.valueOf(c.get(c.YEAR)));
		sb.append("_");
		/*
		 * sb.append(String.valueOf(c.get(c.HOUR_OF_DAY))); sb.append("_");
		 * sb.append(String.valueOf(c.get(c.MINUTE))); sb.append("_");
		 * sb.append(String.valueOf(c.get(c.SECOND)));
		 */

		return sb.toString();
	}

	private File makeFile(String name) {
		File filename = new File(getDIR().toString() + File.separator + name
				+ FILE_TYPE);
		return filename;
	}
	
public void deleteFile(String name){
	File filename = new File(getDIR().toString() + File.separator + name
			+ FILE_TYPE);
	filename.delete();
	}

	private void createDIR() {
		File f = getDIR();
		f.mkdirs();
	}

	public File getDIR() {
		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + DIRECTORY_NAME + File.separator);
		return f;
	}

	public Boolean saveFile(ArrayList<Table> tableList, String fileName) {
		try {
			File f = makeFile(fileName);
			f.createNewFile();
			// write the bytes in file
			FileOutputStream fo = new FileOutputStream(f);
			PrintStream p = new PrintStream(fo);
			String s = generateXML(tableList);
			p.print(s);
			p.close();
			fo.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Table> getFile(String filename) {
		
		try {
			File f = makeFile(filename);
			// write the bytes in file
			System.out.println("getting file "+f.toString());
			if (f.exists()) {
				 InputStream in = new FileInputStream(f.toString());
				 TableXMLParser parser = new TableXMLParser(mContext);
				 ArrayList<Table> tables = new ArrayList<Table>();
				 try{
				 tables = parser.parse(in);
				 }catch(XmlPullParserException e){
					 e.printStackTrace();
				 }
				 return tables;
				    //InputStreamReader inputStreamReader = new InputStreamReader(in);
				   // BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				  //  StringBuilder sb = new StringBuilder();
				  //  String line;
				  //  while ((line = bufferedReader.readLine()) != null) {
				  //      sb.append(line);
				  //  }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public File[] getAllBlueprintNames() {
		File dir = getDIR();
		File[] files = dir.listFiles();
		return files;
	}

	public void removePromotion(String name) {
		File f = makeFile(name);
		f.delete();
	}

	public String generateXML(ArrayList<Table> tables) {
		System.out.println("printing FILE");
		String xml = "";
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<feed>");
		for (int i = 0; i < tables.size(); ++i) {
			sb.append("<Table>");
			sb.append("<"+TABLE_ID+">");
			sb.append(tables.get(i).getTableId());
			sb.append("</"+TABLE_ID+">");
			sb.append("<"+TABLE_TYPE+">");
			sb.append(tables.get(i).getTableType());
			sb.append("</"+TABLE_TYPE+">");
			sb.append("<"+TABLE_WIDTH+">");
			sb.append(tables.get(i).getStretchX());
			sb.append("</"+TABLE_WIDTH+">");
			sb.append("<"+TABLE_HEIGHT+">");
			sb.append(tables.get(i).getStretchY());
			sb.append("</"+TABLE_HEIGHT+">");
			sb.append("<"+TABLE_SEATSX+">");
			sb.append(tables.get(i).getSeatsX());
			sb.append("</"+TABLE_SEATSX+">");
			sb.append("<"+TABLE_NUMBER+">");
			sb.append(tables.get(i).getTableNumber());
			sb.append("</"+TABLE_NUMBER+">");
			sb.append("<"+TABLE_ANCHORX+">");
			sb.append(tables.get(i).getX());
			sb.append("</"+TABLE_ANCHORX+">");
			sb.append("<"+TABLE_ANCHORY+">");
			sb.append(tables.get(i).getY());
			sb.append("</"+TABLE_ANCHORY+">");
			sb.append("<"+ROTATION+">");
			sb.append(tables.get(i).getRotation());
			sb.append("</"+ROTATION+">");
			sb.append("</Table>");
		}
		sb.append("</feed>");
		xml = sb.toString();

		return xml;
	}

}
