package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.util.List;

/**
 * Created by dstankovic on 4/27/16.
 */
public interface ParallelExperimentsListener {
  void experimentsStarted();
  void experimentsUpdated(List<LogModel> paretoFrontier);
}
