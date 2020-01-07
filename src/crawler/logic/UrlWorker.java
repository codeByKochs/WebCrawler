package crawler.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlWorker extends Thread {

    private ConcurrentLinkedQueue<Url> urlQueue;
    private Map<Url, String> outputMap;
    private Url currentWorkingUrl;
    private String htmlText;
    private UrlFormatChecker urlFormatChecker;
    private int maximumCrawlingDepth;

    public UrlWorker(ConcurrentLinkedQueue<Url> urlQueue, Map<Url, String> outputMap, int maximumCrawlingDepth) {
        this.urlQueue = urlQueue;
        this.outputMap = outputMap;
        this.currentWorkingUrl = null;
        this.htmlText = "";
        this.urlFormatChecker = new UrlFormatChecker();
        this.maximumCrawlingDepth = maximumCrawlingDepth;
    }

    @Override
    public void run(){
        // get next Url object
        if (urlQueue.peek() != null) {
            currentWorkingUrl = urlQueue.poll();
        }

        if (currentWorkingUrl != null){
            //read htmlText from Url
            readTextFromUrl();
            currentWorkingUrl.setTitle(findUrlTitle(currentWorkingUrl));
            addUrlToOutputMap(currentWorkingUrl);

            // find new links from source code and add them to the queue
            findLinks();
        }
    }

    private String findUrlTitle(Url url){
        // pattern which matches html title indication
        String htmlTitleWrapperBeginning = "<title>";
        String htmlTitleWrapperEnd = "</title>";
        Pattern pattern = Pattern.compile(htmlTitleWrapperBeginning+".+"+htmlTitleWrapperEnd);
        Matcher matcher = pattern.matcher(htmlText);

        if (matcher.find()){
            return  matcher.group().substring(htmlTitleWrapperBeginning.length(),matcher.group().length()-htmlTitleWrapperEnd.length());
        }
        else{
            return null;
        }
    }

    private void findLinks(){
        // pattern which matches html link indication
        Pattern pattern = Pattern.compile("href=([\"'])(.*?)([\"'])");
        Matcher matcher = pattern.matcher(htmlText);

        while (matcher.find()){
            String foundUrlString = matcher.group().substring(6,matcher.group().length()-1);
            foundUrlString = urlFormatChecker.fixUrlFormat(foundUrlString, currentWorkingUrl.getUrl());
            if (foundUrlString != null){
                if (currentWorkingUrl.getCrawlDepth() <= maximumCrawlingDepth){
                    Url foundUrl = new Url(foundUrlString, currentWorkingUrl.getCrawlDepth()+1);
                    addUrlToQueue(foundUrl);
                }
            }
        }
    }

    private void addUrlToQueue(Url url){
        if (!urlQueue.contains(url)){
            urlQueue.add(url);
        }
    }

    private void addUrlToOutputMap(Url url){
        if (url.getTitle() != null){
            if (!outputMap.values().contains(url.getTitle())){
                outputMap.put(url, url.getTitle());
            }
        }
    }

    private void readTextFromUrl(){
        try {
            String LINE_SEPARATOR = System.getProperty("line.separator");

            final URLConnection urlConnection = new URL(currentWorkingUrl.getUrl()).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            final StringBuilder stringBuilder = new StringBuilder();

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                stringBuilder.append(nextLine);
                stringBuilder.append(LINE_SEPARATOR);
            }
            this.htmlText = stringBuilder.toString();
        } catch (Exception e) {
            /*
            // debug mode
                        System.out.println("An error occurred in thread "+Thread.currentThread()+": " + e.getClass() + " " + e.getMessage()+
                    "\n problem with url: "+currentWorkingUrl.getUrl());
             */

        }
    }
}
