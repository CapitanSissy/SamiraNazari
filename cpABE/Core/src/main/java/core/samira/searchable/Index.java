package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

public class Index implements SimpleSerializable {


//	@Serializable(group="GT")
//	public Element t3;
  // @Serializable(group="G2")
  // public Element gp; // G2 generator

  // @Serializable(group="GT")
  // public Element g_hat_alpha; // GT
  INComponent[] comps;

  public static class INComponent implements SimpleSerializable {
    @Serializable(group = "G1")
    public Element i1; // G1 generator
    @Serializable(group = "GT")
    public Element i2;
    @Serializable
    String keyword;
    @Serializable
    String Addr;
//		@Serializable(group="GT")
//		public Element galphav;
//		@Serializable(group="GT")
//		public Element gbeta;

  }

  // @Serializable(group="G1")
  // public Element h; //G1


}
