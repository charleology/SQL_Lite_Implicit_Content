package com.example.sql_lite_implicit_content;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Records extends AppCompatActivity {

    DbHelper myDb;
    String[] id;
    String[] lname;
    String[] fname;
    String[] minit;
    String[] email;
    String[] contact;
    ListView lvRecord;
    View myView;
    Dialog dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        myDb = new DbHelper(Records.this);
        getData();
        myView = getLayoutInflater().inflate(R.layout.editlayout, null);
        AlertDialog.Builder theBuild = new AlertDialog.Builder(Records.this);
        theBuild.setView(myView);
        dia = theBuild.create();

        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a = adapterView.getItemAtPosition(i).toString();
                displayData(a);
            }
        });
    }

    public void getData() {
        lvRecord = (ListView) findViewById(R.id.lv_rec);

        Cursor res = myDb.getRec("0");
        if (res.getCount() == 0) {
            Toast.makeText(Records.this, "No records found.", Toast.LENGTH_LONG).show();
            id = new String[]{"No records found."};
            lname = new String[]{"No records found."};
            fname = new String[]{"No records found."};
            minit = new String[]{"No records found."};
            email = new String[]{"No records found."};
            contact = new String[]{"No records found."};
        } else {
            id = new String[res.getCount()];
            lname = new String[res.getCount()];
            fname = new String[res.getCount()];
            minit = new String[res.getCount()];
            email = new String[res.getCount()];
            contact = new String[res.getCount()];

            int ctr = 0;
            while (res.moveToNext()) {
                id[ctr] = res.getString(0);
                lname[ctr] = res.getString(2);
                fname[ctr] = res.getString(1);
                minit[ctr] = res.getString(3);
                email[ctr] = res.getString(4);
                contact[ctr] = res.getString(5);
                ctr++;
            }
        }

        CustomAdapter myAdapter = new CustomAdapter(Records.this, id, lname, fname, minit, email, contact);
        lvRecord.setAdapter(myAdapter);
    }

    public void displayData(String id) {
        final String getID = id;
        Cursor res = myDb.getRec(id);
        final EditText ln = (EditText) myView.findViewById(R.id.etLname_edit);
        final EditText fn = (EditText) myView.findViewById(R.id.etFname_edit);
        final EditText mi = (EditText) myView.findViewById(R.id.etMinit_edit);
        final EditText em = (EditText) myView.findViewById(R.id.etEmail_edit);
        final EditText cont = (EditText) myView.findViewById(R.id.etContact_edit);

        Button btnSave = (Button) myView.findViewById(R.id.btnUpRec_edit);
        Button btnDelete = (Button) myView.findViewById(R.id.btnDelRec_edit);

        if (res.getCount() == 0) {
            Toast.makeText(this, "No records found.", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                ln.setText(res.getString(1));
                fn.setText(res.getString(2));
                mi.setText(res.getString(3));
                em.setText(res.getString(4));
                cont.setText(res.getString(5));
            }
            dia.show();

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String last = ln.getText().toString();
                    String first = fn.getText().toString();
                    String middle = mi.getText().toString();
                    String email = em.getText().toString();
                    String contact = cont.getText().toString();

                    Boolean res = myDb.updateRec(getID, last, first, middle, email, contact);
                    if (res == true) {
                        getData();
                        dia.dismiss();
                        Toast.makeText(Records.this, "Record Updated!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Records.this, "Update Error!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dia.dismiss();
                    showDialogDelete(getID);
                }
            });
        }
    }
    public void showDialogDelete(String id){
        final String getID = id;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == DialogInterface.BUTTON_POSITIVE){
                    Boolean res = myDb.deleteRec(getID);
                    if(res == true){
                        dia.dismiss();
                        getData();
                        Toast.makeText(Records.this, "Record Deleted!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(Records.this, "Deletion Error!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(i == DialogInterface.BUTTON_NEGATIVE){
                    dia.dismiss();
                }
            }
        };

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setCancelable(true);
        build.setTitle("DELETE RECORD");
        build.setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener).show();
    }
}