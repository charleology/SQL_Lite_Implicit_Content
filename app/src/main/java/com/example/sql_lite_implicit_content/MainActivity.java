package com.example.sql_lite_implicit_content;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    DbHelper dbHelper;
    EditText ln, fn, mi, em, cn;
    SQLiteDatabase dblite;
    AutoCompleteTextView autoDial, autoEm;
    Button dial,send, search;
    String[] numbers;
    String[] address;
    EditText sub, mess, look;
    String subject, message, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        ln = (EditText) findViewById(R.id.etLname_main);
        fn = (EditText) findViewById(R.id.etFname_main);
        mi = (EditText) findViewById(R.id.etMinit_main);
        em = (EditText) findViewById(R.id.etEmail_main);
        cn = (EditText) findViewById(R.id.etContact_main);
        sub = (EditText) findViewById(R.id.etsubject_main);
        mess = (EditText) findViewById(R.id.etmessage_main);
        look = (EditText) findViewById(R.id.etLook_main);

        autoDial = (AutoCompleteTextView) findViewById(R.id.auto_cn);
        autoEm = (AutoCompleteTextView) findViewById(R.id.auto_em);
        dial = (Button) findViewById(R.id.btnDial);
        send = (Button) findViewById(R.id.btnSend);
        search = (Button) findViewById(R.id.btnSearch);


        //FOR AUTOCOMPLETE DIAL
        autoDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoDial.isPressed()) {
                    Cursor res = dbHelper.getRec("0");
                    if (res.getCount() == 0) {
                        Toast.makeText(MainActivity.this, "No numbers available.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    } else {
                        numbers = new String[res.getCount()];
                        int ctr = 0;
                        while (res.moveToNext()) {
                            numbers[ctr] = res.getString(5);
                            ctr++;
                        }
                    }
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_item, numbers);
                    autoDial.setAdapter(adapt);
                    autoDial.setThreshold(4);
                }
            }
        });

        //FOR AUTOCOMPLETE EMAIL
        autoEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoEm.isPressed()) {
                    Cursor res = dbHelper.getRec("0");
                    if (res.getCount() == 0) {
                        Toast.makeText(MainActivity.this, "No addresses available.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    } else {
                        address = new String[res.getCount()];
                        int ctr = 0;
                        while (res.moveToNext()) {
                            address[ctr] = res.getString(4);
                            ctr++;
                        }
                    }
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_item, address);
                    autoEm.setAdapter(adapt);
                    autoEm.setThreshold(2);
                }
            }
        });

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + autoDial.getText().toString()));
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = mess.getText().toString();
                subject = sub.getText().toString();
                add = autoEm.getText().toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ add});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY,look.getText().toString());
                Intent choose = Intent.createChooser(intent, "Select app...");
                startActivity(choose);
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder theAlert = new AlertDialog.Builder(this);
        theAlert.setCancelable(true);
        theAlert.setTitle(title);
        theAlert.setMessage(message);
        theAlert.show();
    }

    public void viewList(View view) {
        Intent intent = new Intent(this, Records.class);
        startActivity(intent);
    }

    public void addRec(View view) {
        boolean r = dbHelper.insertRec(ln.getText().toString(), fn.getText().toString(), mi.getText().toString(), em.getText().toString(), cn.getText().toString());
        if (r == true) {
            Toast.makeText(this, "Recorded!", Toast.LENGTH_LONG).show();
            ln.setText("");
            fn.setText("");
            mi.setText("");
            em.setText("");
            cn.setText("");
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
        }
    }

    public void viewRec(View view) {
        Cursor res = dbHelper.getRec("0");
        if (res.getCount() == 0) {
            showMessage("ERROR", "Empty Record.");
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                //buffer.append("Customer ID : " +res.getString(0)+"\n");
                buffer.append("Last Name : " + res.getString(2) + "\n");
                buffer.append("First Name : " + res.getString(1) + "\n");
                buffer.append("Middle Initial : " + res.getString(3) + "\n");
                buffer.append("Email : " + res.getString(4) + "\n");
                buffer.append("Contact Number : " + res.getString(5) + "\n\n");
            }
            showMessage("CUSTOMER INFORMATION", buffer.toString());
        }
    }
}
