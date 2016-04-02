package ph.asaboi.mergdpaid.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by P004785 on 2/13/2016.
 */
public class Entity implements  Comparable<Entity> {
    @SerializedName("entityid")
    public int EntityID;
    public String Name;
    public String Description;


    public Def[] Defs;
    public int defid;
    public String definition;
    public int dorder;

    public Entity[] LeftEntities;
    public Entity[] RightEntities;
    public String imgurl;

    @Override
    public int compareTo(Entity another) {
        return this.Name.compareTo(another.Name);
    }
}
