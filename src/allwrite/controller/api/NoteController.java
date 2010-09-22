package allwrite.controller.api;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.EntityNotFoundRuntimeException;
import org.slim3.util.RequestMap;

import util.NotAuthorizedException;

import allwrite.model.Note;
import allwrite.service.NoteService;
import allwrite.service.Util;

public class NoteController extends Controller {
    private NoteService noteService = new NoteService();
    
    @Override
    public Navigation run() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        if (!validate()) {
            data.put("status", "Invalid augument. (email and auth is required)");
            Util.json(request, response, data);
            return null;
        }
        if (!isPost()){
            data.put("status", "Invalid Method. (POST method is required)");
            Util.json(request, response, data);
            return null;
        }

        Note note = null;
        
        try{
            note = noteService.note(new RequestMap(request));
        }catch(EntityNotFoundRuntimeException e){
            data.put("status", "noteid doesn't exist in the datastore.");
            System.out.println(e.getMessage());
            Util.json(request, response, data);
            return null;  
        }catch(NotAuthorizedException e){
            data.put("status", "Auth failed");
            Util.json(request, response, data);
            return null;
        }

        if (note == null){
            data.put("status", "fail");
            return null;
        }
        
        Map<String, String> noteData = new HashMap<String, String>();

        data.put("status", "ok");
        noteData.put("title", note.getTitle());
        noteData.put("text", note.getText());
        noteData.put("noteid", note.getNoteId());
        noteData.put("created", note.getCreatedDate().toString());
        data.put("note", noteData);
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
