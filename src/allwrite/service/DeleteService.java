package allwrite.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.RAMDirectory;
import org.slim3.datastore.Datastore;
import org.slim3.util.RequestMap;

import com.google.appengine.api.datastore.Transaction;


import util.NotAuthorizedException;

import allwrite.meta.NoteMeta;
import allwrite.model.Index;
import allwrite.model.Note;

public class DeleteService {
    static final Logger log = Logger.getLogger(DeleteService.class.getName());

    public Note Delete(RequestMap input) throws NotAuthorizedException {
        String email = (String)input.get("email");
        String token = (String)input.get("auth");
        
        if (!Util.checkToken(email, token)){
            throw new NotAuthorizedException();
        }

        String noteid = (String)input.get("key");
        
        NoteMeta n = new NoteMeta();
        Note note = Datastore.query(n).filter(n.mailAddress.equal(email), n.noteId.equal(noteid)).asSingle();

        if (note == null){
            return null;
        }
        Index index = Datastore.get(Index.class, note.getIndexedEntityKey());
        
        // Delete Document from Index
        try {
            RAMDirectory result = Util.deleteIndex(index.getIndex(), noteid);
            if (result != null){
                index.setIndex(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // Delete from DataStore.
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(index);
        Datastore.delete(note.getKey());
        tx.commit();
        
        log.info("Delete note by " + email + "/title:" + note.getTitle());

        return note;
    }
    

}
