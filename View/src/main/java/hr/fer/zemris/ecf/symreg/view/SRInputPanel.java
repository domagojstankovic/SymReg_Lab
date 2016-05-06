package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.symreg.model.info.InfoService;
import hr.fer.zemris.ecf.symreg.model.info.SupportedFunctionsFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by dstankovic on 4/27/16.
 */
public class SRInputPanel extends JPanel {
  private BrowsePanel inputFileBrowsePnl = null;
  private BrowsePanel errorWeightsFileBrowsePnl = null;
  private DropDownPanel errorMetricsPanel = null;
  private JTextField terminalsetTxtFld = null;
  private CheckboxListPanel checkboxPanel = null;
  private JCheckBox linearScalingCheckBox = null;

  public SRInputPanel() {
    initGUI();
  }

  private void initGUI() {
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Function set
    JLabel functionSetLbl = new JLabel("Function set");
    java.util.List<String> functions = SupportedFunctionsFactory.getProvider().supportedFunctions();
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
    TextValuePair[] errorMetricsTextValue = new TextValuePair[]{
        new TextValuePair("Mean square error", "mean_square_error"),
        new TextValuePair("Mean absolute error", "mean_absolute_error"),
        new TextValuePair("Mean absolute percentage error", "mean_absolute_percentage_error")
    };
    errorMetricsPanel = new DropDownPanel(errorMetricsTextValue);

    GroupLayout layout = new GroupLayout(this);
    setLayout(layout);
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
  }

  public BrowsePanel getInputFileBrowsePnl() {
    return inputFileBrowsePnl;
  }

  public BrowsePanel getErrorWeightsFileBrowsePnl() {
    return errorWeightsFileBrowsePnl;
  }

  public DropDownPanel getErrorMetricsPanel() {
    return errorMetricsPanel;
  }

  public JTextField getTerminalsetTxtFld() {
    return terminalsetTxtFld;
  }

  public CheckboxListPanel getCheckboxPanel() {
    return checkboxPanel;
  }

  public JCheckBox getLinearScalingCheckBox() {
    return linearScalingCheckBox;
  }
}
