package com.msay2.easyupdateapplication.preferences;

import com.msay2.easyupdateapplication.helpers.BuildConfigHelper;

import android.support.annotation.NonNull;

import android.content.Context;

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

public class Preferences
{
	public static BuildConfigHelper getBuild(@NonNull Context context)
	{
		return new BuildConfigHelper(context);
	}
}
