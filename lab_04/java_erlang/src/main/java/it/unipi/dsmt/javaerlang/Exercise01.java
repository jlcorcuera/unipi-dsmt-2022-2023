package it.unipi.dsmt.javaerlang;

import org.passay.*;

import java.util.ArrayList;
import java.util.List;

public class Exercise01 {

    public static void main(String[] args){
        /* 1. Creating a password validator */
        PasswordValidator passwordValidator = new PasswordValidator(
                new AllowedCharacterRule(new char[] { 'a', 'b', 'c' }),
                new CharacterRule(EnglishCharacterData.LowerCase, 5),
                new LengthRule(5, 10)
        );
        /* 2. Defining a list of password to be validated */
        List<String> passwordsToValidate = new ArrayList<>();
        passwordsToValidate.add("abcdefghij");
        passwordsToValidate.add("abcabcabc");
        passwordsToValidate.add("abbbbbbaaa");
        passwordsToValidate.add("aaAABB11");
        /* 3. Validating passwords of (2) */
        passwordsToValidate.stream().forEach(password -> {
            RuleResult ruleResult = passwordValidator.validate(new PasswordData(password));
            System.out.println("Password " + password + ", is it valid? " + ruleResult.isValid());
        });
    }
}
