package me.pineacle.chatgames.API.user;

import java.util.UUID;

@SuppressWarnings("unused")
public interface User {

    UUID getUuid();

    int getWins();

    boolean isToggled();

    void setWins(int i);

    void setToggled(boolean b);

}
