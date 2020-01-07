package crawler.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFormatChecker {

    public String fixUrlFormat(String url, String parentUrl){

        parentUrl = extractBaseUrl(parentUrl);

        //TODO check if page can be pinged → url is working normally

            if (isAbsoluteUrl(url)){
                return url;
            }
            else if (isUrlWithCountryCode(url)){
                return "https:"+url;
            }
            else if (isRelativeUrl(url)){
                url = parentUrl + url;
            return url;
        }
        else if (isUrlWithoutProtocol(url)){
            url = "https://www." + url;
            return url;
        }
        else {
            return null;
        }
    }

    public static boolean isUrlWithoutProtocol(String url){
        Pattern urlWithoutProtocolPattern = Pattern.compile("[/]{0,2}[a-z]{2}\\..+\\.[a-z]{2,3}");
        Matcher matcher = urlWithoutProtocolPattern.matcher(url);
        if (matcher.find()){
            return true;
        }
        return false;
    }

    public static boolean isUrlWithCountryCode(String url){
        Pattern urlWithoutProtocolPattern = Pattern.compile("^//[a-z]{2}\\..+");
        Matcher matcher = urlWithoutProtocolPattern.matcher(url);
        if (matcher.find()){
            return true;
        }
        return false;
    }

    public static boolean isAbsoluteUrl(String url){
        Pattern absoluteUrlPattern = Pattern.compile("https?://([w]{3}|[a-z]{0,2})\\..+\\.[a-z]{2,3}");
        Matcher matcher = absoluteUrlPattern.matcher(url);
        if (matcher.find()){
            return true;
        }
        return false;
    }

    public static boolean isRelativeUrl(String url){
        Pattern relativeUrlPattern = Pattern.compile("^/"); //[^/]
        Matcher matcher = relativeUrlPattern.matcher(url);
        if (matcher.find()){
            return true;
        }
        return false;
    }

    public static String extractBaseUrl(String url){
        Pattern pattern = Pattern.compile("https?://([w]{3}|[a-z]{2})\\..+\\.[a-z]{2,3}");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()){
            return matcher.group();
        }
        else{
            return url;
        }
    }
}
