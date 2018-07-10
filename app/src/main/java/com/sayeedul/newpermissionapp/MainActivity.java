package com.sayeedul.newpermissionapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ToggleButton torch, wifi;
    ImageButton data, info;

    TextView vartext;
    String Info;
    String strPhoneType;
    static final int PERMISSION_READ_STATE = 123;

    WifiManager wifiManager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        torch = (ToggleButton) findViewById(R.id.torchBTN);
        wifi = (ToggleButton) findViewById(R.id.wifiBTN);
        data = (ImageButton) findViewById(R.id.dataBTN);
        info = (ImageButton) findViewById(R.id.infoBTN);

        final boolean camera_flash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);

        final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        torch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (camera_flash == true) {
                    try {

                        String cameraID = cameraManager.getCameraIdList()[0];

                        if (torch.isChecked()) {
                            cameraManager.setTorchMode(cameraID, true);
                        } else {
                            cameraManager.setTorchMode(cameraID, false);
                        }

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "NO FLASHLIGHT AVAILABLE !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wifi.isChecked()) {
                    wifiManager.setWifiEnabled(true);
                } else {
                    wifiManager.setWifiEnabled(false);
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    MyTelephonyManager();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_READ_STATE:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyTelephonyManager();
                } else {
                    Toast.makeText(this, "YOU DONT HAVE REQUIRED PERMISSION.", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void MyTelephonyManager() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int phonetype = telephonyManager.getPhoneType();

        switch (phonetype) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                strPhoneType = " CDMA ";
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                strPhoneType = " GSM ";
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                strPhoneType = " NONE ";
                break;
        }

        boolean isRoaming = telephonyManager.isNetworkRoaming();

        String PhoneType = strPhoneType;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String IMEI1 = telephonyManager.getImei(1);
        String IMEI2 = telephonyManager.getImei(0);
        // String SubscriberID = telephonyManager.getSubscriberId();
        // String SimSerial = telephonyManager.getSimSerialNumber();
        String simOperator = telephonyManager.getSimOperatorName();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String simNumber = telephonyManager.getLine1Number();
        String voiceMail = telephonyManager.getVoiceMailNumber();
        String networkCountryIso = telephonyManager.getNetworkCountryIso();
        String simCountryIso = telephonyManager.getSimCountryIso();

        Info = " PHONE DETAILS : \n ";

        Info += "\n Phone Network type = "+PhoneType;
        Info += "\n IMEI 1 = "+IMEI1;
        Info += "\n IMEI 2 = "+IMEI2;
        Info += "\n SIM OPERATOR NAME = "+simOperator;
        Info += "\n SIM NUMBER = "+simNumber;
        Info += "\n VoiceMail NUMBER = "+voiceMail;
        Info += "\n Network Country ISO = "+networkCountryIso;
        Info += "\n SIM Country ISO = "+simCountryIso;
        Info += "\n IN ROAMING = "+isRoaming;

        vartext = (TextView)findViewById(R.id.idTxtView);
        vartext.setText(Info);
    }


}



