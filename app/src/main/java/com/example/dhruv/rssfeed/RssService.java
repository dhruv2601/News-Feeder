package com.example.dhruv.rssfeed;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.SyncStateContract;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * Created by dhruv on 7/8/16.
 */
public class RssService extends IntentService {

    public static final String TAG = "RssServvice";

/////////////           INDIA          //////////////

//    public static final String RSS_LINK = "http://timesofindia.indiatimes.com/rssfeeds/-2128839596.cms";   //TOI (remember videos too)
//    public static final String RSS_LINK = "http://economictimes.indiatimes.com/rss.cms";  //ECONOMIC TIMES
    public static final String RSS_LINK = "http://www.thehindu.com/navigation/?type=rss"; //THE HINDU

/////////////    ANGREZZI     /////////////////

//    public static final String RSS_LINK = "http://www.nytimes.com/services/xml/rss/index.html";   //NYtimes ye best hai iski sare categ include kr lio
//    private static final String RSS_LINK = "http://www.pcworld.com/index.rss";
//    private static final String RSS_LINK = "http://rss.msn.com/en-in/";   // MSN KI FEED
//    public static final String RSS_LINK = "http://feeds.reuters.com/reuters/topNews"; //reuters ki feed
//    public static final String RSS_LINK = "http://edition.cnn.com/services/rss/"; // CNN KE RSS SITE HAI, AGEE KE EXTENSIONS DYE HUEN HN FOR AROUND 10 THINGS, PARTY!!!
//    public static final String  RSS_LINK = "http://www.bbc.com/news/10628494";  // BBC KE HN SARE ISKE ANDAR
//    public static final String RSS_LINK = "http://news.sky.com/info/rss"; //SKY NEWS KE SARE
//    public static final String RSS_LINK = "http://www.cbsnews.com/rss/";   //even contains a video section
//    public static final String RSS_LINK = "http://feeds.abcnews.com/abcnews/topstories";     // abc news whoa


    public static final String ITEMS = "items";
    public static final String RECEIVER = "receiver";

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service started");
        List<RssItem> rssItems = null;
        try {
            PcWorldRssParser parser = new PcWorldRssParser();
            rssItems = parser.parse(getInputStream(RSS_LINK));
        } catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEMS, (Serializable) rssItems);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        receiver.send(0, bundle);
    }

    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Log.w(TAG, "Exception while retrieving the input stream", e);
            return null;
        }
    }
}