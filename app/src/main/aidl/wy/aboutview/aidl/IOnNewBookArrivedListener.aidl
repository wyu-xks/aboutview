package wy.aboutview.aidl;


import wy.aboutview.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
