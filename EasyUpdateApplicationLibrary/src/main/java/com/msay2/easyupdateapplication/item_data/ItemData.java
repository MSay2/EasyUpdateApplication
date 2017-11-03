package com.msay2.easyupdateapplication.item_data;

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

public class ItemData
{
	private String versionName, versionCode, tested, url, release;

	public ItemData()
	{ }

	public String getVersionName()
	{
		return versionName;
	}

	public String getVersionCode()
	{
		return versionCode;
	}

	public String getTested()
	{
		return tested;
	}

	public String getUrl()
	{
		return url;
	}

	public String getRelease()
	{
		return release;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public void setVersionCode(String versionCode)
	{
		this.versionCode = versionCode;
	}

	public void setTested(String tested)
	{
		this.tested = tested;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public void setRelease(String release)
	{
		this.release = release;
	}
}
