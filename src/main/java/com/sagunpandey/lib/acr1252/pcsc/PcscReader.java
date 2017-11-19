package com.sagunpandey.lib.acr1252.pcsc;

import com.sagunpandey.lib.acr1252.apdu.Apdu;

import javax.smartcardio.*;
import java.util.List;

public class PcscReader {
  protected TerminalFactory _terminalFactory;
  protected List<CardTerminal> _cardTerminalList;
  protected CardTerminal _activeTerminal;
  protected Card _card;
  protected CardChannel _cardChannel;
  protected CommandAPDU _commandApdu;
  protected ResponseAPDU _responseApdu;
  protected byte[] _controlCommand, _controlResponse;
  protected String _preferredProtocol;

  protected int _controlCode;
  protected boolean _connectionActive;
  private int returnCode = 0;

  protected ReaderEvents _eventHandler;
  protected PollingEvent _pollingEventHandler;

  // Default constructor
  public PcscReader() {
    setTerminalFactory(TerminalFactory.getDefault());
    setPreferredProtocol("*");
    setConnectionActive(false);

    // Set a system property for the protocol of the card so that "GET RESPONSE" should be called manually
    System.setProperty("sun.security.smartcardio.t0GetResponse", "false");
    System.setProperty("sun.security.smartcardio.t1GetResponse", "false");
  }

  public TerminalFactory getTerminalFactory() {
    return this._terminalFactory;
  }

  public void setTerminalFactory(TerminalFactory terminalFactory) {
    this._terminalFactory = terminalFactory;
  }

  public List<CardTerminal> getCardTerminalList() {
    return this._cardTerminalList;
  }

  public void setCardTerminalList(List<CardTerminal> cardTerminalList) {
    this._cardTerminalList = cardTerminalList;
  }

  public CardTerminal getCardTerminal(int index) {
    return this._cardTerminalList.get(index);
  }

  public Card getCard() {
    return this._card;
  }

  public void setCard(Card card) {
    this._card = card;
  }

  public CardChannel getCardChannel() {
    return this._cardChannel;
  }

  public void setCardChannel(CardChannel cardChannel) {
    this._cardChannel = cardChannel;
  }

  public CommandAPDU getCommandApdu() {
    return this._commandApdu;
  }

  public void setCommandApdu(CommandAPDU commandApdu) {
    this._commandApdu = commandApdu;
  }

  public ResponseAPDU getResponseApdu() {
    return this._responseApdu;
  }

  public void setResponseApdu(ResponseAPDU responseApdu) {
    this._responseApdu = responseApdu;
  }

  public byte[] getControlCommand() {
    return this._controlCommand;
  }

  public void setControlCommand(byte[] controlCommand) {
    this._controlCommand = controlCommand;
  }

  public byte[] getControlResponse() {
    return this._controlResponse;
  }

  public void setControlResponse(byte[] controlResponse) {
    this._controlResponse = controlResponse;
  }

  public int getControlCode() {
    return this._controlCode;
  }

  public void setControlCode(int controlCode) {
    this._controlCode = controlCode;
  }

  public CardTerminal getActiveTerminal() {
    return this._activeTerminal;
  }

  public void setActiveTerminal(CardTerminal activeTerminal) {
    this._activeTerminal = activeTerminal;
  }

  public String getPreferredProtocol() {
    return this._preferredProtocol;
  }

  public void setPreferredProtocol(String preferredProtocol) {
    this._preferredProtocol = preferredProtocol;
  }

  public ReaderEvents getEventHandler() {
    return this._eventHandler;
  }

  public void setEventHandler(ReaderEvents eventHandler) {
    this._eventHandler = eventHandler;
  }

  public PollingEvent getPollingEventHandler() {
    return this._pollingEventHandler;
  }

  public void setPollingEventHandler(PollingEvent pollingEventHandler) {
    this._pollingEventHandler = pollingEventHandler;
  }

  public boolean isConnectionActive() {
    return this._connectionActive;
  }

  public void setConnectionActive(boolean connectionActive) {
    this._connectionActive = connectionActive;
  }

  // List the available smart card readers
  public String[] listTerminals() throws Exception {
    setCardTerminalList(getTerminalFactory().terminals().list());

    String[] terminals = new String[getCardTerminalList().size()];

    for (int i = 0; i < getCardTerminalList().size(); i++)
      terminals[i] = getCardTerminalList().get(i).getName();

    return terminals;
  }

  public int beginExclusive() throws Exception {

    getCard().beginExclusive();

    setConnectionActive(true);

    return 0;
  }

  public int endExclusive() throws Exception {

    getCard().endExclusive();

    setConnectionActive(false);

    return 0;
  }

  // Connect to the smart card through the specified smart card reader (overloaded function)
  public int connect(int terminalNumber, String preferredProtocol) throws Exception {
    setActiveTerminal(getCardTerminalList().get(terminalNumber));
    setPreferredProtocol(preferredProtocol);

    return connect();
  }

