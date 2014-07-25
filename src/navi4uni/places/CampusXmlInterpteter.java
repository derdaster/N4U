/**
 * 
 */
package navi4uni.places;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import navi4uni.gui.MainActivity;
import navi4uni.netLayer.ImageDownloader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author MKarmanski karmel92@gmail.com
 */

public class CampusXmlInterpteter {
	Context context;
	private XmlPullParserFactory factory = null;
	private XmlPullParser parser = Xml.newPullParser();
	private HashMap<String, universityTuple> campusLinks = new HashMap<String, universityTuple>(); // Mapa Nazwa-Link dla linkow do xmlow ktore opisuja juz campusy
	private HashMap<String, URL> ImagePath = new HashMap<String, URL>();
	
/*	//==========================================================
	*//** Tuple class for return value from parseXML method.
	 * @param <A>	- parametrize for LinkedList (both A and B parameter)
	 * @param <B>
	 *//*
	private class Tuple<A,B>{
		public LinkedList<A> a = new LinkedList<A>();
		public LinkedList<B> b = new LinkedList<B>();
		@SuppressWarnings({ "unused", "unchecked" })
		public Tuple(LinkedList<A> _a, LinkedList<B> _b){
			a = (LinkedList<A>)_a;
			b = (LinkedList<B>)_b;	
			
			// Linie testowe - do podgladania wartosci zwracanej przez funkcje -- USUNAC!
			Log.i("przegladamy", "LISTE!!!!!!!");
			Iterator<A> itr1 = a.iterator();
			Iterator<B> itr2 = b.iterator();
			while(itr1.hasNext()){		
				TestMarker a = (TestMarker) itr1.next();	//castowanie ze strata danych!
				Log.i("budynki", a.name);
			}
			while(itr2.hasNext()){
				TestMarker a = (TestMarker) itr2.next();
				Log.i("miejsca", a.name);
			}
			//-----------------------------------------------------------------
		}
	}*/
	
	//Konstruktory
	public CampusXmlInterpteter(Context _context){
		this.context = _context;
	}
	
	public Map<String, universityTuple> getCampusLinks(){
		return this.campusLinks;
	}
	
	/** 
	 * @param doc - contains Document object with Name-link pairs (by xml)
	 */
	public void parseLinksXML(Document doc){
		if(doc != null){
			universityTuple uni = null;
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			
			for(int i = 0 ; i < nodeList.getLength() ; i++){
				uni = new universityTuple();
				Node node = nodeList.item(i);
				if(node instanceof Element){
					NodeList data = node.getChildNodes();
					
						for (int k = 0 ; k < data.getLength() ; k++){
							Node temp = data.item(k);
							if(temp instanceof Element){
								if(temp.getNodeName().equals("name")){
									uni.setName(temp.getLastChild().getTextContent());
								}
								else if(temp.getNodeName().equals("clong")){
									uni.setClong(temp.getLastChild().getTextContent());
								}
								else if(temp.getNodeName().equals("clat")){
									uni.setClat(temp.getLastChild().getTextContent());
								}
								else if(temp.getNodeName().equals("country")){
									uni.setCountry(temp.getLastChild().getTextContent());
								}
								else if(temp.getNodeName().equals("city")){
									uni.setCity(temp.getLastChild().getTextContent());
								}
								else if(temp.getNodeName().equals("link")){
									uni.setLink(temp.getLastChild().getTextContent());
								}
							}
						}
					campusLinks.put(uni.getName(), uni);
//					Log.i("nazwa", node.getNodeName());
//					Log.i("zawartosc", node.getTextContent());
//					campusLinks.put(node.getNodeName(), node.getTextContent());
						Log.i("University", uni.toString());
				}
			}
		}
	}
	
