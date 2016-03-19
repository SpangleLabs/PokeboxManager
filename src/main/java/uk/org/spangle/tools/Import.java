package uk.org.spangle.tools;

/**
 * Quick and easy tool to import everything
 */
public class Import {

    public static void main(String[] args) {
        ImportIcons imp1 = new ImportIcons();
        imp1.run();
        //move result.png and result.csv
        ImportVeekun imp2 = new ImportVeekun();
        imp2.run();
        ImportGenerations imp3 = new ImportGenerations();
        imp3.run();
        ImportTestUserData imp5 = new ImportTestUserData();
        imp5.run();
    }
}
