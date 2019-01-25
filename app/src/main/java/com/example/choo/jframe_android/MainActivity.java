package com.example.choo.jframe_android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    // Debugging
    private static final String TAG = "Main";

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int DEVICEINFO = 3;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };
    // Layout
    private Button btn_Connect;
    private BluetoothService btService = null;
    private ListViewAdapter listViewAdapter = null;
    private ListView deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        setContentView(R.layout.activity_bluetooth);

        // Main Layout
        btn_Connect = (Button) findViewById(R.id.btn_connect);
//        txt_Result = (TextView) findViewById(R.id.txt_result);
        deviceList = (ListView) findViewById(R.id.frameListView);

        btn_Connect.setOnClickListener(this);

        // BluetoothService Ŭ���� ����
        if (btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

        listViewAdapter = new ListViewAdapter(this);
        deviceList.setAdapter(listViewAdapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String info = view.toString();
                String info = listViewAdapter.getList(position);
                String address = info.substring(info.length() - 17);
                Intent intent = new Intent(getApplicationContext(), ImageGridViewActivity.class);
                intent.putExtra("address", address);
                startActivity(intent);
//                finish();
            }
        });
        deviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int selectedPosition = position;
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                String selectedItem = listViewAdapter.getList(position);
                alertDlg.setTitle(selectedItem.substring(0,selectedItem.length()-17)+"을 삭제하시겠습니까?");
                // click yes
                alertDlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listViewAdapter.remove(selectedPosition);
                        listViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                // click no
                alertDlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
//                alertDlg.setMessage(deviceList.getTextFilter().subSequence(0, 17));
                alertDlg.show();
                return true; // if false, it occur onclick
            }
        });
        loadDeviceInformation();
    }

    @Override
    public void onClick(View v) {
        if (btService.getDeviceState()) {
            btService.enableBluetooth();
        } else {
            finish();
        }
    }

    public void addDeviceList(String device){
        listViewAdapter.add(device);
        listViewAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    btService.getDeviceInfo(data);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Next Step
                    btService.scanDevice();
                } else {

                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
        if(resultCode == DEVICEINFO){
            String deviceinfomation = data.getStringExtra("device_info");
//            String deviceaddress = data.getStringExtra("device_address");
            addDeviceList(deviceinfomation);
//            listViewAdapter.addAddress(deviceaddress);
            Log.d("choo","address = "+deviceinfomation);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveDeviceInformation();
    }

    public void saveDeviceInformation(){
        SharedPreferences sp = getSharedPreferences("JFrame", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sp.edit();
        mEditor.clear();
        mEditor.putInt("Status_size", listViewAdapter.getCount()); // save count of devices
        for(int i=0; i<listViewAdapter.getCount(); i++){
            mEditor.remove("Status_"+i);
            mEditor.putString("Status_"+i, listViewAdapter.getList(i));
        }
        mEditor.commit();
    }

    public void loadDeviceInformation(){
        SharedPreferences sp = getSharedPreferences("JFrame", MODE_PRIVATE);
        int size = sp.getInt("Status_size", 0);
        for(int i=0; i<size; i++){
            listViewAdapter.setDeviceList(sp.getString("Status_"+i, null));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
