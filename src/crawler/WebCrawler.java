package crawler;

import crawler.gui.WebCrawlerWindow;
import crawler.logic.ThreadManager;

public class WebCrawler {
    ThreadManager manager = new ThreadManager();
    WebCrawlerWindow window = new WebCrawlerWindow(manager);

}
