package hr.fer.zemris.ecf.symreg.model.exp;

/**
 * Created by Domagoj on 08/06/15.
 */
public class SRManagerException extends RuntimeException {

  public SRManagerException() {
  }

  public SRManagerException(String message) {
    super(message);
  }

  public SRManagerException(String message, Throwable cause) {
    super(message, cause);
  }

  public SRManagerException(Throwable cause) {
    super(cause);
  }

  public SRManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
