package hr.fer.zemris.ecf.symreg.model.util;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by dstankovic on 4/29/16.
 */
public class ParetoFrontierUtilsTest {

  @Test
  public void testFindParetoFrontier() throws Exception {
    final double fit1 = 0.00884305;
    final double fit2 = 0.00798193;
    final double fit3 = 0.00851194;
    final double fit4 = 0.0084911;
    final int size1 = 12;
    final int size2 = 16;
    final int size3 = 35;
    final int size4 = 36;

    LogModel log11 = TestUtils.generateLog(fit1, size1);
    LogModel log12 = TestUtils.generateLog(fit1, size1);
    LogModel log13 = TestUtils.generateLog(fit1, size1);

    LogModel log21 = TestUtils.generateLog(fit2, size2);
    LogModel log22 = TestUtils.generateLog(fit2, size2);
    LogModel log23 = TestUtils.generateLog(fit2, size2);
    LogModel log24 = TestUtils.generateLog(fit2, size2);

    LogModel log31 = TestUtils.generateLog(fit3, size3);
    LogModel log32 = TestUtils.generateLog(fit3, size3);

    LogModel log41 = TestUtils.generateLog(fit4, size4);
    LogModel log42 = TestUtils.generateLog(fit4, size4);
    LogModel log43 = TestUtils.generateLog(fit4, size4);
    LogModel log44 = TestUtils.generateLog(fit4, size4);
    LogModel log45 = TestUtils.generateLog(fit4, size4);
    LogModel log46 = TestUtils.generateLog(fit4, size4);

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

    List<FitnessSizeLog> list = new ArrayList<>(logs.size());
    for (LogModel log : logs) {
      String hof = HallOfFameUtils.extractHof(log.getRuns().get(0));
      FitnessSizePair fitnessSizePair = HallOfFameUtils.extractFitnessAndSize(hof);
      list.add(new FitnessSizeLog(fitnessSizePair, log));
    }

    List<FitnessSizeLog> paretoFrontier = ParetoFrontierUtils.findParetoFrontier(list);
    assertTrue(paretoFrontier.size() == 4 + 3);

    Set<LogModel> set = new HashSet<>();
    for (FitnessSizeLog fitnessSizeLog : paretoFrontier) {
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
}