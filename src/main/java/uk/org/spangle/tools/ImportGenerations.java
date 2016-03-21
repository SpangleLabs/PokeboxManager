package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.Generation;
import uk.org.spangle.data.GenerationBox;

import java.util.List;

/**
 * Quick and easy tool to import the list of generations and their box templates.
 */
public class ImportGenerations {
    private Session dbSession;

    public static void main(String[] args) {
        ImportGenerations imp = new ImportGenerations();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if generations table is empty
        List listGens = dbSession.createCriteria(Generation.class).list();
        if(listGens.size() > 0) {
            System.out.println("Generations have already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Try and create stuff
        try {
            createGenerations();
        } finally {
            // Shut down cleanly
            dbSession.close();
            sessionFactory.close();
        }
    }

    private void createGenerations() {
        createPokebank();
        createGenI();
        createGenII();
        createGenIII();
        createGenIV();
        createGenV();
        createGenVI();
    }

    private void createPokebank() {
        // Pokebank
        Generation pokeBank = new Generation();
        pokeBank.setName("Pokebank");
        pokeBank.setDescription("Pokebank");
        dbSession.saveOrUpdate(pokeBank);
        // Boxes
        for(int x = 0; x < 100; x++) {
            GenerationBox bankBox = new GenerationBox();
            bankBox.setGeneration(pokeBank);
            bankBox.setOrdinal(x+1);
            bankBox.setSize(30);
            bankBox.setColumns(6);
            bankBox.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(bankBox);
        }
    }

    private void createGenI() {
        Generation gen = new Generation();
        gen.setName("Gen I");
        gen.setDescription("Red, Green, Blue, Yellow");
        dbSession.saveOrUpdate(gen);
        // Add party
        GenerationBox party = new GenerationBox();
        party.setGeneration(gen);
        party.setOrdinal(1);
        party.setSize(6);
        party.setColumns(2);
        party.setName("Party");
        dbSession.saveOrUpdate(party);
        // Boxes
        for(int x = 0; x < 12; x++) {
            GenerationBox box = new GenerationBox();
            box.setGeneration(gen);
            box.setOrdinal(x+2);
            box.setSize(20);
            box.setColumns(5);
            box.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(box);
        }
    }

    private void createGenII() {
        Generation gen = new Generation();
        gen.setName("Gen II");
        gen.setDescription("Gold, Silver, Crystal");
        dbSession.saveOrUpdate(gen);
        // Add party
        GenerationBox party = new GenerationBox();
        party.setGeneration(gen);
        party.setOrdinal(1);
        party.setSize(6);
        party.setColumns(2);
        party.setName("Party");
        dbSession.saveOrUpdate(party);
        // Add boxes
        for(int x = 0; x < 14; x++) {
            GenerationBox box = new GenerationBox();
            box.setGeneration(gen);
            box.setOrdinal(x+2);
            box.setSize(20);
            box.setColumns(5);
            box.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(box);
        }
        // Add daycare
        GenerationBox daycare = new GenerationBox();
        daycare.setGeneration(gen);
        daycare.setOrdinal(1+14+1);
        daycare.setSize(2);
        daycare.setColumns(2);
        daycare.setName("Daycare");
        dbSession.saveOrUpdate(daycare);
    }

    private void createGenIII() {
        Generation gen = new Generation();
        gen.setName("Gen III");
        gen.setDescription("Ruby, Sapphire, Emerald, FireRed, LeafGreen");
        dbSession.saveOrUpdate(gen);
        // Add party
        GenerationBox party = new GenerationBox();
        party.setGeneration(gen);
        party.setOrdinal(1);
        party.setSize(6);
        party.setColumns(2);
        party.setName("Party");
        dbSession.saveOrUpdate(party);
        // Add boxes
        for(int x = 0; x < 14; x++) {
            GenerationBox box = new GenerationBox();
            box.setGeneration(gen);
            box.setOrdinal(x+2);
            box.setSize(30);
            box.setColumns(6);
            box.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(box);
        }
        // Add daycare
        GenerationBox daycare = new GenerationBox();
        daycare.setGeneration(gen);
        daycare.setOrdinal(1+14+1);
        daycare.setSize(2);
        daycare.setColumns(2);
        daycare.setName("Daycare");
        dbSession.saveOrUpdate(daycare);
    }

    private void createGenIV() {
        Generation gen = new Generation();
        gen.setName("Gen IV");
        gen.setDescription("Diamond, Pearl, Platinum, HeartGold, SoulSilver");
        dbSession.saveOrUpdate(gen);
        // Add party
        GenerationBox party = new GenerationBox();
        party.setGeneration(gen);
        party.setOrdinal(1);
        party.setSize(6);
        party.setColumns(2);
        party.setName("Party");
        dbSession.saveOrUpdate(party);
        // Add boxes
        for(int x = 0; x < 18; x++) {
            GenerationBox box = new GenerationBox();
            box.setGeneration(gen);
            box.setOrdinal(x+2);
            box.setSize(30);
            box.setColumns(6);
            box.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(box);
        }
        // Add GTS
        GenerationBox gts = new GenerationBox();
        gts.setGeneration(gen);
        gts.setOrdinal(1+18+1);
        gts.setSize(1);
        gts.setColumns(1);
        gts.setName("GTS");
        dbSession.saveOrUpdate(gts);
        // Add daycare
        GenerationBox daycare = new GenerationBox();
        daycare.setGeneration(gen);
        daycare.setOrdinal(1+18+2);
        daycare.setSize(2);
        daycare.setColumns(2);
        daycare.setName("Daycare");
        dbSession.saveOrUpdate(daycare);
    }

    private void createGenV() {
        Generation gen = new Generation();
        gen.setName("Gen V");
        gen.setDescription("Black, White, Black2, White2");
        dbSession.saveOrUpdate(gen);
        // Add party
        GenerationBox party = new GenerationBox();
        party.setGeneration(gen);
        party.setOrdinal(1);
        party.setSize(6);
        party.setColumns(2);
        party.setName("Party");
        dbSession.saveOrUpdate(party);
        // Add boxes
        for(int x = 0; x < 24; x++) {
            GenerationBox box = new GenerationBox();
            box.setGeneration(gen);
            box.setOrdinal(x+2);
            box.setSize(30);
            box.setColumns(6);
            box.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(box);
        }
        // Add battle box
        GenerationBox battleBox = new GenerationBox();
        battleBox.setGeneration(gen);
        battleBox.setOrdinal(1+24+1);
        battleBox.setSize(6);
        battleBox.setColumns(2);
        battleBox.setName("Battle box");
        dbSession.saveOrUpdate(battleBox);
        // Add GTS
        GenerationBox gts = new GenerationBox();
        gts.setGeneration(gen);
        gts.setOrdinal(1+24+2);
        gts.setSize(1);
        gts.setColumns(1);
        gts.setName("GTS");
        dbSession.saveOrUpdate(gts);
        // Add daycare
        GenerationBox daycare = new GenerationBox();
        daycare.setGeneration(gen);
        daycare.setOrdinal(1+24+3);
        daycare.setSize(2);
        daycare.setColumns(2);
        daycare.setName("Daycare");
        dbSession.saveOrUpdate(daycare);
    }

    private void createGenVI() {
        Generation gen = new Generation();
        gen.setName("Gen VI");
        gen.setDescription("X, Y, OmegaRuby, AlphaSapphire");
        dbSession.saveOrUpdate(gen);
        // Add party
        GenerationBox party = new GenerationBox();
        party.setGeneration(gen);
        party.setOrdinal(1);
        party.setSize(6);
        party.setColumns(2);
        party.setName("Party");
        dbSession.saveOrUpdate(party);
        // Add boxes
        for(int x = 0; x < 31; x++) {
            GenerationBox box = new GenerationBox();
            box.setGeneration(gen);
            box.setOrdinal(x+2);
            box.setSize(30);
            box.setColumns(6);
            box.setName("Box "+Integer.toString(x+1));
            dbSession.saveOrUpdate(box);
        }
        // Add battle box
        GenerationBox battleBox = new GenerationBox();
        battleBox.setGeneration(gen);
        battleBox.setOrdinal(1+31+1);
        battleBox.setSize(6);
        battleBox.setColumns(2);
        battleBox.setName("Battle box");
        dbSession.saveOrUpdate(battleBox);
        // Add GTS
        GenerationBox gts = new GenerationBox();
        gts.setGeneration(gen);
        gts.setOrdinal(1+31+2);
        gts.setSize(1);
        gts.setColumns(1);
        gts.setName("GTS");
        dbSession.saveOrUpdate(gts);
        // Add daycare
        GenerationBox daycare = new GenerationBox();
        daycare.setGeneration(gen);
        daycare.setOrdinal(1+31+3);
        daycare.setSize(2);
        daycare.setColumns(2);
        daycare.setName("Daycare");
        dbSession.saveOrUpdate(daycare);
    }
}
