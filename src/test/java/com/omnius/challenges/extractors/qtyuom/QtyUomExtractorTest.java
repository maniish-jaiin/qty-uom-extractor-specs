package com.omnius.challenges.extractors.qtyuom;

import com.omnius.challenges.extractors.qtyuom.utils.LeftMostUOMExtractor;
import com.omnius.challenges.extractors.qtyuom.utils.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author <a href="mailto:damiano@omnius.com">Damiano Giampaoli</a>
 * @since 25 Jan. 2018
 */
public class QtyUomExtractorTest extends Assert {

    private String regexForCsv;
    private String filePath;

    @Test
    public void extract_whenUOMIstheSecondTokenAndQTYTheFirst() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("40 Stg. FR 60x40x3 Ã  6m 23 Stg. Ro 33,7x2 Ã  6m geschliffen 1.4301");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("40", qty);
        assertEquals("stg.", uom);
    }

    @Test
    public void extractQTYandUOM_inLongDescription1() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("46VE44 Z VentilheizkÃ¶rper mit Mittenanschluss oder mit Anschluss als KompaktheizkÃ¶rper (VHk. Mitte/Kompakt), als Zweifachplatte mit zwei Konvektoren und mit einer BauhÃ¶he von 900 mm und Tiefe 112mm. inkl. Thermostatkopf");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("900", qty);
        assertEquals("mm", uom);
    }

    @Test
    public void extractQTYandUOM_inLongDescription2() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("831263H Z R90 Rohrabschottung Stahl - 42,4x2,6 Rohrwerkstoff: Stahl System: Heizungswasser BrandschutzdurchfÃ¼hrung innerhalb der Wand/Decke: Baustoffklasse: nichtbrennbar nach DIN 4102-1 Schmelzpunkt: >1000Â°C nach DIN 4102-17 Rohdichte: >150 kg/m3 DÃ¤mmdicke nach Herstellerrichtlinien:. . . . . . . . . . . . WeiterfÃ¼hrende DÃ¤mmung: DÃ¤mmlÃ¤nge: 1000 mm - beidseitig DÃ¤mmdicke: 40 mm Rohrdimension: 42,4x2,6 Fabrikat/Type: Rockwoll / Conlit Schale 150 U mit Alukaschierung Fabrikat/Type: Rockwoll / DÃ¤mmschale RS 800 mit Alukaschierung angeb. Fabrikat / Type:. . . . . . . . . . . .");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000", qty);
        assertEquals("mm", uom);
    }

    @Test
    public void extractQTYandUOM_whenUOMHasGermanCharacters() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("00010 327745 100 Stück 10,80 1.080,00 FORMROHR 60X30X3 LG.850,6MM SEND.VERZ. Offene Menge: 100 ST Gelieferte Menge: 0 ST");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("100", qty);
        assertEquals("stück", uom);
    }

    @Test
    public void extractQTYandUOM_whenThereAreSpacesAroundDecimalSeparator() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 10 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("10,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparators() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 1 000 000 000 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000000000", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparator() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 1 000 000 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparatorWithNoSpaces() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 1 000 000,00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasNoDigitSeparatorsAndCommaAsDecimalSeparatorWithNoSpaces() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 1000000,00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparatorAndOtherDigits1() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 87878 1 000 000 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparatorAndOtherDigits2() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 87878 21 000 000 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("21000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparatorAndOtherDigits3() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 87878 331 000 000 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("331000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparatorAndOtherDigits4() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 87878 s 1 000 000 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityNumberHasSpaceDigitSeparatorsAndCommaAsDecimalSeparatorAndOtherDigits5() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 87878 001 100 000 , 00 Stk 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("001100000,00", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenQuantityIsSecondToken() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("40 stg. fr 60x40x3 à 6m 23 stg. ro 33,7x2 à 6m geschliffen 1.4301");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("40", qty);
        assertEquals("stg.", uom);
    }

    @Test
    public void extractQTYandUOM_issueFRA_332() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("1 10057 Blech 3 3000x1500 87878 001 1 000,00 Kg 28.04.16");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("1000,00", qty);
        assertEquals("kg", uom);
    }

    @Test
    public void extractQTYandUOM_whenDecimalSeparatorIsFirstOccurrence() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract(", 6 stk ßmuffe 1/4' st37 1 stk p86814048 rest lageranin,iag");
        String qty = result.getFirst();
        String uom = result.getSecond();
        assertEquals("6", qty);
        assertEquals("stk", uom);
    }

    @Test
    public void extractQTYandUOM_whenDescriptionIsNull() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract(null);
        assertNull(result);
    }

    @Test
    public void extractQTYandUOM_whenDescriptionIsEmpty() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("");
        assertNull(result);
    }

    @Test
    public void extractQTYandUOM_whenDescriptionIsMadeOfBlankspaces() {
        QtyUomExtractor matcher = new LeftMostUOMExtractor();
        Pair<String, String> result = matcher.extract("\t\t\n   ");
        assertNull(result);
    }

    @Test
    public void computeAccuracy_checkIsHigherThan20Percent() throws IOException {
        // Load provided CSV
        // For each line in the CSV run the LeftMostUOMExtractor
        // calculate how many corrected guess your algorithm compute
        // Assert the accuracy higher than 20%

        String line;
        int passedCases = 0;
        int total = 0;
        filePath = "src/test/resources/qty_uom_challenge_dataset_clean.csv";
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        while ((line = reader.readLine()) != null) {
            QtyUomExtractor matcher = new LeftMostUOMExtractor();
            regexForCsv = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            String[] tokens = line.split(regexForCsv, -1);
            if (tokens.length == 3) {
                total++;
                String t = tokens[0];
                Pair<String, String> result = matcher.extract(t.replace("\"", ""));
                if (result != null) {
                    String qty = result.getFirst();
                    String uom = result.getSecond();
                    if (tokens[1].replace("\"", "").equals(qty)
                            && tokens[2].replace("\"", "").equals(uom)) {
                        passedCases++;
                    }
                }
            }
        }
        double accuracy = ((double) passedCases / total) * 100;
        assertTrue(accuracy > 20);
        assertEquals(100.0, accuracy, 0);

        reader.close();
    }
}

