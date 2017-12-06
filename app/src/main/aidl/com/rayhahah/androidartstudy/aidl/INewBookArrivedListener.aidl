// INewBookArrivedListener.aidl
package com.rayhahah.androidartstudy.aidl;
import com.rayhahah.androidartstudy.aidl.Book;

// Declare any non-default types here with import statements

interface INewBookArrivedListener {
void onNewBookArrived(in Book book);
}
