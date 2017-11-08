package com.msay2.easyupdateapplication.app;

import com.msay2.easyupdateapplication.R;
import com.msay2.easyupdateapplication.preferences.Preferences;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.support.annotation.StringRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.DrawableRes;
import android.support.annotation.DimenRes;
import android.support.annotation.ColorRes;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;  
import android.view.MotionEvent; 
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	private static ListView list;
	private static ViewGroup custom_view;
	private static RecyclerView recycler;
	private static CharSequence mCharSequenceTitle;
	private static CharSequence mCharSequenceMessage;
	private static CharSequence mCharSequencePositive;
	private static CharSequence mCharSequenceNegative;
	
	private static final String LOG_TAG = ms_AlertDialog.class.getSimpleName();

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
		list = (ListView)layout.findViewById(R.id.id_list);
		recycler = (RecyclerView)layout.findViewById(R.id.id_recycler);
		
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
			if (scroll.getVisibility() == View.VISIBLE)
			{
				scroll.setPadding(0, 0, 0, dim(56));
				
				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_scroll);
				
				button_bar.setLayoutParams(params);
			}
			else if (list.getVisibility() == View.VISIBLE)
			{
				list.setPadding(0, 0, 0, dim(56));
				
				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_list);

				button_bar.setLayoutParams(params);
			}
			else if (custom_view.getVisibility() == View.VISIBLE)
			{
				custom_view.setPadding(0, 0, 0, dim(56));
				
				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_custom_view);

				button_bar.setLayoutParams(params);
			}
			else if (recycler.getVisibility() == View.VISIBLE)
			{
				recycler.setPadding(0, 0, 0, dim(56));

				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_recycler);

				button_bar.setLayoutParams(params);
			}
			else if ((scroll.getVisibility() == View.GONE) && (list.getVisibility() == View.GONE) && (custom_view.getVisibility() == View.GONE) && (recycler.getVisibility() == View.GONE))
			{
				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.BELOW, R.id.id_title);

				button_bar.setLayoutParams(params);
			}
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
			if (scroll.getVisibility() == View.VISIBLE)
			{
				scroll.setPadding(0, 0, 0, dim(56));

				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_scroll);

				button_bar.setLayoutParams(params);
			}
			else if (list.getVisibility() == View.VISIBLE)
			{
				list.setPadding(0, 0, 0, dim(56));

				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_list);

				button_bar.setLayoutParams(params);
			}
			else if (custom_view.getVisibility() == View.VISIBLE)
			{
				custom_view.setPadding(0, 0, 0, dim(56));

				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_custom_view);

				button_bar.setLayoutParams(params);
			}
			else if (recycler.getVisibility() == View.VISIBLE)
			{
				recycler.setPadding(0, 0, 0, dim(56));
				
				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.id_recycler);

				button_bar.setLayoutParams(params);
			}
			else if ((scroll.getVisibility() == View.GONE) && (list.getVisibility() == View.GONE) && (custom_view.getVisibility() == View.GONE) && (recycler.getVisibility() == View.GONE))
			{
				ViewGroup.LayoutParams layout_params = button_bar.getLayoutParams();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_params;
				params.addRule(RelativeLayout.BELOW, R.id.id_title);

				button_bar.setLayoutParams(params);
			}
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
	
	public ms_AlertDialog setAListViewAdapter(ListAdapter adapter)
	{
		list.setVisibility(View.VISIBLE);
		list.setAdapter(adapter);
		
		return this;
	}
	
	public ms_AlertDialog setARecyclerViewAdapter(RecyclerView.Adapter adapter)
	{
		recycler.setVisibility(View.VISIBLE);
		recycler.setAdapter(adapter);
		
		return this;
	}
	
	public ms_AlertDialog setARecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager)
	{
		obtainARecyclerView().setLayoutManager(layoutManager);
		return this;
	}
	
	public ms_AlertDialog setARecyclerViewItemAnimator(RecyclerView.ItemAnimator animator)
	{
		obtainARecyclerView().setItemAnimator(animator);
		return this;
	}
	
	public ms_AlertDialog setARecyclerViewItemDecoration(RecyclerView.ItemDecoration decoration)
	{
		obtainARecyclerView().addItemDecoration(decoration);
		return this;
	}
	
	public ms_AlertDialog setARecyclerViewItemDecoration(RecyclerView.ItemDecoration decoration, int index)
	{
		obtainARecyclerView().addItemDecoration(decoration, index);
		return this;
	}
	
	public ms_AlertDialog setARecyclerViewOnItemClickListener(DialogInterface.RecyclerViewOnItemClickListener listener)
	{
		obtainARecyclerView().addOnItemTouchListener(listener);
		return this;
	}
	
	public ms_AlertDialog setAListViewOnItemClickListener(AdapterView.OnItemClickListener listener)
	{
		obtainAListView().setOnItemClickListener(listener);
		return this;
	}
	
	public ms_AlertDialog setAListViewOnItemLongClickListener(AdapterView.OnItemLongClickListener listener)
	{
		obtainAListView().setOnItemLongClickListener(listener);
		return this;
	}
	
	public ms_AlertDialog setAContentTitleSize(@FloatRange float size)
	{
		obtainAContentTitle().setTextSize(size);
		return this;
	}
	
	public ms_AlertDialog setAContentTitleColor(@IntRange int color)
	{
		obtainAContentTitle().setTextColor(color);
		return this;
	}
	
	public ms_AlertDialog setAContentTitleColorResources(int resId)
	{
		return setAContentTitleColor(getContext().getResources().getColor(resId));
	}
	
	public ms_AlertDialog setAContentMessageSize(@FloatRange float size)
	{
		obtainAContentMessage().setTextSize(size);
		return this;
	}
	
	public ms_AlertDialog setAContentMessageColor(@IntRange int color)
	{
		obtainAContentMessage().setTextColor(color);
		return this;
	}
	
	public ms_AlertDialog setAContentMessageColorResources(int resId)
	{
		return setAContentMessageColor(getContext().getResources().getColor(resId));
	}
	
	public ms_AlertDialog setAPositiveButtonSize(@FloatRange float size)
	{
		obtainAPositiveButton().setTextSize(size);
		return this;
	}
	
	public ms_AlertDialog setAPositiveButtonColor(@IntRange int color)
	{
		obtainAPositiveButton().setTextColor(color);
		return this;
	}
	
	public ms_AlertDialog setAPositiveButtonColorResources(int resId)
	{
		return setAPositiveButtonColor(getContext().getResources().getColor(resId));
	}
	
	public ms_AlertDialog setANegativeButtonSize(@FloatRange float size)
	{
		obtainANegativeButton().setTextSize(size);
		return this;
	}

	public ms_AlertDialog setANegativeButtonColor(@IntRange int color)
	{
		obtainANegativeButton().setTextColor(color);
		return this;
	}

	public ms_AlertDialog setANegativeButtonColorResources(int resId)
	{
		return setANegativeButtonColor(getContext().getResources().getColor(resId));
	}
	
	public ms_AlertDialog setAListViewDivider(Drawable drawable)
	{
		obtainAListView().setDivider(drawable);
		return this;
	}
	
	public ms_AlertDialog setAListViewDividerColor(@ColorRes int resId)
	{
		ColorDrawable color = new ColorDrawable(getContext().getResources().getColor(resId));
		obtainAListView().setDivider(color);
		
		return this;
	}
	
	public ms_AlertDialog setAListViewDivider(@DrawableRes int resId)
	{
		return setAListViewDivider(getContext().getResources().getDrawable(resId));
	}
	
	public ms_AlertDialog setAListViewDividerHeight(int height)
	{
		obtainAListView().setDividerHeight(height);
		return this;
	}
	
	public ms_AlertDialog setAListViewAddHeaderView(View view)
	{
		obtainAListView().addHeaderView(view);
		return this;
	}
	
	public ms_AlertDialog setAListViewAddHeaderView(View view, Object data, boolean isSeletable)
	{
		obtainAListView().addHeaderView(view, data, isSeletable);
		return this;
	}
	
	public ms_AlertDialog setAListViewAddFooterView(View view)
	{
		obtainAListView().addFooterView(view);
		return this;
	}

	public ms_AlertDialog setAListViewAddFooterView(View view, Object data, boolean isSeletable)
	{
		obtainAListView().addFooterView(view, data, isSeletable);
		return this;
	}
	
	private TextView obtainAContentTitle()
	{
		if (title.getVisibility() == View.VISIBLE)
		{
			return title;
		}
		else
		{
			Log.d(LOG_TAG, "S'il vous plaît, veuillez appeler la méthode setAContentTitle()");
			return null;
		}
	}
	
	private TextView obtainAContentMessage()
	{
		if (content.getVisibility() == View.VISIBLE)
		{
			return content;
		}
		else
		{
			Log.d(LOG_TAG, "S'il vous plaît, veuillez appeler la méthode setAContentMessage()");
			return null;
		}
	}
	
	private Button obtainAPositiveButton()
	{
		if (content.getVisibility() == View.VISIBLE)
		{
			return positive;
		}
		else
		{
			Log.d(LOG_TAG, "S'il vous plaît, veuillez appeler la méthode setAPositiveButton()");
			return null;
		}
	}
	
	private Button obtainANegativeButton()
	{
		if (content.getVisibility() == View.VISIBLE)
		{
			return negative;
		}
		else
		{
			Log.d(LOG_TAG, "S'il vous plaît, veuillez appeler la méthode setANegativeButton()");
			return null;
		}
	}
	
	private ListView obtainAListView()
	{
		if (list.getVisibility() == View.VISIBLE)
		{
			return list;
		}
		else
		{
			Log.d(LOG_TAG, "S'il vous plaît, veuillez appeler la méthode setAListViewAdapter()");
			return null;
		}
	}
	
	private RecyclerView obtainARecyclerView()
	{
		if (recycler.getVisibility() == View.VISIBLE)
		{
			return recycler;
		}
		else
		{
			Log.d(LOG_TAG, "S'il vous plaît, veuillez appeler la méthode setARecyclerViewAdapter()");
			return null;
		}
	}
	
	private int dim(int dp)
	{
		return Preferences.setDimensionPixels(dp, getContext());
	}

	public static class DialogInterface
	{
		public static class ButtonOnClickListener implements View.OnClickListener
		{
			@Override
			public void onClick(View p1)
			{
				dismiss();
			}
			
			public static void dismiss()
			{
				dialog.dismiss();
			}
		}
		
		public static class RecyclerViewOnItemClickListener implements RecyclerView.OnItemTouchListener
		{
			private OnItemClickListener listener;
			private GestureDetector gestureDetector;
			
			public interface OnItemClickListener
			{
				public void onItemClick(View view, int position);
			}
			
			public RecyclerViewOnItemClickListener(Context context, OnItemClickListener listener)
			{
				this.listener = listener;
				gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
				{
					@Override
					public boolean onSingleTapUp(MotionEvent e)
					{
						return true;
					}
				});
			}

			@Override
			public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent)
			{
				View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
				if (childView != null && listener != null && gestureDetector.onTouchEvent(motionEvent))
				{
					listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
				}
				return false;
			}
			
			@Override
			public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent)
			{ }

			@Override
			public void onRequestDisallowInterceptTouchEvent(boolean aBoolean)
			{ }
		}
	}
}
