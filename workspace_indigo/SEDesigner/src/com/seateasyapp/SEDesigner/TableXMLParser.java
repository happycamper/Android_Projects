package com.seateasyapp.SEDesigner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class TableXMLParser {
	private static final String ns = null;
	private Context mContext;
	private int TABLE_COUNT;
	
	public TableXMLParser(Context context){
		mContext = context;
		TABLE_COUNT = 1;
	}
	   
	public ArrayList<Table> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    
private ArrayList<Table> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    ArrayList<Table> tables = new ArrayList<Table>();

    parser.require(XmlPullParser.START_TAG, ns, "feed");
    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            continue;
        }
        String name = parser.getName();
        // Starts by looking for the entry tag
        if (name.equals("Table")) {
            tables.add(readTable(parser));
        } else {
            skip(parser);
        }
    }  
    return tables;
}

//Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
//to their respective "read" methods for processing. Otherwise, skips the tag.
private Table readTable(XmlPullParser parser) throws XmlPullParserException, IOException {
 parser.require(XmlPullParser.START_TAG, ns, "Table");
 String Type = null;
 String Width = null;
 String Height = null;
 String SeatsX = null;
 String Number = null;
 String AnchorX = null;
 String AnchorY = null;
 String Id = null;
 String Rotation = null;

 while (parser.next() != XmlPullParser.END_TAG) {
     if (parser.getEventType() != XmlPullParser.START_TAG) {
         continue;
     }
     String name = parser.getName();
     if (name.equals(BlueprintHandler.TABLE_TYPE)) {
         Type = readTableInfo(parser,BlueprintHandler.TABLE_TYPE);
     }else if (name.equals(BlueprintHandler.TABLE_WIDTH)) {
         Width = readTableInfo(parser,BlueprintHandler.TABLE_WIDTH);
     }else if (name.equals(BlueprintHandler.TABLE_HEIGHT)) {
         Height = readTableInfo(parser,BlueprintHandler.TABLE_HEIGHT);
     } else if (name.equals(BlueprintHandler.TABLE_SEATSX)) {
         SeatsX = readTableInfo(parser,BlueprintHandler.TABLE_SEATSX);
     } else if (name.equals(BlueprintHandler.TABLE_NUMBER)) {
         Number = readTableInfo(parser,BlueprintHandler.TABLE_NUMBER);
     } else if (name.equals(BlueprintHandler.TABLE_ANCHORX)) {
         AnchorX = readTableInfo(parser,BlueprintHandler.TABLE_ANCHORX);
     } else if (name.equals(BlueprintHandler.TABLE_ANCHORY)) {
         AnchorY = readTableInfo(parser,BlueprintHandler.TABLE_ANCHORY);
     }else if (name.equals(BlueprintHandler.ROTATION)) {
         Rotation = readTableInfo(parser,BlueprintHandler.ROTATION);
     }else {
         skip(parser);
     }
 }
 Table table = new Table(mContext,Integer.parseInt(Type));
 table.setTableId(TABLE_COUNT);
 TABLE_COUNT += 1;
 table.setTableType(Integer.parseInt(Type));
 table.setStretchX(Math.round(Integer.parseInt(Width)));
 table.setStretchY(Math.round(Integer.parseInt(Height)));
 table.setSeatsX(Integer.parseInt(SeatsX));
 table.setTableNumber(Integer.parseInt(Number));
 table.setAnchorX(Math.round((float)Float.parseFloat(AnchorX)));
 table.setAnchorY(Math.round((float)Float.parseFloat(AnchorY)));
 table.doRotation(Math.round((float)Float.parseFloat(Rotation)));
 return table;
}

//Processes title tags in the feed.
private String readTableInfo(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
 parser.require(XmlPullParser.START_TAG, ns, tag);
 String Name = readText(parser);
 parser.require(XmlPullParser.END_TAG, ns, tag);
 return Name;
}


//Processes link tags in the feed.
private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
 String link = "";
 parser.require(XmlPullParser.START_TAG, ns, "link");
 String tag = parser.getName();
 String relType = parser.getAttributeValue(null, "rel");  
 if (tag.equals("link")) {
     if (relType.equals("alternate")){
         link = parser.getAttributeValue(null, "href");
         parser.nextTag();
     } 
 }
 parser.require(XmlPullParser.END_TAG, ns, "link");
 return link;
}

//Processes summary tags in the feed.
private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
 parser.require(XmlPullParser.START_TAG, ns, "summary");
 String summary = readText(parser);
 parser.require(XmlPullParser.END_TAG, ns, "summary");
 return summary;
}

//For the tags title and summary, extracts their text values.
private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
 String result = "";
 if (parser.next() == XmlPullParser.TEXT) {
     result = parser.getText();
     parser.nextTag();
 }
 return result;
}

private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    if (parser.getEventType() != XmlPullParser.START_TAG) {
        throw new IllegalStateException();
    }
    int depth = 1;
    while (depth != 0) {
        switch (parser.next()) {
        case XmlPullParser.END_TAG:
            depth--;
            break;
        case XmlPullParser.START_TAG:
            depth++;
            break;
        }
    }
 }

}
