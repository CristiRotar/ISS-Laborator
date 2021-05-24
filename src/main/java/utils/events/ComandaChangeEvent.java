package utils.events;

import model.Comanda;

public class ComandaChangeEvent implements Event {
    private EventType type;
    private Comanda data, oldData;

    public ComandaChangeEvent(EventType type, Comanda data, Comanda oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ComandaChangeEvent(EventType type, Comanda data) {
        this.type = type;
        this.data = data;
    }

    public EventType getType() {
        return type;
    }

    public Comanda getData() {
        return data;
    }

    public Comanda getOldData() {
        return oldData;
    }
}
