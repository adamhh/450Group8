package edu.uw.comchat.ui.chat;

import java.io.Serializable;

public class Chat implements Serializable {

    private final String mSender;
    private final String mReceiver;
    private final String mMessage;

    /**
     * Helper class for building messages.
     *
     * @author Jerry Springer
     */
    public static class Builder {
        private String mSender = "";
        private String mReceiver = "";
        private String mMessage = "";

        /**
         * Constructs a new builder.
         *
         * @param sender the person sending the message.
         * @param receiver the person receiving the message.
         * @param message the message being sent.
         */
        public Builder(String sender, String receiver, String message) {
            this.mSender = sender;
            this.mReceiver = receiver;
            this.mMessage = message;
        }

        public Chat build() {
            return new Chat(this);
        }
    }

    private Chat(final Builder builder) {
        this.mSender = builder.mSender;
        this.mReceiver = builder.mReceiver;
        this.mMessage = builder.mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getReceiver() {
        return mReceiver;
    }

    public String getMessage() {
        return mMessage;
    }
}
