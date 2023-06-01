package com.example.nativewebviewcommunication;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText number1EditText;
    private EditText number2EditText;
    private Button sendNumbersButton;
    private TextView resultTextView;

    private boolean isRestarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        number1EditText = findViewById(R.id.number1EditText);
        number2EditText = findViewById(R.id.number2EditText);
        sendNumbersButton = findViewById(R.id.sendNumbersButton);
        resultTextView = findViewById(R.id.resultTextView);

        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "AndroidInterface");
        webView.loadUrl("file:///android_asset/index.html");

        sendNumbersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number1String = number1EditText.getText().toString();
                String number2String = number2EditText.getText().toString();
                String javascriptCode = "javascript:receiveNumbers(" + number1String + ", " + number2String + ")";
                webView.evaluateJavascript(javascriptCode, null);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestarted = true;
    }

    private void clearNativeEntries() {
        if (isRestarted) {
            number1EditText.setText("");
            number2EditText.setText("");
            resultTextView.setText("");
            isRestarted = false; // Reset the flag
        }
    }


    @JavascriptInterface
    public void calculateSum(final int number1, final int number2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int sum = number1 + number2;
                String javascriptCode = "javascript:displaySum(" + sum + ")";
                webView.evaluateJavascript(javascriptCode, null);
            }
        });
    }

    @JavascriptInterface
    public void sendResultToNative(final int result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText("Result from Web: " + result);
            }
        });
    }
}




