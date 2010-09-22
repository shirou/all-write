package allwrite.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import util.NotAuthorizedException;

import allwrite.service.LoginService;
import allwrite.service.Util;

public class LoginController extends Controller {

    @Override
    public Navigation run() throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        if (!validate()){
            data.put("status", "Invalid augument. (email and password is required)");
            Util.json(request, response, data);
            return null;
        }   
        LoginService loginService = new LoginService();
        try{
            String token = loginService.login(new RequestMap(request));
            if (token != null){
                data.put("status", "ok");
                data.put("token", token);
            }else{
                data.put("status", "Unknown user");
            }
        }catch(NotAuthorizedException e){
            data.put("status", "Authentication failed");
        }
            
        
        
        Util.json(request, response, data);
        return null;
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("email", v.required());
        v.add("password", v.required());
        return v.validate();
    }
    
}
