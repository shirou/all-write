package allwrite.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import util.NotAuthorizedException;

import allwrite.service.SearchService;
import allwrite.service.Util;

public class SearchController extends Controller {
    SearchService searchService = new SearchService();
    
    @Override
    public Navigation run() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        if (!validate()){
            data.put("status", "Invalid augument. (email, auth is required)");
            Util.json(request, response, data);
            return null;
        }
        try{
            List<Map<String, String>> notes = null;
            notes = searchService.Search(new RequestMap(request));
            if (notes == null){
                data.put("status", "fail");
            }else{
                data.put("status", "ok");
                data.put("notes", notes);
            }
        }catch (NotAuthorizedException e){
            data.put("status", "Not authorized");
        }

        Util.json(request, response, data);
        return null;
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("email", v.required());
        v.add("auth", v.required());
        //v.add("q", v.required());
        return v.validate();
    }

}