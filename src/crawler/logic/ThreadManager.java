package crawler.logic;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadManager{

    static final int NUMBER_OF_THREADS_UPPER_LIMIT = 20;
    static final int NUMBER_OF_THREADS_LOWER_LIMIT = 1;

    static final int CRAWLING_DEPTH_UPPER_LIMIT = 50;
    static final int CRAWLING_DEPTH_LOWER_LIMIT = 1;

    static final int TIME_LIMIT_UPPER_LIMIT = 1000;
    static final int TIME_LIMIT_LOWER_LIMIT = 1;

    //TODO remove?
    static final int DEFAULT_TIME_LIMIT = 30;
    static final int DEFAULT_CRAWLING_DEPTH_LIMIT = 50;

    private ConcurrentLinkedQueue<Url> urlQueue;
    private Timer timer;
    private int numberOfThreads;
    private int maximumCrawlingDepth;
    private int parsingTimeLimit;

    private Map<Url, String> outputMap;
    private UrlWorker[] urlWorkers;
    private JLabel messageLabel;
    private boolean isTimeLimited;
    private boolean isDepthLimited;


    public ThreadManager() {
        this.urlQueue = new ConcurrentLinkedQueue<Url>();
        this.outputMap = new ConcurrentHashMap<Url, String>();

        //set default values for timeLimit and maximum crawling depth
        this.parsingTimeLimit = DEFAULT_TIME_LIMIT;
        this.maximumCrawlingDepth = DEFAULT_CRAWLING_DEPTH_LIMIT;

        this.timer = new Timer();
        this.isTimeLimited = false;

        this.isDepthLimited = false;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void setMaximumCrawlingDepth(int maximumCrawlingDepth) {
        isDepthLimited = true;
        this.maximumCrawlingDepth = maximumCrawlingDepth;
    }

    public void setTimeLimit(int ParsingTimeLimit) {
        isTimeLimited = true;
        timer.setTimeLimit(ParsingTimeLimit);
    }

    public void setStartingUrl(String startingUrl) {
        urlQueue.add(new Url(startingUrl,0));
    }

    public void setMessageLabel(JLabel messageLabel) {this.messageLabel = messageLabel;}

    public Map<Url, String> getOutputMap() {
        return outputMap;
    }

    public int getParsedPagesCount(){
        return outputMap.size();
    }

    public int getElapsedTime(){return timer.getElapsedTime();}

    public void run(){

        //check if all inputs are valid
        if (isValidInput()){
            //reset timer
            timer.reset();

            createUrlWorkers();

            messageLabel.setForeground(Color.BLACK);
            messageLabel.setText("parsing...");

            timer.start();

            for (UrlWorker worker : urlWorkers){
                worker.start();
            }

            while (!timer.getIsTimeLimitReached()){
                runWorkers();
            }

            stop();

            messageLabel.setForeground(Color.BLACK);
            messageLabel.setText("finished parsing");

            /*
            //for debugging
            for (Url url : outputMap.keySet()){
                System.out.println(url.getUrl()+"  →  "+url.getTitle()+"    →    "+url.getCrawlDepth());
            }
             */
        }
    }

    public void stop(){
        for (UrlWorker worker : urlWorkers){
            worker.interrupt();
            timer.stop();
        }
    }

    private void createUrlWorkers(){
        this.urlWorkers = new UrlWorker[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            this.urlWorkers[i] = new UrlWorker(urlQueue, outputMap, maximumCrawlingDepth);
        }
    }

    private void runWorkers(){
        for (UrlWorker worker : urlWorkers){
            worker.run();
        }
    }

    private boolean isValidInput(){

        if (urlQueue.isEmpty()){
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Set start URL");
            return false;
        }

        if (NUMBER_OF_THREADS_LOWER_LIMIT > numberOfThreads || NUMBER_OF_THREADS_UPPER_LIMIT < numberOfThreads){
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Set number of Thread Workers to a number between " + NUMBER_OF_THREADS_LOWER_LIMIT + " and " + NUMBER_OF_THREADS_UPPER_LIMIT);
            return false;
        }

        if (!isTimeLimited && !isDepthLimited){
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Limit crawling depth or set time limit");
            return false;
        }

        if (isDepthLimited){
            if (CRAWLING_DEPTH_LOWER_LIMIT > maximumCrawlingDepth || CRAWLING_DEPTH_UPPER_LIMIT < maximumCrawlingDepth){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Set crawling depth to a number between " + CRAWLING_DEPTH_LOWER_LIMIT + " and " + CRAWLING_DEPTH_UPPER_LIMIT);
                return false;
            }
        }

        if (isTimeLimited){
            if (TIME_LIMIT_LOWER_LIMIT > parsingTimeLimit || TIME_LIMIT_UPPER_LIMIT < parsingTimeLimit){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Set time Limit to a number between " + TIME_LIMIT_LOWER_LIMIT + " and " + TIME_LIMIT_UPPER_LIMIT);
                return false;
            }
        }

        messageLabel.setText("");
        return true;
    }
}
