package org.ia.transporter.events;

import org.ia.transporter.domain.Client;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class AddFriendEvent {
    private Client client;

    public AddFriendEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
