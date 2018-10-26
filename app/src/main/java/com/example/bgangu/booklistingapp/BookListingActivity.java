package com.example.bgangu.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.TextView;

import java.util.ArrayList;

public class BookListingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Books>> {

    private static final int BOOKS_LOADER_ID = 1;
    Loader mLoader;
    private EditText mSearchText;
    private ImageButton mSearchButton;
    private ListView mBookList;
    private String PLAYBOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String mTextToSearch;
    private BooksAdapter mBooksAdapter;
    private ProgressBar mProgressbar;
    private TextView mDispayError;
    private LoaderManager loaderManager = getLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);
        mBookList = (ListView) findViewById(R.id.book_list);
        mSearchText = (EditText) findViewById(R.id.search_text);
        mSearchButton = (ImageButton) findViewById(R.id.search_button);
        mDispayError = (TextView) findViewById(R.id.display_error);
        mBooksAdapter = new BooksAdapter(this, new ArrayList<Books>());
        ConnectivityManager mCM = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        final NetworkInfo mNI = mCM.getActiveNetworkInfo();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderManager.destroyLoader(BOOKS_LOADER_ID);
                mDispayError.setText("");
                if (mNI == null || !mNI.isConnected()) {
                    mDispayError.setText(R.string.no_internet_connection);
                } else if (mSearchText.getText().toString().matches("")) {
                    mDispayError.setText(R.string.no_input);
                } else {
                    mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
                    mProgressbar.setVisibility(View.VISIBLE);
                    mBookList.setAdapter(mBooksAdapter);
                    PLAYBOOKS_REQUEST_URL = URLWithUserInput();
                    loaderManager.initLoader(BOOKS_LOADER_ID, null, BookListingActivity.this);
                }
            }
        });
    }

    private String URLWithUserInput() {
        mTextToSearch = mSearchText.getText().toString().replaceAll(" ", "%20");
        mTextToSearch = PLAYBOOKS_REQUEST_URL + mTextToSearch;
        return mTextToSearch;
    }

    @Override
    public Loader<ArrayList<Books>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this, PLAYBOOKS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Books>> loader, ArrayList<Books> books) {
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressbar.setVisibility(View.GONE);
        mBooksAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mBooksAdapter.addAll(books);
        } else {
            mDispayError.setText(R.string.no_books_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Books>> loader) {
        mBooksAdapter.clear();
        mTextToSearch = null;
        PLAYBOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
        mDispayError = (TextView) findViewById(R.id.display_error);
        mDispayError.setText("");
    }
}
