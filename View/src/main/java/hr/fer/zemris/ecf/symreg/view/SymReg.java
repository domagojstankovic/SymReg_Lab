package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.console.Job;
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
import java.util.List;

/**
 * Created by Domagoj on 06/06/15.
 */
public class SymReg extends JFrame implements JobListener {

    private BrowsePanel browsePnl = null;
    private JTextField terminalsetTxtFld = null;
    private CheckboxListPanel checkboxPanel = null;
    private ButtonsPanel btnsPanel = null;
    private LogModel log = null;

    public SymReg() {
        super();
        initGUI();

        setVisible(true);
    }

    private void initGUI() {
        setTitle("Symbolic regression Lab");
        setLocation(300, 100);
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(panel);

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        List<String> functions = SupportedFunctionsFactory.getProvider().supportedFunctions();
        checkboxPanel = new CheckboxListPanel(functions);
        add(checkboxPanel);

        terminalsetTxtFld = new JTextField("");
        terminalsetTxtFld.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) terminalsetTxtFld.getPreferredSize().getHeight()));
        add(terminalsetTxtFld);

        String startDir = ".";
        InfoService.getInstance().setLastSelectedPath(startDir);
        browsePnl = new BrowsePanel("", new File(startDir));
        add(browsePnl);

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

        btnsPanel = new ButtonsPanel(runBtn, resBtn);

        add(btnsPanel);

        pack();
    }

    private void resClicked() {
        System.out.println(log);
    }

    private void runClicked() {
        String terminalset = terminalsetTxtFld.getText();
        String inputFile = browsePnl.getText();
        List<String> functions = checkboxPanel.getCheckedItems();

        SRManager manager = new SRManager(this);
        manager.run(terminalset, inputFile, functions);
    }

    public static void main(String[] args) {
        ConfigurationService.getInstance().setReader(new XmlConfigurationReader());
        ConfigurationService.getInstance().setWriter(new XmlConfigurationWriter());

        Logger logger = new FileLogger("res/log/log.txt");
        LoggerProvider.setLogger(logger);

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
    public void jobFinished(Job job, LogModel logModel) {
        btnsPanel.getResBtn().setEnabled(true);
        btnsPanel.getResBtn().setText("Finished");
        log = logModel;
    }

    @Override
    public void jobFailed(Job job) {
        btnsPanel.getResBtn().setText("Failed");
    }
}
