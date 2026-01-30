package net.creeperhost.sharerecipe;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ModpackInfoAPI {
    private static final String WEBSITE_BASE_URL = "https://www.creeperhost.net/api/v2/";
    private static final String REQUEST_URL = WEBSITE_BASE_URL + "lookup/";
    private static final String CURSEFORGE_URL = REQUEST_URL + "curseforge/";
    private static String FTB_URL = REQUEST_URL + "ftb/";
    private static final Gson GSON = new Gson();

    public static int getWebsiteIdCurseForge(long packId) {
        String requestURL = CURSEFORGE_URL + packId;
        return doWebsiteIdRequest(requestURL);
    }

    public static int getWebsiteIdCurseForge(String packId) {
        String requestURL = CURSEFORGE_URL + packId;
        return doWebsiteIdRequest(requestURL);
    }

    public static int getWebsiteIdFTB(long packId) {
        return getWebsiteIdFTB(packId, -1);
    }

    public static int getWebsiteIdFTB(long packId, long version) {
        String requestURL = FTB_URL + packId + (version > 0 ? version + "/" : "");
        return doWebsiteIdRequest(requestURL);
    }

    private static int doWebsiteIdRequest(String urlPath) {
        return 1;
//        try {
//            URL url = new URI(urlPath).toURL();
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
//            int respCode = urlConnection.getResponseCode();
//            if (respCode == 200) {
//                InputStream inputStream = urlConnection.getInputStream();
//                String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//                LookupResponse lookupResponse = GSON.fromJson(body, LookupResponse.class);
//                if (lookupResponse != null) {
//                    return lookupResponse.id;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;
    }

    private record LookupResponse(int id) {};
}
