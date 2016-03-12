package uk.org.spangle.tools;

/**
 * Quick and easy tool to import everything
 */
public class Import {

    public static void main(String[] args) {
        ImportPokemonAndForms imp1 = new ImportPokemonAndForms();
        imp1.run();
        ImportGenerations imp2 = new ImportGenerations();
        imp2.run();
        ImportPokeBalls imp3 = new ImportPokeBalls();
        imp3.run();
        ImportStatsAndNatures imp4 = new ImportStatsAndNatures();
        imp4.run();
        ImportTestUserData imp5 = new ImportTestUserData();
        imp5.run();
    }
}