	/** method parses XML code from param and fills NaviMarker.markerList - static List of Markers
	 * @param doc - contains Document with campus description in xml
	 * @param campus - campus name
	 * @param setDefaultPosition - take default position from XML and set NaviMarker.defaultPosition static variable
	 * @throws MalformedURLException 
	 */
	public ArrayList<NaviMarker> parseCampusXMLfromDoc(Document doc, String campus) throws MalformedURLException{
		ArrayList<NaviMarker> localMarkerlist = new ArrayList<NaviMarker>();
		if(doc != null){
			boolean isBuilding = false;
			boolean isPlace = false;
			BuildingMarker tempB = null;											
			PlacesMarker tempP = null;
			
			NodeList nodeList = doc.getDocumentElement().getChildNodes();	//zawartosc <campus>
			for(int i = 0 ; i < nodeList.getLength() ; i++){
					Node node = nodeList.item(i); //<Building> & <Places>
					if(node.getNodeName().equals("building")){
						tempB = new BuildingMarker();
						isBuilding = true;
						isPlace = false;
						Log.i("obiekt", "Utworzono budynek!");
					}
					if(node.getNodeName().equals("place")){
						tempP = new PlacesMarker();
						isBuilding = false;
						isPlace = true;
						Log.i("obiekt", "Utworzono miejsce!");
					}
					
					if(node instanceof Element){
						NodeList childNodes = node.getChildNodes();	//zawartosc <Building>...  -  <name> & <snippet> & <phone> etc.
						for(int j = 0 ; j < childNodes.getLength() ; j++){
							Node cNode = childNodes.item(j);	//definicja markerow
							if (cNode instanceof Element){
								String text = null;
								if(cNode.hasChildNodes())	text = cNode.getLastChild().getTextContent();
								else text = " ";
								if(isBuilding == true)	manageData(cNode.getNodeName(), text, tempB);
								else	manageData(cNode.getNodeName(), text, tempP);
							}
						}
						if(isBuilding == true){
							tempB.configurePositons();	//zebrane dane wrzucamy do obiektu MarkerOptions(googlowski)
							Log.i("buildingDocument", tempB.toString());
							tempB.setCampusName(campus);
							localMarkerlist.add(tempB);
							ImagePath.put(tempB.getName(), new URL(tempB.getPhoto()));
						}
						else if (isPlace == true){
							tempP.configurePositons();
							Log.i("PlaceDocument", tempP.toString());
							tempP.setCampusName(campus);
							localMarkerlist.add(tempP);
						}
					}
			}
		}
		return localMarkerlist;
	}
	
	
	
	/** method parses XML code from param and fills NaviMarker.markerList - static List of Markers
	 * @param xmlAsString - contains xml with campus definition
	 * @param campus - campus name
	 * @return Tuple Object contains LinkedList (places and buildings with data from XML)
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ArrayList<NaviMarker> parseXml(String xmlAsString, String campus) throws XmlPullParserException, IOException{
		factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		parser = factory.newPullParser();
		parser.setInput(new StringReader(xmlAsString));
		
		ArrayList<NaviMarker> localMarkerlist = new ArrayList<NaviMarker>();
		
		if(xmlAsString != null){											//jezeli nasz string wejsciowy nie jest nullem
			Double latitude = null;
			String name = null;
			String text = null;
			BuildingMarker tempB = null;											
			PlacesMarker tempP = null;										
			boolean isBulding = false;										
			
					while(parser.getEventType() != XmlPullParser.END_DOCUMENT){
						name = parser.getName();
						
						switch(parser.getEventType()){
						
						case XmlPullParser.START_DOCUMENT:					//Jak juz cos ten dokument ma, to inicjujemy kontenery
							break;
							
						case XmlPullParser.START_TAG:						//wykrywamy tagi otwierajace
							if (name.equals("campus")){
								Log.i("parser", "campus description");
							}
							if(name.equals("building")){
								tempB = new BuildingMarker();
								isBulding = true;			//ustawiamy flagi zeby pozniej rozpoznac do jakiego obiektu ladowac dane
								Log.i("parser", "building description");
							}
							if (name.equals("place")){
								tempP = new PlacesMarker();
								isBulding = false;
								Log.i("parser", "place description");
							}
							
							break;
							
							
						case XmlPullParser.TEXT:							//zawartosc miedzy tagami (cialo)
							text = parser.getText();
							break;
							
							
						case XmlPullParser.END_TAG:	//po wczytaniu TEXT, zrzucamy dane do obiektu na wyjsciu z tag-u/a
							
							if(tempB != null || tempP != null){	//co by runtime nie bylo ze wpisujemy dane do nulla.
								if(isBulding == true){	//uzupelniamy pola budynku
									
									manageData(name, text, tempB);	//metoda wstawia dane w odpowiednie miejsce
									
									if (name.equals("building")){	//znacznik koncowy </building>
										tempB.configurePositons();	//zebrane dane wrzucamy do obiektu MarkerOptions(googlowski)
										Log.i("building", tempB.toString());
										tempB.setCampusName(campus);
										localMarkerlist.add(tempB);
										ImagePath.put(tempB.getName(), new URL(tempB.getPhoto()));
									}
								}
								else if(isBulding == false){					//uzupelniamy pola miejsca
									
									manageData(name, text, tempP);	//metoda wstawia dane w odpowiednie miejsce
									
									if (name.equals("place")){	
										tempP.configurePositons();
										Log.i("Place", tempP.toString());
										tempB.setCampusName(campus);
										localMarkerlist.add(tempP);
									}
								}
							}
							break;
							
						default: break;
						}
					parser.next();						
					}
		}
		return localMarkerlist;
	}
	
	
	public void downloadImages(){
		if(!ImagePath.isEmpty()){
			ImageDownloader imgDownloader = new ImageDownloader(context);
			HashMap<String, Bitmap> image = imgDownloader.download(ImagePath);
			
			NaviMarker.images.putAll(image);
			//MainActivity.storage.saveImageCollection(image);
		}
	}
	
	/**
	 * @return LinkedList contains keys from campusLinks
	 */
	public LinkedList<String> getCampusNames(){
		LinkedList<String> keys = new LinkedList<String>();
			for(String a : campusLinks.keySet()){
				keys.add(a);
			}
			return keys;
	}
	
	
	
