package hr.fer.zemris.ecf.symreg.model.exp;

import hr.fer.zemris.ecf.lab.engine.console.DetectOS;

/**
 * Created by Domagoj on 08/06/15.
 */
public class EcfFileProvider {

    public static String getEcfFile() {
        if (DetectOS.isMac()) {
            return "mac_SRM";
        }
        if (DetectOS.isWindows()) {
            return "win_SRM.exe";
        }
        if (DetectOS.isUnix()) {
            return "linux_SRM";
        }
        return null;
    }
}
