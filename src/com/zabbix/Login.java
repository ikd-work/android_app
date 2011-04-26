package com.zabbix;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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

public class Login extends Activity {
	
	private static final int SHOW_EDITOR = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.main);
        getWindow().setFeatureDrawableResource( Window.FEATURE_LEFT_ICON, R.drawable.zabbix );
        setTitle(R.string.title_login);
        Button button = (Button)findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener(){
        	
        	public void onClick(View v) {
        		Intent intent = new Intent(Login.this, SuccessActivity.class);
        		EditText hostText = (EditText)findViewById(R.id.Hostname);
        		EditText editText = (EditText)findViewById(R.id.account_name);
        		EditText passText = (EditText)findViewById(R.id.password);
        		CharSequence host = hostText.getText();
        		CharSequence account_name = editText.getText();
        		CharSequence pass = passText.getText();
        		String auth_key = null;
				try {
					auth_key = getAuthKey(host, account_name, pass);
				} catch (UnsupportedEncodingException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
        		intent.putExtra("NAME", auth_key);
        		intent.putExtra("PASS", pass);
        		startActivityForResult(intent, SHOW_EDITOR);
        	}
        });
        
    }
    
    private String getAuthKey(CharSequence host, CharSequence account_name, CharSequence pass) throws UnsupportedEncodingException {
    	Uri.Builder uriBuilder = new Uri.Builder();
    	uriBuilder.scheme("http");
    	uriBuilder.authority(host.toString());
    	uriBuilder.path("/zabbix/api_jsonrpc.php");
    	//uriBuilder.appendQueryParameter("auth", null);
    	//uriBuilder.appendQueryParameter("method", "user.authenticate");
    	//uriBuilder.appendQueryParameter("jsonrpc", "2.0");
    	//uriBuilder.appendQueryParameter("params.user", "api");
    	//uriBuilder.appendQueryParameter("params.password", "zabbix");
    	String uri = Uri.decode(uriBuilder.build().toString());
    	
    	HttpPost httpPost = new HttpPost(uri);
       	httpPost.setHeader("Content-type", "application/json-rpc");
       	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
       	nameValuePair.add(new BasicNameValuePair("id", "1"));
       	nameValuePair.add(new BasicNameValuePair("method", "user.authenticate"));
       	nameValuePair.add(new BasicNameValuePair("params","{\"user\":\"api\",\"password\":\"zabbix\"}"));
       	
       	BasicNameValuePair test = new BasicNameValuePair("test","hoge");
       	
//       	nameValuePair.add(new BasicNameValuePair("params.password","zabbix"));
       	nameValuePair.add(new BasicNameValuePair("jsonrpc","2.0"));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        Log.e("TEST",httpPost.getEntity().toString());
        
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			return httpResponse.toString();
    	} catch (ClientProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "hogehoge";
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "hoge";
		}
		
    	
    	
    }
    
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