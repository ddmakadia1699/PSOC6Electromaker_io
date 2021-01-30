package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {

    static String MQTTHOST = "tcp://tailor.cloudmqtt.com:13582";
    static String USERNAME = "ffbqwwfa";
    static String PASSWORD = "PAC8F72zynig";

    MqttAndroidClient client;

    TextView subTextD1,subTextD2;

    String Status,pubString="PSOC6",subString="PSCO6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subTextD1 = (TextView)findViewById(R.id.statusD1);
        subTextD2 = (TextView)findViewById(R.id.statusD2);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        MqttConnectOptions options = new MqttConnectOptions();

        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Not Connected",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                subTextD1.setText(new String(message.getPayload()));
                Status = new String(message.getPayload());
                if(topic.equals("cap1")) {
                    if (Status.equals("TURNON")) {
                        subTextD1.setText("Switch_1 On");
                    }
                    if (Status.equals("TURNOFF")) {
                        subTextD1.setText("Switch_1 Off");
                    }
                }
                else if(topic.equals("cap2")) {
                    if (Status.equals("TURNON")) {
                        subTextD1.setText("Switch_2 On");
                    }
                    if (Status.equals("TURNOFF")) {
                        subTextD1.setText("Switch_2 Off");
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void d1_on(View v){
        String topic = "cap1";
        String message = "TURNON";
        try {
            client.publish(topic, message.getBytes(),0,false);
            subTextD1.setText("Switch_1 On");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void d1_off(View v){
        String topic = "cap1";
        String message = "TURNOFF";
        try {
            client.publish(topic, message.getBytes(),0,false);
            subTextD1.setText("Switch_1 Off");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void d2_on(View v){
        String topic = "cap2";
        String message = "TURNON";
        try {
            client.publish(topic, message.getBytes(),0,false);
            subTextD2.setText("Switch_2 On");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void d2_off(View v){
        String topic = "cap2";
        String message = "TURNOFF";
        try {
            client.publish(topic, message.getBytes(),0,false);
            subTextD2.setText("Switch_2 Off");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSubscription(){
        try{
            client.subscribe(subString,0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
