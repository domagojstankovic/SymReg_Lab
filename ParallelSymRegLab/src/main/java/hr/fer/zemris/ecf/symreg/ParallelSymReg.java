package hr.fer.zemris.ecf.symreg;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.symreg.model.exp.ParallelExperimentsListener;
import hr.fer.zemris.ecf.symreg.model.exp.ParallelSRManager;
import hr.fer.zemris.ecf.symreg.model.logger.Logger;
import hr.fer.zemris.ecf.symreg.model.logger.LoggerProvider;
import hr.fer.zemris.ecf.symreg.model.logger.impl.FileLogger;
import hr.fer.zemris.ecf.symreg.model.util.HallOfFameUtils;
import hr.fer.zemris.ecf.symreg.view.AbstractSymReg;
import hr.fer.zemris.ecf.symreg.view.ButtonsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Domagoj on 06/06/15.
 */
public class ParallelSymReg extends AbstractSymReg implements ParallelExperimentsListener {
  private List<LogModel> paretoFrontier = null;
  private ParallelSRManager srManager = null;

  public ParallelSymReg() {
    super();

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        closing();
      }
    });
  }

  @Override
  protected void initButtons() {
    // Buttons
    JButton runBtn = new JButton(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runClicked();
      }
    });
    runBtn.setText("Run");

    JButton resBtn = new JButton(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        resClicked();
      }
    });

    JButton stopButton = new JButton(new AbstractAction("Stop") {
      @Override
      public void actionPerformed(ActionEvent e) {
        stopClicked();
      }
    });

    btnsPanel = new ButtonsPanel(runBtn, resBtn, stopButton);
  }

  private void closing() {
    if (srManager != null) {
      srManager.stop();
    }
  }

  private void stopClicked() {
    srManager.stop();
    btnsPanel.getResBtn().setText("Finished");
    JButton testBtn = btnsPanel.getTestBtn();
    if (testBtn != null) {
      testBtn.setEnabled(false);
    }
  }

  private void resClicked() {
    displayResults();
    resultsFrame.setVisible(true);
  }

  private void displayResults() {
    Set<String> hofs = new HashSet<>();
    for (LogModel log : paretoFrontier) {
      ExperimentRun run = log.getRuns().get(0);
      String hof = resultsDisplayText(run);
      hofs.add(hof);
    }

    StringBuilder sb = new StringBuilder();
    for (String hof : hofs) {
      sb.append("*************************\n\n").append(hof).append("\n\n\n");
    }
    resultsFrame.setText(sb.toString());
  }

  private static String extractLinearScalingParams(ExperimentRun run) {
    List<String> otherLines = run.getOtherLines();
    for (String line : otherLines) {
      if (line.startsWith("Linear scaling parameters:")) {
        return line;
      }
    }
    return null;
  }

  private static String resultsDisplayText(ExperimentRun run) {
    String hof = HallOfFameUtils.extractHof(run);
    String linearScalingLine = extractLinearScalingParams(run);
    if (linearScalingLine == null) {
      // there are no linear scaling parameters
      return hof;
    } else {
      // there are linear scaling parameters
      return hof + "\n\n" + linearScalingLine;
    }
  }

  private void runClicked() {
    ParallelSRManager manager = getSrManager();
    manager.run(getExperimentInput());
  }

  public ParallelSRManager getSrManager() {
    if (srManager == null) {
      int threads = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);
      srManager = new ParallelSRManager(this, threads);
    }
    return srManager;
  }

  public static void main(String[] args) {
    ConfigurationService.getInstance().setReader(new XmlConfigurationReader());
    ConfigurationService.getInstance().setWriter(new XmlConfigurationWriter());

    Logger logger = new FileLogger("res/log/log.txt");
    LoggerProvider.setLogger(logger);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException |
        UnsupportedLookAndFeelException | IllegalAccessException e) {
      e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> new ParallelSymReg());
  }

  @Override
  public void experimentsStarted() {
    paretoFrontier = null;
    btnsPanel.addResBtn();
    btnsPanel.getResBtn().setText("Started");
    btnsPanel.getResBtn().setEnabled(false);
    JButton testBtn = btnsPanel.getTestBtn();
    if (testBtn != null) {
      testBtn.setVisible(true);
      testBtn.setEnabled(false);
    }
  }

  @Override
  public void experimentsUpdated(List<LogModel> paretoFrontier) {
    btnsPanel.getResBtn().setEnabled(true);
    btnsPanel.getResBtn().setText("Running");
    JButton testBtn = btnsPanel.getTestBtn();
    if (testBtn != null) {
      testBtn.setEnabled(true);
    }
    this.paretoFrontier = paretoFrontier;

    displayResults();
  }
}
