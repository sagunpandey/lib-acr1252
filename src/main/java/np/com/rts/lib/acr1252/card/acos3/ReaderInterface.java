package np.com.rts.lib.acr1252.card.acos3;

import np.com.rts.lib.acr1252.acs.ACR1252U;

public class ReaderInterface extends ACR1252U {

  public ReaderInterface(String platform) {
    super(platform);
  }

  public enum CHIP_TYPE {
    UNKNOWN(0x01),
    ACOS3(0x02),
    ACOS3COMBI(0x03),
    ACOS3CONTACTLESS(0x04);

    private final int _id;

    CHIP_TYPE(int id) {
      this._id = id;
    }

    public int getId() {
      return _id;
    }
  }

  public CHIP_TYPE getChipType() {
    byte[] atr;
    byte[] cardVersion;
    CHIP_TYPE cardType = CHIP_TYPE.UNKNOWN;
    Acos3 acos3 = new Acos3(this);

    try {
      atr = this.getAtr();

      if (atr.length == 8) { // Contactless
        if (atr[4] != (byte) 0x41)
          return CHIP_TYPE.UNKNOWN;

        cardVersion = acos3.getCardInfo(Acos3.CARD_INFO_TYPE.VERSION_NUMBER);

        if (cardVersion != null) {
          if (cardVersion[4] != (byte) 0x03)
            cardType = CHIP_TYPE.UNKNOWN;
          else {
            if (atr[6] == (byte) 0x26)
              cardType = CHIP_TYPE.ACOS3CONTACTLESS;
            else
              cardType = CHIP_TYPE.UNKNOWN;
          }
        }
      } else {
        if (atr[5] != (byte) 0x41)
          return CHIP_TYPE.UNKNOWN;

        cardVersion = acos3.getCardInfo(Acos3.CARD_INFO_TYPE.VERSION_NUMBER);

        if (cardVersion != null) {
          if (cardVersion[4] != (byte) 0x03)
            cardType = CHIP_TYPE.UNKNOWN;
          else {
            if (atr[7] == (byte) 0x17)
              cardType = CHIP_TYPE.ACOS3COMBI;
            else
              cardType = CHIP_TYPE.ACOS3;
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return cardType;
  }
}
