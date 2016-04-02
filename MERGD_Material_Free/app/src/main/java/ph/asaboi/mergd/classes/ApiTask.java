package ph.asaboi.mergd.classes;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by P004785 on 2/14/2016.
 */
public class ApiTask extends AsyncTask
{
    private final Activity mAct;
    private final Callback mCallback;

    public ApiTask(Activity act,Callback callback)
    {
        mAct = act;
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String param = (String)params[0];
        try {
        switch (param)
        {
            case "GAMES":

                return API.GetGames();

            case "METAS":
                int gameID = (int)params[1];
                return API.GetMetas(gameID);
            case "ENTITIES":
                int metaID = (int)params[1];
                int mgameID = (int)params[2];
//                Log.d("APITASK","MetaID = " + metaID);
                return  API.GetEntities(metaID,mgameID);
            case "ENTITY":
                int entID = (int)params[1];
//                Log.d("APITASK","EntID = " + entID);
                return  API.GetEntity(entID);
        }
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
       return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        mCallback.onResult(o);

    }
}

