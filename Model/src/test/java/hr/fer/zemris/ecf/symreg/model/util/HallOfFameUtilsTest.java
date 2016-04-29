package hr.fer.zemris.ecf.symreg.model.util;

import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by dstankovic on 4/27/16.
 */
public class HallOfFameUtilsTest {

  @Test
  public void testExtractFitnessAndSizeFullHof() throws Exception {
    String hof = "<HallOfFame size=\"1\">\n" +
        "\t<Individual size=\"1\" gen=\"2\">\n" +
        "\t\t<FitnessMin value=\"9.80899e-39\"/>\n" +
        "\t\t<Tree size=\"4\">sin / x1 3 </Tree>\n" +
        "\t</Individual>\n" +
        "</HallOfFame>";

    FitnessSizePair fitnessSizePair = HallOfFameUtils.extractFitnessAndSize(hof);
    assertTrue(Math.abs(fitnessSizePair.getFitness() - 9.80899e-39) < 1e-9);
    assertTrue(fitnessSizePair.getSize() == 4);
  }

  @Test
  public void testExtractFitnessAndSizePartialHof() throws Exception {
    String hof = "<Individual size=\"1\">\n" +
        "\t<FitnessMin value=\"0.119231\"/>\n" +
        "\t<Tree size=\"13\">* cos sin cos * 3 x1 sin / * 3 2 1 </Tree>\n" +
        "</Individual>";

    FitnessSizePair fitnessSizePair = HallOfFameUtils.extractFitnessAndSize(hof);
    assertTrue(Math.abs(fitnessSizePair.getFitness() - 0.119231) < 1e-9);
    assertTrue(fitnessSizePair.getSize() == 13);
  }

  @Test
  public void testExtractParetoFrontier() throws Exception {
    List<LogModel> logs = new ArrayList<>();
    LogModel log1 = generateLog(12, 4);
    LogModel log2 = generateLog(7, 6);
    LogModel log3 = generateLog(4, 9);
    LogModel log4 = generateLog(3, 14);
    LogModel log5 = generateLog(15, 15);
    logs.add(log4);
    logs.add(log2);
    logs.add(log1);
    logs.add(log5);
    logs.add(log3);
    List<FitnessSizeLog> filtered = HallOfFameUtils.extractParetoFrontier(logs);

    assertTrue(filtered.size() == 4);
    Set<LogModel> set = new HashSet<>();
    for (FitnessSizeLog fitnessSizeLog : filtered) {
      set.add(fitnessSizeLog.logModel);
    }

    assertTrue(set.contains(log1));
    assertTrue(set.contains(log2));
    assertTrue(set.contains(log3));
    assertTrue(set.contains(log4));
    assertTrue(!set.contains(log5));
  }

  @Test
  public void testExtractParetoFrontier2() throws Exception {
    final double fit1 = 0.00884305;
    final double fit2 = 0.00798193;
    final double fit3 = 0.00851194;
    final double fit4 = 0.0084911;
    final int size1 = 12;
    final int size2 = 16;
    final int size3 = 35;
    final int size4 = 36;

    LogModel log11 = generateLog(fit1, size1);
    LogModel log12 = generateLog(fit1, size1);
    LogModel log13 = generateLog(fit1, size1);

    LogModel log21 = generateLog(fit2, size2);
    LogModel log22 = generateLog(fit2, size2);
    LogModel log23 = generateLog(fit2, size2);
    LogModel log24 = generateLog(fit2, size2);

    LogModel log31 = generateLog(fit3, size3);
    LogModel log32 = generateLog(fit3, size3);

    LogModel log41 = generateLog(fit4, size4);
    LogModel log42 = generateLog(fit4, size4);
    LogModel log43 = generateLog(fit4, size4);
    LogModel log44 = generateLog(fit4, size4);
    LogModel log45 = generateLog(fit4, size4);
    LogModel log46 = generateLog(fit4, size4);

    List<LogModel> logs = new ArrayList<>();
    logs.add(log11);
    logs.add(log12);
    logs.add(log13);
    logs.add(log21);
    logs.add(log22);
    logs.add(log23);
    logs.add(log24);
    logs.add(log31);
    logs.add(log32);
    logs.add(log41);
    logs.add(log42);
    logs.add(log43);
    logs.add(log44);
    logs.add(log45);
    logs.add(log46);
    List<FitnessSizeLog> filtered = HallOfFameUtils.extractParetoFrontier(logs);

    assertTrue(filtered.size() == 4 + 3);
    Set<LogModel> set = new HashSet<>();
    for (FitnessSizeLog fitnessSizeLog : filtered) {
      set.add(fitnessSizeLog.logModel);
    }

    assertTrue(set.contains(log11));
    assertTrue(set.contains(log12));
    assertTrue(set.contains(log13));
    assertTrue(set.contains(log21));
    assertTrue(set.contains(log22));
    assertTrue(set.contains(log23));
    assertTrue(set.contains(log24));
  }

  private static LogModel generateLog(double fitness, int size) {
    List<ExperimentRun> runs = new ArrayList<>(1);
    ExperimentRun run = new ExperimentRun(new LinkedList<>());
    run.setHallOfFame(generateHallOfFame(fitness, size));
    runs.add(run);
    return new LogModel(runs);
  }

  private static String generateHallOfFame(double fitness, int size) {
    return "<Individual size=\"1\">\n" +
        "\t<FitnessMin value=\"" + fitness + "\"/>\n" +
        "\t<Tree size=\"" + size + "\">* cos sin cos * 3 x1 sin / * 3 2 1 </Tree>\n" +
        "</Individual>";
  }
}