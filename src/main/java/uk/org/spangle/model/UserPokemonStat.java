package uk.org.spangle.model;

import uk.org.spangle.data.*;

public class UserPokemonStat {
    UserPokemon userPokemon;
    Stat stat;

    private final int ERROR_UNKNOWN = -1;

    public UserPokemonStat(UserPokemon userPokemon, Stat stat) {
        this.userPokemon = userPokemon;
        this.stat = stat;
    }

    public UserPokemon getUserPokemon() {
        return this.userPokemon;
    }

    public Stat getStat() {
        return this.stat;
    }

    public String getName() {
        return stat.getName();
    }

    public String getIV() {
        UserPokemonIV upi = userPokemon.getUserPokemonIV(stat);
        if(upi == null) return "Unknown";
        return Integer.toString(upi.getValue());
    }

    public UserPokemonIV setIV(String newIV) {
        int iv = Integer.parseInt(newIV);
        UserPokemonIV upi = userPokemon.getUserPokemonIV(stat);
        if(upi == null) upi = new UserPokemonIV(userPokemon,stat,0);
        upi.setValue(iv);
        return upi;
    }

    public String getEV() {
        UserPokemonEV upe = userPokemon.getUserPokemonEV(stat);
        if(upe == null) return "Unknown";
        return Integer.toString(upe.getValue());
    }

    public UserPokemonEV setEV(String newEV) {
        int ev = Integer.parseInt(newEV);
        UserPokemonEV upe = userPokemon.getUserPokemonEV(stat);
        if(upe == null) upe = new UserPokemonEV(userPokemon,stat,0);
        upe.setValue(ev);
        return upe;
    }

    public String getBaseStat() {
        UserPokemonForm upf = userPokemon.getUserPokemonForm();
        if(upf == null) return "Unknown";
        PokemonFormBaseStat pfbs = upf.getPokemonForm().getPokemonFormBaseStat(stat);
        return Integer.toString(pfbs.getValue());
    }

    public String getValue() {
        int value;
        if(stat.getAbbr().equals(Configuration.STAT_HP)) {
            value = getHPValue();
        } else {
            value = getStatValue();
        }
        if(value == ERROR_UNKNOWN) return "Unknown";
        return Integer.toString(value);
    }

    private int getHPValue() {
        UserPokemonIV upi = userPokemon.getUserPokemonIV(stat);
        UserPokemonEV upe = userPokemon.getUserPokemonEV(stat);
        UserPokemonLevel upl = userPokemon.getUserPokemonLevel();
        UserPokemonForm upf = userPokemon.getUserPokemonForm();
        if(upi == null || upe == null || upl == null || upf == null) {
            return ERROR_UNKNOWN;
        }
        PokemonFormBaseStat pfbs = upf.getPokemonForm().getPokemonFormBaseStat(stat);
        int base = pfbs.getValue();
        int iv = upi.getValue();
        int ev = upe.getValue();
        int level = upl.getLevel();
        return ((2*base+iv+(ev/4))*level)/100+level+10;
    }

    private int getStatValue() {
        UserPokemonIV upi = userPokemon.getUserPokemonIV(stat);
        UserPokemonEV upe = userPokemon.getUserPokemonEV(stat);
        UserPokemonLevel upl = userPokemon.getUserPokemonLevel();
        UserPokemonForm upf = userPokemon.getUserPokemonForm();
        UserPokemonNature upn = userPokemon.getUserPokemonNature();
        if(upi == null || upe == null || upl == null || upf == null || upn == null) {
            return ERROR_UNKNOWN;
        }
        PokemonFormBaseStat pfbs = upf.getPokemonForm().getPokemonFormBaseStat(stat);
        int base = pfbs.getValue();
        int iv = upi.getValue();
        int ev = upe.getValue();
        int level = upl.getLevel();
        double natureModifier = 1.0;
        Stat upStat = upn.getNature().getStatUp();
        Stat downStat = upn.getNature().getStatDown();
        if(stat == upStat) natureModifier = 1.1;
        if(stat == downStat) natureModifier = 0.9;
        if(upStat == downStat) natureModifier = 1.0;
        //TODO
        return (int) ((((2*base+iv+(ev/4))*level)/100+5)*natureModifier);
    }
}
