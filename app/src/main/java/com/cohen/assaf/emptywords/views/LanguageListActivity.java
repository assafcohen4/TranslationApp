package com.cohen.assaf.emptywords.views;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cohen.assaf.emptywords.LanguagesSingleton;
import com.cohen.assaf.emptywords.model.Language;
import com.cohen.assaf.emptywords.LanguageAdapter;
import com.cohen.assaf.emptywords.R;

public class LanguageListActivity extends ListActivity {

    public static final String RESULT = "result";
    public static final String ICON_RESULT = "icon";
    private ListView mListView1;
    private Language mChosenLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        LanguagesSingleton languages = LanguagesSingleton.getInstance();
        Language language_data[] =languages.getAllLanguagesList();

        LanguageAdapter adapter = new LanguageAdapter(this,
                R.layout.listview_item_row, language_data);


        mListView1 = (ListView)findViewById(android.R.id.list);

        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        mListView1.addHeaderView(header);
        mListView1.setAdapter(adapter);

        //Send chosen language back to main
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                mChosenLanguage = (Language) parent.getItemAtPosition(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT,  mChosenLanguage);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}

