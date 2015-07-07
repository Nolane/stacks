package com.nolane.stacks.controller.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.nolane.stacks.R;
import com.nolane.stacks.controller.activities.TrainingActivity;
import com.nolane.stacks.model.CardsContract;

/**
 * This fragment is used for navigation drawer.
 */
public class NavigationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, AdapterView.OnItemClickListener {
    // UI elements.
    private ListView lvNavigation;
    private Button btnAdd;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DictionariesQuery._TOKEN, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.navigation_drawer, container, false);
        lvNavigation = (ListView) view.findViewById(R.id.lv_navigation);
        lvNavigation.setOnItemClickListener(this);
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity().getBaseContext(), CreateDictionaryFragment.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity().getBaseContext(), TrainingActivity.class);
        intent.setData(ContentUris.withAppendedId(CardsContract.Dictionary.CONTENT_URI, id));
        startActivity(intent);
    }

    /**
     * Query for all dictionaries in conjunction with _id. _id is required when we start
     * {@link TrainingActivity} by clicking at dictionary in the list.
     */
    private interface DictionariesQuery {
        int _TOKEN = 0;

        String[] COLUMNS = {
                CardsContract.Dictionary.DICTIONARY_ID,
                CardsContract.Dictionary.DICTIONARY_TITLE
        };

        int ID = 0;
        int TITLE = 1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                CardsContract.Dictionary.CONTENT_URI,
                DictionariesQuery.COLUMNS,
                null,
                null,
                CardsContract.Dictionary.SORT_DEFAULT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor dictionaries) {
        if (null == dictionaries)
            throw new IllegalArgumentException("Loader was failed. (dictionaries = null)");
        lvNavigation.setAdapter(new SimpleCursorAdapter(getActivity(), R.layout.navigation_drawer_item, dictionaries,
                new String[]{CardsContract.Dictionary.DICTIONARY_TITLE},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.NO_SELECTION));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
