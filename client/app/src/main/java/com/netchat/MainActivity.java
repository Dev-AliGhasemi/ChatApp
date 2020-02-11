package com.netchat;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSend;
    EditText edtTextSend;
    LinearLayout linearLayoutChat;
    ScrollView scrollView;
    LinearLayout.LayoutParams layoutParams;
    TextView textViewChat;
    private Socket socket(){
        Socket socket = null;
        try {
            socket = IO.socket("http://192.168.43.64:8000");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return socket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        socket().connect();
        socket().on("msg", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewChat = new TextView(MainActivity.this);
                        textViewChat.setText(args[0].toString());
                        textViewChat.setTextSize(18);
                        textViewChat.setTypeface(Typeface.DEFAULT_BOLD);
                        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                        textViewChat.setLayoutParams(layoutParams);
                        linearLayoutChat.addView(textViewChat);
                        scrollView.isSmoothScrollingEnabled();
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                });
            }
        });
    }

    private void init() {
        btnSend = findViewById(R.id.btn_send);
        edtTextSend = findViewById(R.id.edt_text_send);
        linearLayoutChat = findViewById(R.id.linear_chat);
        scrollView = findViewById(R.id.scroll_view);
    }

    @Override
    public void onClick(View view) {
        socket().emit("msg" , edtTextSend.getText().toString().trim());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket().disconnect();
    }
}
