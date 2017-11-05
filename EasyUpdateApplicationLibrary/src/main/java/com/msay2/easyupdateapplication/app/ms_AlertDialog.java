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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ms_AlertDialog extends AlertDialog
{
	private static Activity acivity;
	private static AlertDialog dialog;
	private static AlertDialog.Builder builder;
	private static TextView title, content;
	private static RelativeLayout button_bar;
	private static Button positive, negative;
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
		View layout = LayoutInflater.from(context).inflate(R.layout.ms_dialog, null);

		title = (TextView)layout.findViewById(R.id.id_title);
		content = (TextView)layout.findViewById(R.id.id_content_text);
		button_bar = (RelativeLayout)layout.findViewById(R.id.bar_button);
		positive = (Button)layout.findViewById(R.id.button_positive);
		negative = (Button)layout.findViewById(R.id.button_negative);

		builder = new AlertDialog.Builder(context)
		    .setView(layout);

		dialog = builder.create();
		dialog.show();
	}

	public ms_AlertDialog setContentTitle(CharSequence charSequence)
	{
		mCharSequenceTitle = charSequence;
		if (title != null)
		{
			title.setText(mCharSequenceTitle);
		}
		return this;
	}

	public ms_AlertDialog setContentTitle(@StringRes int resId)
	{
		return setContentTitle(getContext().getString(resId));
	}

	public ms_AlertDialog setContentMessage(CharSequence charSequence)
	{
		mCharSequenceMessage = charSequence;
		if (content != null)
		{
			content.setText(mCharSequenceMessage);
		}
		return this;
	}

	public ms_AlertDialog setContentMessage(@StringRes int resId)
	{
		return setContentMessage(getContext().getString(resId));
	}

	public ms_AlertDialog setPositiveButton(CharSequence charSequence, View.OnClickListener listener)
	{
		mCharSequencePositive = charSequence;
		if (positive != null)
		{
			positive.setText(mCharSequencePositive);
			positive.setOnClickListener(listener);
		}
		return this;
	}

	public ms_AlertDialog setPositiveButton(@StringRes int resId, View.OnClickListener listener)
	{
		return setPositiveButton(getContext().getString(resId), listener);
	}

	public ms_AlertDialog setNegativeButton(CharSequence charSequence, View.OnClickListener listener)
	{
		mCharSequenceNegative = charSequence;
		if (negative != null)
		{
			negative.setVisibility(View.VISIBLE);
			negative.setText(mCharSequenceNegative);
			negative.setOnClickListener(listener);
		}
		return this;
	}

	public ms_AlertDialog setNegativeButton(@StringRes int resId, View.OnClickListener listener)
	{
		return setNegativeButton(getContext().getString(resId), listener);
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
