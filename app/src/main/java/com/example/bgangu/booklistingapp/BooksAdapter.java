package com.example.bgangu.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Books> {

    private Context mContext;

    public BooksAdapter(Activity context, ArrayList<Books> books) {
        super(context, 0, books);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        final Books currentBook = getItem(position);

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);
        }

        TextView mBookTitle = (TextView) listView.findViewById(R.id.book_title);
        TextView mAuthorName = (TextView) listView.findViewById(R.id.author_name);
        TextView mPublishedDate = (TextView) listView.findViewById(R.id.published_date);

        mBookTitle.setText(currentBook.getBookTitle());
        mAuthorName.setText(currentBook.getBookAuthor());
        mPublishedDate.setText(currentBook.getBookPublishedDate());

        return listView;
    }
}
