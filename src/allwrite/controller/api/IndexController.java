package allwrite.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import util.NotAuthorizedException;

import allwrite.service.IndexService;
import allwrite.service.Util;


public class IndexController extends Controller {
    IndexService indexService = new IndexService();
    private static final Logger log = Logger.getLogger(IndexController.class.getName());
    
    @Override
    public Navigation run() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        if (!validate()){
            data.put("status", "Invalid augument. (email and auth is required)");
            Util.json(request, response, data);
            return null;
        }
        
        try{
            List<Map<String, String>> notes=null;
            notes = indexService.index(new RequestMap(request));
            if (notes == null){
                
            }else{
                data.put("status", "ok");
                data.put("notes", notes);
            }
        }catch(NotAuthorizedException e){
            data.put("status", "Not authorized");            
        }
        
        
        Util.json(request, response, data);
        return null;
    }
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("email", v.required());
        v.add("auth", v.required());
        return v.validate();
    }

}
