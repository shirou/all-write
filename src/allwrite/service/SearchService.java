package allwrite.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.mortbay.log.Log;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;

import util.NGramAnalyzerForQuery;
import util.NotAuthorizedException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import allwrite.model.Index;
import allwrite.model.Note;
import allwrite.model.UserInfo;
import allwrite.meta.IndexMeta;
import allwrite.meta.NoteMeta;

public class SearchService {
    static final Logger log = Logger.getLogger(SearchService.class.getName());

    private static final int MAX_RESULT_SIZE = 30;
    private IndexMeta    i = new IndexMeta();
    
    public Query getQuery(){
        String q = "[????-??-??]";
        Term term = new Term("text", q);
        WildcardQuery query = new WildcardQuery(term);

        return query;
    }
    
    public List<Map<String, String>> Search(Map<String, Object> input) throws CorruptIndexException, IOException, ClassNotFoundException, ParseException, NotAuthorizedException {
        String email = (String)input.get("email");
        String token = (String)input.get("auth");
//        long start;
//  start = System.currentTimeMillis();
//        log.info("Search start:" + start);

        UserInfo user = Util.findUser(email);
        if (!user.getTokens().contains(token)){
            throw new NotAuthorizedException();
        }

        // get indexes.
        List<Index> result = new ArrayList<Index>();
        result =  Datastore.get(i,user.getIndexes());
//        log.info("Search get Indexes:" + (System.currentTimeMillis() - start));
        
        // create a MultiReader from indexes.        
        IndexReader[] idxReaders = new IndexReader[result.size()];
        int c=0;
        if (result.size() == 0){
            return null;
        }
        for(Index idx: result){
            IndexReader r = IndexReader.open(idx.getIndex(),  true); // create Read only indexreader.
            idxReaders[c] = r;
            c++;
        }
//        log.info("Search get multiple index:"  + (System.currentTimeMillis() - start));
        
        MultiReader mReader = new MultiReader(idxReaders, true);
        IndexSearcher searcher = new IndexSearcher(mReader);
//        log.info("Search get MultiReader:"  + (System.currentTimeMillis() - start));
        
        Query query;
        if (input.containsKey("schedule")){
            Term term = new Term("text", "[????-??-??]");
            query = new WildcardQuery(term);
        }else{
            String q = (String)input.get("q");
            query = new QueryParser(Version.LUCENE_CURRENT, "text", new NGramAnalyzerForQuery(1,3)).parse(q);
        }

        System.out.println("Query:" + query.toString());
//        log.info("Search start:" + (System.currentTimeMillis() - start));

        TopDocs td = searcher.search(query, MAX_RESULT_SIZE);
//        log.info("Search end:" + (System.currentTimeMillis() - start));
        
        // get note keys 
        Document doc;
        c = 0;
        String[] keys = new String[td.scoreDocs.length];
        
        System.out.println("result: " + td.scoreDocs.length);
        for(ScoreDoc sd: td.scoreDocs){
            doc = searcher.doc(sd.doc);
            keys[c] = doc.get("key");
            c++;
        }
//        log.info("Search get doc:" + (System.currentTimeMillis() - start));

        List<Note> notes = findNotes(keys);
        List<Map<String,String>> retval = new ArrayList<Map<String,String>>();
        for(Note note: notes){
            Map<String, String> map = new HashMap<String, String>();
            map.put("title", note.getTitle());
            map.put("text", note.getText());
            map.put("created", note.getCreatedDate().toString());
            map.put("noteid", note.getNoteId());
            retval.add(map);
        }
//        log.info("Search create Return:" + (System.currentTimeMillis() - start));

        return retval; 
    }

    public static List<Note> findNotes(String[] keys){
        NoteMeta n = new NoteMeta();
        List<Note> notes = new ArrayList<Note>(keys.length);
        List<Key> keyList = new ArrayList<Key>(keys.length);
        for(String key: keys){
            Key mkey = KeyFactory.stringToKey(key);
            keyList.add(mkey);
        }
        
        try{
            notes = Datastore.get(n, keyList);
        }catch(EntityNotFoundRuntimeException e){
            log.warning("some entity is not found in findNotes. index is broken?");
            return null;
        }
        return notes;
    }
    
}
