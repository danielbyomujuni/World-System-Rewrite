package org.trainerlord.worldsystem.objects;

import org.apache.commons.lang.RandomStringUtils;

public class WorldSystemData {
    public PlayerData[] players;



    public class PlayerData {
        public String UUID;
        public String WSUUID;

        public String getUUID() {
            return UUID;
        }
    }
}
