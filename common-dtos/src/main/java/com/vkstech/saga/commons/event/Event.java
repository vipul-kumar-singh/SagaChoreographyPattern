package com.vkstech.saga.commons.event;

import java.util.Date;
import java.util.UUID;

public interface Event {

    UUID getEventid();

    Date getDate();
}
