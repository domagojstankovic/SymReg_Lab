package hr.fer.zemris.ecf.symreg.model.util;

import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dstankovic on 4/27/16.
 */
public class HallOfFameUtils {
  public static String extractHof(ExperimentRun run) {
    if (run.getHallOfFame() != null) {
      return run.getHallOfFame();
    } else {
      for (int i = run.getGenerations().size() - 1; i >= 0; i--) {
        String hof = run.getGenerations().get(i).hallOfFame;
        if (hof != null && !hof.trim().isEmpty()) {
          return hof;
        }
      }
    }
    return null;
  }

  public static List<FitnessSizeLog> extractParetoFrontier(List<LogModel> logs) {
    List<MultiObjectiveIndividual> list = new ArrayList<>(logs.size());
    for (LogModel log : logs) {
      String hof = HallOfFameUtils.extractHof(log.getRuns().get(0));
      FitnessSizePair fitnessSizePair = HallOfFameUtils.extractFitnessAndSize(hof);
      list.add(new FitnessSizeLog(fitnessSizePair, log));
    }

    List<List<MultiObjectiveIndividual>> fronts = ParetoFrontierUtils.nonDominatedSort(list);

    return filterParetoFrontier(fronts);
  }

  private static List<FitnessSizeLog> filterParetoFrontier(List<List<MultiObjectiveIndividual>> fronts) {
    List<FitnessSizeLog> filtered = new ArrayList<>();
    List<MultiObjectiveIndividual> front = fronts.get(fronts.size() - 1);
    for (MultiObjectiveIndividual individual : front) {
      filtered.add((FitnessSizeLog)individual);
    }
    return filtered;
  }

  public static FitnessSizePair extractFitnessAndSize(String hof) {
    return new FitnessSizePair(extractFitness(hof), extractSize(hof));
  }

  private static double extractFitness(String hof) {
    int fitnessMinIndex = hof.indexOf("<FitnessMin ");
    int valueIndex = hof.indexOf("value=", fitnessMinIndex);
    int openBracketIndex = hof.indexOf("\"", valueIndex);
    int closeBracketIndex = hof.indexOf("\"", openBracketIndex + 1);
    return Double.parseDouble(hof.substring(openBracketIndex + 1, closeBracketIndex));
  }

  private static int extractSize(String hof) {
    int treeIndex = hof.indexOf("<Tree ");
    int valueIndex = hof.indexOf("size=", treeIndex);
    int openBracketIndex = hof.indexOf("\"", valueIndex);
    int closeBracketIndex = hof.indexOf("\"", openBracketIndex + 1);
    return Integer.parseInt(hof.substring(openBracketIndex + 1, closeBracketIndex));
  }
}
