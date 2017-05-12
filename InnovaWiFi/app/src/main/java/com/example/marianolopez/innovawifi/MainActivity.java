package com.example.marianolopez.innovawifi;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageButton foward,left,rigth,back,stop,auto,linea;
    private SeekBar velocidad;
    private Vibrator v;
    private TabHost host;
    private VolleyCall volley;
    private Map<String, String> params;
    private EditText url;
    //private String url = "http://172.16.0.187:8080/?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        host.addTab(newTab(host,"smartcar",R.id.tab1));
        host.addTab(newTab(host,"config",R.id.tab2));
        this.volley = new VolleyCall(this);
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        setupTab1();
        url = (EditText) findViewById(R.id.ip);
    }

    private void setupTab1(){
        foward = (ImageButton)findViewById(R.id.go_foward);
        left = (ImageButton)findViewById(R.id.go_left);
        rigth = (ImageButton)findViewById(R.id.go_rigth);
        back = (ImageButton)findViewById(R.id.go_back);
        stop = (ImageButton)findViewById(R.id.stop);
        auto = (ImageButton)findViewById(R.id.automatico);
        linea = (ImageButton)findViewById(R.id.linea);
        velocidad = (SeekBar)findViewById(R.id.velocidad);
        initEvents1();
    }

    private void initEvents1(){
        velocidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendAction("speed="+progress);
                showShortToast("Speed: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /*foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("f");
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("l");
            }
        });
        rigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("r");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("b");
            }
        });*/
        foward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendAction("f");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    sendAction("s");
                }
                return true;
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendAction("l");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    sendAction("s");
                }
                return true;
            }
        });
        rigth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendAction("r");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    sendAction("s");
                }
                return true;
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendAction("b");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    sendAction("s");
                }
                return true;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("s");
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("piloto");
            }
        });
        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("linea");
            }
        });
    }

    private void sendAction(String go){
        v.vibrate(100);
        /*params = new HashMap<String, String>();
        params.put("accion",go);*/
        volley.call(url.getText().toString()+"/?accion="+go, Request.Method.GET, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {showShortToast(error.toString());}
        });

    }

    private void showShortToast(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    //para pestañas nuevas
    private TabHost.TabSpec newTab(TabHost host,String name, int resource){
        TabHost.TabSpec spec = host.newTabSpec(name);
        spec.setContent(resource);
        spec.setIndicator(name);
        return spec;
    }
}
