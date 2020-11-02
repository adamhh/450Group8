package edu.uw.comchat.util;

import java.util.Optional;
import java.util.function.Function;

import static edu.uw.comchat.util.PasswordValidator.ValidationResult;

/**
 * This class provides a tool to validate whether an input password
 * is valid (client-side).
 * @author Hung Vu
 */
public interface PasswordValidator extends Function<String,
        Optional<ValidationResult>> {
  /**
   * Minimum password length.
   */
  static final int MIN_PASSWORD_LENGTH = 6;

  /**
   * Return a validator to see whether a given password meets
   * minimum requirement in length. For example, if the requirement
   * is 6, then the password must have at least 6 character.
   *
   * When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing ValidationResult.PWD_SUCCESS when s.length >= 6,
   * otherwise ValidationResult.PWD_INVALID_LENGTH.
   *
   * @return a validator validates the length of Password (String)
   */
  static PasswordValidator checkPwdLength(){
    return checkPwdLength(MIN_PASSWORD_LENGTH);
  }

  /**
   * Return a validator to see whether a given password meets
   * minimum requirement in length.
   *
   * When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing ValidationResult.PWD_SUCCESS when
   * s.length >= minPasswordLength, otherwise ValidationResult.PWD_INVALID_LENGTH.
   *
   * @param minPasswordLength a minimum password length
   * @return a validator validates the length of Password (String)
   */
  static PasswordValidator checkPwdLength(int minPasswordLength){
    return password -> Optional.of(
            password.length() >= minPasswordLength
                    ? ValidationResult.PWD_SUCCESS
                    : ValidationResult.PWD_INVALID_LENGTH
    );
  }

  /**
   * Return a validator to see whether a given password contains
   * at least 1 uppercase letter.
   *
   * When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing ValidationResult.PWD_SUCCESS when s has at least
   * 1 uppercase letter, otherwise ValidationResult.PWD_INVALID_LENGTH.
   *
   * @return a validator validates the length of Password (String)
   */
  static PasswordValidator checkUpperCase(){
    return password -> Optional.of(
            password.equals(password.toLowerCase())
                    ? ValidationResult.PWD_SUCCESS
                    : ValidationResult.PWD_MISSING_UPPER
    );
  }

  /**
   * Result of password validation.
   */
  enum ValidationResult{
    PWD_SUCCESS,
    PWD_INVALID_LENGTH,
    PWD_MISSING_UPPER
  }
}
