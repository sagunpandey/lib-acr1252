package np.com.rts.lib.acr1252.exceptions;

public class InvalidResponseException extends RuntimeException {
  private byte[] _response;

  public byte[] getResponse() {
    return _response;
  }

  public InvalidResponseException(byte[] response) {
    this._response = response;
  }
}
