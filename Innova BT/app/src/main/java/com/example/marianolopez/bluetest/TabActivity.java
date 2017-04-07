package com.example.marianolopez.bluetest;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marianolopez.bluetest.util.AdapterZ;
import com.example.marianolopez.bluetest.util.ViewHolderZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by MarianoLopez on 5/4/2017.
 */

public class TabActivity extends AppCompatActivity implements ViewHolderZ {
    private int REQUEST_ENABLE_BT = 1;//intent callback code
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice aux;

    private Vibrator v;//no es lo que pensas..
    private TabHost host;

    //tab 1
    private ImageButton foward,left,rigth,back,stop,auto,linea;
    private SeekBar velocidad;

    //tab 2
    private SeekBar rotacion, principal, secundario, terminal;
    private int rotacion_min = 0;
    private int principal_min = 30;
    private int secundario_min = 50;
    private int terminal_min = 30;

    //tab 3
    private Button send;
    private ConnectBT bt;
    private EditText bt_send;
    private boolean socket_status = false;
    private ListView lv;
    private ArrayList<Object> devices_sync;//bt devices listview
    private int list_index_selected = 0;//listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);
        setup();
    }
    //init
    private void setup(){
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        lv = (ListView)findViewById(R.id.bt_list);
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        host.addTab(newTab(host,"smartcar",R.id.tab1));
        host.addTab(newTab(host,"arm",R.id.tab2));
        host.addTab(newTab(host,"bluetooth",R.id.tab3));
        setupTab1();
        setupTab2();
        setupTab3();
        getSyncDevices();//BT
        host.setCurrentTab(2);//blue
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_index_selected = position;
                aux =  (BluetoothDevice)devices_sync.get(position);
                openContextMenu(lv);
            }
        });
        // Register the ListView  for Context menu
        registerForContextMenu(lv);
    }

    //link
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
    private void setupTab2(){
        rotacion = (SeekBar)findViewById(R.id.rotacion);
        principal = (SeekBar)findViewById(R.id.principal);
        secundario = (SeekBar)findViewById(R.id.secundario);
        terminal = (SeekBar)findViewById(R.id.terminal);
        rotacion.setProgress(rotacion_min);
        principal.setProgress(principal_min);
        secundario.setProgress(secundario_min);
        terminal.setProgress(terminal_min);
        initEvents2();
    }
    private void setupTab3(){
        send = (Button)findViewById(R.id.enviar);
        bt_send = (EditText)findViewById(R.id.mensaje);
        initEvents3();
    }
    //eventos
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
        foward.setOnClickListener(new View.OnClickListener() {
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
    private void initEvents2(){
        rotacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendToServo(rotacion,progress,rotacion_min,"rotacion="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        principal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendToServo(principal,progress,principal_min,"principal="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        secundario.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendToServo(secundario,progress,secundario_min,"secundario="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        terminal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Servo","change");
                sendToServo(terminal,progress,terminal_min,"terminal="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void initEvents3(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket_status){
                    try {
                        bt.write(bt_send.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        showShortToast(e.toString());
                    }
                }
            }
        });
    }
    //enviar mensaje al socket
    private void sendAction(String go){if(socket_status){try {v.vibrate(100);bt.write(go);} catch (IOException e) {showShortToast(e.toString());}}else{showShortToast("No hay bluetooth activado");}}
    //mensajes servo
    private void sendToServo(SeekBar servo, int progress, int min, String action){
        if(progress>=min){
            //sendAction(action);
            showShortToast(""+progress);
        }else{
            progress+=(min-progress);
            servo.setProgress(progress);
        }
    }

    private void disconnectBT(){if(bt!=null){bt.cancel();}}
    private void connectBT(){
        disconnectBT();
        final ProgressDialog pd = new ProgressDialog(TabActivity.this);
        pd.setTitle("Bluetooth");
        pd.setMessage("Conectando a: "+aux.getName()+" - "+aux.getAddress());
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(aux!=null){
                        bt = new ConnectBT(TabActivity.this,aux);
                        socket_status = bt.connect();
                        if(socket_status){
                            TabActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    send.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            send.setEnabled(true);
                                            showShortToast("Conectado");
                                            host.setCurrentTab(0);//car
                                        }
                                    });
                                }
                            });
                        }
                    }
                }catch (final Exception e){
                    Log.e("Error on thread",e.toString());
                    TabActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            send.post(new Runnable() {
                                @Override
                                public void run() {
                                    showShortToast(e.toString());
                                    send.setEnabled(false);
                                }
                            });
                        }
                    });
                }finally {
                    pd.dismiss();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socket_status){bt.cancel();}
    }

    //para mensajes
    private void showShortToast(String mensaje){Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();}

    //para pestañas nuevas
    private TabHost.TabSpec newTab(TabHost host,String name, int resource){
        TabHost.TabSpec spec = host.newTabSpec(name);
        spec.setContent(resource);
        spec.setIndicator(name);
        return spec;
    }


    //bluetooth dispositivos sincronizados + intent check
    private void getSyncDevices(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showShortToast("El dispositivo no admite Bluetooth");
        }else{
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
            }else{
                Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
                if(devices != null){
                    devices_sync = new ArrayList<>();
                    for (BluetoothDevice device : devices){
                        devices_sync.add(device);
                        Log.i("Device sync",device.getName()+" - "+device.getAddress());
                    }
                    refreshLV();
                }
            }
        }
    }
    //actializar listview
    private void refreshLV(){
        if(devices_sync!=null){
            lv.setAdapter(new AdapterZ(this,devices_sync,R.layout.bt_list_item,this));//fill
            lv.invalidateViews();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Acción");
        menu.add(0, v.getId(), 0, "Conectar");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Desconectar");
    }
    //cambiar color del item seleccionado
    private void setSelectedItemColor(){
            for(int i=0;i<devices_sync.size();i++){
                if(i==list_index_selected){
                    lv.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    lv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Conectar"){
            connectBT();
            setSelectedItemColor();
        }
        else if(item.getTitle()=="Desconectar"){
            disconnectBT();
            lv.getChildAt(list_index_selected).setBackgroundColor(Color.TRANSPARENT);
            send.setEnabled(false);
        }else{
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            getSyncDevices();
        }
    }


    @Override
    public void fill(View item, ArrayList coleccion, int position) {
        try{
            TextView nombre = (TextView) item.findViewById(R.id.nombre);
            TextView mac = (TextView) item.findViewById(R.id.mac);
            BluetoothDevice device =  (BluetoothDevice)coleccion.get(position);
            nombre.setText(device.getName());
            mac.setText(device.getAddress());
        }catch(NullPointerException e){
            Log.e("fill error",e.toString());
        }
    }
}
