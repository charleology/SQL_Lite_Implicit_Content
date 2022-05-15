package com.example.sql_lite_implicit_content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends ArrayAdapter<String> {
    Context con;
    String[] id;
    String[] ln;
    String[] fn;
    String[] mi;
    String[] em;
    String[] cnum;
    LayoutInflater inflater;

    public CustomAdapter(Context context, String[] id, String[] lname, String[] fname, String[] minit, String[] email, String[] cnumber){
        super(context,R.layout.rowrecords,id);
        this.con = context;
        this.ln = lname;
        this.fn = fname;
        this.mi = minit;
        this.em = email;
        this.cnum = cnumber;
    }

    public class viewHolder{
        TextView tvlname, tvfname, tvminit, tvemail, tvcnumber;
    }

    @Override
    public View getView(int Position, View convertView, ViewGroup parent){
        if(convertView == null){
            inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowrecords, null);
        }
        viewHolder holder = new viewHolder();
        holder.tvlname = (TextView) convertView.findViewById(R.id.ln_row);
        holder.tvfname = (TextView) convertView.findViewById(R.id.fn_row);
        holder.tvminit = (TextView) convertView.findViewById(R.id.mi_row);
        holder.tvemail = (TextView) convertView.findViewById(R.id.email_row);
        holder.tvcnumber = (TextView) convertView.findViewById(R.id.contact_row);

        holder.tvlname.setText(ln[Position]);
        holder.tvfname.setText(fn[Position]);
        holder.tvminit.setText(mi[Position]);
        holder.tvemail.setText(em[Position]);
        holder.tvcnumber.setText(cnum[Position]);

        return convertView;
    }
}
