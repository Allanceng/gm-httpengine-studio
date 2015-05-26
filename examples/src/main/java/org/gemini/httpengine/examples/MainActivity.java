package org.gemini.httpengine.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.gemini.httpengine.annotation.InjectFactory;
import org.gemini.httpengine.library.GMHttpRequest;
import org.gemini.httpengine.library.GMHttpResponse;
import org.gemini.httpengine.library.OnResponseListener;

import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener,OnResponseListener{

    private Button mTestButton;
    private OtherAPI mLoginAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestButton = (Button)findViewById(R.id.test_btn);
        mTestButton.setOnClickListener(this);
        mLoginAPI = new OtherAPI();
    }

    @Override
    public void onClick(View v) {
        if(v == mTestButton) {
            UserAPI api = InjectFactory.inject(UserAPI.class);
            api.login(this, "geminiwen", "password");
        }
    }

    @Override
    public void onResponse(GMHttpResponse response, GMHttpRequest request) {
        byte[] result = null;
        try {
            result = response.getRawData();
            Log.d("result", new String(result));
        } catch (Exception e) {
            Log.e("error", "wtf?", e);
        }

//        Toast.makeText(this,result,Toast.LENGTH_LONG).show();
    }
}
