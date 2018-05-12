package com.omnius.challenges.extractors.qtyuom.utils;

import com.omnius.challenges.extractors.qtyuom.QtyUomExtractor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements {@link QtyUomExtractor} identifying as <strong>the most relevant UOM</strong> the leftmost UOM found in the articleDescription.
 * The {@link UOM} array contains the list of valid UOMs. The algorithm search for the leftmost occurence of UOM[i], if there are no occurrences then tries UOM[i+1].
 * <p>
 * Example
 * <ul>
 * <li>article description: "black steel bar 35 mm 77 stck"</li>
 * <li>QTY: "77" (and NOT "35")</li>
 * <li>UOM: "stck" (and not "mm" since "stck" has an higher priority as UOM )</li>
 * </ul>
 *
 * @author <a href="mailto:damiano@searchink.com">Damiano Giampaoli</a>
 * @since 4 May 2018
 */
public class LeftMostUOMExtractor implements QtyUomExtractor {

    public static final String WHITE_SPACE = " ";
    public static final String EMPTY_STRING = "";
    /**
     * Array of valid UOM to match. the elements with lower index in the array has higher priority
     */
    public static String[] UOM = {"stk", "stk.", "stck", "stück", "stg", "stg.", "st", "st.", "stange", "stange(n)", "tafel", "tfl", "taf", "mtr", "meter", "qm", "kg", "lfm", "mm", "m"};

    public LeftMostUOMExtractor() {
    }

    @Override
    public Pair<String, String> extract(String articleDescription) {
        //mock implementation
        if (articleDescription != null && !articleDescription.trim().isEmpty()) {
            return extractQtyAndUomUsingRegex(articleDescription);
        }
        return null;
    }

    @Override
    public Pair<Double, String> extractAsDouble(String articleDescription) {
        //mock implementation
        return new Pair<>(34.5d, "m");
    }

    private Pair<String, String> extractQtyAndUomUsingRegex(String articleDes) {

        String[] eachWordFromDescription = articleDes.toLowerCase().split(WHITE_SPACE);

        for (String aUOM : UOM) {
            String measurement = Pattern.quote(aUOM);
            String regexForUom = "\\s(" + measurement + ")(\\s|\\Z)";
            ArrayList<String> listOfRegex = getRegexListForUom(regexForUom);

            StringBuilder articleDescriptionBuilder = new StringBuilder();
            articleDescriptionBuilder.append(eachWordFromDescription[0]);
            Pattern pattern;
            Matcher matcher;
            for (int j = 0; j < eachWordFromDescription.length; j++) {
                if (j < eachWordFromDescription.length - 1) {
                    articleDescriptionBuilder.append(WHITE_SPACE).append(eachWordFromDescription[j + 1]);
                    for (String regex : listOfRegex) {
                        pattern = Pattern.compile(regex);
                        matcher = pattern.matcher(articleDescriptionBuilder.toString());
                        if (matcher.find()) {
                            return new Pair<>(matcher.group(1).replace(WHITE_SPACE, EMPTY_STRING), matcher.group(2));
                        }
                    }
                }
            }
        }

        return new Pair<>("", "");
    }

    private ArrayList<String> getRegexListForUom(String regexForUom) {
        ArrayList<String> listOfRegex = new ArrayList<>();
        listOfRegex.add("\\s(\\d{1,3}\\s(?:\\d{3}(?:\\s))*\\d{3}\\s?(?:,|\\.)\\s?\\d+)" + regexForUom);
        listOfRegex.add("(\\d{1,3}\\s(?:\\d{3}(?:\\s))+\\d{3}(?:\\s|(?:,|\\.)\\d+)?)" + regexForUom);
        listOfRegex.add("(?:\\s)(\\d{1,3},(?:\\d{3}(?:,))*\\d{3}(?:\\s|(?:\\.)\\d+)?)" + regexForUom);
        listOfRegex.add("(\\d{1,3}\\.(?:\\d{3}(?:\\.))*\\d{3}(?:\\s|(?:,)\\d+)?)" + regexForUom);
        listOfRegex.add("(\\d+\\s(?:,|\\.)\\s\\d+)" + regexForUom);
        listOfRegex.add("(?:\\s)(\\d+(?:\\.|,)\\d+)" + regexForUom);
        listOfRegex.add("(?:\\A|\\s+)(\\d+)" + regexForUom);

        return listOfRegex;
    }


}
