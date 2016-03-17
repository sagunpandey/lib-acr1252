package np.com.rts.lib.acr1252.exceptions;

public class InvalidResponseTypeException extends RuntimeException {
  private byte _expectedResponseType;

  public byte getExpectedResponseType() {
    return _expectedResponseType;
  }

  private byte _actualResponseType;

  public byte getActualResponseType() {
    return _actualResponseType;
  }

  public InvalidResponseTypeException(byte expectedResponseType, byte actualResponseType) {
    this._expectedResponseType = expectedResponseType;
    this._actualResponseType = actualResponseType;
  }
}
