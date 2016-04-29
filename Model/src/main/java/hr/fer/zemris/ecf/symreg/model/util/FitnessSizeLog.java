package hr.fer.zemris.ecf.symreg.model.util;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;

/**
 * Created by dstankovic on 4/28/16.
 */
public class FitnessSizeLog implements Comparable<FitnessSizeLog>, MultiObjectiveIndividual {
  public FitnessSizePair fitnessSizePair;
  public LogModel logModel;

  public FitnessSizeLog(FitnessSizePair fitnessSizePair, LogModel logModel) {
    this.fitnessSizePair = fitnessSizePair;
    this.logModel = logModel;
  }

  @Override
  public int compareTo(FitnessSizeLog o) {
    return fitnessSizePair.compareTo(o.fitnessSizePair);
  }

  @Override
  public String toString() {
    return fitnessSizePair.toString();
  }

  @Override
  public double fitnessAt(int index) {
    return fitnessSizePair.fitnessAt(index);
  }

  @Override
  public int size() {
    return fitnessSizePair.size();
  }
}
