package org.trainerlord.worldsystem.utils;


//TODO Clean Code
//TODO Document this Mess
//TODO More Comments
//TODO Fix Errors

import org.bukkit.Bukkit;

public class VersionUtil {

    private static int version;

    private VersionUtil() {
    }

    public static int getVersion() {
        if (version == 0) {
            // Detect version
            String v = Bukkit.getVersion();
            if (v.contains("1.16"))
                version = 16;
        }
        if (version == 0) {
            System.err.println("[WorldSystem] Incompatible Version: " + Bukkit.getVersion());
            System.err.println("[WorldSystem] Choosing version 1.16.5");
            System.err.println("[WorldSystem] If your version is Lower than this please use version 2.4.X");
            version = 16;
        }
        return version;
    }
}
