package allwrite.service;

import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

import allwrite.model.Index;
import allwrite.model.UserInfo;

public class RegisterService {

    public UserInfo register(Map<String, Object> input) {
        // FIXME: check 
        
        UserInfo user = new UserInfo();
        Key key = Datastore.allocateId(UserInfo.class);
        user.setKey(key);
        String mailAddress = (String)input.get("mailaddress");
        
        if (isMailAddressExists(mailAddress)){
            return null;
        }
        String rawPass = (String)input.get("password");

        user.setHashedPassword(Util.hash(rawPass, "SHA-256"));
        user.setMailAddress(mailAddress);

        // create first index
        Index index = Util.createNewIndex(user);
        user.setLastIndexKey(index.getKey());
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(user);
        Datastore.put(index);
        tx.commit();
        return user;
    }
    
    public static boolean isMailAddressExists(String mailAddress){
        UserInfo user = Util.findUser(mailAddress);
        if (user==null){
            return false;
        }else{
            return true;
        }
    }
    

}
