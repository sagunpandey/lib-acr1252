package np.com.rts.lib.acr1252.pcsc;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class PollingEvent {

  public class CardPollingEventArg extends EventObject {

    public CardPollingEventArg(Object sender) {
      super(sender);
    }
  }

  public interface CardPollingHandler {
    void onCardFound(CardPollingEventArg event);

    void onCardRemoved(CardPollingEventArg event);
  }

  private List<CardPollingHandler> _listener = new ArrayList<CardPollingHandler>();

  public List<CardPollingHandler> getListeners() {
    return this._listener;
  }

  public void setListener(List<CardPollingHandler> listener) {
    this._listener = listener;
  }

  public synchronized void addEventListener(CardPollingHandler listener) {
    this.getListeners().add(listener);
  }

  public synchronized void removeEventListener(CardPollingHandler listener) {
    this.getListeners().add(listener);
  }

  public synchronized void cardFound() {
    CardPollingEventArg event = new CardPollingEventArg(this);

    for (CardPollingHandler cardPollingHandler : this.getListeners()) {
      cardPollingHandler.onCardFound(event);
    }
  }

  public synchronized void cardRemoved() {
    CardPollingEventArg event = new CardPollingEventArg(this);

    for (CardPollingHandler cardPollingHandler : this.getListeners()) {
      cardPollingHandler.onCardRemoved(event);
    }
  }
}
