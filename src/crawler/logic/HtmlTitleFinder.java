package crawler.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTitleFinder {

    private static HtmlTitleFinder instance;

    // implements singelton pattern
    public static HtmlTitleFinder getInstance(){
        if (instance == null){
            instance = new HtmlTitleFinder();
        }
        return instance;
    }

    public String findTitle(String url){

        String data = readTextFromUrl(url);

        String htmlTitleWrapperBeginning = "<title>";
        String htmlTitleWrapperEnd = "</title>";
        Pattern pattern = Pattern.compile(htmlTitleWrapperBeginning+".+"+htmlTitleWrapperEnd);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()){
            return  matcher.group().substring(htmlTitleWrapperBeginning.length(),matcher.group().length()-htmlTitleWrapperEnd.length());
        }
        else{
            return "Title not found";
        }
    }

    public String readTextFromUrl(String url) {
        try {
            String LINE_SEPARATOR = System.getProperty("line.separator");

            final URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            final StringBuilder stringBuilder = new StringBuilder();

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                stringBuilder.append(nextLine);
                stringBuilder.append(LINE_SEPARATOR);
            }
            final String siteText = stringBuilder.toString();
            return siteText;
        } catch (Exception e) {
            return "An error occurred: " + e.getClass() + " " + e.getMessage();
        }
    }
}
