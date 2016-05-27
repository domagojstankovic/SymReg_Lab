package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import hr.fer.zemris.ecf.lab.engine.param.EntryList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by dstankovic on 5/18/16.
 */
public class ExperimentUtils {
  /**
   * Returns same configuration.
   */
  public static Configuration anulateBatchRepeats(Configuration configuration) {
    final String batchRepeatsKey = "batch.repeats";
    Entry batchRepeatsEntry = configuration.registry.getEntryWithKey(batchRepeatsKey);
    if (batchRepeatsEntry == null) {
      // no batch.repeats entry in registry
      configuration.registry.getEntryList().add(new Entry(batchRepeatsKey, "0"));
    } else {
      batchRepeatsEntry.value = "0";
    }
    return configuration;
  }

  public static void updateConfiguration(Configuration conf, ExperimentInput experimentInput) {
    updateConfiguration(
        conf,
        experimentInput.getTerminalset(),
        experimentInput.getInputFile(),
        experimentInput.getFunctions(),
        experimentInput.isLinearScaling(),
        experimentInput.getErrorWeightsFile(),
        experimentInput.getErrorMetric()
    );
  }

  private static void updateConfiguration(Configuration conf,
                                          String terminalset,
                                          String inputFile,
                                          List<String> functions,
                                          boolean linearScaling,
                                          String errorWeightsFile,
                                          String errorMetric) {

    List<EntryBlock> genotypes = conf.genotypes.get(0);
    EntryBlock treeGen = genotypes.get(0);

    Entry functionsetEntry = treeGen.getEntryWithKey("functionset");
    Entry terminalsetEntry = treeGen.getEntryWithKey("terminalset");

    EntryList registry = conf.registry;
    Entry inputfileEntry = registry.getEntryWithKey("input_file");
    Entry linearScalingEntry = registry.getEntryWithKey("linear_scaling");

    functionsetEntry.value = extractFunctionset(functions);
    terminalsetEntry.value = extractInputVars(inputFile) + terminalset;
    inputfileEntry.value = inputFile;
    linearScalingEntry.value = linearScaling ? "true" : "false";

    if (errorWeightsFile != null && !errorWeightsFile.trim().isEmpty()) {
      // error weights file is defined
      Entry errorWeightsFileEntry = new Entry("error_weights.file", errorWeightsFile);
      registry.getEntryList().add(errorWeightsFileEntry);
    }

    if (errorMetric != null) {
      // add error metric parameter
      Entry errorMetricEntry = new Entry("error_metric", errorMetric);
      registry.getEntryList().add(errorMetricEntry);
    }
  }

  private static String extractFunctionset(List<String> functions) {
    if (functions == null || functions.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    for (String f : functions) {
      sb.append(f + " ");
    }
    sb.deleteCharAt(sb.length() - 1);

    return sb.toString();
  }

  private static String extractInputVars(String inputFile) {
    int num = numOfInputVars(inputFile);

    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= num; i++) {
      sb.append("x" + i + " ");
    }
    return sb.toString();
  }

  private static int numOfInputVars(String inputFile) {
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
      num = parts.length - 1;
      break;
    }

    sc.close();
    return num;
  }
}
