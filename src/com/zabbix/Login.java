package com.zabbix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        		EditText hostText = (EditText)findViewById(R.id.hostname);
        		EditText editText = (EditText)findViewById(R.id.account_name);
        		EditText passText = (EditText)findViewById(R.id.password);
        		CharSequence host = hostText.getText();
        		CharSequence text = editText.getText();
        		CharSequence pass = passText.getText();
        		
        		intent.putExtra("NAME", text);
        		intent.putExtra("PASS", pass);
        		startActivityForResult(intent, SHOW_EDITOR);
        	}
        });
        
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