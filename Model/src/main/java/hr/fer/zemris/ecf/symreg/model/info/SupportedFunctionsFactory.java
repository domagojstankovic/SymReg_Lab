package hr.fer.zemris.ecf.symreg.model.info;

/**
 * Created by Domagoj on 08/06/15.
 */
public class SupportedFunctionsFactory {

    public static SupportingFunctionsProvider getProvider() {
        return new HardcodedFunctionsProvider();
    }
}
