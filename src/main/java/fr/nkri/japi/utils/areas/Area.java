package fr.nkri.japi.utils.areas;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
Objet représentant une zone
 */

public class Area {

    @Expose(serialize = true)
    private int minx, miny, minz;
    @Expose(serialize = true)
    private int maxx, maxy, maxz;

    public Area(final Location min, final Location max){
        setMin(min);
        setMax(max);
    }

    public boolean isInArea(final Player player) {
        if(player.isDead()) {
            return false;
        }

        final Location loc = player.getLocation();
        return minx <= loc.getBlockX() && maxx >= loc.getBlockX() && miny <= loc.getBlockY()
                && maxy >= loc.getBlockY() && minz <= loc.getBlockZ() && maxz >= loc.getBlockZ();
    }

    public boolean isInArea(final Location loc) {
        return minx <= loc.getBlockX() && maxx >= loc.getBlockX() && miny <= loc.getBlockY()
                && maxy >= loc.getBlockY() && minz <= loc.getBlockZ() && maxz >= loc.getBlockZ();
    }

    public void setMin(final Location min) {
        this.minx = min.getBlockX();
        this.miny = min.getBlockY();
        this.minz = min.getBlockZ();
    }

    public void setMin(final int minx, final int miny, final int minz) {
        this.minx = minx;
        this.miny = miny;
        this.minz = minz;
    }

    public void setMax(final Location max) {
        this.maxx = max.getBlockX();
        this.maxy = max.getBlockY();
        this.maxz = max.getBlockZ();
    }

    public void setMax(final int maxx, final int maxy, final int maxz) {
        this.maxx = maxx;
        this.maxy = maxy;
        this.maxz = maxz;
    }

    public Location getCenter() {
        final int centerX = (minx + maxx) / 2;
        final int centerY = (miny + maxy) / 2;
        final int centerZ = (minz + maxz) / 2;

        return new Location(Bukkit.getWorld("world"), centerX, centerY+2, centerZ);
    }

    public int getMinx() {return minx;}
    public int getMiny() {return miny;}
    public int getMinz() {return minz;}

    public int getMaxx() {return maxx;}
    public int getMaxy() {return maxy;}
    public int getMaxz() {return maxz;}
}