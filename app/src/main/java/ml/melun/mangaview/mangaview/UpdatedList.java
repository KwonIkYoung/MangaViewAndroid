package ml.melun.mangaview.mangaview;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UpdatedList {
    public void UpdatedList(){

        //
    }
    public void fetch(){
        result = new ArrayList<>();
        String url = "https://mangashow.me/bbs/board.php?bo_table=msm_manga&page=";
        if(!last) {
            try {
                page++;
                Document document = Jsoup.connect(url + page)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                        .get();
                Elements items = document.select("div.list-row");
                if (items.size() < 50) last = true;
                for(Element item : items){
                    String ttmp = (item.selectFirst("div.thumb").attr("style").split("\\(")[1].split("\\)")[0]);
                    String ntmp = item.selectFirst("div.subject").text().replace('\n',' ');
                    int itmp = Integer.parseInt(item.selectFirst("div.data-container").attr("data-wrid"));
                    Manga tmp = new Manga(itmp,ntmp);
                    tmp.addThumb(ttmp);
                    result.add(tmp);
                }
            } catch (Exception e) {
                page--;
            }
        }
    }

    public ArrayList<Manga> getResult() {
        return result;
    }
    public boolean isLast(){return last;}

    Boolean last = false;
    ArrayList<Manga> result;
    int page = 0;
}
