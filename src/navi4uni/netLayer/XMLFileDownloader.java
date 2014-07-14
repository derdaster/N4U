package navi4uni.netLayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import navi4uni.gui.R;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//adres testowy z opisem campusu : http://masuk.drl.pl/xmlfile/Navi4UniTestXML2.xml
//adres test. z linkami : http://masuk.drl.pl/xmlfile/links2.xml
public class XMLFileDownloader {

	private class DownloadTask extends AsyncTask<String, Void, Document> {
		@Override
		protected Document doInBackground(String... params) {
				errLog = "";
				URL urlAddress = null;
				try {
					urlAddress = new URL(params[0]);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					errLog = context
							.getString(R.string.wrongUrl);
					return null;
				}
				// =================================
				InputStream stream = null;
				try {
					stream = urlAddress.openStream();
				} catch (IOException e) {
					e.printStackTrace();
					//errLog = context
					//		.getString(R.string.turnOnInternetConnection);
					return null;
				}
				// =================================
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					errLog = context
							.getString(R.string.parserError);
					return null;
				}
				// =================================
				Document doc = null;
				try {
					doc = builder.parse(stream);
				} catch (SAXException e) {
					e.printStackTrace();
					errLog = context
							.getString(R.string.parserError);
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					errLog = context
							.getString(R.string.parserError);
					return null;
				}
				return doc;
		}

		@Override
		protected void onPostExecute(Document result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	Context context = null;
	String errLog = null;
	static DownloadTask dTask;

	public XMLFileDownloader(Context _context) {
		this.context = _context;
	}

	public Document download(String url)throws InterruptedException, ExecutionException {
		if(url != null){
			dTask = new DownloadTask();
			Document doc = dTask.execute(url).get();
			if(!errLog.isEmpty()){
				 Toast.makeText(context, errLog , Toast.LENGTH_LONG).show();
			 }
			return doc;
		}
		return null;
	}
}
