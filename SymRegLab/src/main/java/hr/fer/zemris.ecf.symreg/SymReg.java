package hr.fer.zemris.ecf.symreg;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import hr.fer.zemris.ecf.symreg.model.exp.SRManager;
import hr.fer.zemris.ecf.symreg.model.logger.Logger;
import hr.fer.zemris.ecf.symreg.model.logger.LoggerProvider;
import hr.fer.zemris.ecf.symreg.model.logger.impl.FileLogger;
import hr.fer.zemris.ecf.symreg.model.util.HallOfFameUtils;
import hr.fer.zemris.ecf.symreg.view.ButtonsPanel;
import hr.fer.zemris.ecf.symreg.view.ResultsFrame;
import hr.fer.zemris.ecf.symreg.view.SRInputPanel;
import hr.fer.zemris.ecf.symreg.view.TestFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

/**
 * Created by Domagoj on 06/06/15.
 */
public class SymReg extends JFrame implements JobListener {

  private SRInputPanel inputPanel;
  private ButtonsPanel btnsPanel = null;
  private LogModel log = null;
  private SRManager srManager = null;
  private ResultsFrame resultsFrame = new ResultsFrame();

  public SymReg() {
    super();
    initGUI();

    setVisible(true);
  }

  private void initGUI() {
    setTitle("Symbolic regression Lab");
    setLocation(300, 100);
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    inputPanel = new SRInputPanel();
    JPanel generalPanel = new JPanel();
    generalPanel.setLayout(new BorderLayout());
    setContentPane(generalPanel);

    initBtns();
    initMenuBar();

    generalPanel.add(inputPanel, BorderLayout.CENTER);
    generalPanel.add(btnsPanel, BorderLayout.SOUTH);

    pack();
  }

  private void initBtns() {
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

    JButton testBtn = new JButton(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        testClicked();
      }
    });
    testBtn.setText("Test");


    btnsPanel = new ButtonsPanel(runBtn, resBtn, testBtn);
  }

  private void initMenuBar() {
    JMenu testMenu = new JMenu("Test");
    testMenu.add(new AbstractAction("Test individual") {
      @Override
      public void actionPerformed(ActionEvent e) {
        testIndividual();
      }
    });

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(testMenu);

    setJMenuBar(menuBar);
  }

  private void testIndividual() {
    new TestFrame().setVisible(true);
  }

  private void testClicked() {
    SRManager manager = getSrManager();
    try {
      manager.runTest(log);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void resClicked() {
    ExperimentRun run = log.getRuns().get(0);
    String hof = resultsDisplayText(run);

    resultsFrame.setText(hof);
    resultsFrame.setVisible(true);
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
    String terminalset = inputPanel.getTerminalsetTxtFld().getText();
    String inputFile = inputPanel.getInputFileBrowsePnl().getTextField();
    List<String> functions = inputPanel.getCheckboxPanel().getCheckedItems();
    boolean linearScaling = inputPanel.getLinearScalingCheckBox().isSelected();
    String errorWeightsFile = inputPanel.getErrorWeightsFileBrowsePnl().getTextField();
    String errorMetric = inputPanel.getErrorMetricsPanel().getSelectedValue();

    SRManager manager = getSrManager();
    manager.run(terminalset, inputFile, functions, linearScaling, errorWeightsFile, errorMetric);
  }

  public SRManager getSrManager() {
    if (srManager == null) {
      srManager = new SRManager(this);
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

    SwingUtilities.invokeLater(() -> new SymReg());
  }

  @Override
  public void jobInitialized(Job job) {
    log = null;
    btnsPanel.addResBtn();
    btnsPanel.getResBtn().setText("Initialized");
    btnsPanel.getResBtn().setEnabled(false);
  }

  @Override
  public void jobStarted(Job job) {
    btnsPanel.getResBtn().setText("Started");
  }

  @Override
  public void jobPartiallyFinished(Job job, LogModel logModel) {
    btnsPanel.getResBtn().setEnabled(true);
    btnsPanel.getResBtn().setText("Running");
    log = logModel;

    ExperimentRun run = log.getRuns().get(0);
    String hof = resultsDisplayText(run);

    resultsFrame.setText(hof);
  }

  @Override
  public void jobFinished(Job job, LogModel logModel) {
    btnsPanel.getResBtn().setEnabled(true);
    btnsPanel.getResBtn().setText("Finished");
    log = logModel;
    btnsPanel.getTestBtn().setVisible(true);

    ExperimentRun run = log.getRuns().get(0);
    String hof = resultsDisplayText(run);

    resultsFrame.setText(hof);
  }

  @Override
  public void jobFailed(Job job) {
    btnsPanel.getResBtn().setText("Failed");
  }
}
