package fr.nkri.japi.cooldowns;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CooldownManager {

    private static CooldownManager INSTANCE;
    private HashMap<UUID, List<Cooldown>> cooldownMap;
    public CooldownManager() {
        INSTANCE = this;
        this.cooldownMap = new HashMap<>();
    }

    //Supprime tout les cooldowns des joueurs
    public void clearCooldown(){
        this.cooldownMap.clear();
    }

    //Ajoute un cooldown à un joueur (paramètre : joueur, temps du cooldown en seconds, l'itemstack ou la commande: ItemStack, String etc...).
    public void setCooldown(Player player, int seconds, final Object obj) {
        final double delay = (System.currentTimeMillis() + (seconds * 1000L));
        final Cooldown cooldown = new Cooldown(delay, obj);

        if(getCooldwonPlayer(player) == null){
            final List<Cooldown> cooldownList = new ArrayList<>();
            cooldownList.add(cooldown);

            cooldownMap.put(player.getUniqueId(), cooldownList);
            return;
        }

        cooldownMap.get(player.getUniqueId()).add(cooldown);
    }

    //False = le joueur n'a pas de cooldown
    public boolean isCooldown(final Player player, final Object obj) {
        final Cooldown cooldown = getCooldown(player, obj);
        return cooldown != null && cooldownMap.containsKey(player.getUniqueId()) && cooldown.getTime() > System.currentTimeMillis();
    }

    //Retire un cooldown
    public void removeCooldwon(final Player player, final Object obj){
        final Cooldown cooldown = getCooldown(player, obj);

        if(cooldown == null){
            return;
        }
        this.cooldownMap.get(player.getUniqueId()).remove(cooldown);
    }

    //Récupère l'obj cooldown du joueur
    public Cooldown getCooldown(final Player player, final Object obj){
        if(getCooldwonPlayer(player) == null){
            return null;
        }

        for(Cooldown cooldown : getCooldwonPlayer(player)){
            if(cooldown.getObj() == obj){
                return cooldown;
            }
        }

        return null;
    }

    public List<Cooldown> getCooldwonPlayer(final Player player){
        final UUID uuid = player.getUniqueId();
        if(cooldownMap.get(uuid) != null){
            return cooldownMap.get(uuid);
        }

        return null;
    }

    public static CooldownManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CooldownManager();
        }

        return INSTANCE;
    }
}
