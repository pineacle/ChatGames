package me.pineacle.chatgames.utils.exeptions;

public class UnavailableUserException extends Exception {
    public UnavailableUserException(String errorMessage) {
        super(errorMessage);
    }
}
