package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Domagoj on 08/06/15.
 */
public class SRManagerTest {

    private static boolean alreadySet = false;

    @Before
    public void setUp() throws Exception {
        if (!alreadySet) {
            ConfigurationService.getInstance().setReader(new XmlConfigurationReader());
            ConfigurationService.getInstance().setWriter(new XmlConfigurationWriter());
        }
        alreadySet = true;
    }

    @Test
    public void testRun() throws Exception {

    }

    @Test
    public void testConfig() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(SRManager.CONFIG_FILE);
        ConfigurationReader reader = ConfigurationService.getInstance().getReader();
        Configuration conf = reader.readArchive(is);

        List<EntryBlock> algs = conf.algorithms;
        assertTrue("More than 1 algorithm", algs.size() == 1);
        assertTrue("Not steady state tournament", algs.get(0).getName().equals("SteadyStateTournament"));

        List<EntryBlock> gens = conf.genotypes.get(0);
        assertTrue("More than one genotype", gens.size() == 1);
        EntryBlock tree = gens.get(0);
        assertTrue("Genotype is not tree", tree.getName().equals("Tree"));

        Entry functionset = gens.get(0).getEntryWithKey("functionset");
        Entry terminalset = gens.get(0).getEntryWithKey("terminalset");

        assertTrue("functionset entry exists", functionset != null);
        assertTrue("terminalset entry exists", terminalset != null);

        assertTrue("input_file entry exists", conf.registry.getEntryWithKey("input_file") != null);
    }
}