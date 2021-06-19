package pl.polsl.pp.backapp.topic;

import java.util.Comparator;

public class TopicComparator implements Comparator<Topic> {

    public int compare(Topic a, Topic b)
    {
        return a.getPageViews() - b.getPageViews();
    }
}
