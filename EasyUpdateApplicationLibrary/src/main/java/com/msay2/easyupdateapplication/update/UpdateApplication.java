package com.msay2.easyupdateapplication.update;

import com.msay2.easyupdateapplication.R;
import com.msay2.easyupdateapplication.app.ms_AlertDialog;
import com.msay2.easyupdateapplication.item_data.ItemData;
import com.msay2.easyupdateapplication.preferences.Preferences;

import android.support.annotation.NonNull;

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
	private static AsyncTask<Void, Integer, Integer> downloadApplication;
	private static List<ItemData> itemsData;
	private static ItemData item;
	private static ProgressDialog progressDialog;
	
	private static final String LOG_TAG = "UpdateApplicationLog";
	
	public static final int BUFFER_SIZE_FAST = 8192;
	public static final int BUFFER_SIZE_MID = 4096;
	public static final int BUFFER_SIZE_DEFAULT = 1024;
	public static final int BUFFER_SIZE_SLOW = 512;
	
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
	
	public static void checkUpdateApplication(@NonNull Activity context, @NonNull String url_json, @NonNull String name_of_your_application, @NonNull int buffer_size)
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
							dialogYesUpdate(context, url, release, name_of_your_application + "/" + context.getResources().getString(R.string.updater_directory), buffer_size);
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
	
	public static void checkUpdateApplication2(@NonNull Activity context, @NonNull String url_json, @NonNull String name_of_your_application, @NonNull int buffer_size)
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
							dialogYesUpdate(context, url, release, name_of_your_application + "/" + context.getResources().getString(R.string.updater_directory), buffer_size);
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
		String title = activity.getResources().getString(R.string.updater_available_title);
		String btn_updater = activity.getResources().getString(R.string.updater_btn_download);
		String btn_back = activity.getResources().getString(R.string.updater_btn_back);

		new ms_AlertDialog(activity)
		    .setContentTitle(title)
		    .setContentMessage(content_release)
		    .setPositiveButton(btn_updater, new ms_AlertDialog.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					downloadApplication(activity, url, folder_update).execute();
					ms_AlertDialog.OnClickListener.dismissDialog();
				}
			})
		    .setNegativeButton(btn_back, new ms_AlertDialog.OnClickListener());
	}
	
	private static void dialogYesUpdate(@NonNull Activity activity, @NonNull String url, @NonNull String content_release, @NonNull String folder_update, @NonNull int buffer_size)
	{
		String title = activity.getResources().getString(R.string.updater_available_title);
		String btn_updater = activity.getResources().getString(R.string.updater_btn_download);
		String btn_back = activity.getResources().getString(R.string.updater_btn_back);

		new ms_AlertDialog(activity)
		    .setContentTitle(title)
		    .setContentMessage(content_release)
		    .setPositiveButton(btn_updater, new ms_AlertDialog.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					downloadApplication(activity, url, folder_update, buffer_size).execute();
					ms_AlertDialog.OnClickListener.dismissDialog();
				}
			})
		    .setNegativeButton(btn_back, new ms_AlertDialog.OnClickListener());
	}

	private static void dialogNoUpdate(@NonNull Activity activity, @NonNull String name_app)
	{
		String title = activity.getResources().getString(R.string.updater_not_available_title);
		String content = String.format(activity.getResources().getString(R.string.updater_not_available_description), name_app);
		String btn_ok = activity.getResources().getString(R.string.updater_btn_ok);

		new ms_AlertDialog(activity)
		    .setContentTitle(title)
		    .setContentMessage(content)
		    .setPositiveButton(btn_ok, new ms_AlertDialog.OnClickListener());
	}

	private static void dialogNoTested(@NonNull Activity activity, @NonNull String name_app)
	{
		String title = activity.getResources().getString(R.string.updater_tested_title);
		String content = String.format(activity.getResources().getString(R.string.updater_tested_prompt), name_app);
		String btn_ok = activity.getResources().getString(R.string.updater_btn_ok);

		new ms_AlertDialog(activity)
		    .setContentTitle(title)
		    .setContentMessage(content)
		    .setPositiveButton(btn_ok, new ms_AlertDialog.OnClickListener());
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
	
	private static AsyncTask<Void, Integer, Integer> downloadApplication(@NonNull Activity activity, @NonNull String aurl, @NonNull String FOLDER_UPDATE)
	{
		return downloadApplication = new AsyncTask<Void, Integer, Integer>()
		{
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();

				showDialog(activity);
			}

			@Override
			protected Integer doInBackground(Void... params)
			{
				Integer result = 0;
				HttpURLConnection urlConnection;
				int count;
				
				try
				{
					Thread.sleep(1);

					URL url = new URL(aurl);
					URLConnection conexion = url.openConnection();
					urlConnection = (HttpURLConnection)url.openConnection();
					int statusCode = urlConnection.getResponseCode();
					
					if (statusCode == 200)
					{
						int lenghtOfFile = conexion.getContentLength();
						Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

						InputStream input = new BufferedInputStream(url.openStream());
						OutputStream output = new FileOutputStream("/sdcard/" + FOLDER_UPDATE + "/" + "app.apk");

						byte data[] = new byte[BUFFER_SIZE_DEFAULT];

						long total = 0;

						while ((count = input.read(data)) != -1) 
						{
							total += count;
							publishProgress((int)((total * 100) / lenghtOfFile));
							output.write(data, 0, count);
						}

						output.flush();
						output.close();
						input.close();
						
						result = 1;
					}
					else
					{
						result = 0;
					}
				} 
				catch (Exception e)
				{ }
				return result;
			}

			protected void onProgressUpdate(Integer... progress) 
			{
				progressDialog.setProgress(progress[0]);
				
				super.onProgressUpdate(progress);
			}

			@Override
			protected void onPostExecute(Integer result)
			{
				progressDialog.dismiss();

				if (result == 1)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					File file = new File("/mnt/sdcard/" + FOLDER_UPDATE + "/" + "app.apk");

					intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
					activity.startActivity(intent);
				}
				else
				{
					Toast.makeText(activity, activity.getResources().getString(R.string.updater_donwload_json_failed), Toast.LENGTH_LONG).show();
				}
			}
		};
	}
	
	private static AsyncTask<Void, Integer, Integer> downloadApplication(@NonNull Activity activity, @NonNull String aurl, @NonNull String FOLDER_UPDATE, @NonNull int buffer_size)
	{
		return downloadApplication = new AsyncTask<Void, Integer, Integer>()
		{
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();

				showDialog(activity);
			}

			@Override
			protected Integer doInBackground(Void... params)
			{
				Integer result = 0;
				HttpURLConnection urlConnection;
				int count;

				try
				{
					Thread.sleep(1);

					URL url = new URL(aurl);
					URLConnection conexion = url.openConnection();
					urlConnection = (HttpURLConnection)url.openConnection();
					int statusCode = urlConnection.getResponseCode();

					if (statusCode == 200)
					{
						int lenghtOfFile = conexion.getContentLength();
						Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

						InputStream input = new BufferedInputStream(url.openStream());
						OutputStream output = new FileOutputStream("/sdcard/" + FOLDER_UPDATE + "/" + "app.apk");

						byte data[] = new byte[buffer_size];

						long total = 0;

						while ((count = input.read(data)) != -1) 
						{
							total += count;
							publishProgress((int)((total * 100) / lenghtOfFile));
							output.write(data, 0, count);
						}

						output.flush();
						output.close();
						input.close();

						result = 1;
					}
					else
					{
						result = 0;
					}
				} 
				catch (Exception e)
				{ }
				return result;
			}

			protected void onProgressUpdate(Integer... progress) 
			{
				progressDialog.setProgress(progress[0]);

				super.onProgressUpdate(progress);
			}

			@Override
			protected void onPostExecute(Integer result)
			{
				progressDialog.dismiss();

				if (result == 1)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					File file = new File("/mnt/sdcard/" + FOLDER_UPDATE + "/" + "app.apk");

					intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
					activity.startActivity(intent);
				}
				else
				{
					Toast.makeText(activity, activity.getResources().getString(R.string.updater_donwload_json_failed), Toast.LENGTH_LONG).show();
				}
			}
		};
	}
}
