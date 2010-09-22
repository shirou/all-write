package allwrite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import util.NotAuthorizedException;

import com.google.appengine.api.datastore.Transaction;

import allwrite.model.UserInfo;



public class LoginService {

    public String login(Map<String, Object> input) throws NotAuthorizedException {
        String token="";
        
        String mailaddress = (String)input.get("email");
        String rawPass = (String)input.get("password");

        UserInfo user = Util.findUser(mailaddress);
        if (user==null){
            return null;
        }else{
            String hashedPass = Util.hash(rawPass, "SHA-256"); 
            if (hashedPass.equals(user.getHashedPassword())){
                token = createToken(mailaddress);
                Date now = new Date();
                long now_l = now.getTime();
                Date last = user.getLastTokenUsedTime();
                if (last == null){
                    user.setLastTokenUsedTime(now);
                    user.setTokens(new ArrayList<String>()); // initialize
                }
                long last_l = user.getLastTokenUsedTime().getTime();
                if ((now_l -  last_l) > 1000* 60 * 60){ // over 1hour ago
                    user.setTokens(new ArrayList<String>()); //  clear all tokens 
                }
                List<String> tokens = user.getTokens();
                tokens.add(token);
                user.setLastTokenUsedTime(now);
                user.setTokens(tokens);
            }else{
                throw new NotAuthorizedException();
            }
        }
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(user);
        tx.commit();
        
        System.out.println("email: " +mailaddress + " is authenticated.");

        return token;
    }

    public static String createToken(String mailaddress){
        Date now = new Date();
        
        return Util.hash(mailaddress+"-"+now.toString(), "SHA-256");
    }
    
}
