package me.pineacle.chatgames.utils.exeptions;

public class UnavailableUserException extends RuntimeException {
    public UnavailableUserException(String errorMessage) {
        super(errorMessage);
    }
}
