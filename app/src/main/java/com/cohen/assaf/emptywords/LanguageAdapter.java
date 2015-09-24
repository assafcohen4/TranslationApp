package com.cohen.assaf.emptywords;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cohen.assaf.emptywords.model.Language;

/**
 * Created by User on 17/09/2015.
 */
public class LanguageAdapter extends ArrayAdapter<Language> {


        Context context;
        int layoutResourceId;
        Language data[] = null;

        public LanguageAdapter(Context context, int layoutResourceId, Language[] data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LanguageHolder holder = null;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new LanguageHolder();
                holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
                holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

                row.setTag(holder);
            }
            else
            {
                holder = (LanguageHolder)row.getTag();
            }

            Language language = data[position];
            holder.txtTitle.setText(language.title);
            holder.imgIcon.setImageResource(language.icon);

            return row;
        }

        static class LanguageHolder
        {
            ImageView imgIcon;
            TextView txtTitle;
        }
    }

