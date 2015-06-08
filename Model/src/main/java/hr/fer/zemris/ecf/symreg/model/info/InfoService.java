package hr.fer.zemris.ecf.symreg.model.info;

/**
 * Created by Domagoj on 08/04/15.
 */
public class InfoService {

    private String lastSelectedPath;

    private static InfoService instance = null;

    private InfoService() {
    }

    public static InfoService getInstance() {
        if (instance == null) {
            instance = new InfoService();
        }
        return instance;
    }

    public void setLastSelectedPath(String lastSelectedPath) {
        this.lastSelectedPath = lastSelectedPath;
    }

    public String getLastSelectedPath() {
        return lastSelectedPath;
    }
}
