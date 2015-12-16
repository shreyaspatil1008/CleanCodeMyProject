package main.java.util;

import java.io.IOException;

import javax.inject.Inject;

import main.java.model.User;
import main.java.validator.EmailValidator;
import sun.misc.BASE64Decoder;

/**
 * A utility class to 
 * 1. Authenticate auth string
 * 2. Authenticate user
 * @Author shreyas patil
 */
public class AuthenticationUtil {
	@Inject
	private EmailValidator emailValidator;
	@Inject
	private User user;

	
	public boolean isUserAuthenticated(String authString,User user){
		this.user = user;
		return fillAndValidateAuthParts(authString);
	}

	private boolean fillAndValidateAuthParts(String authString) {
		String[] authParts = authString.split("\\s+");
		return validateAuthParts(authParts);
	}

	private boolean validateAuthParts(String[] authParts) {
		if(authParts.length>1){
			return fillAndValidateAuthInfoWithTryCatch(authParts);
		}else{
			return false;	
		}
	}

	private boolean fillAndValidateAuthInfoWithTryCatch(String[] authParts) {
		String authInfo = authParts[1];
		try{
			return fillAndValidateDecodeAuth(authInfo);
		}catch(Exception e){
			return false;
		}
	}

	@SuppressWarnings("restriction")
	private boolean fillAndValidateDecodeAuth(String authInfo) throws IOException {
		String decodeAuth = new String(new BASE64Decoder().decodeBuffer(authInfo));
		return validateDecodeAuth(decodeAuth);
	}

	private boolean validateDecodeAuth(String decodeAuth) {
		if(decodeAuth.contains(":")){
			return fillAndValidateAuthPart(decodeAuth);
		}else{
			return false;
		}
	}

	private boolean fillAndValidateAuthPart(String decodeAuth) {
		String[] authPart;
		authPart = decodeAuth.split(":");
		return validateAuthPart(authPart);
	}

	private boolean validateAuthPart(String[] authPart) {
		if(authPart.length>1){
			return fillAndValidateUser(authPart);					
		}else{
			return false;
		}
	}

	private boolean fillAndValidateUser(String[] authPart) {
		user = emailValidator.findValidateAndReturnByEmailAndPasswordUser(authPart[0],authPart[1]);
		return validateUser();
	}

	private boolean validateUser() {
		if(null!=user){
			return true;
		}else{
			return false;
		}
	}

}
