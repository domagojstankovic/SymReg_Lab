package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import hr.fer.zemris.ecf.lab.engine.param.EntryList;
import hr.fer.zemris.ecf.lab.engine.task.ExperimentsManager;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Domagoj on 08/06/15.
 */
public class SRManager implements JobListener {

    public static final String CONFIG_FILE = "srm_config.xml";
    private String ecfPath = null;

    public void run(String terminalset, String inputFile, List<String> functions) {
        Configuration conf = readConfig();
        updateConfig(conf, terminalset, inputFile, functions);

        ExperimentsManager manager = new ExperimentsManager(this);

        String ecfPath = getEcfFilePath();
        String confPath = generateTempConfigFile();
        int threads = 1;
        manager.runExperiment(conf, ecfPath, confPath, threads);
    }

    private String generateTempConfigFile() {
        try {
            File file = File.createTempFile("ecf_srm", ".conf");
            file.deleteOnExit();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SRManagerException(e);
        }
    }

    private String getEcfFilePath() {
        if (ecfPath != null) {
            return ecfPath;
        }
        String filePath = EcfFileProvider.getEcfFile();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filePath);

        try {
            File file = File.createTempFile("ecf_srm", ".exec");
            FileUtils.copyInputStreamToFile(is, file);
            file.deleteOnExit();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SRManagerException(e);
        }
    }

    private Configuration readConfig() {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(CONFIG_FILE);
        ConfigurationReader reader = ConfigurationService.getInstance().getReader();
        return reader.readArchive(is);
    }

    private void updateConfig(Configuration conf, String terminalset, String inputFile, List<String> functions) {
        List<EntryBlock> genotypes = conf.genotypes.get(0);
        EntryBlock treeGen = genotypes.get(0);

        Entry functionsetEntry = treeGen.getEntryWithKey("functionset");
        Entry terminalsetEntry = treeGen.getEntryWithKey("terminalset");

        EntryList registry = conf.registry;
        Entry inputfileEntry = registry.getEntryWithKey("input_file");

        functionsetEntry.value = extractFunctionset(functions);
        terminalsetEntry.value = extractInputVars(inputFile) + terminalset;
        inputfileEntry.value = inputFile;
    }

    private String extractInputVars(String inputFile) {
        int num = numOfInputVars(inputFile);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            sb.append("x" + i + " ");
        }
        return sb.toString();
    }

    private int numOfInputVars(String inputFile) {
        Scanner sc;
        try {
            sc = new Scanner(new File(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new SRManagerException(e);
        }

        int num = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+");
            num = parts.length;
            break;
        }

        sc.close();
        return num;
    }

    private String extractFunctionset(List<String> functions) {
        StringBuilder sb = new StringBuilder();

        for (String f : functions) {
            sb.append(f + " ");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public void jobInitialized(Job job) {

    }

    @Override
    public void jobStarted(Job job) {

    }

    @Override
    public void jobFinished(Job job, LogModel logModel) {

    }

    @Override
    public void jobFailed(Job job) {

    }
}
