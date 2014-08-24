package pl.aetas.android.smscode.model;

import java.util.Set;

public class Sender {

    private final String officialName;
    private final Set<String> senderIds;
    private final CodesRegularExpressions codesRegularExpressions;

    public Sender(String officialName, Set<String> senderIds, CodesRegularExpressions codesRegularExpressions) {
        if (officialName == null) throw new NullPointerException("Official name cannot be null");
        if (senderIds == null || senderIds.isEmpty()) throw new IllegalArgumentException("Sender has to contains minimum 1 sender's id");
        if (codesRegularExpressions == null) throw new NullPointerException("codesRegularExpressions cannot be null");

        this.officialName = officialName;
        this.senderIds = senderIds;
        this.codesRegularExpressions = codesRegularExpressions;
    }

    public String getOfficialName() {
        return officialName;
    }

    public Set<String> getSenderIds() {
        return senderIds;
    }

    public CodesRegularExpressions getCodesRegularExpressions() {
        return codesRegularExpressions;
    }
}
