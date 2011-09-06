package com.zabiroid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

public class ZabbixApiAccess {
	
	private static final String ZABBIX_API_PATH = "/api_jsonrpc.php";
	private static final String CONTENT_TYPE = "application/json-rpc";
	private String uri;
	private String host;
	private HttpPost httpPost;
	private JSONObject jsonObject = new JSONObject();
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<String> itemIdList = new ArrayList<String>();
	private int position = 0;
	private String authToken;
	JSONObject jsonEntity = null;
	HttpClient httpClient = new DefaultHttpClient();
	
	ZabbixApiAccess(String host, boolean https){
		this.host = host;
		if ( https == true ) {
			this.uri = this.makeHttpsUri(host);
		}else {
			this.uri = this.makeUri(host);
		}
		
		this.setBasicJSONParams();
	}
	
	ZabbixApiAccess(String uri,String authToken){
		this.uri = uri;
		this.authToken = authToken;
		try {
			this.jsonObject.put("auth", authToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.setBasicJSONParams();
		
	}
	public void setHost(String host)
	{
		this.host = host;
		
	}
	
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	
	public String getHost()
	{
		return this.host;
	}
	
	public String getUri()
	{
		return this.uri;
	}
	
	private String makeUri(String host)
	{
		Uri.Builder uriBuilder = new Uri.Builder();
    	uriBuilder.scheme("http");
    	uriBuilder.authority(host);
    	uriBuilder.path(ZABBIX_API_PATH);
    	this.uri = Uri.decode(uriBuilder.build().toString());
    	return this.uri;
	}
	private String makeHttpsUri(String host)
	{
		Uri.Builder uriBuilder = new Uri.Builder();
    	uriBuilder.scheme("https");
    	uriBuilder.authority(host);
    	uriBuilder.path(ZABBIX_API_PATH);
    	this.uri = Uri.decode(uriBuilder.build().toString());
    	return this.uri;
	}
	
	private void setBasicJSONParams()
	{
		try {
			jsonObject.put("jsonrpc", "2.0");
			jsonObject.put("id", "1");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
	}
	
	public JSONObject getJsonObject()
	{
		return this.jsonObject;
	}
	
	public void setMethod(String method)
	{
		try {
			jsonObject.put("method", method);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getMethod() {
		try {
			return this.jsonObject.getString("method");
		} catch (JSONException e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	private JSONObject Access() throws IOException {
		httpPost = new HttpPost(this.uri);
		httpPost.setHeader("Content-type", CONTENT_TYPE);
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(this.jsonObject.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.setEntity(stringEntity);
		
		if ( this.uri.substring(0,5).matches("https") ) {
			return this.httpsAccess();
		}else {
			return this.httpAccess();
		}
	}
 	
	private JSONObject httpAccess() throws IOException {
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			Log.e("response",httpResponse.toString());
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e("Statuscode",Integer.toString(statusCode));
			if (statusCode == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpResponse.getEntity());
				try {
					jsonEntity = new JSONObject(entity);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonEntity;
				
			}else
			{
				
				return null;
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("ClientProtocol",e.getMessage());
			return null;
		}	
	}
	
	private JSONObject httpsAccess() throws IOException {
	//	JSONObject jsonEntity = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpContext httpcontext = new BasicHttpContext();
		 
		KeyStore trustStore = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		 try {
			trustStore.load(null,null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 SSLSocketFactory sf = null;
		try {
			sf = new MySSLSocketFactory(trustStore);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		//	Log.e("KeyManagement","KeyManagement");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		//	Log.e("NoSuchAlgorithm","NoSuchAlgorithm");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		//	Log.e("KeyStore","KeyStore");
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		//	Log.e("UnrecoverableKey","UnrecoverableKey");
		}
		 Scheme https = new Scheme("https", sf, 443);
		// Log.e("Scheme",https.getSocketFactory().toString());
		// Log.e("connectionManager1",httpclient.getConnectionManager().toString());
	     httpclient.getConnectionManager().getSchemeRegistry().register(https);
	    // Log.e("connectionManager2",httpclient.getConnectionManager().getSchemeRegistry().toString());
	     httpcontext.setAttribute(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	     httpcontext.setAttribute(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
	     httpcontext.setAttribute(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
		 try {
			 Log.e("httpPost",httpPost.getURI().toString());
			HttpResponse httpResponse = httpclient.execute(httpPost,httpcontext);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpResponse.getEntity());
				try {
					jsonEntity = new JSONObject(entity);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonEntity;
				
			}else
			{
				
				return null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("ClientProtocol","ClientProtocol");
			return null;
		}
		 
	}
	
	public String zabbixAuthenticate(String account_name, String pass) throws IOException {
		this.setMethod("user.authenticate");
		JSONObject params = new JSONObject();
		try {
			params.put("user", account_name);
			params.put("password", pass);
			jsonObject.put("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject response = this.Access();
		try {
			return response.getString("result");
		} catch (JSONException e) {
			e.printStackTrace();
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	
	}

	public ArrayList<Host> getHostList(String filter) throws IOException
	{
		ArrayList<Host> hostList = new ArrayList<Host>();
		ArrayList<Trigger> trigger = new ArrayList<Trigger>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		
		try {
				//	this.jsonObject.put("method", "host.get");
			subParams.put("extendoutput", "true");
			if ( filter != "all" )
			{
				subsubParams.put("host", "[\""+filter+"\"]");
				subParams.put("filter", subsubParams);
			}
		
			this.setMethod("host.get");
			this.jsonObject.put("params", subParams);
			response = this.Access();
	//		response = this.apiAccess(authKey, subParams);
			if (response != null) {
				JSONArray resultObject = response.getJSONArray("result");
			
				int count = resultObject.length();
			
				for (int i=0; i<count; i++)
				{
					Host host = new Host();
					host.setHostId(resultObject.getJSONObject(i).getString("hostid"));
					host.setHostName(resultObject.getJSONObject(i).getString("host"));
					host.setHostStatus(resultObject.getJSONObject(i).getString("status"));
					host.setHostDns(resultObject.getJSONObject(i).getString("dns"));
					host.setHostIp(resultObject.getJSONObject(i).getString("ip"));
					//hostList.add(resultObject.getJSONObject(i).getString("host"));
					Log.e("hostID",host.getHostId());
					Log.e("hostName",host.getHostName());
					trigger = this.getTriggerList(host.getHostId(), 0);
					int errornum = 0;
					if (trigger != null) {
						errornum = trigger.size();
					}
					Log.e("errornum",Integer.toString(errornum));
					host.setErrorNum(errornum);
					hostList.add(host);
				}			
				return hostList;
			}
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return hostList;
		}
		
	}
	
	public ArrayList<Item> getItemList(String hostid, ArrayList<String> itemIdList, int num) throws IOException {
		
		
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
		//	this.jsonObject.put("method", "item.get");
			subParams.put("output","extend");
			JSONArray idarray = new JSONArray();
			//Log.e("itemid",itemIdList.get(0));
			Log.e("position",Integer.toString(position));
			for (int i=0; i<num; i++) {
				if ( position < itemIdList.size() ){
					idarray.put(itemIdList.get(this.position));
					this.position = this.position + 1;
				}
				else {
					break;
				}
			}
			subsubParams.put("itemid", idarray);
			//subsubParams.put("hostid",hostid);
			subParams.put("filter", subsubParams);
			//subParams.put("limit", num);
			this.setMethod("item.get");
			this.jsonObject.put("params", subParams);
			response = this.Access();
			//response = this.apiAccess(authKey, subParams);
			
			if(response != null) {
				JSONArray resultObject = response.getJSONArray("result");
				
				int count = resultObject.length();
			
				for (int i=0; i<count; i++)
				{
					Item item = new Item();
					item.setItemId(resultObject.getJSONObject(i).getString("itemid"));
					item.setItemDescription(resultObject.getJSONObject(i).getString("description"));
					item.setItemKey(resultObject.getJSONObject(i).getString("key_"));
					item.setItemValueType(resultObject.getJSONObject(i).getString("value_type"));
					item.setItemValue(resultObject.getJSONObject(i).getString("lastvalue"));
					item.setItemUnits(resultObject.getJSONObject(i).getString("units"));
					//item.setItemValue(this.getItemValue(authKey,item));
					//hostList.add(resultObject.getJSONObject(i).getString("host"));
					itemList.add(item);
				}			
			}
			return itemList;
		} catch (JSONException e) {
			e.printStackTrace();
			return itemList;
		}
		
		
	}
	
	public ArrayList<String> getItemIdList(String hostid) throws IOException {
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
		//	this.jsonObject.put("method", "item.get");
			subParams.put("output","shorten");
			subsubParams.put("hostid",hostid);
			subParams.put("filter", subsubParams);
			
			this.setMethod("item.get");
			this.jsonObject.put("params", subParams);
			response = this.Access();
		//	response = this.apiAccess(authKey, subParams);
			
			if(response != null) {
				JSONArray resultObject = response.getJSONArray("result");
				
				int count = resultObject.length();
			
				for (int i=0; i<count; i++)
				{
					String itemid = null;
					this.itemIdList.add(resultObject.getJSONObject(i).getString("itemid"));
				}			
			}
			return this.itemIdList;
		} catch (JSONException e) {
			e.printStackTrace();
			return itemIdList;
		}
		
		
	}
	
	public void clearItemListCount(){
		position = 0;
		itemList.clear();
	}
	
	public ArrayList<HistoryData> getHistoryData(Item item, TimeRange timerange ) throws IOException {

		ArrayList<HistoryData> historyDataList = new ArrayList<HistoryData>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
			//this.jsonObject.put("method", "history.get");
			subParams.put("output","extend");
			subParams.put("itemids", item.getItemId());
			subParams.put("time_from", timerange.getTimeFrom());
			subParams.put("time_till", timerange.getTimeTill());
			subParams.put("history", item.getItemValueType());
			this.setMethod("history.get");
			this.jsonObject.put("params",subParams);
			response = this.Access();
			//response = this.apiAccess(authKey, subParams);
			
			if(response != null) {
				JSONArray resultObject = response.getJSONArray("result");
				
				int count = resultObject.length();
			    
				for (int i=0; i<count; i++)
				{
					HistoryData historyData = new HistoryData();
					historyData.setUnixtime(resultObject.getJSONObject(i).getString("clock"));
					historyData.setValue(resultObject.getJSONObject(i).getString("value"));
					
					Log.e("Clock",historyData.getUnixtime());
					Log.e("VaLue",historyData.getValue());
					//hostList.add(resultObject.getJSONObject(i).getString("host"));
					historyDataList.add(historyData);
				}			
			}
			return historyDataList;
		} catch (JSONException e) {
			e.printStackTrace();
			return historyDataList;
		}

		
		
	}
	
	public ArrayList<Trigger> getTriggerList(String hostId, int limit) throws IOException {

		ArrayList<Trigger> triggerList = new ArrayList<Trigger>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
			JSONArray idarray = new JSONArray();
			idarray.put(hostId);
		//	this.jsonObject.put("method", "trigger.get");
			subParams.put("output","extend");
			subParams.put("hostids", idarray);
			subParams.put("sortfield", "lastchange");
			subParams.put("sortorder", "DESC");
			if ( limit != 0 ){
				subParams.put("limit", limit);
			}
			
			subsubParams.put("value", 1);
			subsubParams.put("status", 0);
			subParams.put("filter", subsubParams);
			this.setMethod("trigger.get");
			this.jsonObject.put("params", subParams);
			response = this.Access();
			
			if(response != null) {
				JSONArray resultObject = response.getJSONArray("result");
				
				int count = resultObject.length();
			    
				for (int i=0; i<count; i++)
				{
					Trigger trigger = new Trigger();
					trigger.setTriggerId(resultObject.getJSONObject(i).getString("triggerid"));
					trigger.setDescription(resultObject.getJSONObject(i).getString("description"));
					trigger.setLastchange(resultObject.getJSONObject(i).getString("lastchange"));
					trigger.setValue(resultObject.getJSONObject(i).getString("value"));
					
					triggerList.add(trigger);
				}			
			}
			return triggerList;
		} catch (JSONException e) {
			e.printStackTrace();
			return triggerList;
		}	
		
	}


}
