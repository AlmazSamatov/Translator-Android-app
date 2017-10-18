package com.example.almaz.translator;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by almaz on 12.04.17.
 */

public class CustomAdapter extends ArrayAdapter<Word> {

    private ArrayList<Word> originalItems;
    private ArrayList<Word> filteredItems;
    private LayoutInflater inflater;

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource,
                         @NonNull ArrayList<Word> objects) {
        super(context, resource, objects);
        this.originalItems = objects;
        filteredItems = new ArrayList<Word>();
        filteredItems.addAll(this.originalItems);
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return filteredItems.size();
    }

    public Word getItem(int position) {
        return filteredItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                final ArrayList<Word> filteredList = new ArrayList<Word>();

                if (constraint.equals("") || constraint.toString().trim().length() == 0) {
                    results.values = originalItems;
                } else {
                    String textToFilter = constraint.toString().toLowerCase();
                    for (Word word : originalItems) {
                        if (word.getWord().length() >= textToFilter.length() &&
                                word.getWord().toLowerCase().contains(textToFilter)) {
                            filteredList.add(word);
                        }
                    }
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    filteredItems = (ArrayList<Word>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Word item = filteredItems.get(position);
        View v = null;

        if (convertView == null) {
            v = inflater.inflate(R.layout.list_item, parent, false);
        } else {
            v = convertView;
        }

        ImageButton button = (ImageButton) v.findViewById(R.id.addToFavourites2);
        TextView text = (TextView) v.findViewById(R.id.text);
        TextView translation = (TextView) v.findViewById(R.id.translation);
        final TextView language = (TextView) v.findViewById(R.id.languages);

        text.setText(item.getWord());
        translation.setText(item.getTranslation());
        language.setText(item.getSourceLanguage() + "-" +
                item.getTargetLanguage());

        DataBaseHelper dbhelper = new DataBaseHelper(v.getContext(), "Favourites.db");
        if (dbhelper.isInDataBase(item)) {
            button.setImageResource(R.drawable.selected_favourites_icon);
        }
        dbhelper.close();

        button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        String text = item.getWord();
                        String translation = item.getTranslation();
                        int[] languages = {item.getSourcePosition(), item.getTargetPosition()};
                        ImageButton button = (ImageButton) v.findViewById(R.id.addToFavourites2);
                        Word word = new Word(text, translation, languages[0], languages[1]);

                        DataBaseHelper dbhelper = new DataBaseHelper(getContext(), "Favourites.db");
                        if (dbhelper.isInDataBase(word)) {
                            dbhelper.setDeleted(word);
                            button.setImageResource(R.drawable.default_favourites_icon);
                        } else {
                            dbhelper.insertWord(word);
                            button.setImageResource(R.drawable.selected_favourites_icon);
                        }
                        dbhelper.close();

                    }
                });

        return v;
    }
}
