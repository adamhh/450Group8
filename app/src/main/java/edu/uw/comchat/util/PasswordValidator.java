package edu.uw.comchat.util;

import static edu.uw.comchat.util.PasswordValidator.PasswordValidationResult;

import java.util.Optional;
import java.util.function.Function;



/**
 * This class provides a validator to check whether an input password
 * is valid (client-side).
 *
 * @author Hung Vu
 */
public interface PasswordValidator extends Function<String,
        Optional<PasswordValidationResult>> {
  /**
   * Minimum password length.
   */
  static final int MIN_PASSWORD_LENGTH = 6;

  /**
   * Return a validator to see whether a given password meets
   * minimum requirement in length. For example, if the requirement
   * is 6, then the password must have at least 6 character.
   *
   * <p>When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing PasswordValidationResult.PWD_SUCCESS when s.length >= 6,
   * otherwise PasswordValidationResult.PWD_INVALID_LENGTH.</p>
   *
   * @return a validator validates the length of Password (String)
   */
  static PasswordValidator checkPwdLength() {
    return checkPwdLength(MIN_PASSWORD_LENGTH);
  }

  /**
   * Return a validator to see whether a given password meets
   * minimum requirement in length.
   *
   * <p>When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing PasswordValidationResult.PWD_SUCCESS when
   * s.length >= minPasswordLength, otherwise PasswordValidationResult.PWD_INVALID_LENGTH.</p>
   *
   * @param minPasswordLength a minimum password length
   * @return a validator validates the length of Password (String)
   */
  static PasswordValidator checkPwdLength(int minPasswordLength) {
    return password -> Optional.of(
            password.length() >= minPasswordLength
                    ? PasswordValidationResult.PWD_SUCCESS
                    : PasswordValidationResult.PWD_INVALID_LENGTH
    );
  }

  /**
   * Return a validator to see whether a given password contains
   * at least 1 uppercase letter.
   *
   * <p>When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing PasswordValidationResult.PWD_SUCCESS when s has at least
   * 1 uppercase letter, otherwise PasswordValidationResult.PWD_INVALID_LENGTH.</p>
   *
   * @return a validator validates the length of Password (String)
   */
  static PasswordValidator checkPwdContainsUppercase() {
    return password -> Optional.of(
            password.equals(password.toLowerCase())
                    ? PasswordValidationResult.PWD_MISSING_UPPER
                    : PasswordValidationResult.PWD_SUCCESS
    );
  }


  /**
   * Result of password validation.
   */
  enum PasswordValidationResult {
    PWD_SUCCESS,
    PWD_INVALID_LENGTH,
    PWD_MISSING_UPPER
  }
  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}
