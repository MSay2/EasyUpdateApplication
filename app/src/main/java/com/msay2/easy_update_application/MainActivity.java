package com.msay2.easy_update_application;

import android.support.v7.app.AppCompatActivity;

import com.msay2.easyupdateapplication.update.UpdateApplication;

import android.content.Context;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity 
{
	// link of JSON file, link Raw (server github)
	public static final String URL_JSON = "https://raw.githubusercontent.com/MSay2/EasyUpdateApplication/master/Example%20of%20JSON/update.json";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		if (isConnected())
		{
			// Wi-Fi is available
			// default buffer_size is 1024
			UpdateApplication.checkUpdateApplication(this, URL_JSON, "MyApp");
			// UpdateApplication.checkUpdateApplication2(this, URL_JSON, "MyApp");
			
			// this for the custom buffer_size, this is for the downloading of the application
			// UpdateApplication.checkUpdateApplication(this, URL_JSON, "MyApp", 4096);
			// UpdateApplication.checkUpdateApplication2(this, URL_JSON, "MyApp", 4096);
			// UpdateApplication.checkUpdateApplication(this, URL_JSON, "MyApp", UpdateApplication.BUFFER_SIZE_MID);
			// UpdateApplication.checkUpdateApplication2(this, URL_JSON, "MyApp", UpdateApplication.BUFFER_SIZE_MID);
			
			// UpdateApplication.BUFFER_SIZE_FAST is 8192
			// UpdateApplication.BUFFER_SIZE_MID is 4086
			// UpdateApplication.BUFFER_SIZE_DEFAULT is 1024
			// UpdateApplication.BUFFER_SIZE_SLOW is 512
		}
		else
		{
			// Wi-Fi is not available
			Toast.makeText(this, "ERROR ! Wi-Fi is not available", Toast.LENGTH_LONG).show();
		}
    }
	
	// check if the Wi-Fi connexion is available 
	public Boolean isConnected()
	{
		ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null)
			{
				if (info.isConnected()) 
				{
					return true;
				}
			}
		}
		return false;
	}
}
