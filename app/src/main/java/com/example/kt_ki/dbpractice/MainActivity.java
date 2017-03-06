package com.example.kt_ki.dbpractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final static int INTENT_VALUE = 2;

    Button mAdd, mAll, mDelete, mListNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdd = (Button) findViewById(R.id.add_record);
        mAll = (Button) findViewById(R.id.all_record);
        mDelete = (Button) findViewById(R.id.delete);
        mListNames = (Button) findViewById(R.id.list_names);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("Add Record", INTENT_VALUE);
                startActivity(intent);
            }
        });

        mAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllRecord.class);
                intent.putExtra("All Record", INTENT_VALUE);
                startActivity(intent);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(MainActivity.this, "All Data Deleted", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainActivity.this, AllRecord.class);
//                        intent.putExtra("Cancel", INTENT_VALUE);
//                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "No Data Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        mListNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("All Names", INTENT_VALUE);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_link, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                String mimetype = "text/plain";
                String title = "title";
                String text = "Link Here";
                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setChooserTitle(title)
                        .setType(mimetype)
                        .setText(text)
                        .startChooser();
        return super.onOptionsItemSelected(item);
    }
}
