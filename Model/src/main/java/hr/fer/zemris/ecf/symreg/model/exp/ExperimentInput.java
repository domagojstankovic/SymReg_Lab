package hr.fer.zemris.ecf.symreg.model.exp;

import java.util.List;

/**
 * Created by dstankovic on 4/27/16.
 */
public class ExperimentInput {
  private String terminalset;
  private String inputFile;
  private List<String> functions;
  private boolean linearScaling;
  private boolean intervalArithmetic;
  private String errorWeightsFile;
  private String errorMetric;

  public ExperimentInput(String terminalset,
                         String inputFile,
                         List<String> functions,
                         boolean linearScaling,
                         boolean intervalArithmetic,
                         String errorWeightsFile,
                         String errorMetric) {
    this.terminalset = terminalset;
    this.inputFile = inputFile;
    this.functions = functions;
    this.linearScaling = linearScaling;
    this.intervalArithmetic = intervalArithmetic;
    this.errorWeightsFile = errorWeightsFile;
    this.errorMetric = errorMetric;
  }

  public String getTerminalset() {
    return terminalset;
  }

  public String getInputFile() {
    return inputFile;
  }

  public List<String> getFunctions() {
    return functions;
  }

  public boolean isLinearScaling() {
    return linearScaling;
  }

  public boolean isIntervalArithmetic() {
    return intervalArithmetic;
  }

  public String getErrorWeightsFile() {
    return errorWeightsFile;
  }

  public String getErrorMetric() {
    return errorMetric;
  }
}
