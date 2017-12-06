// IBookManager.aidl
package com.rayhahah.androidartstudy.aidl;
import com.rayhahah.androidartstudy.aidl.Book;
import com.rayhahah.androidartstudy.aidl.INewBookArrivedListener;

// Declare any non-default types here with import statements

interface IBookManager {
//用于从远端服务器获取数据
String getMessage();

//用于从远端服务器获取数据
List<Book> getBookList();
void addBook(in Book book);
void registerListener(INewBookArrivedListener listener);
void unregisterListener(INewBookArrivedListener listener);
}
