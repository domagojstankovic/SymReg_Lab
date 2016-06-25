package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.symreg.model.exp.ExperimentInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by dstankovic on 5/18/16.
 */
public abstract class AbstractSymReg extends JFrame implements ExperimentInputProvider {
  protected SRInputPanel panel;
  protected ButtonsPanel btnsPanel = null;
  protected ResultsFrame resultsFrame = new ResultsFrame();

  public AbstractSymReg() throws HeadlessException {
    super();

    initGUI();
    setVisible(true);
  }

  private void initGUI() {
    setTitle("Symbolic regression Lab");
    setLocation(300, 100);
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    panel = new SRInputPanel();
    JPanel generalPanel = new JPanel();
    generalPanel.setLayout(new BorderLayout());
    setContentPane(generalPanel);

    initButtons();
    initMenuBar();

    generalPanel.add(panel, BorderLayout.CENTER);
    generalPanel.add(btnsPanel, BorderLayout.SOUTH);

    pack();
  }

  protected abstract void initButtons();

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
    new TestFrame(this).setVisible(true);
  }

  @Override
  public ExperimentInput getExperimentInput() {
    String terminalset = panel.getTerminalsetTxtFld().getText();
    String inputFile = panel.getInputFileBrowsePnl().getTextField();
    List<String> functions = panel.getCheckboxPanel().getCheckedItems();
    boolean linearScaling = panel.getLinearScalingCheckBox().isSelected();
    boolean intervalArithmetic = panel.getIntervalArtihmeticCheckBox().isSelected();
    String errorWeightsFile = panel.getErrorWeightsFileBrowsePnl().getTextField();
    String errorMetric = panel.getErrorMetricsPanel().getSelectedValue();

    return new ExperimentInput(
        terminalset,
        inputFile,
        functions,
        linearScaling,
        intervalArithmetic,
        errorWeightsFile,
        errorMetric);
  }
}
