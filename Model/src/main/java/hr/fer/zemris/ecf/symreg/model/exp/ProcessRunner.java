package hr.fer.zemris.ecf.symreg.model.exp;

import java.io.IOException;

/**
 * Created by dstankovic on 1/22/16.
 */
public class ProcessRunner {
  public static void runProcess(String... args) throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder(args);
    Process process = pb.start();
    process.waitFor();
  }
}
