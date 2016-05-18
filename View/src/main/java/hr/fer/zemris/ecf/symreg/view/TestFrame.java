package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import hr.fer.zemris.ecf.symreg.model.exp.ExperimentUtils;
import hr.fer.zemris.ecf.symreg.model.exp.SRManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by dstankovic on 5/7/16.
 */
public class TestFrame extends JFrame {
  private ExperimentInputProvider experimentInputProvider;
  private SRManager srManager = null;

  private BrowsePanel hofPanel = null;
  private BrowsePanel testPanel = null;

  public TestFrame(ExperimentInputProvider experimentInputProvider) throws HeadlessException {
    super();

    this.experimentInputProvider = experimentInputProvider;

    setTitle("Test");
    setLocation(400, 200);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    hofPanel = new BrowsePanel("hof.txt");
    testPanel = new BrowsePanel("testOut.txt");

    panel.add(hofPanel);
    panel.add(testPanel);

    JButton testBtn = new JButton(new AbstractAction("Test") {
      @Override
      public void actionPerformed(ActionEvent e) {
        testClicked();
      }
    });

    panel.add(testBtn);
    add(panel);

    pack();
  }

  private void testClicked() {
    SRManager manager = getSrManager();
    try {
      Configuration templateConfiguration = SRManager.readTemplateConfiguration();
      ExperimentUtils.updateConfiguration(templateConfiguration, experimentInputProvider.getExperimentInput());
      ExperimentUtils.anulateBatchRepeats(templateConfiguration);

      File confFile = File.createTempFile("srmlab-test-config", ".txt");
      ConfigurationWriter configurationWriter = ConfigurationService.getInstance().getWriter();
      configurationWriter.write(confFile, templateConfiguration);
      manager.runTest(confFile.getAbsolutePath(), hofPanel.getTextField(), testPanel.getTextField());
      confFile.deleteOnExit();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public SRManager getSrManager() {
    if (srManager == null) {
      srManager = new SRManager(new JobListener() {
        // no need for a listener
        @Override
        public void jobInitialized(Job job) {
        }

        @Override
        public void jobStarted(Job job) {
        }

        @Override
        public void jobPartiallyFinished(Job job, LogModel logModel) {
        }

        @Override
        public void jobFinished(Job job, LogModel logModel) {
        }

        @Override
        public void jobFailed(Job job) {
        }
      });
    }
    return srManager;
  }
}
