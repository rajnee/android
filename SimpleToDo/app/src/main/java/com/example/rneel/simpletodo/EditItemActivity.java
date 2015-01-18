package com.example.rneel.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    private EditText editText;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editText = (EditText)findViewById(R.id.editItem);
        Intent intent = getIntent();
        editText.setText(intent.getStringExtra(Strings.EDITED_ITEM_TXT));
        this.pos = intent.getIntExtra(Strings.EDITED_ITEM_POS, -1);
    }

    public void onSaveClick(View v)
    {
        Intent intent = new Intent();
        String s = editText.getText().toString();
        intent.putExtra(Strings.EDITED_ITEM_TXT, s);
        intent.putExtra(Strings.EDITED_ITEM_POS, pos);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
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
