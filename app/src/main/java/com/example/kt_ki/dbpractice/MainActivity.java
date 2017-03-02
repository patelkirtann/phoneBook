package com.example.kt_ki.dbpractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final static int INTENT_VALUE = 2;

    Button add;
    Button all;
    Button delete;
    Button mListNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (Button) findViewById(R.id.add_record);
        all = (Button) findViewById(R.id.all_record);
        delete = (Button) findViewById(R.id.delete);
        mListNames = (Button) findViewById(R.id.list_names);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AddActivity.class);
                intent.putExtra("Add Record" , INTENT_VALUE );
                startActivity(intent);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AllRecord.class);
                intent.putExtra("All Record" , INTENT_VALUE );
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DBForm dbForm = new DBForm(MainActivity.this);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Confirmation");
                dialog.setMessage("Delete All Details ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbForm.deleteAll();
                        Toast.makeText(MainActivity.this, "All Data Deleted" , Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainActivity.this, AllRecord.class);
//                        intent.putExtra("Cancel", INTENT_VALUE);
//                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "No Data Deleted" , Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        mListNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ListActivity.class);
                intent.putExtra("All Names" , INTENT_VALUE );
                startActivity(intent);
            }
        });
    }
}
