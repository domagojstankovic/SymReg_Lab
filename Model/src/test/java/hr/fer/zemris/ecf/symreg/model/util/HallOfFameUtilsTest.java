package hr.fer.zemris.ecf.symreg.model.util;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    LogModel log1 = TestUtils.generateLog(12, 4);
    LogModel log2 = TestUtils.generateLog(7, 6);
    LogModel log3 = TestUtils.generateLog(4, 9);
    LogModel log4 = TestUtils.generateLog(3, 14);
    LogModel log5 = TestUtils.generateLog(15, 15);
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
}