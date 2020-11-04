/*
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 25 mars 03
 * Time: 15:37:46
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package net.thevpc.common.swings.util;

public class BracketsTokenizer {
    private String string;
    private String brackets;
    private SubString currentToken;


    public BracketsTokenizer(String string, String brackets) {
        this.string = string;
        this.brackets = brackets;
    }

    public boolean hasNext() {
        SubString oldToken = currentToken;
        if (oldToken == null) {
            if (string.length() == 0) {
                return false;
            }
            currentToken = getBrackets(string, 0, brackets);
            if (currentToken == null) {
                currentToken = new SubString(string, 0, string.length());
            } else if (currentToken.getOffset() > 0) {
                currentToken = new SubString(string, 0, currentToken.getOffset());
            }
            return true;
        } else {
            if (string.length() - (oldToken.getOffset() + oldToken.length()) == 0) {
                return false;
            }
            currentToken = getBrackets(string, oldToken.getOffset() + oldToken.length(), brackets);
            if (currentToken == null) {
                currentToken = new SubString(string, oldToken.getOffset() + oldToken.length(), string.length() - (oldToken.getOffset() + oldToken.length()));
            } else if (currentToken.getOffset() > oldToken.getOffset() + oldToken.length()) {
                currentToken = new SubString(string, oldToken.getOffset() + oldToken.length(), currentToken.getOffset() - (oldToken.getOffset() + oldToken.length()));
            }
            return true;

        }
    }

    public SubString next() {
        return currentToken;
    }

    public static SubString getBrackets(String string, int start, String allBrackets) {
        int[] bracketCount = new int[allBrackets.length() / 2];
        int brackets_start = -1;
        for (int i = start; i < string.length(); i++) {
            char c = string.charAt(i);
            for (int x = 0; x < bracketCount.length; x++) {
                if (allBrackets.charAt(2 * x) == c) {
                    if (brackets_start < 0) {
                        brackets_start = i;
                    }
                    bracketCount[x]++;
                } else if (allBrackets.charAt(2 * x + 1) == c) {
                    bracketCount[x]--;
                    boolean ok = true;
                    for (int y = 0; y < bracketCount.length; y++) {
                        if (bracketCount[y] != 0) {
                            ok = false;
                        }
                    }
                    if (ok) {
                        return new SubString(string, brackets_start, i - brackets_start + 1, x);
                    }
                }
            }
        }
        return null;
    }


}
