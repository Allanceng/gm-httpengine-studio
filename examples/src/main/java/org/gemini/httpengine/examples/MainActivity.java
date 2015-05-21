package org.gemini.httpengine.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.gemini.httpengine.annotation.InjectFactory;
import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;
import org.gemini.httpengine.library.GMHttpResponse;
import org.gemini.httpengine.library.GMHttpService;
import org.gemini.httpengine.library.OnResponseListener;

import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener,OnResponseListener{

    private Button mTestButton;
    private LoginAPI mLoginAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestButton = (Button)findViewById(R.id.test_btn);
        mTestButton.setOnClickListener(this);
        mLoginAPI = new LoginAPI();
    }

    @Override
    public void onClick(View v) {
        if(v == mTestButton) {
            AwesomeAPI api = InjectFactory.inject(AwesomeAPI.class);
            api.doSomethingAwesome(this, "papatuo", "nickname");
        }
    }

    @Override
    public void onResponse(GMHttpResponse response, GMHttpRequest request) {
        byte[] result = null;
        try {
            result = response.getRawData();
            Map<String, String> cookies = response.getCookies();
        } catch (Exception e) {
            Log.e("error", "wtf?", e);
        }

//        Toast.makeText(this,result,Toast.LENGTH_LONG).show();
    }
}
