package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.symreg.model.exp.ExperimentInput;
import hr.fer.zemris.ecf.symreg.model.exp.ParallelExperimentsListener;
import hr.fer.zemris.ecf.symreg.model.exp.ParallelSRManager;
import hr.fer.zemris.ecf.symreg.model.logger.Logger;
import hr.fer.zemris.ecf.symreg.model.logger.LoggerProvider;
import hr.fer.zemris.ecf.symreg.model.logger.impl.FileLogger;
import hr.fer.zemris.ecf.symreg.model.util.HallOfFameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Domagoj on 06/06/15.
 */
public class ParallelSymReg extends JFrame implements ParallelExperimentsListener {

  private ButtonsPanel btnsPanel = null;
  private List<LogModel> paretoFrontier = null;
  private ParallelSRManager srManager = null;
  private ResultsFrame resultsFrame = new ResultsFrame();
  private SRInputPanel panel;

  public ParallelSymReg() {
    super();
    initGUI();

    setVisible(true);
  }

  private void initGUI() {
    setTitle("Symbolic regression Lab");
    setLocation(300, 100);
    setSize(400, 300);
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    panel = new SRInputPanel();
    JPanel generalPanel = new JPanel();
    generalPanel.setLayout(new BorderLayout());
    setContentPane(generalPanel);

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
        srManager.stop();
      }
    });
    btnsPanel = new ButtonsPanel(runBtn, resBtn, stopButton);

    generalPanel.add(panel, BorderLayout.CENTER);
    generalPanel.add(btnsPanel, BorderLayout.SOUTH);

    pack();
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
    String terminalset = panel.getTerminalsetTxtFld().getText();
    String inputFile = panel.getInputFileBrowsePnl().getTextField();
    List<String> functions = panel.getCheckboxPanel().getCheckedItems();
    boolean linearScaling = panel.getLinearScalingCheckBox().isSelected();
    String errorWeightsFile = panel.getErrorWeightsFileBrowsePnl().getTextField();
    String errorMetric = panel.getErrorMetricsPanel().getSelectedValue();

    ParallelSRManager manager = getSrManager();
    manager.run(new ExperimentInput(terminalset, inputFile, functions, linearScaling, errorWeightsFile, errorMetric));
  }

  public ParallelSRManager getSrManager() {
    if (srManager == null) {
      int threads = Runtime.getRuntime().availableProcessors();
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
    btnsPanel.getTestBtn().setVisible(true);
    btnsPanel.getTestBtn().setEnabled(false);
  }

  @Override
  public void experimentsUpdated(List<LogModel> paretoFrontier) {
    btnsPanel.getResBtn().setEnabled(true);
    btnsPanel.getResBtn().setText("Running");
    btnsPanel.getTestBtn().setEnabled(true);
    this.paretoFrontier = paretoFrontier;

    displayResults();
  }
}
