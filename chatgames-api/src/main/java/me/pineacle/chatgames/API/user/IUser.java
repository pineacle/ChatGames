package me.pineacle.chatgames.API.user;

import java.util.UUID;

@SuppressWarnings("unused")
public interface IUser {

    UUID getUuid();

    int getWins();

}
