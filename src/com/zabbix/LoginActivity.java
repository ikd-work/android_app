package com.zabbix;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	private static final int SHOW_EDITOR = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.login);
        getWindow().setFeatureDrawableResource( Window.FEATURE_LEFT_ICON, R.drawable.zabbix );
        setTitle(R.string.title_login);
        Button button = (Button)findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener(){
        	
        	public void onClick(View v) {
        		Intent intent = new Intent(LoginActivity.this, HostListActivity.class);
        		EditText hostText = (EditText)findViewById(R.id.Hostname);
        		EditText editText = (EditText)findViewById(R.id.account_name);
        		EditText passText = (EditText)findViewById(R.id.password);
        		CharSequence host = hostText.getText();
        		CharSequence account_name = editText.getText();
        		CharSequence pass = passText.getText();
        		ZabbixApiAccess zabbix = new ZabbixApiAccess();
        		zabbix.setHost(host.toString());
        		String uri = zabbix.makeUri(host.toString());
        	//	zabbix.setUri(uri);
        		zabbix.setHttpPost(uri);
        		zabbix.setBasicJSONParams();
        		zabbix.setMethod("user.authenticate");
        		Log.e("ZABBIXAPIURI",zabbix.getUri());
        		Log.e("ZABBIXAPIMETHOD",zabbix.getMethod());
        		Log.e("ZABBIXAPIJSON",zabbix.getJsonObject().toString());
        		String auth_key = zabbix.zabbixAuthenticate(account_name.toString(), pass.toString());
        		
        		
        	//	String auth_key = null;
			/*	try {
					auth_key = getAuthKey(host, account_name, pass);
				} catch (UnsupportedEncodingException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				*/
        		intent.putExtra("AUTH_KEY", auth_key);
        		intent.putExtra("PASS", pass);
        		intent.putExtra("HOSTNAME", host);
        		startActivityForResult(intent, SHOW_EDITOR);
        	}
        });
        
    }
    
    
     /**
     * @param host
     * @param account_name
     * @param pass
     * @return
     * @throws UnsupportedEncodingException
     */
 /*   private String getAuthKey(CharSequence host, CharSequence account_name, CharSequence pass) throws UnsupportedEncodingException {
    	Uri.Builder uriBuilder = new Uri.Builder();
    	uriBuilder.scheme("http");
    	uriBuilder.authority(host.toString());
    	uriBuilder.path("/zabbix/api_jsonrpc.php");
 
    	String uri = Uri.decode(uriBuilder.build().toString());
    	
    	HttpPost httpPost = new HttpPost(uri);
       	httpPost.setHeader("Content-type", "application/json-rpc");
 
       	JSONObject jsonObj = new JSONObject();
       	
       	
       	try {
       		jsonObj.put("jsonrpc", "2.0");
			jsonObj.put("id", "1");
			jsonObj.put("method", "user.authenticate");
			JSONObject params = new JSONObject();
			params.put("user", account_name);
			params.put("password", pass);
			jsonObj.put("params", params);
			Log.e("jsonObj",jsonObj.toString());
		} catch (JSONException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		StringEntity stringEntity = new StringEntity(jsonObj.toString());
		httpPost.setEntity(stringEntity);
		
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpResponse.getEntity());
				Log.e("Response", entity);
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
    	
    }*/
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == SHOW_EDITOR) {
    		if(resultCode == RESULT_OK) {
    			TextView textView = (TextView)findViewById(R.id.account_name);
    			textView.setText(data.getCharSequenceExtra("NAME"));
    		}
    	}
    }
}