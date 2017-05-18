package wy.aboutview.aidl;

import wy.aboutview.aidl.Book;
import wy.aboutview.aidl.IOnNewBookArrivedListener;

interface IBookManger {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
