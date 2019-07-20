package com.tunglain.atmj02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edUserid;
    private EditText edPassword;
    private CheckBox cbRemember;
    private Intent helloService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.container_news,NewsFragment.getInstance());
        fragmentTransaction.commit();

        findViews();


        //Service
        /*helloService = new Intent(this,HelloService.class);
        helloService.putExtra("NAME","T1");
        startService(helloService);
        helloService.putExtra("NAME","T2");
        startService(helloService);
        helloService.putExtra("NAME","T3");
        startService(helloService);*/

        /*Intent testService = new Intent(this,TestService.class);
        startService(testService);*/
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Hello " + intent.getAction());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(HelloService.ACTION_HELLO_DONE);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void findViews() {
        edUserid = findViewById(R.id.userid);
        edPassword = findViewById(R.id.password);
        cbRemember = findViewById(R.id.cb_rem_userid);

        cbRemember.setChecked(getSharedPreferences("atm",MODE_PRIVATE)
        .getBoolean("REMEMBER_USERID",false));

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getSharedPreferences("atm",MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_USERID",b)
                        .apply();
            }
        });

        String userid = getSharedPreferences("atm",MODE_PRIVATE)
                .getString("USERID",null);
        edUserid.setText(userid);
    }

    public void login(View view) {
        final String userid = edUserid.getText().toString();
        final String password = edPassword.getText().toString();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userid)
                .child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        if (pw.equals(password)) {
                            boolean remember = getSharedPreferences("atm",MODE_PRIVATE)
                                    .getBoolean("REMEMBER_USERID",false);
                            if (remember) {
                                getSharedPreferences("atm", MODE_PRIVATE)
                                        .edit()
                                        .putString("USERID", userid)
                                        .apply();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登入結果")
                                    .setMessage("登入失敗")
                                    .setPositiveButton("OK",null)
                                    .show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
