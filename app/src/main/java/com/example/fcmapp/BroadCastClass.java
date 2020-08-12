package com.example.fcmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BroadCastClass extends BroadcastReceiver {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onReceive(final Context context, Intent intent) {
            if(intent!=null)
                Toast.makeText(context,"SMS Received",Toast.LENGTH_LONG).show();
            Bundle bundle=intent.getExtras();
            if(bundle!=null)
            {
                Object pdus[]=(Object[])bundle.get("pdus");
                final SmsMessage[] message=new SmsMessage[pdus.length];
                for(int i=0;i<message.length;i++)
                {
                    if (Build.VERSION.SDK_INT >= 23
                    ) {

                        String format=bundle.getString("format");
                        message[i]=SmsMessage.createFromPdu((byte[])pdus[i],format);

                    }
                    String msg[]=message[i].getMessageBody().split("\\n");
                    Toast.makeText(context,String.valueOf(message.length),Toast.LENGTH_LONG).show();
                    HashMap<String,String> map=new HashMap<>();
                    map.put("name",msg[0]);
                    map.put("phone",msg[1]);
                    map.put("rating",msg[2]);
                    db.collection("fpo").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(context,"Success!",Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Failure!",Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
            }
}
