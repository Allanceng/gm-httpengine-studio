package org.gemini.httpengine.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;
import org.gemini.httpengine.library.GMHttpResponse;
import org.gemini.httpengine.library.GMHttpService;
import org.gemini.httpengine.library.OnResponseListener;


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
            mLoginAPI.uploadImages("02191ae95cee91ef3fa772f2868add5f", this);
        }
    }

    @Override
    public void onResponse(GMHttpResponse response, GMHttpRequest request) {
        String result = null;
        try {
            result = response.parseAsString();
            Log.d("test", result);
        } catch (Exception e) {
            result = "no content";
        }

//        Toast.makeText(this,result,Toast.LENGTH_LONG).show();
    }
}
