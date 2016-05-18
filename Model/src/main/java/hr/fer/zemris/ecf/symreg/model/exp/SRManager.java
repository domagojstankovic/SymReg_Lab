package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.DetectOS;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.task.ExperimentsManager;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Domagoj on 08/06/15.
 */
public class SRManager {

  public static final String CONFIG_FILE = "srm_config.xml";
  private String ecfPath = null;
  private String currConfPath = null;

  private JobListener listener;

  public SRManager(JobListener listener) {
    this.listener = listener;
  }

  public void run(ExperimentInput experimentInput) {
    Configuration conf = readTemplateConfiguration();
    ExperimentUtils.updateConfiguration(conf, experimentInput);

    ExperimentsManager manager = new ExperimentsManager(listener);

    String ecfPath = generateECFexe();
    String confPath = generateTempConfigFile();
    int threads = 1;
    currConfPath = confPath;
    manager.runExperiment(conf, ecfPath, confPath, threads, true);
  }

  public void runTest(LogModel log) throws IOException, InterruptedException {
    String hofFile = writeHofToFile(log);
    File tempOutFile = File.createTempFile("ecf_srm_test_data_out", ".txt", new File("./"));
    String tempOutPath = tempOutFile.getAbsolutePath();
    runTest(currConfPath, hofFile, tempOutPath);
    tempOutFile.deleteOnExit();
  }

  public void runTest(String confFile, String hofFile, String testOutFile) throws IOException, InterruptedException {
    String ecfExe = generateECFexe();
    System.out.println("Test: " + ecfExe + " " + confFile + " " + hofFile + " " + testOutFile);
    ProcessRunner.runProcess(ecfExe, confFile, hofFile, testOutFile);
  }

  private static String writeHofToFile(LogModel log) throws IOException {
    File tempHofFile = File.createTempFile("ecf_srm-hof", ".xml", new File("./"));
    PrintWriter pw = new PrintWriter(tempHofFile);
    pw.print(log.getRuns().get(0).getHallOfFame());
    pw.close();
    tempHofFile.deleteOnExit();
    return tempHofFile.getAbsolutePath();
  }

  private String generateTempConfigFile() {
    try {
      File file = File.createTempFile("ecf_srm-conf", ".txt");
      file.deleteOnExit();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
      throw new SRManagerException(e);
    }
  }

  private String generateECFexe() {
    if (ecfPath != null) {
      return ecfPath;
    }
    String filePath = EcfFileProvider.getEcfFile();
    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filePath);

    try {
      String suffix = "";
      if (DetectOS.isWindows()) {
        suffix = ".exe";
      }
      File file = File.createTempFile("ecf_srm", suffix);
      file.setExecutable(true, false);
      file.setReadable(true, false);
      file.setWritable(true, false);

      FileUtils.copyInputStreamToFile(is, file);
      file.deleteOnExit();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
      throw new SRManagerException(e);
    }
  }

  public static Configuration readTemplateConfiguration() {
    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(CONFIG_FILE);
    ConfigurationReader reader = ConfigurationService.getInstance().getReader();
    return reader.readArchive(is);
  }
}
