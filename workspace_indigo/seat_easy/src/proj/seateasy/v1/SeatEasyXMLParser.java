package proj.seateasy.v1;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class SeatEasyXMLParser {
	private static final String ns = null;
	public static final String ADD_RESTAURANT = "AddRestaurant";
	private static final int PARSE_KEEP_XML = 70;
	private static final int PARSE_ADD_RESTAURANT = 71;
	   
	public List<Restaurant> parse(InputStream in) throws XmlPullParserException, IOException {
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

    
private List<Restaurant> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    List<Restaurant> restaurants = new ArrayList<Restaurant>();

    parser.require(XmlPullParser.START_TAG, ns, "feed");
    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            continue;
        }
        String name = parser.getName();
        // Starts by looking for the entry tag
        if (name.equals("Restaurant")) {
            restaurants.add(readRestaurant(parser));
        } else {
            skip(parser);
        }
    }  
    return restaurants;
}

//Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
//to their respective "read" methods for processing. Otherwise, skips the tag.
private Restaurant readRestaurant(XmlPullParser parser) throws XmlPullParserException, IOException {
 parser.require(XmlPullParser.START_TAG, ns, "Restaurant");
 String Name = null;
 String Lat = null;
 String Long = null;
 String Rating = null;
 String Promo = null;
 String HashTag = null;

 while (parser.next() != XmlPullParser.END_TAG) {
     if (parser.getEventType() != XmlPullParser.START_TAG) {
         continue;
     }
     String name = parser.getName();
     if (name.equals("Name")) {
         Name = readRestaurantInfo(parser,"Name");
     }else if (name.equals("Lat")) {
         Lat = readRestaurantInfo(parser,"Lat");
     }else if (name.equals("Long")) {
         Long = readRestaurantInfo(parser,"Long");
     } else if (name.equals("Rating")) {
         Rating = readRestaurantInfo(parser,"Rating");
     } else if (name.equals("Promo")) {
         Promo = readRestaurantInfo(parser,"Promo");
     } else if (name.equals("HashTag")) {
         HashTag = readRestaurantInfo(parser,"HashTag");
     } else {
         skip(parser);
     }
 }
 Restaurant rest = new Restaurant(Name);
 rest.setLat(Lat);
 rest.setLong(Long);
 rest.setPromo(Promo);
 rest.setRating(Rating);
 rest.setHashTag(HashTag);
 return rest;
}

//Processes title tags in the feed.
private String readRestaurantInfo(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
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
