package org.trainerlord.worldsystem.objects;

import java.lang.management.BufferPoolMXBean;

public class WorldSettingData {
    public WorldInfo worldInfo;
    public WorldSettings worldSettings;




    public static class WorldInfo {
        public int worldNumber;
        public Owner Owner;
        public String templateKey;


        public static class Owner {
            public String PlayerUUID;
            public String Actualname;
        }
    }

    public static class WorldSettings {
        public Boolean TNTDamage;
        public Boolean Fire;
    }
}
