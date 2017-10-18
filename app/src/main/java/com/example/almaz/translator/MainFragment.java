package com.example.almaz.translator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

/**
 * Created by almaz on 16.04.17.
 */

public class MainFragment extends Fragment {

    private View rootView;
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText textToTranslate;
    private ImageButton addToFavourites;
    private ImageButton changeLanguages;
    private TextView translatedText;
    private boolean isFavourite; // if current word is favourite.
    private boolean noTranslate; // do not translate at 1-st text changing. Need when initialize
                                // with some text.
    private TextView apiInfo;
    private TextView link;

    /**
     * Initialize widget elements and create view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return created view of fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        spinner1 = (Spinner) rootView.findViewById(R.id.languages1);
        spinner2 = (Spinner) rootView.findViewById(R.id.languages2);
        textToTranslate = (EditText) rootView.findViewById(R.id.textToTranslate);
        textToTranslate.setMovementMethod(new ScrollingMovementMethod());
        textToTranslate.setVerticalScrollBarEnabled(true);
        changeLanguages = (ImageButton) rootView.findViewById(R.id.changeLanguages);
        addToFavourites = (ImageButton) rootView.findViewById(R.id.addToFavourites1);
        translatedText = (TextView) rootView.findViewById(R.id.translatedText);
        translatedText.setMovementMethod(new ScrollingMovementMethod());
        translatedText.setVerticalScrollBarEnabled(true);
        link = (TextView) rootView.findViewById(R.id.link);
        apiInfo = (TextView) rootView.findViewById(R.id.api_info);
        setArgs();
        return rootView;
    }

    /**
     * Add listeners and set data.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setSpinners();
        textChangedListener();
        addButtonListener();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("selection1", spinner1.getSelectedItemPosition());
        editor.putInt("selection2", spinner2.getSelectedItemPosition());
        editor.putString("textToTranslate", textToTranslate.getText().toString());
        editor.putString("translatedText", translatedText.getText().toString());
        editor.putBoolean("isFavourite", isFavourite);
        editor.apply();
        super.onDestroyView();
    }

    public void addToHistory() {
        String text = String.valueOf(textToTranslate.getText()).trim();
        if(!text.equals("")){
            DataBaseHelper dataBaseHelper = new DataBaseHelper(rootView.getContext(), "History.db");
            dataBaseHelper.insertWord(new Word(textToTranslate.getText().toString().trim(),
                    translatedText.getText().toString(), spinner1.getSelectedItemPosition(),
                    spinner2.getSelectedItemPosition()));
            dataBaseHelper.close();
        }
    }

    public void checkIfInFavourites(){
        String text = String.valueOf(textToTranslate.getText());
        if(!text.equals("")){
            addToFavourites.setVisibility(View.VISIBLE);
            apiInfo.setVisibility(View.VISIBLE);
            link.setVisibility(View.VISIBLE);

            DataBaseHelper dataBaseHelper = new DataBaseHelper(rootView.getContext(), "Favourites.db");
            if(dataBaseHelper.isInDataBase(new Word(text, translatedText.getText().toString(),
                    spinner1.getSelectedItemPosition(), spinner2.getSelectedItemPosition()))) {
                addToFavourites.setImageResource(R.drawable.selected_favourites_icon);
                isFavourite = true;
            } else{
                addToFavourites.setImageResource(R.drawable.default_favourites_icon);
                isFavourite = false;
            }
            dataBaseHelper.close();
        } else{
            isFavourite = false;
            addToFavourites.setVisibility(View.INVISIBLE);
            apiInfo.setVisibility(View.INVISIBLE);
            link.setVisibility(View.INVISIBLE);
            translatedText.setText("");
        }
    }

    public void setArgs() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
        String text = sharedPref.getString("textToTranslate", "");
        String translation = sharedPref.getString("translatedText", "");
        int selection1 = sharedPref.getInt("selection1", 0);
        int selection2 = sharedPref.getInt("selection2", 1);
        isFavourite = sharedPref.getBoolean("isFavourite", false);
        if (!text.equals("")) {
            noTranslate = true;
            textToTranslate.setText(text);
            spinner1.setSelection(selection1);
            spinner2.setSelection(selection2);
            translatedText.setText(translation);
            addToFavourites.setVisibility(View.VISIBLE);
            apiInfo.setVisibility(View.VISIBLE);
            link.setVisibility(View.VISIBLE);
            if(isFavourite){
                addToFavourites.setImageResource(R.drawable.selected_favourites_icon);
            } else{
                addToFavourites.setImageResource(R.drawable.default_favourites_icon);
            }
        }
    }

    public void addButtonListener() {

        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(v.getContext(),
                        "Favourites.db");
                String text = textToTranslate.getText().toString().trim();
                String translation = translatedText.getText().toString();
                int source = spinner1.getSelectedItemPosition();
                int target = spinner2.getSelectedItemPosition();
                Word item = new Word(text, translation, source, target);
                if(dataBaseHelper.isInDataBase(item)){
                    dataBaseHelper.deleteWord(item);
                    addToFavourites.setImageResource(R.drawable.default_favourites_icon);
                    isFavourite = false;
                } else{
                    isFavourite = true;
                    dataBaseHelper.insertWord(item);
                    addToFavourites.setImageResource(R.drawable.selected_favourites_icon);
                }
                dataBaseHelper.close();
            }
        });

        changeLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sourceLng = spinner1.getSelectedItemPosition();
                int targetLng = spinner2.getSelectedItemPosition();

                spinner1.setSelection(targetLng);
                spinner2.setSelection(sourceLng);

                translate(textToTranslate.getText().toString().trim());
            }
        });

    }

    public void setSpinners() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        if(Locale.getDefault().getLanguage().equals("en")) {
            Collections.addAll(categories, Languages.getLangsEN());
        } else{
            Collections.addAll(categories, Languages.getLangsRU());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
        spinner2.setSelection(1);
    }

    public void textChangedListener() {

        // Translate the text after 500 milliseconds when user ends to typing
        RxTextView.textChanges(textToTranslate).
                filter(charSequence -> charSequence.length() > 0).
                debounce(500, TimeUnit.MILLISECONDS).
                subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                translate(charSequence.toString().trim());
            }
        });

        RxTextView.textChanges(textToTranslate).
                filter(charSequence -> charSequence.length() == 0).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkIfInFavourites();
                            }
                        });
                    }
                });
    }

    private void translate(String text){
        if(noTranslate){
            noTranslate = false;
            return;
        }

        String APIKey = "trnsl.1.1.20170314T200256Z.c558a20c3d6824ff.7" +
                "860377e797dffcf9ce4170e3c21266cbc696f08";
        String language1 = String.valueOf(spinner1.getSelectedItem());
        String language2 = String.valueOf(spinner2.getSelectedItem());

        Retrofit query = new Retrofit.Builder().baseUrl("https://translate.yandex.net/").
                addConverterFactory(GsonConverterFactory.create()).build();
        APIHelper apiHelper = query.create(APIHelper.class);
        Call<TranslatedText> call = apiHelper.getTranslation(APIKey, text,
                langCode(language1) + "-" + langCode(language2));

        call.enqueue(new Callback<TranslatedText>() {
            @Override
            public void onResponse(Call<TranslatedText> call, Response<TranslatedText> response) {
                if(response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            translatedText.setText(response.body().getText().get(0));
                            checkIfInFavourites();
                            addToHistory();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TranslatedText> call, Throwable t) {}
        });
    }

    public String langCode(String selectedLang) {
        String code = null;

        if(Locale.getDefault().getLanguage().equals("en")) {
            for (int i = 0; i < Languages.getLangsEN().length; i++) {
                if(selectedLang.equals(Languages.getLangsEN()[i])){
                    code = Languages.getLangCodeEN(i);
                }
            }
        } else{
            for (int i = 0; i < Languages.getLangsRU().length; i++) {
                if(selectedLang.equals(Languages.getLangsRU()[i])){
                    code = Languages.getLangCodeRU(i);
                }
            }
        }
        return code;
    }
}
