package com.example.universidadedocaldo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    public TextView quoteTextView;
    public ImageView nextbuttonImageView;
    public ImageView prevbuttonImageView;
    public ImageView sharebuttonImageView;
    public ImageView favebuttonImageView;
    public ArrayList<Quote> quoteList;
    public Stack<Quote> previousQuotes;
    public int index;
    public boolean isPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = (TextView) findViewById(R.id.quotes_textview);
        nextbuttonImageView = (ImageView) findViewById(R.id.next_button);
        prevbuttonImageView = (ImageView) findViewById(R.id.previous_button);
        sharebuttonImageView = (ImageView) findViewById(R.id.share_button);
        favebuttonImageView = (ImageView) findViewById(R.id.fave_button_off);

        // Step 1 import quotes from strings.xml
        Resources res = getResources();
        String[] allQuotes = res.getStringArray(R.array.quotes);
        String[] allAuthors = res.getStringArray(R.array.authors);
        quoteList = new ArrayList<>();
        addToQuoteList(allQuotes, allAuthors);

        previousQuotes = new Stack<>();

        // Step 2 generate random quotes
        final int quotesLength = quoteList.size();
        index = getRandomQuote (quotesLength-1);
        quoteTextView.setText(quoteList.get(index).toString());


        // Step 3 generate new random quote when next button is pressed
        nextbuttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrevious= false;
                index = getRandomQuote (quotesLength-1);
                quoteTextView.setText(quoteList.get(index).toString());
                previousQuotes.push(quoteList.get(index));
            }
        });
        // Step 4 recall previous quote when back button pressed
        prevbuttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPrevious && previousQuotes.size() > 0) {
                    previousQuotes.pop();
                    isPrevious = true;
                }
                if(previousQuotes.size() > 0 && isPrevious)
                    quoteTextView.setText(previousQuotes.pop().toString());
                else
                    Toast.makeText(MainActivity.this, "Pulsa la otra flecha para ver una nueva frase.", Toast.LENGTH_SHORT).show();
            }
        });

        // Step 5 share quote on social media
        sharebuttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, quoteList.get(index).toString());
                sendIntent.setType("text/plain");
                startActivity (sendIntent);
            }
        });

        // Step 6 fave quote function
        favebuttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quoteList.get(index).isFavorite()) {
                    favebuttonImageView.setImageResource(R.mipmap.heart_off);
                    quoteList.get(index).setFavorite(false);
                }
                else {
                    favebuttonImageView.setImageResource(R.mipmap.heart_on);
                    quoteList.get(index).setFavorite(true);
                }
            }
        });
    }

    public void addToQuoteList(String[] allQuotes, String[] allAuthors) {
        for(int i=0; i < allQuotes.length; i++) {
            String quote = allQuotes[i];
            String author = allAuthors[i];
            Quote newquote = new Quote(quote, author);
            quoteList.add(newquote);
        }

    }

    public int getRandomQuote (int length) {
        return (int) (Math.random() * length) +1;

    }



}