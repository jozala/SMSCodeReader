package pl.aetas.android.smscode.smsprocessor.parsing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.aetas.android.smscode.model.CodeRegularExpression;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CodesParsingTest {

    private static final String SMS_DATA_XML_FILE_PATH = "res/raw/sms_data.xml";
    private static final String SENDER_TAG_NAME = "sender";

    private String senderName;
    private String regexp;
    private int relevantGroupNo;
    private String smsContent;
    private String expectedCode;

    public CodesParsingTest(final String senderName, final String regexp, final int relevantGroupNo, final String smsContent, final String expectedCode) {
        this.senderName = senderName;
        this.regexp = regexp;
        this.relevantGroupNo = relevantGroupNo;
        this.smsContent = smsContent;
        this.expectedCode = expectedCode;
    }

    @Parameters
    public static Iterable<Object[]> testData() throws ParserConfigurationException, IOException, SAXException {
        final Collection<Object[]> testData = new ArrayList<Object[]>();

        NodeList senderNodes = readSenderNodes(SMS_DATA_XML_FILE_PATH);
        for (int sendersIndex = 0; sendersIndex < senderNodes.getLength(); sendersIndex++) {
            Element senderElement = (Element) senderNodes.item(sendersIndex);
            NodeList messageNodes = senderElement.getElementsByTagName("message");
            for (int messagesIndex = 0; messagesIndex < messageNodes.getLength(); messagesIndex++) {
                Element messageElement = (Element) messageNodes.item(messagesIndex);
                NodeList testMessagesNodes = messageElement.getElementsByTagName("test-message");
                for (int testMessageIndex = 0; testMessageIndex < testMessagesNodes.getLength(); testMessageIndex++) {
                    Element testMessageElement = (Element) testMessagesNodes.item(testMessageIndex);
                    testData.add(createTestParametersUsingXmlElements(senderElement, messageElement, testMessageElement));
                }
            }
        }

        return testData;
    }

    private static Object[] createTestParametersUsingXmlElements(Element senderElement, Element messageElement, Element testMessageElement) {
        Element expressionElement = (Element) messageElement.getElementsByTagName("expression").item(0);
        String senderName = senderElement.getAttribute("name");
        int relevantGroupNumber = Integer.parseInt(expressionElement.getAttribute("relevant-group-number"));
        String regularExpression = expressionElement.getFirstChild().getNodeValue();
        String expectedCode = testMessageElement.getAttribute("expected-code");
        String testMessageContent = testMessageElement.getFirstChild().getNodeValue();
        return new Object[] {senderName, regularExpression, relevantGroupNumber, testMessageContent, expectedCode};
    }

    private static NodeList readSenderNodes(String smsDataXmlFilePath) throws ParserConfigurationException, SAXException, IOException {
        File file = new File(smsDataXmlFilePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName(SENDER_TAG_NAME);
    }

    @Test
    public void testParsing() throws Exception {
        final CodeRegularExpression codeRegularExpression = new CodeRegularExpression(regexp, relevantGroupNo);
        final String parsedCode = codeRegularExpression.getCodeFromString(smsContent);

        assertThat("Code has not been parsed correctly for " + senderName, parsedCode, is(equalTo(expectedCode)));


    }
}