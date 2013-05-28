package pl.aetas.android.smscode.analyser;

import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.KnownSendersResource;

public class SMSAnalyser {

    private final KnownSendersResource knownSendersResource;
    private final SMSCodeParser smsCodeParser;

    public SMSAnalyser(final KnownSendersResource knownSendersResource, final SMSCodeParser smsCodeParser) {
        this.knownSendersResource = knownSendersResource;
        this.smsCodeParser = smsCodeParser;
    }

    /**
     * Analyse given SMS sender and body and returns {@link SMSInfo} generated using this data
     *
     * @param sender bare sender of an SMS. Can be number or a name.
     * @param body   full body of an SMS
     * @return information about analysed SMS sender and body
     */
    public SMSInfo analyse(String sender, String body) {
        if (!knownSendersResource.isSenderKnown(sender)) {
            return SMSWithoutCodeInfo.createSMSWithoutCodeInfo();
        }

        if (!smsCodeParser.checkIfBodyContainsCode(body)) {
            Log.w(SMSAnalyser.class.getName(), "Sender is known: " + sender + ", but code has not been found in sms body: " + body);
            return SMSWithoutCodeInfo.createSMSWithoutCodeInfo();
        }

        String code = smsCodeParser.retrieveCodeFromSMSBodyForKnownSender(sender, body);
        return new SMSWithCodeInfo(knownSendersResource.getSenderOfficialName(sender), body, code);
    }
}
