package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PairingManager {

  private static final String TYPE_A = "a.properties";
  public static final Pairing defaultPairing = PairingFactory.getPairing(TYPE_A);

  public Pairing getPairing(String parametersPath) {
    return PairingFactory.getPairing(parametersPath);
  }
}
