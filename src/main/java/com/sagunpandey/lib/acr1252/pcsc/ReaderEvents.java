package com.sagunpandey.lib.acr1252.pcsc;

import com.sagunpandey.lib.acr1252.utility.Helper;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class ReaderEvents {
  @SuppressWarnings("serial")
  public class TransmitApduEventArg extends EventObject {
    private byte[] _data;

    public byte[] getData() {
      return this._data;
    }

    public void setData(byte[] data) {
      this._data = data;
    }

    public TransmitApduEventArg(Object sender, byte[] data) {
      super(sender);
      this._data = data;
    }

    public String getAsString(boolean spaceInBetween) {
      if (this.getData() == null)
        return "";

      return Helper.byteAsString(this.getData(), spaceInBetween);
    }
  }

  public interface TransmitApduHandler {
    void onSendCommand(TransmitApduEventArg event);

    void onReceiveCommand(TransmitApduEventArg event);
  }

  private List<TransmitApduHandler> _listeners = new ArrayList<TransmitApduHandler>();

  public List<TransmitApduHandler> getListeners() {
    return this._listeners;
  }

  public void setListeners(List<TransmitApduHandler> listeners) {
    this._listeners = listeners;
  }

  public synchronized void addEventListener(TransmitApduHandler listener) {
    this.getListeners().add(listener);
  }

  public synchronized void removeEventListener(TransmitApduHandler listener) {
    this.getListeners().remove(listener);
  }

  public synchronized void sendCommandData(byte[] data) {
    TransmitApduEventArg event = new TransmitApduEventArg(this, data);

    for (TransmitApduHandler transmitApduHandler : this.getListeners()) {
      transmitApduHandler.onSendCommand(event);
    }
  }

  public synchronized void receiveCommandData(byte[] data) {
    TransmitApduEventArg event = new TransmitApduEventArg(this, data);

    for (TransmitApduHandler transmitApduHandler : this.getListeners()) {
      transmitApduHandler.onReceiveCommand(event);
    }
  }
}
