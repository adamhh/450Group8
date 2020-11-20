package edu.uw.comchat.util;

import static edu.uw.comchat.util.EmailValidator.EmailValidationResult;

import java.util.Optional;
import java.util.function.Function;

/**
 * This class provides validator to check whether
 * an input email address is valid (client-side).
 *
 * @author Hung Vu
 */
public interface EmailValidator extends Function<String,
        Optional<EmailValidationResult>> {
  /**
   * A username section of an email address.
   */
  static final String USER_NAME = "^[a-zA-Z0-9-_.]+";

  /**
   * "At" symbol (@).
   */
  static final String AT = "@";

  /**
   * A domain section of an email address.
   */
  static final String DOMAIN = "[a-zA-Z0-9-_.]+.[a-zA-Z]{2,}";

  /**
   * An end of line symbol.
   */
  static final String END_OF_ADDRESS = "$";

  /**
   * Return a validator to see whether a given password follows the given rules:
   * For both USER_NAME and DOMAIN section: A-Z, a-z, 0-9, hyphen (-), underscore (_),
   * and dot (.) characters are permitted.
   * The domain must contain top-level domain such as .com, .edu, etc.
   *
   * <p>When a String s is applied to the returning validator, it will evaluate
   * to an Optional containing EmailValidationResult.EMAIL_SUCCESS when the above rule
   * above is followed, otherwise EmailValidationResult.EMAIL_INVALID.</p>
   *
   * @return a validator validates an email address.
   */
  static EmailValidator checkEmail() {
    String pattern = USER_NAME + AT + DOMAIN + END_OF_ADDRESS;
    return email -> Optional.of(
            email.matches(pattern)
                    ? EmailValidationResult.EMAIL_SUCCESS
                    : EmailValidationResult.EMAIL_INVALID
    );
  }

  /**
   * Result of email validation.
   */
  enum EmailValidationResult {
    EMAIL_SUCCESS,
    EMAIL_INVALID
  }

}