  // Connect to the smart card through the specified smart card reader (overloaded function)
  public int connect(String readerName, String preferredProtocol) throws Exception {
    for (int i = 0; i < getCardTerminalList().size(); i++) {
      if (getCardTerminalList().get(i).getName().contains(readerName)) {
        setActiveTerminal(getCardTerminalList().get(i));
        setPreferredProtocol(preferredProtocol);
        break;
      }
    }

    return connect();
  }

  // Connect to the smart card through the specified smart card reader (overloaded function)
  public int connect(int terminalNumber) throws Exception {
    setActiveTerminal(getCardTerminalList().get(terminalNumber));

    return connect();
  }

  // Connect to the smart card through the specified smart card reader
  public int connect() throws Exception {
    setCard(getActiveTerminal().connect(getPreferredProtocol()));
    setCardChannel(getCard().getBasicChannel());

    setConnectionActive(true);

    return 0;
  }

  // Connect directly to the smart card reader (overloaded function)
  public int connectDirect(int terminalNumber) throws Exception {
    setActiveTerminal(getCardTerminalList().get(terminalNumber));

    return connectDirect();
  }

  // Connect directly to the smart card reader
  public int connectDirect() throws Exception {
    setCard(getActiveTerminal().connect("direct"));

    setConnectionActive(true);

    return 0;
  }

  // Disconnect from the smart card
  public int disconnect() throws Exception {
    // The disconnect method of Card.java has a reset parameter which is used as follows:
    // SCardDisconnect(cardId, (reset ? SCARD_LEAVE_CARD : SCARD_RESET_CARD));
    // So if reset is true, the card is not being reset, if false, the card is being reset.
    getCard().disconnect(false);

    setConnectionActive(false);

    return returnCode;
  }

  // Send APDU commands to the smart card (overloaded function)
  public int sendApduCommand(Apdu apdu) throws Exception {
    int sendDataLength = 0;
    int sendApduCommand = 0;
    if (apdu.getSendData() != null)
      sendDataLength = apdu.getSendData().length;

    byte[] commandApdu = new byte[5 + sendDataLength];

    System.arraycopy(new byte[]{apdu.getCla(), apdu.getIns(), apdu.getP1(), apdu.getP2(), apdu.getP3()}, 0, commandApdu, 0, 5);

    if (sendDataLength > 0)
      System.arraycopy(apdu.getSendData(), 0, commandApdu, 5, apdu.getSendData().length);

    sendApduCommand = sendApduCommand(commandApdu);

    apdu.setSw(new byte[]{(byte) getResponseApdu().getSW1(), (byte) getResponseApdu().getSW2()});
    if (getResponseApdu().getData().length > 0)
      apdu.setReceiveData(getResponseApdu().getData());

    return sendApduCommand;
  }


  // Send APDU commands to the smart card (overloaded function)
  public int sendApduCommand(byte[] apdu) throws Exception {
    setCommandApdu(new CommandAPDU(apdu));

    return sendApduCommand();
  }

  // Send APDU commands to the smart card
  public int sendApduCommand() throws Exception {
    getEventHandler().sendCommandData(getCommandApdu().getBytes());
    setResponseApdu(getCardChannel().transmit(getCommandApdu()));
    getEventHandler().receiveCommandData(getResponseApdu().getBytes());

    return returnCode;
  }

  // Send direct control commands to the smart card reader (overloaded function)
  public int sendControlCommand(int controlCode, byte[] controlCommand) throws Exception {
    setControlCode(controlCode);
    setControlCommand(controlCommand);

    return sendControlCommand();
  }

  // Send direct control commands to the smart card reader (overloaded function)
  public int sendControlCommand(byte[] controlCommand) throws Exception {
    setControlCommand(controlCommand);

    return sendControlCommand();
  }

  // Send direct control commands to the smart card reader
  public int sendControlCommand() throws Exception {
    getEventHandler().sendCommandData(getControlCommand());
    setControlResponse(getCard().transmitControlCommand(getControlCode(), getControlCommand()));
    getEventHandler().receiveCommandData(getControlResponse());

    return returnCode;
  }

  // Get the ATR of the smart card
  public byte[] getAtr() throws Exception {
    return getCard().getATR().getBytes();
  }

  // Get the protocol in use of the card
  public String getCardProtocol() throws Exception {
    return getCard().getProtocol();
  }

  public int getStatusChange() throws CardException, Exception {
    if (!getActiveTerminal().isCardPresent()) {
      getPollingEventHandler().cardRemoved();
      return 0;
    } else {
      // Establish a connection with the card using
      // "T=0", "T=1" or "*"
      connect();

      getPollingEventHandler().cardFound();

      return 1;
    }
  }

  public byte[] getFirmwareVersion() throws Exception {
    return null;
  }
}
