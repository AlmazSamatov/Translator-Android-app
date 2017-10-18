package com.example.almaz.translator;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(savedInstanceState == null){
            changeToMainView();
        }
        bottomNavigationListener();
    }

    public void bottomNavigationListener() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_translate:
                                changeToMainView();
                                break;
                            case R.id.action_favourites:
                                changeToListView("Favourites.db");
                                break;
                            case R.id.action_history:
                                changeToListView("History.db");
                                break;
                        }
                        return true;
                    }
                });
    }

    public void changeToListView(String nameOfDB) {
        // Change current fragment in activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ListViewFragment listViewFragment = new ListViewFragment().newInstance(nameOfDB);
        ft.replace(R.id.fragment, listViewFragment);
        ft.commit();
    }

    public void changeToMainView() {
        // Change current fragment in activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new MainFragment());
        ft.commit();
    }

    public void TrashOnClick(View v) {
        String title = getSupportActionBar().getTitle().toString();
        if (title.equals(getString(R.string.text_history))) {
            showConfirmationDialog(R.string.delete_confirmation_of_history_words, "History.db",
                    v.getContext());
        } else {
            showConfirmationDialog(R.string.delete_confirmation_of_favourite_words, "Favourites.db",
                    v.getContext());
        }
    }

    public void showConfirmationDialog(int answerID, final String nameOfDB, final Context context) {
        int title = R.string.text_history;
        if (nameOfDB.equals("Favourites.db")) {
            title = R.string.text_favourites;
        }
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(answerID)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    // if user agrees to delete words, then delete
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteWordsFromDB(context, nameOfDB);
                        changeToListView(nameOfDB);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void deleteWordsFromDB(Context context, String nameOfDB) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, nameOfDB);
        dataBaseHelper.deleteAllWords();
        dataBaseHelper.close();
    }
}