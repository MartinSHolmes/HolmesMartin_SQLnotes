package com.example.mycontactapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName;
    EditText editNumber;
    EditText editAddress;
    public static final String EXTRA_NAME = "com.example.mycontactapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyContactApp", "MainActivity: setting up the layout");
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editNumber = findViewById(R.id.editText_Number);
        editAddress = findViewById(R.id.editText_Address);


        myDb = new DatabaseHelper ( this);
        Log.d( "MyContactApp", "MainActivity: instantiated DatbaseHelper");
    }

    public void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity:  showMessage - building alert dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void addData(View view){
        Cursor curs = myDb.getAllData();
        while (curs.getCount() > 0 && curs.moveToNext()) {
            if (editName.getText().toString().equals(curs.getString(1))
                    && editNumber.getText().toString().equals(curs.getString(2))
                    && editAddress.getText().toString().equals(curs.getString(3))) {
                Toast.makeText(MainActivity.this, "FAILED - Contact already in database", Toast.LENGTH_LONG).show();
                return;
            }
        }

        boolean isInserted = myDb.insertData(editName.getText().toString(),editNumber.getText().toString(),editAddress.getText().toString());

        if (isInserted) {
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }

        else {
            Toast.makeText(MainActivity.this, "contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view){
        Log.d("MyContactApp", "MainActivity: View Contact Button Pressed");
        Cursor res = myDb.getAllData();

        if (res.getCount()==0){
            showMessage("Error","no data found in database");
        }

        if (res.getCount()>0) {
            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                buffer.append("ID: " + res.getString(0) + "\n" +
                        "Name: " + res.getString(1) + "\n" +
                        "Phone number: " + res.getString(2) + "\n" +
                        "Home address: " + res.getString(3) + "\n\n");
            }

            Log.d("MyContactApp", "MainActivity: In viewData - Buffer assembled");
            showMessage("Data", buffer.toString());
        }
    }
    public void searchRecord(View view) {
        Log.d("MyContactApp", "MainActivity: launching search");
        Cursor curs = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        if (editName.getText().toString().isEmpty() && editNumber.getText().toString().isEmpty()
                && editAddress.getText().toString().isEmpty()) {
            showMessage("Error", "No input");
            return;
        }

        while (curs.moveToNext()){
            if ((editName.getText().toString().isEmpty() || editName.getText().toString().equals(curs.getString(1)))
                    && (editNumber.getText().toString().isEmpty() || editNumber.getText().toString().equals(curs.getString(2)))
                    && (editAddress.getText().toString().isEmpty() || editAddress.getText().toString().equals(curs.getString(3))))
            {
                buffer.append("ID: " + curs.getString(0) + "\n" +
                        "Name: " + curs.getString(1) + "\n" +
                        "Phone number: " + curs.getString(2) + "\n" +
                        "Home address: " + curs.getString(3) + "\n\n");
            }
        }
        if (buffer.toString().isEmpty()) {
            showMessage("Error", "No matches");
            return;
        }
        showMessage("Search results", buffer.toString());
    }
}
