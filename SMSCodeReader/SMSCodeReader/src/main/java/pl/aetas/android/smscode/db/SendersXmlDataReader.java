package pl.aetas.android.smscode.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SendersXmlDataReader {

    public static final String SENDER_XML_TAG = "sender";
    public static final String SENDER_NAME_XML_ATTRIBUTE = "name";
    public static final String SENDER_DISPLAY_NAME_XML_ATTRIBUTE = "display-name";
    public static final String MESSAGE_XML_TAG = "message";
    public static final String EXPRESSION_XML_TAG = "expression";
    public static final String EXPRESSION_RELEVANT_GROUP_NUMBER_XML_ATTRIBUTE = "relevant-group-number";
    public static final String MESSAGE_TYPE_XML_ATTRIBUTE = "type";

    private final DocumentBuilder documentBuilder;

    public SendersXmlDataReader(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public List<SenderData> loadSendersDataFromXml(InputStream xmlInputStream) throws IOException, SAXException {
        if (xmlInputStream == null) {
            throw new NullPointerException("Given XML cannot be null");
        }
        
        List<SenderData> senders = new LinkedList<SenderData>();
        Document document = documentBuilder.parse(xmlInputStream);
        document.getDocumentElement().normalize();
        
        NodeList senderNodes = document.getElementsByTagName(SENDER_XML_TAG);
        for (int sendersIndex = 0; sendersIndex < senderNodes.getLength(); sendersIndex++) {
            Element senderElement = (Element) senderNodes.item(sendersIndex);
            SenderData sender = createSenderData(senderElement);
            List<MessageData> messages = getSendersMessagesFromSenderNode(senderElement);
            if (messages.size() == 0) {
                throw new IllegalStateException("No messages data found in XML for sender: " + sender.getName());
            }
            sender.addMessages(messages);
            senders.add(sender);
        }
        return senders;
    }

    private SenderData createSenderData(Element senderElement) {
        String senderName = senderElement.getAttribute(SENDER_NAME_XML_ATTRIBUTE);
        String senderDisplayName = senderElement.getAttribute(SENDER_DISPLAY_NAME_XML_ATTRIBUTE);
        if (senderName.equals("")) {
            throw new IllegalStateException("Sender name has to be specified. Application XML data are incorrect!");
        }
        if (senderDisplayName.equals("")) {
            throw new IllegalStateException("Sender (" + senderName + ") display name has to be specified. Application XML data are incorrect!");
        }
        return new SenderData(senderName, senderDisplayName);
    }

    private List<MessageData> getSendersMessagesFromSenderNode(Element senderElement) {
        List<MessageData> messages = new LinkedList<MessageData>();
        NodeList messageNodes = senderElement.getElementsByTagName(MESSAGE_XML_TAG);
        for (int messagesIndex = 0; messagesIndex < messageNodes.getLength(); messagesIndex++) {
            Element messageElement = (Element) messageNodes.item(messagesIndex);
            MessageData messageData = createMessageData(messageElement);
            messages.add(messageData);
        }
        return messages;
    }

    private MessageData createMessageData(Element messageElement) {
        Element expressionElement = (Element) messageElement.getElementsByTagName(EXPRESSION_XML_TAG).item(0);
        String messageType = messageElement.getAttribute(MESSAGE_TYPE_XML_ATTRIBUTE);
        String regexp = expressionElement.getFirstChild().getNodeValue();
        String relevantGroupString = expressionElement.getAttribute(EXPRESSION_RELEVANT_GROUP_NUMBER_XML_ATTRIBUTE);
        if (relevantGroupString.equals("")) {
            throw new IllegalStateException("Relevant group number has to be always set for expression. Application XML data are incorrect!");
        }
        int relevantGroup = Integer.parseInt(relevantGroupString);
        return new MessageData(messageType, regexp, relevantGroup);
    }

    public static class SenderData {
        private final String name;
        private final String displayName;
        private final List<MessageData> messagesData;

        public SenderData(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
            messagesData = new LinkedList<MessageData>();
        }

        public void addMessages(Collection<MessageData> messages) {
            messagesData.addAll(messages);
        }

        public List<MessageData> getMessages() {
            return Collections.unmodifiableList(messagesData);
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class MessageData {
        private final String type;
        private final String regexp;
        private final int relevantGroup;

        public MessageData(String type, String regexp, int relevantGroup) {
            this.type = type;
            this.regexp = regexp;
            this.relevantGroup = relevantGroup;
        }

        public String getType() {
            return type;
        }

        public String getRegexp() {
            return regexp;
        }

        public int getRelevantGroup() {
            return relevantGroup;
        }
    }
}
