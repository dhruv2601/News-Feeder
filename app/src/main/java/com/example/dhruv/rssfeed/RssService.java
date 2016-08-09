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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhruv on 7/8/16.
 */
public class RssService extends IntentService {

    public static final String TAG = "RssService";


    public String[] topStories = new String[]
            {
                    "http://www.thehindu.com/?service=rss",
                    "http://timesofindia.indiatimes.com/rssfeedstopstories.cms",
                    "http://feeds.abcnews.com/abcnews/topstories",
                    "http://rss.nytimes.com/services/xml/rss/nyt/AsiaPacific.xml",
                    "http://economictimes.indiatimes.com/rssfeedstopstories.cms",
                    "http://indiatoday.intoday.in/rss/article.jsp?sid=36"
            };

    public String sports[] = new String[]
            {
                    "http://timesofindia.indiatimes.com/rssfeeds/4719148.cms",
                    "http://www.mirror.co.uk/sport/football/rss.xml",  // football with love
                    "http://indiatoday.intoday.in/rss/article.jsp?sid=41",
                    "http://www.espncricinfo.com/rss/content/story/feeds/6.xml"
            };



    public static final String ITEMS = "items";
    public static final String RECEIVER = "receiver";

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service started");
        List<RssItem> rssItems = new ArrayList<RssItem>();
        List<RssItem> temp = null;

        int j = 0;
        for (int i = 0; i < topStories.length; i++) {
            try {
                PcWorldRssParser parser = new PcWorldRssParser();
                temp = parser.parse(getInputStream(topStories[i]));
                int l = 2;
                for (int k = j; k <= j + 3; k++) {
                    Log.d(TAG,"temp: "+temp.get(l).getTitle());
                    rssItems.add(k, temp.get(l));
                    ++l;
                }
                ++j;

            } catch (XmlPullParserException e) {
                Log.w(e.getMessage(), e);
            } catch (IOException e) {
                Log.w(e.getMessage(), e);
            }
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