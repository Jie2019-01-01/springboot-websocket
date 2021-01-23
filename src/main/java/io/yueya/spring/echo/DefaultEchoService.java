package io.yueya.spring.echo;

public class DefaultEchoService implements EchoService {

    private final String echoFormat;

    public DefaultEchoService(String echoFormat) {
        this.echoFormat = echoFormat;
    }

    @Override
    public String getMessage(String message) {
        return String.format(this.echoFormat, message);
    }

}
