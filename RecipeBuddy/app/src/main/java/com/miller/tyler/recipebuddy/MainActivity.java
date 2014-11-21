package com.miller.tyler.recipebuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mBrowseList;
    private ActionBarDrawerToggle mDrawerListener;
    private EditText mSearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            PopularFragment popFrag = new PopularFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, popFrag)
                    .commit();
        }

        //set up search box
        mSearchBox = (EditText) findViewById(R.id.search_recipe);

        mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                PopularFragment popFrag = new PopularFragment();

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    popFrag.KEY_SEARCH = mSearchBox.getText().toString();

                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, popFrag)
                            .commit();
                    //Set actionbar title
                    ActionBar ab = getActionBar();
                    ab.setTitle("Recipe Buddy");

                    mDrawerLayout.closeDrawers();
                    dimissKeyboard();

                    return true;
                }
                return false;
            }
        });


        mDrawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListener = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerListener);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);




        // Set up array for drawer list
        mBrowseList = getResources().getStringArray(R.array.browse_by);
        // Set up drawer list
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_row_layout, R.id.row_item, mBrowseList));

        mDrawerList.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerListener.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PopularFragment popFrag = new PopularFragment();



        switch (position) {
            case 0:
                mDrawerLayout.closeDrawers();

                popFrag.KEY_SEARCH = "";
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, popFrag)
                        .commit();


                break;
            case 1:
                mDrawerLayout.closeDrawers();

                popFrag.KEY_SEARCH = "meat";
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, popFrag)
                        .commit();


                break;
            case 2:
                mDrawerLayout.closeDrawers();
                popFrag.KEY_SEARCH = "poultry";
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, popFrag)
                        .commit();
                break;
            case 3:
                mDrawerLayout.closeDrawers();
                popFrag.KEY_SEARCH = "desserts";
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, popFrag)
                        .commit();
                break;
            case 4:
                mDrawerLayout.closeDrawers();
                popFrag.KEY_SEARCH = "salads";
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, popFrag)
                        .commit();
                break;
            case 5:
                mDrawerLayout.closeDrawers();
                popFrag.KEY_SEARCH = "drinks";
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, popFrag)
                        .commit();
                break;
            case 6:
                mDrawerLayout.closeDrawers();
                //Toast.makeText(this, mBrowseList[position]+ " Selected ", Toast.LENGTH_LONG).show();
                ConverterFragment converterFragment = new ConverterFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, converterFragment)
                        .commit();
                break;


        }
        selectItem(position);

    }

    public void selectItem(int position) {

        mDrawerList.setItemChecked(position, true);
        setTitle(mBrowseList[position]);

    }

    public void setTitle(String title) {

        getActionBar().setTitle(title);
    }

    private void dimissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mSearchBox.getWindowToken(), 0);

        mSearchBox.setText(null);


    }
}
