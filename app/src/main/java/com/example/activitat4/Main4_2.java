package com.example.activitat4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Main4_2 extends ListActivity {

    private ListAdapter adapter;
    private ProgressDialog pd;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main4_2);
        llistaContactes();
    }

    public void llistaContactes() {
        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI =
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI =
                ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID =
                ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
                null);
        // Obtenir contactes
        Log.i(getClass().getSimpleName(), "Contactes");
        if (cursor.getCount() > 0) { // Hi ha contactes?
            ArrayList<HashMap<String, String>> llista = new
                    ArrayList<HashMap<String, String>>();
            Log.i(getClass().getSimpleName(), "Hi ha");
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String contacte_id =
                        cursor.getString(cursor.getColumnIndex(_ID));
                String nom =
                        cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    map.put("nom", nom);
                    // Cercar els telefons del contacte
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI,
                            null,
                            Phone_CONTACT_ID + " = ?", new
                                    String[]{contacte_id}, null);
                    String telefons = "";
                    while (phoneCursor.moveToNext()) {
                        phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        telefons = telefons + "\nTelèfon:" + phoneNumber;
                    }
                    phoneCursor.close();
                    map.put("telefon", telefons);
                    // Cercar els email per a cada contacte
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,
                            null, EmailCONTACT_ID + " = ?", new String[]{contacte_id}, null);
                    String emails = "";
                    while (emailCursor.moveToNext()) {
                        email =
                                emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        emails = emails + "\nEmail:" + email;
                    }
                    emailCursor.close();
                    map.put("email", emails);
                    llista.add(map);
                }
            }
            //Assignar a la listview
            adapter = new SimpleAdapter(this, llista, R.layout.llista_detall,
                    new String[]{"nom", "telefon", "email"}, new int[]{R.id.nom,
                    R.id.telefon, R.id.email});
            setListAdapter(adapter);
        }
    }
}
