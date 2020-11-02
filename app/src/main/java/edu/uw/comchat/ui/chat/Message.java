package edu.uw.comchat.ui.chat;

import java.io.Serializable;

/**
 * Class to represent a message between two people.
 *
 * @author Jerry Springer
 * @version 2 November 2020
 */
public class Message implements Serializable {

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

        public Message build() {
            return new Message(this);
        }
    }

    private Message(final Builder builder) {
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
