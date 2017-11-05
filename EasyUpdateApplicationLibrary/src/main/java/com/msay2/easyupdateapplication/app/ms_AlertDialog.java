package com.msay2.easyupdateapplication.app;

import com.msay2.easyupdateapplication.R;

import android.support.v7.app.AlertDialog;

import android.support.annotation.StringRes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class ms_AlertDialog extends AlertDialog
{
	private static Activity acivity;
	private static AlertDialog dialog;
	private static AlertDialog.Builder builder;
	private static TextView title, content;
	private static LinearLayout button_bar;
	private static Button positive, negative;
	private static ScrollView scroll;
	private static View layout;
	private static ViewGroup custom_view;
	private static CharSequence mCharSequenceTitle;
	private static CharSequence mCharSequenceMessage;
	private static CharSequence mCharSequencePositive;
	private static CharSequence mCharSequenceNegative;

	public ms_AlertDialog(Context context)
	{
		super(context);

		acivity = (Activity)context;
		initAlertDialog(acivity);
	}

	private void initAlertDialog(Context context)
	{
		layout = LayoutInflater.from(context).inflate(R.layout.ms_dialog, null);
		
		custom_view = (ViewGroup)layout.findViewById(R.id.id_custom_view);
		
		title = (TextView)layout.findViewById(R.id.id_title);
		content = (TextView)layout.findViewById(R.id.id_content_text);
		button_bar = (LinearLayout)layout.findViewById(R.id.bar_button);
		positive = (Button)layout.findViewById(R.id.button_positive);
		negative = (Button)layout.findViewById(R.id.button_negative);
		scroll = (ScrollView)layout.findViewById(R.id.id_scroll);
		
		builder = new AlertDialog.Builder(context)
		    .setView(layout);

		dialog = builder.create();
		dialog.show();
	}
	
	public ms_AlertDialog setAViewLayout(View view)
	{
		custom_view.setVisibility(View.VISIBLE);
		custom_view.addView(view);
		
		return this;
	}

	public ms_AlertDialog setAContentTitle(CharSequence charSequence)
	{
		mCharSequenceTitle = charSequence;
		if (title != null)
		{
			title.setVisibility(View.VISIBLE);
			title.setText(mCharSequenceTitle);
		}
		return this;
	}

	public ms_AlertDialog setAContentTitle(@StringRes int resId)
	{
		return setAContentTitle(getContext().getString(resId));
	}

	public ms_AlertDialog setAContentMessage(CharSequence charSequence)
	{
		mCharSequenceMessage = charSequence;
		if (content != null)
		{
			scroll.setVisibility(View.VISIBLE);
			content.setText(mCharSequenceMessage);
		}
		return this;
	}

	public ms_AlertDialog setAContentMessage(@StringRes int resId)
	{
		return setAContentMessage(getContext().getString(resId));
	}

	public ms_AlertDialog setAPositiveButton(CharSequence charSequence, View.OnClickListener listener)
	{
		mCharSequencePositive = charSequence;
		if (positive != null)
		{
			button_bar.setVisibility(View.VISIBLE);
			positive.setVisibility(View.VISIBLE);
			positive.setText(mCharSequencePositive);
			positive.setOnClickListener(listener);
		}
		return this;
	}

	public ms_AlertDialog setAPositiveButton(@StringRes int resId, View.OnClickListener listener)
	{
		return setAPositiveButton(getContext().getString(resId), listener);
	}

	public ms_AlertDialog setANegativeButton(CharSequence charSequence, View.OnClickListener listener)
	{
		mCharSequenceNegative = charSequence;
		if (negative != null)
		{
			button_bar.setVisibility(View.VISIBLE);
			negative.setVisibility(View.VISIBLE);
			negative.setText(mCharSequenceNegative);
			negative.setOnClickListener(listener);
		}
		return this;
	}

	public ms_AlertDialog setANegativeButton(@StringRes int resId, View.OnClickListener listener)
	{
		return setANegativeButton(getContext().getString(resId), listener);
	}

	public static class OnClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			dismissDialog();
		}

		public static void dismissDialog()
		{
			dialog.dismiss();
		}
	}
}
