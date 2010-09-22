package allwrite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.slim3.datastore.Datastore;

import util.NotAuthorizedException;

import com.google.appengine.api.datastore.KeyFactory;

import allwrite.meta.NoteMeta;
import allwrite.model.Note;

public class IndexService {
    final static int MAX_INDEX_NOTE_SIZE = 20;
    private static final Logger log = Logger.getLogger(IndexService.class.getName());

    public List<Map<String,String>> index(Map<String, Object> input) throws NotAuthorizedException{
        String mailaddress = (String)input.get("email");
        String token = (String)input.get("auth");
       
        if (!Util.checkToken(mailaddress, token)){
            throw new NotAuthorizedException();
        }
        NoteMeta n = new NoteMeta();

	log.info("index requested by " + mailaddress);
        
        List<Note> notes = Datastore.query(n)
            .filter(n.mailAddress.equal(mailaddress))
            .sort(n.createdDate.asc)
            .limit(MAX_INDEX_NOTE_SIZE)
            .asList();
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        for(Note note: notes){
            Map<String, String> map = new HashMap<String, String>();
            map.put("title", note.getTitle());
            map.put("text", note.getText());
            map.put("created", note.getCreatedDate().toString());
            map.put("noteid", note.getNoteId());
            result.add(map);
        }
        
        return result;
    }

}
