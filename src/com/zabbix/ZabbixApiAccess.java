package com.zabbix;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

public class ZabbixApiAccess {
	
	private static final String ZABBIX_API_PATH = "/zabbix/api_jsonrpc.php";
	private static final String CONTENT_TYPE = "application/json-rpc";
	private String uri;
	private String host;
	private HttpPost httpPost;
	private JSONObject jsonObject = new JSONObject();
	
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
	
	public String makeUri(String host)
	{
		Uri.Builder uriBuilder = new Uri.Builder();
    	uriBuilder.scheme("http");
    	uriBuilder.authority(host);
    	uriBuilder.path(ZABBIX_API_PATH);
    	this.uri = Uri.decode(uriBuilder.build().toString());
    	return this.uri;
	}
	
	public void setHttpPost(String uri)
	{
		httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-type", CONTENT_TYPE);
	}
	
	public void setBasicJSONParams()
	{
		try {
			jsonObject.put("jsonrpc", "2.0");
			jsonObject.put("id", "1");
			
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}	
		
	}
	
	public JSONObject getJsonObject()
	{
		return this.jsonObject;
	}
	
	public void setMethod(String method)
	{
		this.setBasicJSONParams();
		try {
			jsonObject.put("method", method);
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public String getMethod()
	{
		try {
			return this.jsonObject.getString("method");
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "error";
		}
	}
	
	
	public String zabbixAuthenticate(String account_name, String pass)
	{
		JSONObject params = new JSONObject();
		try {
			params.put("user", account_name);
			params.put("password", pass);
			jsonObject.put("params", params);
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		StringEntity stringEntity = null ;
		try {
			stringEntity = new StringEntity(jsonObject.toString());
			try {
				Log.e("APIcheck",EntityUtils.toString(stringEntity));
			} catch (ParseException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		httpPost.setEntity(stringEntity);
		DefaultHttpClient httpClient = new DefaultHttpClient();
    	try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonEntity = new JSONObject(entity);
				return jsonEntity.getString("result");
				
			}else
			{
				return "error";
			}
			//return EntityUtils.toString(httpResponse.getEntity());
    	} catch (ClientProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "error";
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "error";
		}
		
	}
	
	private JSONObject apiAccess(String authKey, JSONObject params)
	{
		JSONObject jsonEntity = null;
		try {
			jsonObject.put("jsonrpc", "2.0");
			jsonObject.put("params", params);
			jsonObject.put("auth", authKey);
			jsonObject.put("id", "1");
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			httpPost.setEntity(stringEntity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.e("test",authKey);
			if(authKey == "error") {
				return jsonEntity;
			}
			Log.e("Req",EntityUtils.toString(httpPost.getEntity()));
			HttpResponse httpResponse = httpClient.execute(httpPost);
	//		Log.e("httpRes",Integer.toString(httpResponse.getStatusLine().getStatusCode()));
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpResponse.getEntity());
				Log.e("Response", entity);
				jsonEntity = new JSONObject(entity);
				return jsonEntity;
				
			}else
			{
				return jsonEntity;
			}
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return jsonEntity;
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return jsonEntity;
		} catch (ClientProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return jsonEntity;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return jsonEntity;
		}
		
	}
	public ArrayList<Host> getHostList(String authKey, String filter)
	{
		ArrayList<Host> hostList = new ArrayList<Host>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		
		try {
			this.jsonObject.put("method", "host.get");
			subParams.put("extendoutput", "true");
			if ( filter != "all" )
			{
				subsubParams.put("host", "[\""+filter+"\"]");
				subParams.put("filter", subsubParams);
			}
			response = this.apiAccess(authKey, subParams);
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
					hostList.add(host);
				}			
				return hostList;
			}
			return null;
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return hostList;
		}
		
	}
	public ArrayList<Item> getItemList(String authKey, String hostid, ArrayList<String> itemIdList, int num) {
		
		ArrayList<Item> itemList = new ArrayList<Item>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
			this.jsonObject.put("method", "item.get");
			subParams.put("output","extend");
			subsubParams.put("hostids", itemIdList);
			//subsubParams.put("hostid",hostid);
			subParams.put("filter", subsubParams);
			subParams.put("limit", num);
			
			response = this.apiAccess(authKey, subParams);
			
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
					//item.setItemValue(this.getItemValue(authKey,item));
					//hostList.add(resultObject.getJSONObject(i).getString("host"));
					itemList.add(item);
				}			
			}
			return itemList;
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return itemList;
		}
		
		
	}
	
	public ArrayList<String> getItemIdList(String authKey, String hostid) {
		
		ArrayList<String> itemIdList = new ArrayList<String>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
			this.jsonObject.put("method", "item.get");
			subParams.put("output","shorten");
			subsubParams.put("hostid",hostid);
			subParams.put("filter", subsubParams);
			
			response = this.apiAccess(authKey, subParams);
			
			if(response != null) {
				JSONArray resultObject = response.getJSONArray("result");
				
				int count = resultObject.length();
			
				for (int i=0; i<count; i++)
				{
					String itemid = null;
					itemIdList.add(resultObject.getJSONObject(i).getString("itemid"));
				}			
			}
			return itemIdList;
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return itemIdList;
		}
		
		
	}
	
	public ArrayList<HistoryData> getHistoryData(String authKey, Item item, TimeRange timerange ) {

		ArrayList<HistoryData> historyDataList = new ArrayList<HistoryData>();
		
		JSONObject subParams = new JSONObject();
		JSONObject subsubParams = new JSONObject();
		JSONObject response = null;
		
		try {
			this.jsonObject.put("method", "history.get");
			subParams.put("output","extend");
			subParams.put("itemids", item.getItemId());
			subParams.put("time_from", timerange.getTimeFrom());
			subParams.put("time_till", timerange.getTimeTill());
			subParams.put("history", item.getItemValueType());
			response = this.apiAccess(authKey, subParams);
			
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
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return historyDataList;
		}

		
		
	}


}
