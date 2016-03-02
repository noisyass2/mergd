package ph.asaboi.mergd.classes;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by P004785 on 2/13/2016.
 */
public class API {

    public static String GetMetas() {
        Gson gson = new Gson();
        String json = SendAPI("http://mergd.herokuapp.com/api/v1/metas/1");
        String results = "";
        Meta[] metas = gson.fromJson(json, Meta[].class);
        for (Meta meta:metas) {
            results += meta.Name + ":" + meta.Description + "\n";
        }

        return results;
    }

    public static String GetEntities(int metaID) {
        Gson gson = new Gson();
        String json = SendAPI("http://mergd.herokuapp.com/api/v1/entities/1/" + metaID);
        String results = "";
        Entity[] entities = gson.fromJson(json, Entity[].class);
        for (Entity entity :
                entities) {
            results += entity.Name + ":" + entity.Description + "\n";
        }
        return results;
    }

    private static String SendAPI(String url)
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  "{}";
    }
}
