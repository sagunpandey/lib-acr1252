package np.com.rts.lib.acr1252.exceptions;

public class InvalidCommandResponseException extends RuntimeException {
  public enum COMMAND_RESPONSE_CODE {
    CMD_OKAY,
    INVALID_PARAMETERS,
    INVALID_COMMAND_CODE,
    INVALID_COMMAND_LENGTH,
    CANNOT_EXECUTE_COMMAND,
    TIMEOUT,
    SCRIPT_ERROR,
    UNKNOWN
  }

  private COMMAND_RESPONSE_CODE _responseCode;

  public COMMAND_RESPONSE_CODE getResponseCode() {
    return _responseCode;
  }

  private byte[] _response;

  public byte[] getResponse() {
    return _response;
  }

  public String getMessage() {
    return getResponseCode().toString();
  }

  public InvalidCommandResponseException(byte[] response) {
    this._response = response;
    _responseCode = getResponseCode(response);
  }

  private COMMAND_RESPONSE_CODE getResponseCode(byte[] response) {
    if (response[0] == (byte) 0x00 && response[1] == (byte) 0x00)
      return COMMAND_RESPONSE_CODE.CMD_OKAY;

    if (response[0] == (byte) 0xFF && response[1] == (byte) 0xFF)
      return COMMAND_RESPONSE_CODE.INVALID_PARAMETERS;

    if (response[0] == (byte) 0xFF && response[1] == (byte) 0xFE)
      return COMMAND_RESPONSE_CODE.INVALID_COMMAND_CODE;

    if (response[0] == (byte) 0xFF && response[1] == (byte) 0xFD)
      return COMMAND_RESPONSE_CODE.INVALID_COMMAND_LENGTH;

    if (response[0] == (byte) 0xFF && response[1] == (byte) 0xFC)
      return COMMAND_RESPONSE_CODE.CANNOT_EXECUTE_COMMAND;

    if (response[0] == (byte) 0xFF && response[1] == (byte) 0xFB)
      return COMMAND_RESPONSE_CODE.TIMEOUT;

    if (response[0] == (byte) 0xFF && response[1] == (byte) 0xFA)
      return COMMAND_RESPONSE_CODE.SCRIPT_ERROR;

    return COMMAND_RESPONSE_CODE.UNKNOWN;
  }

}
