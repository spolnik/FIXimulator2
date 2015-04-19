/*
 * File     : LogMessage.java
 *
 * Author   : Brian M. Coyner
 * 
 * Contents : This class is a basic LogMessage object that is used to 
 *            create and store log message details.  The file was 
 *            taken from the Log4FIX project and adapted for the needs
 *            of FIXimulator by Zoltan Feledy.
 * 
 */

package org.nprogramming.fiximulator2.log4fix;
/*
 * The Log4FIX Software License
 * Copyright (c) 2006 - 2007 opentradingsolutions.org  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the product (Log4FIX), nor opentradingsolutions.org,
 *    nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written 
 *    permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL OPENTRADINGSOLUTIONS.ORG OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.MsgType;

import java.util.*;

/**
 * @author Brian M. Coyner
 */
public class LogMessage implements Comparable<Object> {

    public static final char DEFAULT_DELIMETER = '|';
    public static final char SOH_DELIMETER = (char) 0x01;

    private boolean incoming;
    private String rawMessage;
    private String messageTypeName;
    private Date sendingTime;
    private DataDictionary dictionary;
    private int messageIndex;

    private static final Logger LOG = LoggerFactory.getLogger(LogMessage.class);

    public LogMessage(int messageIndex, boolean incoming, SessionID sessionId,
            String rawMessage, DataDictionary dictionary) {
        this.messageIndex = messageIndex;

        this.dictionary = dictionary;
        this.rawMessage = rawMessage.replace(SOH_DELIMETER, DEFAULT_DELIMETER);
        this.incoming = incoming;

        sendingTime = lookupSendingTime();
        messageTypeName = lookupMessageTypeName();
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }
    
    public int getMessageIndex() {
    	return messageIndex;
    }

    public boolean isIncoming() {
        return incoming;
    }

    /**
     * The sending time of the message. This value may be null if the
     * SendingTime field is not found in the message.
     * @return the sending time of the message or null if the message was 
     * missing the sending time.
     */
    public Date getSendingTime() {
        return sendingTime;
    }

    /**
     * This method collects all <code>Fields</code> into a map. Each field
     * is picked out of the raw message string and is looked up from the map. 
     * This ensures that we display the fields in the same order as the raw 
     * message string. Why? Because the message class does not provide a way to 
     * get the fields in the sent order. The only time we care about the field 
     * order is when logging.
     * This method executes on the Event Dispatch Thread, so we are not slowing 
     * down the quickfix thread.
     *
     * This object does <strong>not</strong> cache the field objects. Each 
     * invocation of this method creates a new list of <code>LogField</code> 
     * objects. This is done so that memory utilization is kept low. The caller 
     * should clear the returned list when it is finished with the values. The 
     * typical caller would clear the list when a new message is displayed. 
     * @return locally created <tt>List</tt> of <tt>LogField</tt> objects;
     * not a cached value.
     */
    public List<LogField> getLogFields() {


        Message message = createMessage();

        List<LogField> logFields = new ArrayList<>();

        Map<Integer, Field> allFields = getAllFields(message);

        String[] fields = rawMessage.split("\\|");

        for (String fieldString : fields) {
            int indexOfEqual = fieldString.indexOf('=');
            int tag = Integer.parseInt(fieldString.substring(0, indexOfEqual));

            Field field = allFields.remove(tag);
            if (field != null) {
                logFields.add(createLogField(message, field));
            }
        }

        return logFields;
    }

    @Override
    public int compareTo(Object o) {

        LogMessage rhs = (LogMessage) o;
        int rhsMessageIndex = rhs.messageIndex;
        return (messageIndex < rhsMessageIndex ? -1 :
                (messageIndex == rhsMessageIndex ? 0 : 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogMessage that = (LogMessage) o;

        return messageIndex == that.messageIndex;

    }

    @Override
    public int hashCode() {
        return messageIndex;
    }

    @Override
    public String toString() {
        return "" + messageIndex;
    }

    @SuppressWarnings("unchecked")
	private LogField createLogField(Message message, Field field) {

        MsgType messageType = getMessageType(message);
        String messageTypeValue = messageType.getValue();

        LogField logField = 
                LogField.createLogField(messageType, field, dictionary);

        final DataDictionary.GroupInfo groupInfo = dictionary.getGroup(
                    messageTypeValue, field.getTag());
        if (groupInfo != null) {

            int delimeterField = groupInfo.getDelimeterField();
            Group group = new Group(field.getTag(), delimeterField);
            int numberOfGroups =  Integer.valueOf((String) field.getObject());
            for (int index = 0; index < numberOfGroups; index++) {
                LogGroup logGroup = 
                        new LogGroup(messageType, field, dictionary);

                try {

                    message.getGroup(index + 1, group);

                    Iterator groupIterator = group.iterator();
                    while (groupIterator.hasNext()) {
                        Field groupField = (Field) groupIterator.next();
                        logGroup.addField(LogField.createLogField(messageType,
                                groupField, dictionary));

                    }
                } catch (FieldNotFound fieldNotFound) {
                    LOG.error("Error: ", fieldNotFound);
                }

                logField.addGroup(logGroup);
            }
        }

        return logField;
    }

    private Message createMessage() {
        String sohMessage = 
                rawMessage.replace(DEFAULT_DELIMETER, SOH_DELIMETER);
        try {
            return new Message(sohMessage, dictionary, true);
        } catch (InvalidMessage invalidMessage) {
            LOG.error("Parent Error: ", invalidMessage);

            try {
                return new Message(sohMessage, dictionary, false);
            } catch (InvalidMessage ugh) {
                LOG.error("Error: ", ugh);
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
	private Map<Integer, Field> getAllFields(Message genericMessage) {
        Map<Integer, Field> allFields = new LinkedHashMap<>();

        Iterator iterator = genericMessage.getHeader().iterator();
        while (iterator.hasNext()) {
            Field field = (Field) iterator.next();
            allFields.put(field.getTag(), field);
        }

        iterator = genericMessage.iterator();
        while (iterator.hasNext()) {
            Field field = (Field) iterator.next();
            int tag = field.getTag();
            if (!allFields.containsKey(tag)) {
                allFields.put(tag, field);
            }
        }

        iterator = genericMessage.getTrailer().iterator();
        while (iterator.hasNext()) {
            Field field = (Field) iterator.next();
            allFields.put(field.getTag(), field);
        }

        return allFields;
    }

    private String lookupMessageTypeName() {
        String messageTypeValue = FIXMessageHelper.getMessageType(rawMessage,
                DEFAULT_DELIMETER);
        if (messageTypeValue == null) {
            return null;
        }
        return dictionary.getValueName(MsgType.FIELD, messageTypeValue);
    }

    private Date lookupSendingTime() {
        try {
            Date date = FIXMessageHelper.getSendingTime(
                    rawMessage, DEFAULT_DELIMETER);
            if (date == null) {
                return null;
            }
            return date;
        } catch (FieldConvertError fieldConvertError) {
            LOG.error("Error: ", fieldConvertError);
            return null;
        }
    }

    private MsgType getMessageType(Message message) {
        try {
            return (MsgType) message.getHeader().getField(new MsgType());
        } catch (FieldNotFound fieldNotFound) {
            throw new RuntimeException(fieldNotFound);
        }
    }
}
