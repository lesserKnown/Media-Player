import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class lyricsOnline {

    protected WebDriver driver;
    protected String url = "https://www.lyrical-nonsense.com/#search";
    protected String urlGen = "https://genius.com/" ;

    public lyricsOnline() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options); //add options for headless
    }

    public void getURL(String s){
        lyricsDump.lyricsHTML0 = null;
        lyricsDump.albumImageURL = null;

        threadGetURL(s,'g');
        if(lyricsDump.lyricsHTML0 == null && lyricsDump.albumImageURL == null){
            threadGetURL(s,'n');
        }
    }

    public void threadGetURL(String s, char c){ //get url for lyrics and image
        switch(c){
            case 'g':{
                if(linkExists(urlGen)){ //genius.com
                    driver.get(urlGen);
                }
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                WebElement inputGen = driver.findElement(By.name("q"));
                inputGen.sendKeys(s);
                WebElement searchGen = driver.findElement(By.xpath("//div[contains(@class,'Icon')]"));
                searchGen.click();
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                try{
                    WebElement topResult = driver.findElement(By.xpath("//div[contains(@tracking,'click')]"));
                    topResult.click();
                    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                    Document doc = Jsoup.connect(driver.getCurrentUrl()).userAgent("Chrome").get();
                    if(s.toUpperCase().contains(doc.selectFirst("h1").text().toUpperCase())
                            && s.toUpperCase().contains(doc.selectFirst("h2").text().toUpperCase())) {
                        WebElement image = driver.findElement(By.className("cover_art-image"));
                        lyricsDump.albumImageURL = image.getAttribute("src");
                        lyricsDump.lyricsHTML0 = driver.getCurrentUrl();
                    }
                    else{
                        throw new Exception("incorrect header");
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
                if(lyricsDump.lyricsHTML0 != null && lyricsDump.albumImageURL != null) {
                    driver.close();
                }
                break;
            }
            case 'n':{
                if(linkExists(url)) { //lyrical-nonsense
                    driver.get(url);
                }
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                WebElement search = driver.findElement(By.name("search"));
                search.sendKeys(s);
                WebElement searchButton = driver.findElement(By.className("gsc-search-button"));
                searchButton.click();
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                try {
                    WebElement weblink = driver.findElement(By.className("gsc-thumbnail-inside"));
                    weblink.click();
                    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                    for (String handle : driver.getWindowHandles()) {
                        driver.switchTo().window(handle);
                        if(!driver.getCurrentUrl().contains(url)){
                            Document doc = Jsoup.connect(driver.getCurrentUrl()).userAgent("Chrome").get();
                            String[] text = doc.select("h1").text().split("歌詞");
                            if(s.contains(text[0].trim())
                                    && s.contains(text[1].trim())) {
                                lyricsDump.lyricsHTML0 = driver.getCurrentUrl();
                                WebElement img = driver.findElement(By.className("imgbor"));
                                lyricsDump.albumImageURL = img.getAttribute("srcset");
                            }
                            else{
                                throw new Exception("incorrect header");
                            }
                            break;
                        }
                    }
                } catch (Exception i){
                    i.printStackTrace();
                }
                driver.quit();
                break;
            }
            default: System.out.println("incorrect url char");
        }
    }

    public static String parseLyrics(String html) throws IOException {
        Document doc = Jsoup.connect(html).userAgent("Chrome").get();
        String lyrics;
        int remove1, remove2;
        if(html.contains("genius.com")) {
            Elements lyricsElem = doc.select("div.lyrics");
            lyrics = lyricsElem.select("p").toString();
            while(lyrics.contains("<a")) {
                remove1 = lyrics.indexOf("<a");
                remove2 = lyrics.indexOf(">", remove1);
                lyrics = lyrics.replace(lyrics.substring(remove1,remove2+1),"");
            }
            lyrics = lyrics.replace("<br>", "\n");
            lyrics = lyrics.replace("</a>","");
            lyrics = lyrics.replace("<p>", "");
            lyrics = lyrics.replace("</p>", "");
            lyrics = lyrics.replace("<i>","");
            lyrics = lyrics.replace("</i>","");

        }
        else {
            Elements lyricsElem = doc.select("div.olyrictext");
            lyrics = lyricsElem.select("p").toString();
            lyrics = lyrics.replace("<p>", "\n");
            lyrics = lyrics.replace("</p>", "");
        }
        lyrics = lyrics.trim();
        return lyrics;
    }

    public static boolean linkExists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection conn = (HttpURLConnection) new URL(URLName).openConnection();
            conn.setRequestMethod("HEAD");
            return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);

        } catch (Exception e){
            return false;
        }
    }

}
