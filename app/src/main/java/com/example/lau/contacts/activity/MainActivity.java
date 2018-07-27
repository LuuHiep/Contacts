package com.example.lau.contacts.activity;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lau.contacts.R;
import com.example.lau.contacts.adapter.ContactAdapter;
import com.example.lau.contacts.database.Database;
import com.example.lau.contacts.model.Contacts;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvContact;
    ArrayList<Contacts> arrayListContact;
    ContactAdapter adapter;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){

        lvContact = (ListView) findViewById(R.id.lv_contacts);
        arrayListContact = new ArrayList<>();
        adapter = new ContactAdapter(this, R.layout.item_contact,arrayListContact);
        lvContact.setAdapter(adapter);

        // Tạo database PhoneBook
        database = new Database(this, "phonebook", null, 1);
        // Tạo bảng Contact
        database.queryData(" CREATE TABLE IF NOT EXISTS Contact(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR (100), PhoneNumber VARCHAR (200))");
        // Insert data
        database.queryData("INSERT INTO Contact VALUES(null, 'Hiệp', '0123456789')");

        getDataContact();

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               showDialogUpdate(arrayListContact.get(i).getName().toString(),arrayListContact.get(i).getPhoneNumber().toString(),arrayListContact.get(i).getId());
            }
        });

    }

    private void getDataContact(){
        // Select data
        Cursor dataContact = database.getData("SELECT * FROM Contact");
        arrayListContact.clear();
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);
            String name = dataContact.getString(1);
            String phoneNumber = dataContact.getString(2);
            arrayListContact.add(new Contacts(id, name, phoneNumber));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            showDialogAdd();
        return super.onOptionsItemSelected(item);
    }

    private void showDialogAdd(){
        final Dialog dialogAdd = new Dialog(this);
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.setContentView(R.layout.dialog_add);
        final EditText etAddName = (EditText) dialogAdd.findViewById(R.id.et_add_name);
        final EditText etAddPhoneNumber = (EditText) dialogAdd.findViewById(R.id.et_add_phone_number);
        Button btAddSave = (Button) dialogAdd.findViewById(R.id.bt_add_save);
        btAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameContact = etAddName.getText().toString();
                String phoneNumberContact = etAddPhoneNumber.getText().toString();
                if (nameContact.equals("") && phoneNumberContact.equals("")){
                    Toast.makeText(MainActivity.this, "You have not entered enough data", Toast.LENGTH_SHORT).show();
                } else {
                    database.queryData("INSERT INTO Contact VALUES(null, '"+nameContact+"', '"+phoneNumberContact+"')");
                    Toast.makeText(MainActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                    dialogAdd.dismiss();
                    getDataContact();
                }
            }
        });

        Button btAddClose = (Button) dialogAdd.findViewById(R.id.bt_add_close);
        btAddClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd.dismiss();
            }
        });
        dialogAdd.show();
    }

    private void showDialogUpdate(String name, String phoneNumber, final int id){

        final Dialog dialogUpdate = new Dialog(MainActivity.this);
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(R.layout.dialog_update);
        Button btUpdateSave = (Button) dialogUpdate.findViewById(R.id.bt_update_save);
        final EditText etUpdateName = (EditText) dialogUpdate.findViewById(R.id.et_update_name);
        final EditText etUpdatePhoneNumber = (EditText)dialogUpdate.findViewById(R.id.et_update_phone_number);
        etUpdateName.setText(name);
        etUpdatePhoneNumber.setText(phoneNumber);

        btUpdateSave.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View view) {
                String updateName = etUpdateName.getText().toString().trim();
                String updatePhoneNumber = etUpdatePhoneNumber.getText().toString().trim();
                database.queryData("UPDATE Contact SET Name = '"+updateName+"', PhoneNumber = '"+updatePhoneNumber+"' WHERE Id = '"+id+"'");
                Toast.makeText(MainActivity.this, "Update Success",Toast.LENGTH_SHORT).show();
                dialogUpdate.dismiss();
                getDataContact();
            }
        });

        Button btUpdateDelete = (Button) dialogUpdate.findViewById(R.id.bt_update_delete);
        btUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.queryData("DELETE FROM Contact WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                dialogUpdate.dismiss();
                getDataContact();
            }
        });

        Button btUpdateClose = (Button) dialogUpdate.findViewById(R.id.bt_update_close);
        btUpdateClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.dismiss();
            }
        });
        dialogUpdate.show();
    }
}
