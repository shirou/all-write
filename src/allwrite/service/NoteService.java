package allwrite.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import util.NGramAnalyzer;
import util.NotAuthorizedException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import allwrite.meta.NoteMeta;
import allwrite.model.Index;
import allwrite.model.Note;
import allwrite.model.UserInfo;

/**
 * create new note
 * @author shirou
 *
 */
public class NoteService {
    private static final int DATASTORE_MAX_SIZE = 1024000;
    static final Logger log = Logger.getLogger(NoteService.class.getName());

    public Note note(Map<String, Object> input) throws NotAuthorizedException {
        String email  = null;
        String text   = null;
        String noteid = null;
        String token  = null;
        String modify = null;
        text = checkContains(input, "text");
        email = checkContains(input, "email");
        noteid = checkContains(input, "key");
        token = checkContains(input, "auth");
        modify = checkContains(input, "modify");

        // Invalid argument
        if (email == null || token == null){
            return null;
        }
        if (!Util.checkToken(email, token)){
            throw new NotAuthorizedException();
        }
        if (noteid != null && modify == null){
            return getOneNote(noteid, email);
        }
        if (modify != null){
            return updateNote(noteid, email, modify, text);
        }
        
        return newNote(email, text);
    }
    
    private Note updateNote(String noteid, String email, String modify, String text) {
        Note note = getOneNote(noteid, email);
        if (note == null){
            return null;
        }
        
        // update index
        Index index = Datastore.get(Index.class, note.getIndexedEntityKey());
        
        // Delete document from Index and add again 
        try {
            RAMDirectory ramIndex = Util.deleteIndex(index.getIndex(), noteid);
            if (ramIndex != null){
                index.setIndex(ramIndex);
            }

            SingleInstanceLockFactory lockFac = new SingleInstanceLockFactory();
            ramIndex.setLockFactory(lockFac);

            // Add index
            Document doc = createDocument(note, text);
            writeToIndex(ramIndex, doc, index);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
         // update note
        note.setText(text);
        note.setLastmodifiedDate(new Date());
         
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(note);
        Datastore.put(index);
        tx.commit();
        
        Memcache.put(index.getKey(), index);
        
        return note;
    }

    public Note newNote(String email, String text) {
        if (text == null || text.length() == 0){
            return null;
        }
        boolean isCreateIndex = false;
        
        Note note = new Note();
        note.setMailAddress(email);
        
        UserInfo user = Util.findUser(email); // FIXME: should be sanitize.
        if (user == null){
            return null;
        }
        note.setKey(Datastore.allocateId(user.getKey(), Note.class));
        note.setText(text);
        note.setNoteId(createNoteId(email));

        // Get index Entity
        Index index;
        index = Datastore.get(Index.class, user.getLastIndexKey()); // get key of last used index entity
       
        // if index left space is small, create new index
        if (index.getSize() > (DATASTORE_MAX_SIZE - (text.length() * 2))){ 
            log.info("create new index belongs to " + email);
            index = Util.createNewIndex(user);
            isCreateIndex = true;
        }
        
        // get RAMDirectory from index
        RAMDirectory ramIndex = null;
        try {
            ramIndex = index.getIndex();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        SingleInstanceLockFactory lockFac = new SingleInstanceLockFactory();
        ramIndex.setLockFactory(lockFac);

        Document doc = createDocument(note, text);
        writeToIndex(ramIndex, doc, index);
        
        // set index entity key in order to update index
        note.setIndexedEntityKey(index.getKey());
        
        Transaction tx = Datastore.beginTransaction();
        if (isCreateIndex){
            Datastore.put(user);
        }
        Datastore.put(note);
        Datastore.put(index);
        tx.commit();
        
        Memcache.put(index.getKey(), index);

//        log.info("create new note success. : " + email);   

        return note;
    }
    
  
    public static void printIndex(RAMDirectory dir){
        try {
            IndexReader reader = IndexReader.open(dir);
            Document doc;
            for(int i=0;i<reader.numDocs();i++){
                doc = reader.document(i);
                System.out.println("t(" + i + "/" + reader.numDocs() + "):"+ doc.get("title"));
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        System.out.println("------done ");
    }
    
    private Document createDocument(Note note, String text){
        Document doc = new Document();
        doc.add(new Field("text", text, Store.NO, org.apache.lucene.document.Field.Index.ANALYZED));
        doc.add(new Field("noteid", text, Store.YES, org.apache.lucene.document.Field.Index.NOT_ANALYZED));
        doc.add(new Field("key", KeyFactory.keyToString(note.getKey()), Store.YES, org.apache.lucene.document.Field.Index.NOT_ANALYZED));
        return doc;
    }

    
    private void writeToIndex(RAMDirectory ramIndex, Document doc, Index index){
        // Write to Index
        try {
            IndexWriter iWriter =
                new IndexWriter(ramIndex, new NGramAnalyzer(1,3), false, MaxFieldLength.UNLIMITED);
            SerialMergeScheduler serialMerge = new SerialMergeScheduler();
            iWriter.setMergeScheduler(serialMerge);
            
            iWriter.addDocument(doc);
            iWriter.optimize();
            iWriter.close();
            index.setIndex(ramIndex);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Note getOneNote(String noteid, String email){
        NoteMeta n = new NoteMeta();
        Note note = Datastore.query(n).filter(n.mailAddress.equal(email), n.noteId.equal(noteid)).asSingle();

        return note;
    }
    private String checkContains(Map<String, Object> input, String key){
        if (input.containsKey(key)){
            return (String)input.get(key);
        }else{
            return null;
        }
    }
    
    public static String createNoteId(String mailaddress){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss-");
        
        return format.format(now) + Util.hash(mailaddress, "SHA-256");  
    }
    
}
