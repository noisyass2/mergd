package ph.asaboi.mergdpaid.classes;

import ph.asaboi.mergdpaid.adapters.EntityListAdapter;

/**
 * Created by neo on 3/9/2016.
 */
public class EntityItem {

        public String Definition;
        public int DOrder;
        public int EntityID;
        public String Name;
        public String Description;
        public String ImgUrl;

        public int ItemType;
        public int defid;

        public EntityItem(Entity entity) {
            this.Name = entity.Name;
            this.Description = entity.Description;
            this.EntityID = entity.EntityID;

            this.DOrder = entity.dorder;
            this.Definition = entity.definition;
            this.ItemType = EntityListAdapter.TYPE_ITEM;
            this.defid = entity.defid;
            this.ImgUrl = entity.imgurl;
        }

        public EntityItem(String name) {
            this.Name = name;
            this.ItemType = EntityListAdapter.TYPE_GROUP;
        }

        public EntityItem(int entityID, String name) {
            this.EntityID = entityID;
            this.Name    = name;
        }

        @Override
        public int hashCode() {
            return this.EntityID;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof EntityItem){
                EntityItem toCompare = (EntityItem) o;
                return this.EntityID == toCompare.EntityID;
            }
            return false;
        }
    }