	/** method called for End-tag 
	 * @param name - current xml tag
	 * @param text	- body of xmlEvent (e.g : <tag> text </tag>)
	 * @param tempB	- current temporary object storing data
	 * @return	true if text-data was put into Object, false if not.
	 */
	private boolean manageData(String name, String text, BuildingMarker tempB){
//		Log.i("Bpassed text", text);
//		Log.i("Bpassed tagname", name);
		if(text == null){
			text = " ";			
		}
		if(name.equals("id")){
			tempB.setId(text);
			return true;
		}
		if(name.equals("tag")){
			tempB.setTag(text);
			NaviMarker.tagSet.add(text);
			return true;
		}
		if(name.equals("name")){
			tempB.setName(text);
			return true;
		}
		if(name.equals("snippet")){
			tempB.setSnippet(text);
			return true;
		}
		if(name.equals("lat")){
			tempB.setLat(text);
			return true;
		}
		if(name.equals("long")){
			tempB.setLong(text);
			return true;
		}
		if(name.equals("open_hours")){
			tempB.setOpenHours(text);
			return true;
		}
		if(name.equals("address")){
			tempB.setAddress(text);
			return true;
		}
		if(name.equals("department")){
			tempB.setDepartment(text);
			return true;
		}
		if(name.equals("directions")){
			tempB.setDirections(text);
			return true;
		}
		if(name.equals("photo")){
			tempB.setPhoto(text);
			return true;
		}
		if(name.equals("description")){
			tempB.setDescription(text);
			return true;
		}
		if(name.equals("phone")){
			tempB.setPhone(text);
			return true;
		}
		if(name.equals("mail")){
			tempB.setMail(text);
			return true;
		}
		if(name.equals("www")){
			tempB.setWww(text);
			return true;
		}
		else{
			return false;
		}

	}
	
	
	
