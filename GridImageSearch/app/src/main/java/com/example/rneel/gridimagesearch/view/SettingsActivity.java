package com.example.rneel.gridimagesearch.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.rneel.gridimagesearch.R;
import com.example.rneel.gridimagesearch.model.SettingsModel;
import com.example.rneel.gridimagesearch.model.SettingsUtil;


public class SettingsActivity extends ActionBarActivity {

    SettingsModel settingsModel;
    private Spinner imageSizeSpinner;
    private Spinner imageTypeSpinner;
    private Spinner colorSpinner;
    private EditText etSiteFilter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        settingsModel = (SettingsModel)intent.getSerializableExtra("settings");
        getSpinners();
        initializeSettings();
    }

    private void getSpinners() 
    {
        imageSizeSpinner = (Spinner)findViewById(R.id.spinnerImageSize);
        imageTypeSpinner = (Spinner)findViewById(R.id.spinnerImageType);
        colorSpinner = (Spinner)findViewById(R.id.spinnerColorFilter);
        etSiteFilter = (EditText)findViewById(R.id.etSiteFilter);
        
    }
    private void initializeSettings()
    {
        imageTypeSpinner.setSelection(SettingsUtil.getPositionForImageType(settingsModel.imageType));
        imageSizeSpinner.setSelection(SettingsUtil.getPositionForImageSize(settingsModel.imageSize));
        colorSpinner.setSelection(SettingsUtil.getPositionForColorFilter(settingsModel.colorFilter));
        etSiteFilter.setText(settingsModel.siteFilter);
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    
    public void onSaveClick(View view) {
        settingsModel.colorFilter = colorSpinner.getSelectedItem().toString();
        settingsModel.imageType = imageTypeSpinner.getSelectedItem().toString();
        settingsModel.imageSize = imageSizeSpinner.getSelectedItem().toString();
        settingsModel.siteFilter = etSiteFilter.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("settings", settingsModel);
        setResult(RESULT_OK,intent);
        finish();
    }
}
