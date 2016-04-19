package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import hr.fer.zemris.ecf.symreg.model.exp.SRManager;
import hr.fer.zemris.ecf.symreg.model.info.InfoService;
import hr.fer.zemris.ecf.symreg.model.info.SupportedFunctionsFactory;
import hr.fer.zemris.ecf.symreg.model.logger.Logger;
import hr.fer.zemris.ecf.symreg.model.logger.LoggerProvider;
import hr.fer.zemris.ecf.symreg.model.logger.impl.FileLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Domagoj on 06/06/15.
 */
public class SymReg extends JFrame implements JobListener {

  private BrowsePanel inputFileBrowsePnl = null;
  private BrowsePanel errorWeightsFileBrowsePnl = null;
  private RadioButtonsPanel errorMetricsPanel = null;
  private JTextField terminalsetTxtFld = null;
  private CheckboxListPanel checkboxPanel = null;
  private JCheckBox linearScalingCheckBox = null;
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
    setSize(400, 300);
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    JPanel generalPanel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    generalPanel.setLayout(new BorderLayout());
    setContentPane(generalPanel);

    // Function set
    JLabel functionSetLbl = new JLabel("Function set");
    List<String> functions = SupportedFunctionsFactory.getProvider().supportedFunctions();
    checkboxPanel = new CheckboxListPanel(functions);

    // Terminal set
    JLabel terminalSetLbl = new JLabel("Terminal set");
    terminalsetTxtFld = new JTextField("");
    terminalsetTxtFld.setMaximumSize(new Dimension(Integer.MAX_VALUE,
        (int) terminalsetTxtFld.getPreferredSize().getHeight()));

    // Linear scaling
    JLabel linearScalingLbl = new JLabel("Linear scaling");
    linearScalingCheckBox = new JCheckBox();

    // Input file
    JLabel inputFileLbl = new JLabel("Input file");
    String startDir = ".";
    InfoService.getInstance().setLastSelectedPath(startDir);
    inputFileBrowsePnl = new BrowsePanel("", new File(startDir));

    // Error weights file
    JLabel errorWeightsFileLbl = new JLabel("Error weights file");
    errorWeightsFileBrowsePnl = new BrowsePanel("", new File(startDir));

    // Error metrics
    JLabel errorMetricLbl = new JLabel("Error metric");
    List<TextValuePair> errorMetricsTextValue = new LinkedList<>();
    errorMetricsTextValue.add(new TextValuePair("Mean square error", "mean_square_error"));
    errorMetricsTextValue.add(new TextValuePair("Mean absolute error", "mean_absolute_error"));
    errorMetricsTextValue.add(new TextValuePair("Mean absolute percentage error", "mean_absolute_percentage_error"));
    errorMetricsPanel = new RadioButtonsPanel(errorMetricsTextValue);

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

    GroupLayout layout = new GroupLayout(panel);
    panel.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    layout.setHorizontalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(functionSetLbl)
                .addComponent(terminalSetLbl)
                .addComponent(linearScalingLbl)
                .addComponent(inputFileLbl)
                .addComponent(errorWeightsFileLbl)
                .addComponent(errorMetricLbl)
            ).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(checkboxPanel)
            .addComponent(terminalsetTxtFld)
            .addComponent(linearScalingCheckBox)
            .addComponent(inputFileBrowsePnl)
            .addComponent(errorWeightsFileBrowsePnl)
            .addComponent(errorMetricsPanel)
        )
    );

    layout.setVerticalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(functionSetLbl)
                .addComponent(checkboxPanel)
            ).addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(terminalSetLbl)
            .addComponent(terminalsetTxtFld)
        ).addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(linearScalingLbl)
            .addComponent(linearScalingCheckBox)
        ).addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(inputFileLbl)
            .addComponent(inputFileBrowsePnl)
        ).addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(errorWeightsFileLbl)
            .addComponent(errorWeightsFileBrowsePnl)
        ).addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(errorMetricLbl)
            .addComponent(errorMetricsPanel)
        )
    );

    generalPanel.add(panel, BorderLayout.CENTER);
    generalPanel.add(btnsPanel, BorderLayout.SOUTH);

    pack();
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

  private static String extractHof(ExperimentRun run) {
    if (run.getHallOfFame() != null) {
      return run.getHallOfFame();
    } else {
      for (int i = run.getGenerations().size() - 1; i >= 0; i--) {
        String hof = run.getGenerations().get(i).hallOfFame;
        if (hof != null) {
          return hof;
        }
      }
    }
    return null;
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
    String hof = extractHof(run);
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
    String terminalset = terminalsetTxtFld.getText();
    String inputFile = inputFileBrowsePnl.getTextField();
    List<String> functions = checkboxPanel.getCheckedItems();
    boolean linearScaling = linearScalingCheckBox.isSelected();
    String errorWeightsFile = errorWeightsFileBrowsePnl.getTextField();
    String errorMetric = errorMetricsPanel.getSelectedValue();

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
