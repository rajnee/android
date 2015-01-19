package com.example.rneel.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Item> items;
    private ArrayAdapter<String> itemsAdapter1;
    private ItemAdapter itemsAdapter;
    private ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
//        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ItemAdapter(getApplication(), R.layout.one_item, items);
        lvItems.setAdapter(itemsAdapter);
//        items.add("first item");
//        items.add("second item");
        setupListViewListener();
    }


    private void readItems() {
        File todoFile = getFile();
        try {
            List<String> lines = FileUtils.readLines(todoFile);
            items = new ArrayList<>();
            for (String s:lines) {
                items.add(new Item(s));
            }
        }catch (IOException e) {
        }
    }

    private File getFile() {
        File filesDir =  getFilesDir();
        return new File(filesDir, Strings.FILE_NAME);
    }

    private void writeItems() {
        File todoFile = getFile();

        try {
            FileUtils.writeLines(todoFile,items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String s = etNewItem.getText().toString();
        if (s != null && !s.trim().equals("")) {
            itemsAdapter.add(new Item(s.trim(), null));
        }
        etNewItem.setText("");
        writeItems();
    }

    private final int REQUEST_CODE = 20;

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //TODO: need to correct editing
                i.putExtra(Strings.EDITED_ITEM_TXT,items.get(position).toString());
                i.putExtra(Strings.EDITED_ITEM_POS,position);
//                startActivity(i);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            //TODO: may need to pass an item back and forth
            String t = data.getStringExtra(Strings.EDITED_ITEM_TXT);
            int pos = data.getIntExtra(Strings.EDITED_ITEM_POS, -1);
            if (pos != -1 && pos < items.size()) {
                items.set(pos, new Item(t));
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
