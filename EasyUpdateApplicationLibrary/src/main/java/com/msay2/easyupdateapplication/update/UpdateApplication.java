package com.msay2.easyupdateapplication.update;

import com.msay2.easyupdateapplication.R;
import com.msay2.easyupdateapplication.item_data.ItemData;
import com.msay2.easyupdateapplication.preferences.Preferences;

import android.support.annotation.NonNull;

import android.support.v7.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.os.AsyncTask;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

/*
 * EasyUpdateApplication library
 *
 * Copyright (c) 2017 Meclot Yoann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class UpdateApplication
{
	private static AsyncTask<String, Void, Integer> checkUpdate;
	private static AsyncTask<String, String, String> downloadApplication;
	private static List<ItemData> itemsData;
	private static ItemData item;
	private static AlertDialog dialog;
	private static ProgressDialog progressDialog;
	
	private static final String LOG_TAG = "UpdateApplicationLog";
	
	/* Voir si une mise à jour est disponible
	 * Si une mise à jour est disponible cette méthode
	 * Démarrera le téléchargement des informations
	 * Si une mise à jour n'est pas disponible cette méthode
	 * Démarrera des méthodes d'avertissement à l'utilisateur
	 */
	public static void checkUpdateApplication(@NonNull Activity context, @NonNull String url_json, @NonNull String name_of_your_application)
	{
		String getStorage = Environment.getExternalStorageDirectory().toString();

		File newFolder = new File(getStorage + "/" +  name_of_your_application);
		newFolder.mkdir();
		
		initNewDirectory(context, name_of_your_application);
		
		checkUpdate = new AsyncTask<String, Void, Integer>()
		{
			@Override
			protected void onPreExecute() 
			{ }

			@Override
			protected Integer doInBackground(String... params)
			{
				Integer result = 0;
				HttpURLConnection urlConnection;
				try
				{
					URL url = new URL(url_json);
					urlConnection = (HttpURLConnection)url.openConnection();
					int statusCode = urlConnection.getResponseCode();
					
					urlConnection.setConnectTimeout(15000);

					if (statusCode == 200)
					{
						BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
						StringBuilder response = new StringBuilder();
						String line;

						while ((line = r.readLine()) != null)
						{
							response.append(line);
						}

						try 
						{
							JSONObject obj = new JSONObject(response.toString());
							
							item = new ItemData();
							item.setVersionName(obj.optString("versionName"));
							item.setVersionCode(obj.optString("versionCode"));
							item.setTested(obj.optString("tested"));
							item.setUrl(obj.optString("url"));
							
							JSONArray posts = obj.optJSONArray("release");
							itemsData = new ArrayList<>();
							if (posts != null)
							{
								StringBuilder builder = new StringBuilder();

								for (int i = 0; i < posts.length(); ++i) 
								{
									builder.append(posts.getString(i).trim());
									if (i != posts.length() - 1)
									{
										builder.append(System.getProperty("line.separator"));
									}
								}
								item.setRelease(String.format(context.getResources().getString(R.string.updater_available_description), item.getVersionName().toString()) + "\n\n" + builder.toString());
							}
							itemsData.add(item);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						result = 1;
					}
					else 
					{
						result = 0;
					}
				} 
				catch (Exception e)
				{
					Log.d(LOG_TAG, e.getLocalizedMessage());
				}
				return result;
			}

			@Override
			protected void onPostExecute(Integer result)
			{
				if (result == 1) 
				{
					int defaultVersion = 0;
					String app_tested = item.getTested().toString();
					String text = item.getVersionCode().toString();
					String url = item.getUrl().toString();
					String release = item.getRelease().toString();

					defaultVersion = Integer.parseInt(text);

					if (app_tested.equals("yes"))
					{
						if (Preferences.getBuild(context).getVersionCode() < defaultVersion)
						{
							dialogYesUpdate(context, url, release, name_of_your_application + "/" + context.getResources().getString(R.string.updater_directory));
						}
						else
						{
							dialogNoUpdate(context, name_of_your_application);
						}
					}
					else if (app_tested.equals("no"))
					{
						dialogNoTested(context, name_of_your_application);
					}
				} 
				else 
				{
					Toast.makeText(context, context.getResources().getString(R.string.updater_donwload_json_failed), Toast.LENGTH_LONG).show();
				}
			}

		}.execute();
	}
	
	/* Voir si une mise à jour est disponible
	 * Si une mise à jour est disponible cette méthode
	 * Démarrera le téléchargement des informations
	 * Si une mise à jour n'est pas disponible cette méthode
	 * Démarrera aucune méthode d'avertissement à l'utilisateur
	 */
	public static void checkUpdateApplication2(@NonNull Activity context, @NonNull String url_json, @NonNull String name_of_your_application)
	{
		String getStorage = Environment.getExternalStorageDirectory().toString();

		File newFolder = new File(getStorage + "/" +  name_of_your_application);
		newFolder.mkdir();

		initNewDirectory(context, name_of_your_application);

		checkUpdate = new AsyncTask<String, Void, Integer>()
		{
			@Override
			protected void onPreExecute() 
			{ }

			@Override
			protected Integer doInBackground(String... params)
			{
				Integer result = 0;
				HttpURLConnection urlConnection;
				try
				{
					URL url = new URL(url_json);
					urlConnection = (HttpURLConnection)url.openConnection();
					int statusCode = urlConnection.getResponseCode();

					urlConnection.setConnectTimeout(15000);

					if (statusCode == 200)
					{
						BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
						StringBuilder response = new StringBuilder();
						String line;

						while ((line = r.readLine()) != null)
						{
							response.append(line);
						}

						try 
						{
							JSONObject obj = new JSONObject(response.toString());

							item = new ItemData();
							item.setVersionName(obj.optString("versionName"));
							item.setVersionCode(obj.optString("versionCode"));
							item.setTested(obj.optString("tested"));
							item.setUrl(obj.optString("url"));

							JSONArray posts = obj.optJSONArray("release");
							itemsData = new ArrayList<>();
							if (posts != null)
							{
								StringBuilder builder = new StringBuilder();

								for (int i = 0; i < posts.length(); ++i) 
								{
									builder.append(posts.getString(i).trim());
									if (i != posts.length() - 1)
									{
										builder.append(System.getProperty("line.separator"));
									}
								}
								item.setRelease(String.format(context.getResources().getString(R.string.updater_available_description), item.getVersionName().toString()) + "\n\n" + builder.toString());
							}
							itemsData.add(item);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						result = 1;
					}
					else 
					{
						result = 0;
					}
				} 
				catch (Exception e)
				{
					Log.d(LOG_TAG, e.getLocalizedMessage());
				}
				return result;
			}

			@Override
			protected void onPostExecute(Integer result)
			{
				if (result == 1) 
				{
					int defaultVersion = 0;
					String app_tested = item.getTested().toString();
					String text = item.getVersionCode().toString();
					String url = item.getUrl().toString();
					String release = item.getRelease().toString();

					defaultVersion = Integer.parseInt(text);

					if (app_tested.equals("yes"))
					{
						if (Preferences.getBuild(context).getVersionCode() < defaultVersion)
						{
							dialogYesUpdate(context, url, release, name_of_your_application + "/" + context.getResources().getString(R.string.updater_directory));
						}
						else
						{ }
					}
					else if (app_tested.equals("no"))
					{ }
				} 
				else 
				{
					Toast.makeText(context, context.getResources().getString(R.string.updater_donwload_json_failed), Toast.LENGTH_LONG).show();
				}
			}

		}.execute();
	}
	
	private static void initNewDirectory(@NonNull Activity activity, @NonNull String folder)
	{
		String getStorage = Environment.getExternalStorageDirectory().toString();

		File newFolder = new File(getStorage + "/" + folder + "/" + activity.getResources().getString(R.string.updater_directory));
		newFolder.mkdir();
	}
	
	private static void dialogYesUpdate(@NonNull Activity activity, @NonNull String url, @NonNull String content_release, @NonNull String folder_update)
	{
		View view = activity.getLayoutInflater().inflate(R.layout.ms_dialog_update, null);

		TextView title = (TextView)view.findViewById(R.id.id_title);
		TextView content = (TextView)view.findViewById(R.id.id_content_text);
		Button positive = (Button)view.findViewById(R.id.button_positive);
		Button negative = (Button)view.findViewById(R.id.button_negative);

		title.setText(activity.getResources().getString(R.string.updater_available_title));
		content.setText(content_release);
		positive.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dialog.dismiss();
				downloadApplication(activity, folder_update).execute(url);
			}
		});
		negative.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dialog.dismiss();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(activity)
		    .setView(view)
		    .setCancelable(true);

		dialog = builder.create();
		dialog.show();
	}
	
	private static void dialogNoUpdate(@NonNull Activity activity, @NonNull String name_app)
	{
		View view = activity.getLayoutInflater().inflate(R.layout.ms_dialog_update, null);

		TextView title = (TextView)view.findViewById(R.id.id_title);
		TextView content = (TextView)view.findViewById(R.id.id_content_text);
		RelativeLayout rv = (RelativeLayout)view.findViewById(R.id.bar_button);

		rv.setVisibility(View.GONE);
		title.setText(activity.getResources().getString(R.string.updater_not_available_title));
		content.setText(String.format(activity.getResources().getString(R.string.updater_not_available_description), name_app));

		AlertDialog.Builder builder = new AlertDialog.Builder(activity)
		    .setView(view)
		    .setCancelable(true)
		    .setPositiveButton(activity.getResources().getString(R.string.updater_btn_ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int position)
				{ }
			});

		dialog = builder.create();
		dialog.show();
	}
	
	private static void dialogNoTested(@NonNull Activity activity, @NonNull String name_app)
	{
		View view = activity.getLayoutInflater().inflate(R.layout.ms_dialog_update, null);

		TextView title = (TextView)view.findViewById(R.id.id_title);
		TextView content = (TextView)view.findViewById(R.id.id_content_text);
		RelativeLayout rv = (RelativeLayout)view.findViewById(R.id.bar_button);

		rv.setVisibility(View.GONE);
		title.setText(activity.getResources().getString(R.string.updater_tested_title));
		content.setText(String.format(activity.getResources().getString(R.string.updater_tested_prompt), name_app));

		AlertDialog.Builder builder = new AlertDialog.Builder(activity)
		    .setView(view)
		    .setPositiveButton(activity.getResources().getString(R.string.updater_btn_ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int position)
				{ }
			});

		dialog = builder.create();
		dialog.show();
	}
	
	private static ProgressDialog showDialog(@NonNull Activity activity)
	{
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage(activity.getResources().getString(R.string.updater_downloading));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		progressDialog.show();

		return progressDialog;
	}
	
	private static AsyncTask<String, String, String> downloadApplication(@NonNull Activity activity, @NonNull String FOLDER_UPDATE)
	{
		return downloadApplication = new AsyncTask<String, String, String>()
		{
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();

				showDialog(activity);
			}

			@Override
			protected String doInBackground(String... aurl)
			{
				int count;
				try
				{
					URL url = new URL(aurl[0]);
					URLConnection conexion = url.openConnection();
					conexion.connect();

					int lenghtOfFile = conexion.getContentLength();
					Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

					InputStream input = new BufferedInputStream(url.openStream());
					OutputStream output = new FileOutputStream("/sdcard/" + FOLDER_UPDATE + "/" + "app.apk");

					byte data[] = new byte[1024];

					long total = 0;

					while ((count = input.read(data)) != -1) 
					{
						total += count;
						publishProgress(""+(int)((total*100)/lenghtOfFile));
						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();
				} 
				catch (Exception e)
				{ }

				return null;
			}

			protected void onProgressUpdate(String... progress) 
			{
				Log.d("ANDRO_ASYNC", progress[0]);
				progressDialog.setProgress(Integer.parseInt(progress[0]));
			}

			@Override
			protected void onPostExecute(String unused) 
			{
				progressDialog.dismiss();

				Intent intent = new Intent(Intent.ACTION_VIEW);
				File file = new File("/mnt/sdcard/" + FOLDER_UPDATE + "/" + "app.apk");
				
				intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
				activity.startActivity(intent);
			}
		};
	}
}
