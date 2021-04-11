package com.productheaven.user.util;

public final class MessageKey {

	private MessageKey () {
		
		
	}
	
	public static final String VALIDATION_COMMON_MESSAGE     = "validation.common.message";
	public static final String VALIDATION_USERNAME_EMPTY     = "validation.username.empty";
	public static final String VALIDATION_USERNAME_INVALID   = "validation.username.invalid";
	public static final String VALIDATION_EMAIL_EMPTY        = "validation.email.empty";
	public static final String VALIDATION_EMAIL_INVALID      = "validation.email.invalid";
	public static final String VALIDATION_NAME_EMPTY         = "validation.name.empty";
	public static final String VALIDATION_NAME_INVALID       = "validation.name.invalid";	
	public static final String VALIDATION_SURNAME_EMPTY      = "validation.surname.empty";
	public static final String VALIDATION_SURNAME_INVALID    = "validation.surname.invalid";
	public static final String VALIDATION_PASSWORD_EMPTY     = "validation.passwordHashed.empty";
	public static final String VALIDATION_PASSWORD_INVALID   = "validation.passwordHashed.invalid";
	public static final String VALIDATION_USER_ID_INVALID    = "validation.userid.invalid";
	
	public static final String EXCEPTION_NO_USER_FOUND               = "exception.NoUsersFoundException.message";
	public static final String EXCEPTION_USER_ALREADY_EXISTS         = "exception.UserAlreadyExistsException.message";
	public static final String EXCEPTION_USER_NOT_FOUND              = "exception.UserNotFoundException.message";
	public static final String EXCEPTION_COMMON                      = "exception.Exception.message";

}
