package allwrite.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import util.NotAuthorizedException;

import allwrite.model.Note;
import allwrite.service.DeleteService;
import allwrite.service.Util;

public class DeleteController extends Controller {
    DeleteService deleteService = new DeleteService();

    @Override
    public Navigation run() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        if (!validate()){
            data.put("status", "Invalid augument. (email, auth and q is required)");
            Util.json(request, response, data);
            return null;
        }
        try{
            Note note = null;
            note = deleteService.Delete(new RequestMap(request));
            if (note == null){
                data.put("status", "fail");
            }else{
                data.put("status", "ok");
                data.put("note", note);
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
        v.add("key", v.required());
        return v.validate();
    }

}
