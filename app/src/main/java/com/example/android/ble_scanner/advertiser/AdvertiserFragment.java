package com.example.android.ble_scanner.advertiser;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.ble_scanner.R;
import com.example.android.ble_scanner.advertiser.Advertiser_BLE;

import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertiserFragment extends Fragment {

    private Advertiser_BLE mAdvertiser;

    private EditText mLocalNameEditText;

    private EditText mServiceUuidEditText;

    private EditText mCharacteristicEditText;

    public AdvertiserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertiser, container, false);

        mLocalNameEditText = view.findViewById(R.id.server_local_name_edit_text);
        mServiceUuidEditText = view.findViewById(R.id.service_uuid_edit_text);
        mCharacteristicEditText = view.findViewById(R.id.characteristic_edit_text);

        // Create a advertiser object.
        mAdvertiser = new Advertiser_BLE(this);

        // When user click advertise button start advertising. When click again, stop advertising
        Button button = view.findViewById(R.id.start_advertising_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAdvertiser.isAdvertising())
                    mAdvertiser.startServer();
            }
        });



        return view;
    }

    public void displayServersInfo(BluetoothGattServer server){

        // Display for first Service
        List<BluetoothGattService> serviceList = server.getServices();
        for (BluetoothGattService service : serviceList){
            List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();

            for (BluetoothGattCharacteristic characteristic : characteristicList){

                String text = composeCharacteristicInfo(characteristic);
                textView.setText(text);
            }
        }


    }

    private String composeCharacteristicInfo(BluetoothGattCharacteristic characteristic){
        UUID uuid = characteristic.getUuid();
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(uuid);
        StringBuilder builder = new StringBuilder();

        if((characteristic.getProperties() & PROPERTY_READ)!=0){
            builder.append("Read ");
        }

        if((characteristic.getProperties() & PROPERTY_WRITE_NO_RESPONSE)!=0){
            builder.append("Write without response ");
        }

        if((characteristic.getProperties() & PROPERTY_NOTIFY)!=0){
            builder.append("Notify ");
        }

        if((characteristic.getProperties() & PROPERTY_WRITE)!=0){
            builder.append("Write ");
        }

        Integer value = characteristic.getIntValue(FORMAT_UINT16,0);

        builder.append("UUID: ").append(uuid.toString()).append("\n");
        builder.append("Properties: ");
        builder.append("Value: ").append(String.valueOf(value));
        builder.append("Descriptor: ").append(descriptor.toString()).append("\n");

        return  builder.toString();
    }

//    public void startAdvertise(){
//        // do sth on UI
//
//        mAdvertiser.startServer();
//        Utils.showToast(getContext(),"Start advertising");
//    }
//
//    public void stopAdvertise(){
//        mAdvertiser.stopAdvertise();
//        Utils.showToast(getContext(),"Stop advertising");
//    }

}