	/** method called for End-tag event.
	 * @param name - current xml tag
	 * @param text	- body of xmlEvent (e.g : <tag> text </tag>)
	 * @param tempP	- current temporary object storing data
	 * @return	true if text-data was put into Object, false if not.
	 */
	private boolean manageData(String name, String text, PlacesMarker tempP){
//		Log.i("Ppassed text", text);
//		Log.i("Ppassed tagname", name);
		if(name.equals("tag")){
			tempP.setTag(text);
			NaviMarker.tagSet.add(text);
			return true;
		}
		if(name.equals("id")){
			tempP.setId(text);
			return true;
		}
		if(name.equals("name")){
			tempP.setName(text);
			return true;
		}
		if(name.equals("snippet")){
			tempP.setSnippet(text);
			return true;
		}
		if(name.equals("lat")){
			tempP.setLat(text);
			return true;
		}
		if(name.equals("long")){
			tempP.setLong(text);
			return true;
		}
		if(name.equals("open_hours")){
			tempP.setOpenHours(text);
			return true;
		}
		if(name.equals("address")){
			tempP.setAddress(text);
			return true;
		}
		if(name.equals("pbuilding")){
			tempP.setBuilding(text);
			return true;
		}
		if(name.equals("floor")){
			tempP.setFloor(text);
			return true;
		}
		if(name.equals("room")){
			tempP.setRoom(text);
			return true;
		}
		if(name.equals("description")){
			tempP.setDescription(text);
			return true;
		}
		if(name.equals("phone")){
			tempP.setPhone(text);
			return true;
		}
		if(name.equals("mail")){
			tempP.setMail(text);
			return true;
		}
		if(name.equals("www")){
			tempP.setWww(text);
			return true;
		}
		else{
			return false;
		}
	}
	
	
// Metoda testowa, pozniej strumien wejsciowy dla XmlPullParser zmienimy na jakis BufforReader (znakowy) dla polaczenia internetowego	
	private void fillXml(){
		/*xmlAsString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				
				"        <building>\n" + 
				"                <tag>Bulding</tag>                                                                      \n" + 
				"                <name>C1</name>                                                 <!-- Bulding Name -->\n" + 
				"                <snippet>Budynek C13</snippet>                                 	<!-- Snippet description -->\n" + 
				"		                                                       		<!-- Coordinates in WGS84 system -->\n" + 
				"                <lat>123</lat>                                         		<!-- latitude -->\n" + 
				"                <long>345</long>                                        	<!-- longitude -->\n" + 
				"                <open_hours>8:00am-4:00pm</open_hours>                          <!-- Opening hours -->\n" + 
				"                <address>ul. Janiszewskiego 13</address>                        <!-- Address of Bulding -->\n" + 
				"                <department>elektronika W4</department>                         <!-- Department -->\n" + 
				"                <directions>przystanek MPK pl. Grunwaldzki</directions>         <!-- directions tip -->\n" + 
				"                <photo>/src/PWr/</photo>                                        <!-- path to photo on server -->\n" + 
				"                <description>Budynek wydzialu elektroniki</description>         <!-- Bulding Description -->\n" + 
				"                <phone>123456789</phone>                                        <!-- Phone number -->\n" + 
				"                <mail>weka@pwr.wroc.pl</mail>                                   <!-- e-mail -->\n" + 
				"                <www>www.weka.pwr.wroc.pl</www>\n" + 
				"        </building>\n" + 
				" \n" + 
				"        <places>\n" + 
				"                <tag>librares</tag>\n" + 
				"                <name>Biblioteka C3</name>                                      <!-- Places Name -->\n" + 
				"                <snippet>Biblioteka wydzialowa W4</snippet>\n" + 
				"                <lat>123.23</lat>						<!-- Latitude -->\n" + 
				"                <long>432.12</long>						<!-- Longitude -->\n" + 
				"                <open_hours>7:00-11:00</open_hours>\n" + 
				"                <address>ul. Janiszewskiego</address>\n" + 
				"                <pbuilding>C1</pbuilding>                                 	<!-- bulding name -->\n" + 
				"                <floor>2</floor>						\n" + 
				"                <room>123</room>\n" + 
				"                <description>Wypozyczenie tylko z indeksem</description>\n" + 
				"                <phone>987654321</phone>\n" + 
				"                <mail>biblioteka@pwr.wroc.pl</mail>\n" + 
				"                <www>strona www katalogu</www>\n" + 
				"        </places>\n" ;*/
		
		
		/*xmlAsString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<campus>\n" + 
				"        <building>\n" + 
				"                <tag>Bulding</tag>                                                                      \n" + 
				"                <name>C1</name>                                                 <!-- Bulding Name -->\n" + 
				"                <snippet>Budynek C13</snippet>                                 	<!-- Snippet description -->\n" + 
				"		                                                       		<!-- Coordinates in WGS84 system -->\n" + 
				"                <lat>123</lat>                                         		<!-- latitude -->\n" + 
				"                <long>345</long>                                        	<!-- longitude -->\n" + 
				"                <open_hours>8:00am-4:00pm</open_hours>                          <!-- Opening hours -->\n" + 
				"                <address>ul. Janiszewskiego 13</address>                        <!-- Address of Bulding -->\n" + 
				"                <department>elektronika W4</department>                         <!-- Department -->\n" + 
				"                <directions>przystanek MPK pl. Grunwaldzki</directions>         <!-- directions tip -->\n" + 
				"                <photo>/src/PWr/</photo>                                        <!-- path to photo on server -->\n" + 
				"                <description>Budynek wydzialu elektroniki</description>         <!-- Bulding Description -->\n" + 
				"                <phone>123456789</phone>                                        <!-- Phone number -->\n" + 
				"                <mail>weka@pwr.wroc.pl</mail>                                   <!-- e-mail -->\n" + 
				"                <www>www.weka.pwr.wroc.pl</www>\n" + 
				"        </building>\n" + 
				" \n" + 
				"        <places>\n" + 
				"                <tag>librares</tag>\n" + 
				"                <name>Biblioteka C3</name>                                      <!-- Places Name -->\n" + 
				"                <snippet>Biblioteka wydzialowa W4</snippet>\n" + 
				"                <lat>123.23</lat>						<!-- Latitude -->\n" + 
				"                <long>432.12</long>						<!-- Longitude -->\n" + 
				"                <open_hours>7:00-11:00</open_hours>\n" + 
				"                <address>ul. Janiszewskiego</address>\n" + 
				"                <pbuilding>C1</pbuilding>                                 	<!-- bulding name -->\n" + 
				"                <floor>2</floor>						\n" + 
				"                <room>123</room>\n" + 
				"                <description>Wypozyczenie tylko z indeksem</description>\n" + 
				"                <phone>987654321</phone>\n" + 
				"                <mail>biblioteka@pwr.wroc.pl</mail>\n" + 
				"                <www>strona www katalogu</www>\n" + 
				"        </places>\n" + 
				"        <places>\n" + 
				"		<tag>Drukarnie</tag>\n" + 
				"		<name>PolskiDruk</name>                                      <!-- Places Name -->\n" + 
				"		<snippet>Drukarnia A3/A4</snippet>\n" + 
				"		<lat>123.33</lat>						<!-- Latitude -->\n" + 
				"		<long>432.33</long>						<!-- Longitude -->\n" + 
				"		<open_hours>8:00-17:00</open_hours>\n" + 
				"		<address>ul. Janiszewskiego 12</address>\n" + 
				"		<pbuilding>D12</pbuilding>                                 	<!-- bulding name -->\n" + 
				"		<floor>5</floor>						\n" + 
				"		<room>45</room>\n" + 
				"		<description>Cennik : \n" + 
				"		A4 - 30gr\n" + 
				"		A3 - 50</description>\n" + 
				"		<phone>54321</phone>\n" + 
				"		<mail>polski@druk.pl</mail>\n" + 
				"		<www>polski.druk.pl</www>\n" + 
				"	</places>\n" + 
				"\n" + 
				"        <building>\n" + 
				"                <tag>bulding</tag>                                                                      \n" + 
				"                <name>D20</name>                                                 <!-- Bulding Name -->\n" + 
				"                <snippet>Budynek D20</snippet>                                 	<!-- Snippet description -->\n" + 
				"		                                                       		<!-- Coordinates in WGS84 system -->\n" + 
				"                <lat>133</lat>                                         		<!-- latitude -->\n" + 
				"                <long>333</long>                                        	<!-- longitude -->\n" + 
				"                <open_hours>9:00am-9:00pm</open_hours>                          <!-- Opening hours -->\n" + 
				"                <address>ul. Grunwaldzka 13</address>                        <!-- Address of Bulding -->\n" + 
				"                <department>W8</department>                         <!-- Department -->\n" + 
				"                <directions>przystanek MPK pl. Grunwaldzki</directions>         <!-- directions tip -->\n" + 
				"                <photo>/src/PWr/D20.jpg</photo>                                        <!-- path to photo on server -->\n" + 
				"                <description>Budynek D20</description>         <!-- Bulding Description -->\n" + 
				"                <phone>123456789</phone>                                        <!-- Phone number -->\n" + 
				"                <mail>weka@pwr.wroc.pl</mail>                                   <!-- e-mail -->\n" + 
				"                <www>www.weka.pwr.wroc.pl</www>\n" + 
				"        </building>\n" + 
				"\n" + 
				"\n" + 
				"</campus>";*/
		
		/*xmlAsString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<campus>\n" +
                "        <building>\n" +
                "                <tag>Budynek</tag>                                                                      \n" +
                "                <name>C1</name>                                                 <!-- Bulding Name -->\n" +
                "                <snippet>Budynek C1 W-4</snippet>                                  <!-- Snippet description -->\n" +
                "                <lat>51.109135</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.060512</long>\n" +
                "                <open_hours>06:00 - 23:00</open_hours>                          <!-- Opening hours -->\n" +
                "                <address>ul. Janiszewskiego 11/17</address>                        <!-- Address of Bulding -->\n" +
                "                <department>wydział elektroniki W-4 PWr</department>                         <!-- Department -->\n" +
                "                <directions>najbliższy przystanek MPK : Plac Grunwaldzki</directions>         <!-- directions tip -->\n" +
                "                <photo>/src/PWr/W4.jpg</photo>                                        <!-- path to photo on server -->\n" +
                "                <description>Budynek wydzialu elektroniki</description>         <!-- Bulding Description -->\n" +
                "                <phone>123456789</phone>                                        <!-- Phone number -->\n" +
                "                <mail>weka@pwr.wroc.pl</mail>                                   <!-- e-mail -->\n" +
                "                <www>www.weka.pwr.wroc.pl</www>\n" +
                "        </building>\n" +
                "\n" +
                "        <building>\n" +
                "                <tag>Budynek</tag>                                                                      \n" +
                "                <name>C2</name>                                                 <!-- Bulding Name -->\n" +
                "                <snippet>Budynek C2 W-12</snippet>                                  <!-- Snippet description -->\n" +
                "                <lat>51.108940</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.061083</long>\n" +
                "                <open_hours>06:00 - 23:00</open_hours>                          <!-- Opening hours -->\n" +
                "                <address>ul. Janiszewskiego 11/17</address>                        <!-- Address of Bulding -->\n" +
                "                <department>wydział Elektroniki Mikrosystemów i Fotoniki, W-12 PWr</department>                         <!-- Department -->\n" +
                "                <directions>najbliższy przystanek MPK : Plac Grunwaldzki</directions>         <!-- directions tip -->\n" +
                "                <photo>/src/PWr/W12.jpg</photo>                                        <!-- path to photo on server -->\n" +
                "                <description>Budynek wydzialu Elektroniki Mikrosystemow i Fotoniki</description>         <!-- Bulding Description -->\n" +
                "                <phone>123456789</phone>                                        <!-- Phone number -->\n" +
                "                <mail>wemif@pwr.wroc.pl</mail>                                   <!-- e-mail -->\n" +
                "                <www>www.wemif.pwr.wroc.pl</www>\n" +
                "        </building>\n" +
                "\n" +
                "       <building>\n" +
                "                <tag>Food</tag>                                                                      \n" +
                "                <name>Bułkowóz</name>                                                 <!-- Bulding Name -->\n" +
                "                <snippet>Bułki, Pączki</snippet>                                  <!-- Snippet description -->\n" +
                "                <lat>51.110652</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.059286</long>\n" +
                "                <open_hours>08:00 - 15:00</open_hours>                          <!-- Opening hours -->\n" +
                "                <address>Plac Grunwaldzki</address>                        <!-- Address of Bulding -->\n" +
                "                <department> </department>                         <!-- Department -->\n" +
                "                <directions>najbliższy przystanek MPK : Plac Grunwaldzki</directions>         <!-- directions tip -->\n" +
                "                <photo>/src/PWr/W12.jpg</photo>                                        <!-- path to photo on server -->\n" +
                "                <description>Sklep z bulkami (samochod Fiat Ducato)</description>         <!-- Bulding Description -->\n" +
                "                <phone>123456789</phone>                                        <!-- Phone number -->\n" +
                "                <mail> </mail>                                   <!-- e-mail -->\n" +
                "                <www> </www>\n" +
                "        </building>\n" +
                "\n" +
                "       <building>\n" +
                "                <tag>Transport</tag>                                                                      \n" +
                "                <name>Polinka</name>                                                 <!-- Bulding Name -->\n" +
                "                <snippet>Polaczenie z Geocentrum</snippet>                                  <!-- Snippet description -->\n" +
                "                <lat>51.107145</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.058294</long>\n" +
                "                <open_hours>pon-pt 7:00 - 21:00\n sob-nd 10:00 - 18:00 </open_hours>  <!-- Opening hours -->\n" +
                "                <address>ul. Wybrzeże Wyspiańskiego 23-25</address>                                  <!-- Address of Bulding -->\n" +
                "                <department> </department>                                             <!-- Department -->\n" +
                "                <directions>najbliższy przystanek MPK : Most Grunwaldzki \n przyst. Politechnika</directions>         <!-- directions tip -->\n" +
                "                <photo>/src/PWr/polinka.jpg</photo>                                        <!-- path to photo on server -->\n" +
                "                <description>Kolejka gondolowa łącząca Campus z Budynkami L (Geocentrum) </description>         <!-- Bulding Description -->\n" +
                "                <phone>123456789</phone>                                        <!-- Phone number -->\n" +
                "                <mail> </mail>                                   <!-- e-mail -->\n" +
                "                <www>www.polinka.pl</www>\n" +
                "        </building>\n" +
                "\n" +
                "        <places>\n" +
                "                <tag>Biblioteka</tag>\n" +
                "                <name>OCWiINT W4 i W12</name>                                      <!-- Places Name -->\n" +
                "                <snippet>Biblioteka miedzywydzialowa przy W4 i W12</snippet>\n" +
                "                <lat>51.108800</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.060472</long>\n" +
                "                <open_hours>pon 9-16:30 \n wt 9:00 - 16:30\n śr 9:00 - 16:30\n czw 09:00 - 16:30\n pt 09:00 - 15:00\n sob 10:00 - 12:30</open_hours>\n" +
                "                <address>ul. Janiszewskiego 11/17</address>\n" +
                "                <pbuilding>C3</pbuilding>                                       <!-- bulding name -->\n" +
                "                <floor>0</floor>                                               \n" +
                "                <room>12</room>\n" +
                "                <description>Oddział Centrum Wiedzu i Informacji Naukowo-Technicznej przy Wydziale W4 i W12\n Wypozyczenie tylko z indeksem</description>\n" +
                "                <phone>713202878</phone>\n" +
                "                <mail>bi6.17.12@pwr.wroc.pl</mail>\n" +
                "                <www> </www>\n" +
                "        </places>\n" +
                "\n" +
                "        <places>\n" +
                "                <tag>Biblioteka</tag>\n" +
                "                <name>OCWiINT</name>                                      <!-- Places Name -->\n" +
                "                <snippet>Biblioteka miedzywydzialowa przy W4 i W12</snippet>\n" +
                "                <lat>51.107803</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.060126</long>\n" +
                "                <open_hours>pn-czw : 09:00 - 18:00\n pt 09:00 - 15:00</open_hours>\n" +
                "                <address>ul. Norwida 4/6</address>\n" +
                "                <pbuilding>C6</pbuilding>                                       <!-- bulding name -->\n" +
                "                <floor> </floor>                                               \n" +
                "                <room>60</room>\n" +
                "                <description>Wypozyczenie tylko z indeksem</description>\n" +
                "                <phone>320-28-27</phone>\n" +
                "                <mail>bw4.12@pwr.wroc.pl</mail>\n" +
                "                <www> </www>\n" +
                "        </places>\n" +
                "       \n" +
                "               <places>\n" +
                "                <tag>Dziekanat</tag>\n" +
                "                <name>Dziekanat W4</name>                                      <!-- Places Name -->\n" +
                "                <snippet>Highway to Hell</snippet>\n" +
                "                <lat>51.109207</lat>                                                  <!-- Coordinates in WGS84 system -->\n" +
                "                <long>17.060598</long>\n" +
                "                <open_hours>pn - wt : 08:00 - 12:00\n czw 11:00 - 15:00\n pt 08:00 - 12:00 </open_hours>\n" +
                "                <address>ul. Janiszewskiego 11/17</address>\n" +
                "                <pbuilding>C1</pbuilding>                                       <!-- bulding name -->\n" +
                "                <floor>0</floor>                                               \n" +
                "                <room>25b</room>\n" +
                "                <description>Chwala poleglym!</description>\n" +
                "                <phone>713202531</phone>\n" +
                "                <mail> </mail>\n" +
                "                <www>weka.pwr.wroc.pl/dzeikanat,41.dhtml</www>\n" +
                "        </places>\n" +
                "\n" +
                "</campus>\n" +
                "";*/
		
		
	}
	
	
	
}
