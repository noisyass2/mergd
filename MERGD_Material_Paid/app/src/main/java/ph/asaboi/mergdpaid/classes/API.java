package ph.asaboi.mergdpaid.classes;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by P004785 on 2/13/2016.
 */
public class API {

    public static Meta[] GetMetas(int gameID) throws IOException {
        Gson gson = new Gson();
        String json = SendAPI("http://mergd.herokuapp.com/api/v1/metas/" + gameID);
        String results = "";
//        Log.d("JSON", json);
        Meta[] metas = gson.fromJson(json, Meta[].class);
        for (Meta meta:metas) {
            results += meta.Name + ":" + meta.Description + "\n";
        }

        return metas;
    }


    public static Game[] GetGames() throws IOException {
        Gson gson = new Gson();
        String json = SendAPI("http://mergd.herokuapp.com/api/v1/games");
        String results = "";
//        Log.d("JSON", json);
        Game[] games = gson.fromJson(json, Game[].class);
        for (Game game:games) {
            results += game.Name + ":" + game.Description + "\n";
        }
//        Log.d("JSON",results);

        return games;
    }

    public static Entity[]  GetEntities(int metaID, int mgameID) throws IOException {
        Gson gson = new Gson();
        String json = SendAPI("http://mergd.herokuapp.com/api/v1/entities/" + mgameID + "/" + metaID);
        String results = "";
//        Log.d("ENTITY", "json: " + json);
        Entity[] entities = gson.fromJson(json, Entity[].class);
        for (Entity entity :
                entities) {
            results += entity.Name + ":" + entity.Description + "\n";
        }
        return entities;
    }

    public static Entity GetEntity(int entID) throws IOException {
        Gson gson = new Gson();
        String json = SendAPI("http://mergd.herokuapp.com/api/v1/entities/" + entID);
        String results = "";
       // Log.d("APITASK",json);
        Entity entity = gson.fromJson(json, Entity.class);
        return entity;
    }

    private static String SendAPI(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;

            response = client.newCall(request).execute();
            String result = response.body().string();

            return result;


    }


}
