package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polynomial {
  private final BigInteger[] coefficients;
  public ArrayList<Element> coef;
  public int deg;
  Arrays x;
  List<Element> c;

  public Polynomial(BigInteger... coefficients) {
    this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
  }

  public static Element eval_poly(Polynomial q, Element x) {
    int i;
    Element s, t, r;

    r = PairingManager.defaultPairing.getZr().newElement().setToZero();
    t = PairingManager.defaultPairing.getZr().newElement().setToOne();

    for (i = 0; i < q.deg + 1; i++) {
//      s = q.c.get(i).duplicate().mul(t);
      s = q.coef.get(i).duplicate().mul(t);
      r.add(s);
      t.mul(x);
    }
    return r;
  }

  public BigInteger evaluate(BigInteger x) {
    BigInteger y = BigInteger.ZERO;
    for (int i = 0; i < coefficients.length; i++) {
      if (coefficients[i].signum() != 0) {
        y = y.add(coefficients[i].multiply(x.pow(i)));
      }
    }
    return y;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int i = coefficients.length - 1; i >= 0; i--) {
      if (coefficients[i].signum() != 0) {
        builder.append(coefficients[i].toString());
        if (i == 1) {
          builder.append("x");
        } else if (i > 1) {
          builder.append("x^").append(i);
        }

        if (i != 0) {
          builder.append(" + ");
        }
      }
    }
    return builder.toString();
  }
}
