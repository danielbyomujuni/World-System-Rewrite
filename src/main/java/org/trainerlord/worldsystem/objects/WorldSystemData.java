package org.trainerlord.worldsystem.objects;

import org.apache.commons.lang.RandomStringUtils;

import java.util.List;
import java.util.UUID;

public class WorldSystemData {
    public List<PlayerData> players;
    public List<Worlds>  playerWorlds;





    public static class PlayerData {
        public String UUID;
        public String WSUUID;
        public int WorldCount = 0;

        public PlayerData(java.util.UUID uuid, String wsuuid, int worldCount) {
            this.UUID = uuid.toString();
            this.WSUUID = wsuuid;
            this.WorldCount = worldCount;
        }

        public String getUUID() {
            return UUID;
        }
    }

    public static class Worlds {
        //WSUUID

        public String OWNER;
        public String OWNERName;
        public int worldNumber;
        public long lastLoaded;

        public Worlds (String OWNER, String OWNERname, int worldNumber) {
            this.OWNER = OWNER;
            this.OWNERName = OWNERname;
            this.worldNumber = worldNumber;
            this.lastLoaded = 0;
        }
    }
}
