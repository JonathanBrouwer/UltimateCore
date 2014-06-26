package Bammerbom.UltimateCore.Resources.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {
    public static Location convertStringToLocation(String s) {
        String[] split = s.split("\\|");
        return new Location(
        		Bukkit.getWorld(split[0])
        		, Double.parseDouble(split[1])
        		, Double.parseDouble(split[2])
        		, Double.parseDouble(split[3])
        		, Float.parseFloat(split[4])
        		, Float.parseFloat(split[5]));
    }
    public static String convertLocationToString(Location loc) {
        return loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getPitch() + "|" + loc.getYaw();
    }
}
