package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;


public class Pedersen implements SimpleSerializable {
  @Serializable(group = "Zr")
  Element g;
  @Serializable(group = "Zr")
  Element mo;
  @Serializable(group = "Zr")
  Element gmo;
  @Serializable(group = "Zr")
  Element c;
  @Serializable(group = "Zr")
  Element gc;
  @Serializable(group = "Zr")
  Element gama;
  @Serializable(group = "Zr")
  Element ggama;

  public Element getG() {
    return g;
  }

  public void setG(Element g) {
    this.g = g;
  }

  public Element getMo() {
    return mo;
  }

  public void setMo(Element mo) {
    this.mo = mo;
  }

  public Element getGmo() {
    return gmo;
  }

  public void setGmo(Element gmo) {
    this.gmo = gmo;
  }

  public Element getC() {
    return c;
  }

  public void setC(Element c) {
    this.c = c;
  }

  public Element getGc() {
    return gc;
  }

  public void setGc(Element gc) {
    this.gc = gc;
  }

  public Element getGama() {
    return gama;
  }

  public void setGama(Element gama) {
    this.gama = gama;
  }

  public Element getGgama() {
    return ggama;
  }

  public void setGgama(Element ggama) {
    this.ggama = ggama;
  }
}
