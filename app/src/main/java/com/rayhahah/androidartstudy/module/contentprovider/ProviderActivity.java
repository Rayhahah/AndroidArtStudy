package com.rayhahah.androidartstudy.module.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.aidl.Book;
import com.rayhahah.androidartstudy.util.RLog;

public class ProviderActivity extends AppCompatActivity {

    private Uri mBookUri;
    private int idCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        mBookUri = Uri.parse("content://com.rayhahah.androidartstudy.book.provider/book");
    }

    public void clickToDel(View view) {
    }

    public void clickToAdd(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", idCount);
        contentValues.put("name", "hello" + idCount);
        getContentResolver().insert(mBookUri, contentValues);
        idCount++;
    }

    public void clickToQuery(View view) {
        Cursor query = getContentResolver().query(mBookUri, new String[]{"_id", "name"}, null, null, null);
        while (query.moveToNext()) {
            Book book = new Book();
            book.bookPrice = query.getInt(0);
            book.bookName = query.getString(1);
            RLog.e(book.toString());
        }
        query.close();
    }
}
