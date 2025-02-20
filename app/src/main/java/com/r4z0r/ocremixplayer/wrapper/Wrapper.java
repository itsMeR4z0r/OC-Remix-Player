package com.r4z0r.ocremixplayer.wrapper;


import androidx.annotation.RequiresApi;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseResultItemGameList;
import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseResultItemMusicList;
import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseSongInfo;
import com.r4z0r.ocremixplayer.wrapper.models.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Wrapper {
    private final String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.0.0";
    private final String baseUrl = "https://ocremix.org";
    private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
    private final OkHttpClient client;

    public Wrapper(OkHttpClient client) {
        this.client = client;
    }

    public Wrapper() {
        this.client = new OkHttpClient
                .Builder()
                .retryOnConnectionFailure(true)
                .build();
    }
    public void searchByGameTitle(int offset, String gameTitle, OnResponseResultItemGameList onResponseResultItemGameList) {
        String url = baseUrl + "/quicksearch/game/?qs_query=" + URLEncoder.encode(gameTitle) + "&offset=" + offset;
        log.info("searchByGameTitle - URL: " + url);
        log.info("searchByGameTitle - Searching...");
        makeRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onResponseResultItemGameList.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onResponseResultItemGameList.onError(new IOException("Unexpected response"));
                    return;
                }

                List<ResultItemGame> resultItems = new ArrayList<>();
                Document doc = Jsoup.parse(response.body().string());

                doc.select("#main-content > div:nth-child(2) > div > div:nth-child(2) > section > div > table > tbody > tr").forEach(node -> {
                    ResultItemGame tmp = new ResultItemGame();
                    tmp.setSystemUrl(node.child(1).child(0).attr("href"));
                    tmp.setSystem(node.child(1).child(0).child(0).attr("alt"));
                    tmp.setSystemImage(node.child(1).child(0).child(0).attr("src"));
                    tmp.setGameUrl(node.child(3).child(1).attr("href"));
                    tmp.setGameTitle(node.child(3).child(1).text());
                    tmp.setPublisher(node.child(3).child(3).child(2).text());
                    tmp.setPublisherUrl(node.child(3).child(3).child(2).attr("href"));
                    tmp.setYear(Integer.parseInt(node.child(3).child(3).child(3).text().replace(",", "").trim()));

                    node.child(5).childNodes().stream()
                            .filter(nodeArtista -> nodeArtista.nodeName().equals("a"))
                            .forEach(nodeArtista -> tmp.addArtist(Objects.requireNonNull(nodeArtista.firstChild()).outerHtml(), nodeArtista.attr("href")));

                    tmp.setRemixes(getIntFromNode(node.child(7)));
                    tmp.setAlbuns(getIntFromNode(node.child(9)));

                    if (!node.child(11).childNodes().isEmpty()) {
                        tmp.setHasChipTune(true);
                        tmp.setChipTuneUrl(node.child(11).child(0).attr("href"));
                    }
                    resultItems.add(tmp);
                });

                onResponseResultItemGameList.onSuccess(resultItems);
            }
        });
    }


    private HashMap<String, Integer> extractPageNums(Document doc) {
        int numPage = 1;
        int offset = 0;
        int numResults = 0;
        int maxPage = 1;
        for (var node : doc.body().select("#main-content > div:nth-child(2) > div > div:nth-child(2) > section > div > nav:nth-child(2) > ul > li")) {
            if (node.hasClass("page-item d-none d-sm-block")) {
                var temp = node.childNode(0).childNode(0).outerHtml();
                temp = temp.substring(temp.indexOf("to") + 2, temp.indexOf("of")).trim();
                offset = Integer.parseInt(temp);
                temp = node.childNode(0).childNode(0).outerHtml();
                numResults = Integer.parseInt(temp.substring(temp.indexOf("of") + 2).trim());
            }
        }
        maxPage = (int) Math.ceil((double) numResults / offset);
        log.info("searchByGameTitle - Num results: " + numResults);
        log.info("searchByGameTitle - Num page: " + numPage);
        log.info("searchByGameTitle - Offset: " + offset);


        var result = new HashMap<String, Integer>();
        result.put("numPage", numPage);
        result.put("offset", offset);
        result.put("numResults", numResults);
        result.put("maxPage", maxPage);
        return result;
    }

    private void makeRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", userAgent)
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getLastSongs(int offset, OnResponseResultItemMusicList onResponseResultItemMusicList) {
        String url = baseUrl + "/remixes/?&offset=" + offset + "&sort=datedesc";
        log.info("getLastSongs - URL: " + url);
        makeRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onResponseResultItemMusicList.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onResponseResultItemMusicList.onError(new IOException("Unexpected response"));
                    return;
                }

                List<ResultItemMusic> resultItems = new ArrayList<>();
                Document doc = Jsoup.parse(response.body().string());

                doc.select("#main-content > div:nth-child(2) > div > div:nth-child(2) > section > div > table > tbody > tr").forEach(node -> {
                    ResultItemMusic tmp = new ResultItemMusic();
                    SongItem songItem = new SongItem();

                    tmp.setGameImageUrl(node.select("td:nth-child(1) > div.d-none.d-sm-block.img-frame > a > img").attr("src"));
                    var td1 = node.select("td").get(0);

                    tmp.setGameUrl(td1.select("div:nth-child(2) > span:nth-child(1) > a").attr("href"));
                    tmp.setGameTitle(td1.select("div:nth-child(2) > span:nth-child(1) > a").text());

                    var linkMusica = td1.select("div:nth-child(2) > a").get(0);
                    songItem.setUrl(linkMusica.attr("href"));
                    songItem.setName(linkMusica.text().replace("\"", "").trim());
                    songItem.setPreview(linkMusica.attr("data-preview"));

                    td1.select("span.color-secondary.single-line-item > a").forEach(nodeSong ->
                            songItem.addOriginalSong(nodeSong.text(), nodeSong.attr("href")));

                    node.select("td").get(1).select("span > a").forEach(nodeArtist ->
                            songItem.addArtist(nodeArtist.text(), nodeArtist.attr("href")));

                    var td3 = node.select("td.text-center.color-secondary").get(0);
                    try {
                        songItem.setDate(formatter.parse(td3.text().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tmp.getMusicList().add(songItem);
                    resultItems.add(tmp);
                });
                onResponseResultItemMusicList.onSuccess(resultItems);
            }
        });
    }

    public void getMusicByGameOrPeople(String urlParam, OnResponseResultItemMusicList onResponseResultItemMusicList) {
        String url = (urlParam == null) ? baseUrl + "/remixes/?&offset=0&sort=datedesc" : baseUrl + urlParam + "/remixes";
        makeRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onResponseResultItemMusicList.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onResponseResultItemMusicList.onError(new IOException("Unexpected response"));
                    return;
                }

                List<ResultItemMusic> resultItems = new ArrayList<>();
                Document doc = Jsoup.parse(response.body().string());
                ResultItemMusic tmp = null;

                for (var node : doc.select("#main-content > div:nth-child(2) > div > div:nth-child(2) > section > div > table > tbody > tr")) {
                    if (node.hasClass("area-link")) {
                        Date songDate = null;
                        if (!node.child(5).childNodes().isEmpty()) {
                            try {
                                songDate = formatter.parse(node.child(5).text());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        int indexSong;
                        if (tmp != null) {
                            indexSong = tmp.addSong(node.child(1).child(1).text(), node.child(1).child(1).attr("href"), songDate, node.child(1).child(1).attr("data-preview"));
                        } else {
                            indexSong = 0;
                        }
                        ResultItemMusic finalTmp = tmp;
                        node.child(1).child(3).children().forEach(node2 -> {
                            if (node2.nodeName().equals("a")) {
                                finalTmp.getMusicList().get(indexSong).addOriginalSong(node2.text(), node2.attr("href"));
                            }
                        });

                        ResultItemMusic finalTmp1 = tmp;
                        node.child(3).child(0).children().forEach(node2 -> {
                            if (node2.nodeName().equals("a")) {
                                finalTmp1.getMusicList().get(indexSong).addArtist(node2.child(0).outerHtml(), node2.attr("href"));
                            }
                        });
                    } else {
                        tmp = new ResultItemMusic();
                        tmp.setGameUrl(node.child(1).child(1).attr("href"));
                        tmp.setGameTitle(node.child(1).child(1).text());
                        tmp.setGameImageUrl(node.child(1).child(0).child(0).attr("src"));
                        resultItems.add(tmp);
                    }
                }
                onResponseResultItemMusicList.onSuccess(resultItems);
            }
        });
    }

    public void searchByMusicTitle(int offset, String musicTitle, OnResponseResultItemMusicList onResponseResultItemMusicList) throws IOException {
        String url = baseUrl + "/quicksearch/remix/?qs_query=" + URLEncoder.encode(musicTitle) + "&offset=" + offset;
        log.info("searchByMusicTitle - URL: " + url);
        log.info("searchByMusicTitle - Searching...");
        makeRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onResponseResultItemMusicList.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                List<ResultItemMusic> resultItems = new ArrayList<>();
                try {

                    Document doc = Jsoup.parse(response.body().string());
                    ResultItemMusic tmp = null;

                    for (var node : doc.body().select("#main-content > div:nth-child(2) > div > div:nth-child(2) > section > div > table > tbody > tr")) {
                        if (node.hasClass("area-link")) {
                            Date songDate = null;
                            if (!node.childNode(5).childNodes().isEmpty()) {
                                try {
                                    songDate = formatter.parse(node.child(5).text());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            int indexSong = 0;
                            if (tmp != null) {
                                indexSong = tmp.addSong(node.childNode(1).childNode(1).childNode(0).outerHtml(), node.childNode(1).childNode(1).attr("href"), songDate, node.childNode(1).childNode(1).attr("data-preview"));
                            }
                            if (!node.childNode(1).childNode(3).childNodes().isEmpty()) {
                                for (var node2 : node.childNode(1).childNode(3).childNodes()) {
                                    if (node2.nodeName().equals("a")) {
                                        if (tmp != null) {
                                            tmp.getMusicList().get(indexSong).addOriginalSong(node2.childNode(0).outerHtml(), node2.attr("href"));
                                        }
                                    }
                                }
                            }
                            if (!node.childNode(3).childNode(0).childNodes().isEmpty()) {
                                for (var node2 : node.childNode(3).childNode(0).childNodes()) {
                                    if (node2.nodeName().equals("a")) {
                                        if (tmp != null) {
                                            tmp.getMusicList().get(indexSong).addArtist(node2.childNode(0).outerHtml(), node2.attr("href"));
                                        }
                                    }
                                }
                            }
                        } else {
                            if (tmp != null) {
                                resultItems.add(tmp);
                            }
                            tmp = new ResultItemMusic();
                            tmp.setGameImageUrl(node.childNode(0).childNode(1).childNode(0).childNode(0).attr("src"));
                            tmp.setGameUrl(node.childNode(0).childNode(3).attr("href"));
                            tmp.setGameTitle(node.childNode(0).childNode(3).childNode(0).outerHtml());
                            if (node.childNode(0).childNodes().size() > 5) {
                                if (!node.childNode(0).childNode(5).childNodes().isEmpty()) {
                                    for (var node2 : node.childNode(0).childNode(5).childNodes()) {
                                        if (node2.nodeName().equals("a")) {
                                            tmp.addComposer(node2.childNode(0).outerHtml(), node2.attr("href"));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (tmp != null) {
                        resultItems.add(tmp);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                onResponseResultItemMusicList.onSuccess(resultItems);
            }
        });
    }

    public void getRemix(String url, OnResponseSongInfo onResponseSongInfo) throws IOException {
        makeRequest(baseUrl + url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onResponseSongInfo.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                SongInfor song = new SongInfor();
                Document doc = Jsoup.parse(response.body().string());
                var downloads = doc.select("#modalDownload > div > div > div.modal-body > ul:nth-child(2) > li");
                for (var download : downloads) {
                    song.getDownloadList().add(download.childNode(0).attr("href"));
                }

                Element tmpEle = doc.selectFirst("#main-content > div:nth-child(2) > div > div.container-fluid.container-content.container-highlight.no-bottom-padding > section:nth-child(1) > div > div > section:nth-child(2) > div.col-md-7.col-lg-8 > section:nth-child(1) > div > h1");

                song.setName(Objects.requireNonNull(tmpEle).childNode(2).outerHtml().replace("\"", "").trim());
                var node = doc.select("#main-content > div:nth-child(2) > div > div.container-fluid.container-content.container-highlight.no-bottom-padding > section:nth-child(1) > div > div > section:nth-child(2) > div.col-md-7.col-lg-8 > section:nth-child(1) > div > h2 > a");
                for (var artist : node) {
                    var artistItem = new ArtistItem();
                    artistItem.setName(artist.childNode(0).outerHtml());
                    artistItem.setUrl(artist.attr("href"));
                    song.getArtistList().add(artistItem);
                }
                song.setDuration(Objects.requireNonNull(doc.selectFirst("#main-content > div:nth-child(2) > div > div.container-fluid.container-content.container-highlight.no-bottom-padding > section:nth-child(1) > div > div > section:nth-child(2) > div.col-md-7.col-lg-8 > section:nth-child(1) > div > h1 > span.subtext")).childNode(0).outerHtml());
                onResponseSongInfo.onSuccess(song);
            }
        });
    }

    private Integer getIntFromNode(org.jsoup.nodes.Node node) {
        return node.childNodes().isEmpty() ? null : Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(node.firstChild()).firstChild()).outerHtml());
    }
}