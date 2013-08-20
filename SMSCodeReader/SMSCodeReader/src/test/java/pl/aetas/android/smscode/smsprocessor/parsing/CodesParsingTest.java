package pl.aetas.android.smscode.smsprocessor.parsing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import pl.aetas.android.smscode.model.CodeRegularExpression;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CodesParsingTest {

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
    public static Iterable<Object[]> testData() {
        final Collection<Object[]> testData = new ArrayList<Object[]>();
        testData.add(new Object[]{
                "AliorBank 1", ".*(\\s?)(\\d{6})$", 2, "Alior Bank: Przelew na rachunek 24...1525; Odbiorca: Revelco Sp. z o.o.; Kwota 600,00 EUR ; Kod SMS nr 18 z dn. 18-01-2013: 627902", "627902"
        });
        testData.add(new Object[]{
                "AliorBank 2", ".*(\\s?)(\\d{6})$", 2, "Alior Bank: Przelew na rachunek 44...1234; Odbiorca: PayU S.A.; Kwota 125,30 ; Kod SMS nr 3 z dn. 14-01-2013: 746550", "746550"
        });
        testData.add(new Object[]{
                "AliorBank 3", ".*(\\s?)(\\d{6})$", 2, "Alior Bank:  Przelewy do zatwierdzenia; 4 szt., kwota: 269,31 PLN; Kod SMS nr 129 z dn. 19-12-2012: 524965", "524965"
        });
        testData.add(new Object[]{
                "AliorBank 4", ".*(\\s?)(\\d{6})$", 2, "Alior Bank:  Przelewy do zatwierdzenia; 4 szt., kwota: 269,31 PLN; Kod SMS nr 129 z dn. 19-12-2012: 524965", "524965"
        });
        testData.add(new Object[]{
                "mBank 1", ".*(haslo: )(\\d{8})(\\s?).*", 2, "Operacja nr 5 z dn. 06-12-2012 Przelew z rach.: ...05767939 na rach.: 8511...430535 kwota: 620,00 PLN haslo: 12345678 mBank.", "12345678"
        });
        testData.add(new Object[]{
                "mBank 2", ".*(haslo: )(\\d{8})(\\s?).*", 2, "Operacja nr 10 z dn. 09-12-2012 mTransfer z rach.: ...12347939 na rach.: 1511...746928 kwota: 5,80 PLN haslo: 87654321 mBank.", "87654321"
        });
        testData.add(new Object[]{
                "mBank 3", ".*(haslo: )(\\d{8})(\\s?).*", 2, "Operacja nr 3 z dn. 02-12-2012 Doladowanie telefonu nr: 665234432 z rach.: ...88776655 kwota: 10,00 PLN haslo: 04816169 mBank.", "04816169"
        });
        testData.add(new Object[]{
                "BZWBK 1", "^(smsKod: )(\\d{8})(\\s?).*", 2, "smsKod: 27563032 przelew na 97114020040000312345076770 Anna Kowalska 200,00 PLN styczen", "27563032"
        });
        testData.add(new Object[]{
                "BZWBK 2", "^(smsKod: )(\\d{8})(\\s?).*", 2, "smsKod: 17776446 przelew24 na 10109020530000000109114170 DOTPAY SPOLKA AKCYJNA 35,30 PLN APID=1422512-", "17776446"
        });
        testData.add(new Object[]{
                "BZWBK 3", "^(smsKod: )(\\d{8})(\\s?).*", 2, "smsKod: 61633987 dla operacji: logowanie do aplikacji BZWBK24 mobile 09.08.2013 11:02", "61633987"
        });
        testData.add(new Object[]{
                "ING 1", ".*(\\s?to: )(\\S+)(\\s?).*", 2, "ING Bank. Sprawdz KWOTE i RACHUNEK. Kod autoryzacyjny dla przelewu z rachunku 35 XXX 615,\n na kwote 232,12 to: 98765432 ** 2012.10.30 ** HH:MM:SS.", "98765432"
        });
        testData.add(new Object[]{
                "ING 2", ".*(\\s?to: )(\\S+)(\\s?).*", 2, "ING Bank. Kod Autoryzacyjny dla doladowania telefonu numer 600123456 na kwote 25.00 to: 10574818 ** 2012.11.05 ** 07:43:27.", "10574818"
        });
        testData.add(new Object[]{
                "BGZ Optima logowanie", ".*(Haslo: )(\\d{8})$", 2, "BGZOptima operacja nr 1 z dn. 27.05.2013. Logowanie do konta. Haslo: 61790559", "61790559"
        });
        testData.add(new Object[]{
                "BGZ Optima transfer", ".*(Haslo: )(\\d{8})$", 2, "BGZOptima operacja nr 2 z dn. 27.05.2013 Przelew na kwote 1,00 PLN. Haslo: 65905217", "65905217"
        });
        testData.add(new Object[]{
                "BGZ 1", ".*(kod: )(\\d{8}).*", 2, "Operacja nr 2 z dn. 2013-04-03\n" +
                "Edycja ustawien\n" +
                "kod: 20911733\n" +
                "Bank BGZ", "20911733"
        });
        testData.add(new Object[]{
                "BGZ 2", ".*(kod: )(\\d{8}).*", 2, "Operacja nr 3 z dn. 2013-04-03\n" +
                "Przelew z rach.: 12415440\n" +
                "na rach.: PL41...832121\n" +
                "tyt.: tytul\n" +
                "kwota: 1,00 EUR\n" +
                "kod: 04814254\n" +
                "Bank BGZ", "04814254"
        });
        testData.add(new Object[]{
                "BGZ 3", ".*(kod: )(\\d{8}).*", 2, "Operacja nr 1 z dn. 2013-04-03 \n" +
                "Przelew z rach.: 12415440 \n" +
                "na rach.: 6111...779982 \n" +
                "tyt.: 122781901 \n" +
                "kwota: 0,01 PLN \n" +
                "kod: 19340705 \n" +
                "Bank BGZ", "19340705"
        });
        testData.add(new Object[]{
                "Bank Pocztowy", ".*(Kod: )(\\d{6}).*", 2, "Operacja z dn. 08-04-2013, godz. 11:13. Doladowanie nr tel. 886717750 z rach.: 711...001, kwota: 5,00 PLN. Kod: 564303.", "564303"
        });
        testData.add(new Object[]{
                "Google ENG", ".*(code is )(\\d{6}).*", 2, "Your Google verification code is 831234", "831234"
        });
        testData.add(new Object[]{
                "Google PL", ".*(Google to )(\\d{6}).*", 2, "Twój kod weryfikacyjny Google to 650312.", "650312"
        });

        testData.add(new Object[]{
                "Dropbox PL", ".*(Kod zabezpieczajacy to )(\\d{6}).*", 2, "Kod zabezpieczajacy to 697281. Milego korzystania z Dropbox!", "697281"
        });
        testData.add(new Object[]{
                "Dropbox ENG", ".*(security code is )(\\d{6}).*", 2, "Your security code is 533344. Happy Dropboxing!", "533344"
        });
        testData.add(new Object[]{
                "IDEA Bank", ".*(Haslo: )([a-zA-Z0-9]{6})$", 2, "Operacja z dnia: 2013-04-18 19:45 Przelew z rach: 025941940002 na rach: 881240...010005843281 na kwote: 69  Haslo: rw53gs", "rw53gs"
        });


        return testData;
    }

    @Test
    public void testParsing() throws Exception {
        final CodeRegularExpression codeRegularExpression = new CodeRegularExpression(regexp, relevantGroupNo);
        final String parsedCode = codeRegularExpression.getCodeFromString(smsContent);

        assertThat("Code has not been parsed correctly for " + senderName, parsedCode, is(equalTo(expectedCode)));


    }
}