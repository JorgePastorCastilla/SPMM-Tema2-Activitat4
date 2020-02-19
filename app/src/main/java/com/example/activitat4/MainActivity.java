package com.example.activitat4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Les columnes que volem obtenir per a cada element.
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        //Condició: volem obtenir totes les files (per això és null).
//        String where = ContactsContract.Contacts.HAS_PHONE_NUMBER + "= 1";
//        String where = ContactsContract.Contacts.DISPLAY_NAME + "= 'Pep'";
        /*String where = ContactsContract.Contacts.DISPLAY_NAME + "= ?";
        String[] whereArgs = {"Pep"};*/

        inserirEntradaDiccionari();

        String where = null;
        String[] whereArgs = null;
        //Ordre: que estiguin ordenats de forma ascendent.
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor c = getContentResolver().query(
                // Columnes per obtenir de cada fil
                ContactsContract.Contacts.CONTENT_URI, projection,
                where, // Criteri de selecció
                whereArgs, // Criteri de selecció
                sortOrder // Ordre
        );

        if (c == null){
        //Codi per tractar l’error, escriure logs, etc.
        }
        //Si el cursor està buit, el proveïdor no ha trobat resultats.
        else if (c.getCount() < 1) {
        //El cursor està buit, el content provider no té elements.
            Toast.makeText(this, "No hi ha dades", Toast.LENGTH_SHORT).show(); }
        else {
        //Dades obtingudes
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        }

        while(c.moveToNext()) {
            //Obtenir ID
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            //Obtenir nom
            String nomContacte = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //Saber si té telèfon
            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            Toast.makeText(this, "id: "+ contactId + "\n" + "Nom: "+ nomContacte +"\n Tételèfon: " + hasPhone , Toast.LENGTH_SHORT).show();
            String telefon = null;
            String email = null;
            if (hasPhone.compareTo("1") == 0) {
                // Obtenim els telèfons
                Cursor telefons=getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+
                                contactId,null, null); //Recorrem els telèfons
                while (telefons.moveToNext()) {
                    telefon=telefons.getString(telefons.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                //Tanquem el cursor
                telefons.close();
            }
            //Obtenir cursor correus
            Cursor emails =
                    getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,ContactsContract.CommonDataKinds.Email.CONTACT_ID+ " = " +
                                    contactId, null, null); //Recorrem els correus
            while (emails.moveToNext()) {
                email = emails.getString
                        (emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            //Tanquem el cursor
            emails.close();
            //Mostrar
            Toast.makeText(this, "id: "+ contactId + "\n" + "Nom: "+ nomContacte +"\nTelefon: " + telefon + "\n email: " +email,Toast.LENGTH_SHORT).show();
        }
        c.close();

    }//FINAL DEL ONCREATE

    //        A PROBAR COSITAS
    public void inserirEntradaDiccionari(){
        Uri UriNou;
        ContentValues Valors = new ContentValues();
        //Creem el valor de la nova entrada
        Valors.put(UserDictionary.Words.APP_ID, "com.example.activitatt2_4");
        Valors.put(UserDictionary.Words.LOCALE, "es_ES");
        Valors.put(UserDictionary.Words.WORD, "cerdo");
        Valors.put(UserDictionary.Words.FREQUENCY, "100");
        //Inserim
        getContentResolver().insert( UserDictionary.Words.CONTENT_URI,Valors );
    }
}
