package fr.nkri.japi.cooldowns;

import fr.nkri.japi.utils.times.DurationFormatter;

public class Cooldown {

    private double time;
    private Object obj;

    public Cooldown(final double time, final Object obj){
        this.time = time;
        this.obj = obj;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public double getCooldownTime(){
        return getTime();
    }

    public String getCooldownDate(){
        return DurationFormatter.getDurationDate((long)getTime());
    }

    public String getCooldownWord(){
        return DurationFormatter.getDurationWords((long)getTime());
    }
}
