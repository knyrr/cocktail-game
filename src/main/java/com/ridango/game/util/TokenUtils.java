package com.ridango.game.util;

import com.ridango.game.model.CharacterToken;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TokenUtils {

    public static List<CharacterToken> tokeniseName(String input) {
        List<CharacterToken> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            CharacterToken token = new CharacterToken();
            token.setValue(input.charAt(i));
            result.add(token);
        }
        return result;
    }

    public static List<CharacterToken> checkTokenVisibility(List<CharacterToken> tokens,
            List<Character> visibleCharacters) {
        for (CharacterToken token : tokens) {
            if (visibleCharacters.contains(token.getValue())) {
                token.setIsHidden(false);
            }
        }
        return tokens;
    }

    public static String convertTokenisedNameToString(List<CharacterToken> tokens) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            CharacterToken token = tokens.get(i);
            result.append(token.getIsHidden() ? '_' : token.getValue());
            if (i < tokens.size() - 1) {
                result.append(' ');
            }
        }
        return result.toString();
    }

    public static List<Character> extractUniqueCharacters(String input) {
        Set<Character> charSet = new LinkedHashSet<>();
        for (char c : input.toCharArray()) {
            charSet.add(c);
        }
        return new ArrayList<>(charSet);
    }

    public static Character pickRandomCharacter(List<Character> charList) {
        if (charList == null || charList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(charList.size());
        return charList.get(randomIndex);
    }
}
