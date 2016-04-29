package hr.fer.zemris.ecf.symreg.model.util;

import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dstankovic on 4/29/16.
 */
public class TestUtils {
  public static LogModel generateLog(double fitness, int size) {
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
