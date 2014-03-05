package pl.aetas.android.smscode.db;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SendersXmlDataReaderTest {

    private SendersXmlDataReader sendersXmlDataReader;

    @Mock
    private File xmlFile;

    @Before
    public void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        sendersXmlDataReader = new SendersXmlDataReader(documentBuilder);
    }

    @Test
    public void shouldReadAllSendersWhenThereAreMultipleSendersInTheXmlInputStream() throws Exception {
        String xml = "<senders>" +
                "   <sender name=\"testSenderName\" display-name=\"DisplayName\">\n" +
                "        <messages>\n" +
                "            <message type=\"type PL\">\n" +
                "                <expression relevant-group-number=\"2\">.*(haslo: )(\\d{8})(\\s?).*</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>\n" +
                "    <sender name=\"second sender\" display-name=\"Display name of second sender\">\n" +
                "        <messages>\n" +
                "            <message type=\"type 123\">\n" +
                "                <expression relevant-group-number=\"3\">.*(\\s?)(\\d{6})$</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        List<SendersXmlDataReader.SenderData> sendersData = sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
        assertThat(sendersData, hasSize(2));
        assertThat(sendersData.get(0).getName(), is("testSenderName"));
        assertThat(sendersData.get(0).getDisplayName(), is("DisplayName"));
        assertThat(sendersData.get(1).getName(), is("second sender"));
        assertThat(sendersData.get(1).getDisplayName(), is("Display name of second sender"));
    }

    @Test
    public void shouldReadSenderNameAndDisplayNameFromGivenInputStream() throws Exception {
        String xml = "<senders>" +
                "    <sender name=\"some sender name\" display-name=\"sender display name\">\n" +
                "        <messages>\n" +
                "            <message type=\"type 123\">\n" +
                "                <expression relevant-group-number=\"3\">.*(\\s?)(\\d{6})$</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";
        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        List<SendersXmlDataReader.SenderData> senders = sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
        assertThat(senders.get(0).getName(), is("some sender name"));
        assertThat(senders.get(0).getDisplayName(), is("sender display name"));
    }

    @Test
    public void shouldReadMessagesOfSenderFromGivenXmlInputSource() throws Exception {
        String xml = "<senders>" +
                "    <sender name=\"some sender name\" display-name=\"sender display name\">\n" +
                "        <messages>\n" +
                "            <message type=\"type 123\">\n" +
                "                <expression relevant-group-number=\"3\">.*(\\s?)(\\d{6})$</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        List<SendersXmlDataReader.SenderData> sendersData = sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
        List<SendersXmlDataReader.MessageData> senderMessages = sendersData.get(0).getMessages();
        assertThat(senderMessages, hasSize(1));
        assertThat(senderMessages.get(0).getType(), is("type 123"));
        assertThat(senderMessages.get(0).getRegexp(), is(".*(\\s?)(\\d{6})$"));
        assertThat(senderMessages.get(0).getRelevantGroup(), is(3));
    }

    @Test
    public void shouldReadAllSendersWhenThereAreMultipleMessagesForTheSender() throws Exception {
        String xml = "<senders>" +
                "   <sender name=\"testSenderName\" display-name=\"DisplayName\">\n" +
                "        <messages>\n" +
                "            <message type=\"type PL\">\n" +
                "                <expression relevant-group-number=\"2\">.*(haslo: )(\\d{8})(\\s?).*</expression>\n" +
                "            </message>\n" +
                "            <message type=\"type EN\">\n" +
                "                <expression relevant-group-number=\"1\">.*(code: )(\\d{8})(\\s?).*</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>\n" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        List<SendersXmlDataReader.SenderData> sendersData = sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
        List<SendersXmlDataReader.MessageData> senderMessages = sendersData.get(0).getMessages();
        assertThat(senderMessages, hasSize(2));
        assertThat(senderMessages.get(0).getType(), is("type PL"));
        assertThat(senderMessages.get(0).getRegexp(), is(".*(haslo: )(\\d{8})(\\s?).*"));
        assertThat(senderMessages.get(0).getRelevantGroup(), is(2));
        assertThat(senderMessages.get(1).getType(), is("type EN"));
        assertThat(senderMessages.get(1).getRegexp(), is(".*(code: )(\\d{8})(\\s?).*"));
        assertThat(senderMessages.get(1).getRelevantGroup(), is(1));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenNoMessagesForSenderInTheXml() throws Exception {
        String xml = "<senders>" +
                "    <sender name=\"some sender name\" display-name=\"sender display name\">\n" +
                "        <messages>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSenderNameIsNotSpecified() throws Exception {
        String xml = "<senders>" +
                "    <sender display-name=\"sender display name\">\n" +
                "        <messages>\n" +
                "            <message type=\"type 123\">\n" +
                "                <expression relevant-group-number=\"3\">.*(\\s?)(\\d{6})$</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSenderDisplayNameIsNotSpecified() throws Exception {
        String xml = "<senders>" +
                "    <sender name=\"some sender name\">\n" +
                "        <messages>\n" +
                "            <message type=\"type 123\">\n" +
                "                <expression relevant-group-number=\"3\">.*(\\s?)(\\d{6})$</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenRelevantGroupNumberNotSpecifiedForExpressionInXml() throws Exception {
        String xml = "<senders>" +
                "    <sender name=\"some sender name\" display-name=\"sender display name\">\n" +
                "        <messages>\n" +
                "            <message type=\"type 123\">\n" +
                "                <expression>.*(\\s?)(\\d{6})$</expression>\n" +
                "            </message>\n" +
                "        </messages>\n" +
                "    </sender>" +
                "</senders>";

        InputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
        sendersXmlDataReader.loadSendersDataFromXml(xmlInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenGivenXmlIsNull() throws Exception {
        sendersXmlDataReader.loadSendersDataFromXml(null);
    }
}
