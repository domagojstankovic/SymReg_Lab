package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
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
public class SymReg extends JFrame {

    private BrowsePanel browsePnl = null;
    private JTextField terminalsetTxtFld = null;
    private JButton runBtn = null;
    private CheckboxListPanel checkboxPanel = null;

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

        runBtn = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runClicked();
            }
        });
        runBtn.setText("Run");
        add(runBtn);

        pack();
    }

    private void runClicked() {
        String terminalset = terminalsetTxtFld.getText();
        String inputFile = browsePnl.getText();
        List<String> functions = checkboxPanel.getCheckedItems();

        SRManager manager = new SRManager();
        manager.run(terminalset, inputFile, functions);
    }

    public static void main(String[] args) {
        ConfigurationService.getInstance().setReader(new XmlConfigurationReader());
        ConfigurationService.getInstance().setWriter(new XmlConfigurationWriter());

        Logger logger = new FileLogger("res/log/log.txt");
        LoggerProvider.setLogger(logger);

        SwingUtilities.invokeLater(() -> new SymReg());
    }
}
