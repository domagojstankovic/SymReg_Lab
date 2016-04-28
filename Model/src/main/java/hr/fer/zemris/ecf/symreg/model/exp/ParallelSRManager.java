package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import hr.fer.zemris.ecf.symreg.model.util.FitnessSizeLog;
import hr.fer.zemris.ecf.symreg.model.util.HallOfFameUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dstankovic on 4/27/16.
 */
public class ParallelSRManager implements JobListener {
  private ExperimentInput experimentInput;
  private ParallelExperimentsListener listener;
  private int threads;

  private ExecutorService service;
  private boolean stopped = false;
  private int partiallyFinishedCount = 0;
  private boolean started = false;
  private List<LogModel> logs;

  public ParallelSRManager(ParallelExperimentsListener listener, int threads) {
    this.listener = listener;
    this.threads = threads;

    logs = new ArrayList<>(threads);
    service = Executors.newFixedThreadPool(threads);
  }

  public void run(ExperimentInput experimentInput) {
    this.experimentInput = experimentInput;
    List<Callable<Void>> tasks = new ArrayList<>(threads);
    for (int i = 0; i < threads; i++) {
      tasks.add(() -> {
        runNewExperiment();
        return null;
      });
    }
    invokeTasks(tasks);
  }

  public void stop() {
    stopped = true;
    service.shutdown();
  }

  private void invokeNewExperiment() {
    if (!stopped) {
      invokeTask(() -> {
        runNewExperiment();
        return null;
      });
    }
  }

  private void runNewExperiment() {
    SRManager manager = new SRManager(this);
    manager.run(
        experimentInput.getTerminalset(),
        experimentInput.getInputFile(),
        experimentInput.getFunctions(),
        experimentInput.isLinearScaling(),
        experimentInput.getErrorWeightsFile(),
        experimentInput.getErrorMetric()
    );
  }

  private void invokeTask(Callable<Void> task) {
    List<Callable<Void>> tasks = new ArrayList<>(1);
    tasks.add(task);
    invokeTasks(tasks);
  }

  private void invokeTasks(List<Callable<Void>> tasks) {
    try {
      service.invokeAll(tasks);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private List<LogModel> extractParetoFrontier() {
    List<FitnessSizeLog> list = HallOfFameUtils.extractParetoFrontier(logs);

    Set<LogModel> set = new HashSet<>(list.size());
    List<LogModel> paretoFrontier = new ArrayList<>(list.size());
    for (FitnessSizeLog fitnessSizeLog : list) {
      set.add(fitnessSizeLog.logModel);
      paretoFrontier.add(fitnessSizeLog.logModel);
    }
    logs.retainAll(set); // remove obsolete logs

    return paretoFrontier;
  }

  @Override
  public void jobInitialized(Job job) {}

  @Override
  public void jobStarted(Job job) {
    synchronized (this) {
      if (!started) {
        listener.experimentsStarted();
        started = true;
      }
    }
  }

  @Override
  public void jobPartiallyFinished(Job job, LogModel logModel) {
    synchronized (this) {
      logs.add(logModel);
      partiallyFinishedCount++;
      if (partiallyFinishedCount % threads == 0) {
        listener.experimentsUpdated(extractParetoFrontier());
      }
    }
  }

  @Override
  public void jobFinished(Job job, LogModel logModel) {
    synchronized (this) {
      invokeNewExperiment();
    }
  }

  @Override
  public void jobFailed(Job job) {
    synchronized (this) {
      invokeNewExperiment();
    }
  }
}
