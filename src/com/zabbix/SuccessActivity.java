package com.zabbix;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SuccessActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        setTitle(R.string.title_success);
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			TextView textView = (TextView)findViewById(R.id.Message);
			TextView passView = (TextView)findViewById(R.id.Password);
			textView.setText(extras.getCharSequence("NAME"));
			passView.setText(extras.getCharSequence("PASS"));
		}
    }

}
