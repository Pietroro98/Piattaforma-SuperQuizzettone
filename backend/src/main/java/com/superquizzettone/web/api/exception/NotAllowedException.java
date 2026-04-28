package com.superquizzettone.web.api.exception;

public class NotAllowedException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotAllowedException() {
        super("Operazione non consentita per lo stato attuale della risorsa.");
    }

    public NotAllowedException(String message) {
        super(message);
    }
}
