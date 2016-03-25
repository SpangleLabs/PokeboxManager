package uk.org.spangle.tools;

/**
 * Quick and easy tool to import everything
 */
public class Import {

    public static void main(String[] args) {
        ImportIcons imp1 = new ImportIcons();
        imp1.run();
        ImportVeekun imp2 = new ImportVeekun();
        imp2.run();
        ImportGenerations imp3 = new ImportGenerations();
        imp3.run();
        ImportLanguages imp4 = new ImportLanguages();
        imp4.run();
        ImportTestUserData imp5 = new ImportTestUserData();
        imp5.run();
    }
}
