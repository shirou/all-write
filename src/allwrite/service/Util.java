package allwrite.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.slim3.datastore.Datastore;
import org.slim3.util.ThrowableUtil;

import com.google.appengine.api.datastore.Key;

import util.NGramAnalyzer;

import allwrite.meta.UserInfoMeta;
import allwrite.model.Index;
import allwrite.model.UserInfo;

public class Util {
    private static final Logger log = Logger.getLogger(Util.class.getName());

    public static UserInfo findUser(String mailaddress){
        UserInfoMeta u = new UserInfoMeta();
        UserInfo user = Datastore.query(u).filter(u.mailAddress.equal(mailaddress)).asSingle();
        if (user == null){
            return null;
        }
        
        user = Datastore.get(UserInfo.class, user.getKey());
        return user;
    }
    
    public static boolean checkToken(String mailaddress, String token){
        UserInfo user = Util.findUser(mailaddress);
        if (user == null){
            log.warning("could not find user : " + mailaddress);
            return false;
        }
        // TODO: should check last used time?
        List<String> tokens = user.getTokens();
        if (tokens.contains(token)){
            return true;
        }else{
            log.warning("check token failed : " + mailaddress);
            return false;    
        }
    }
    
    public static RAMDirectory deleteIndex(RAMDirectory dir, String noteid){
        try {
            SingleInstanceLockFactory lockFac = new SingleInstanceLockFactory();
            dir.setLockFactory(lockFac);
            IndexReader reader = IndexReader.open(dir, false);

            Document doc;
            for(int i=0;i<reader.numDocs();i++){
                doc = reader.document(i);
                if (doc.get("text") != null){
                    log.info("text:" + doc.get("text"));
                }
                if (doc.get("title") != null){
                    log.info("title:" + doc.get("title"));
                }
                if (doc.get("key") != null){
                    log.info("key:" + doc.get("key"));
                }
                if (doc.get("noteid") == null){
                    log.warning("document field noteid is null in deleteIndex.");
                }else{
                    log.info("noteid:" + doc.get("noteid"));
                }
                if (doc.get("noteid").equals(noteid)){
                    reader.deleteDocument(i);
                    return dir;
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static Index createNewIndex(UserInfo user){
        Index index = new Index();
        index.setKey(Datastore.allocateId(user.getKey(), Index.class));
        RAMDirectory ramIndex = new RAMDirectory();

        try {
            IndexWriter iWriter =
                new IndexWriter(ramIndex, new NGramAnalyzer(1,3), true, MaxFieldLength.UNLIMITED);
            iWriter.close();
            index.setIndex(ramIndex);
            index.setMailAddress(user.getMailAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (user.getIndexes() == null){
            user.setIndexes(new ArrayList<Key>());
        }
        user.setLastIndexKey(index.getKey());
        user.addIndex(index.getKey());
        
        return index; 
    }
    

    /**
     * calc hash
     *
     * @param input     
     * @param algorithm 
     * @return
     */
    public static String hash(String input, String algorithm) {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
            digest.update(input.getBytes());
        }
        catch (NoSuchAlgorithmException e) {
            return "";
        }
        return byte2HexString(digest.digest());
    }

    /**
     * convert byte to hex string
     *
     * @param input
     * @return
     */
    public static String byte2HexString(byte[] input) {

        StringBuffer buff = new StringBuffer();
        int count = input.length;
        for(int i= 0; i< count; i++){
            buff.append(Integer.toHexString( (input[i]>> 4) & 0x0F ) );
            buff.append(Integer.toHexString( input[i] & 0x0F ) );
        }
        return buff.toString();
    }


    /**
     * This method is imported from KenichiroMurata. Thank you.
     * http://d.hatena.ne.jp/KenichiroMurata/20100210/p1
     * @param request
     * @param response
     * @param data
     */
    public static void json(HttpServletRequest request, HttpServletResponse response, Object data) {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        response.setContentType("application/json; charset=" + encoding);
        try {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new OutputStreamWriter(response
                        .getOutputStream(), encoding));
                out.print(JSON.encode(data));
                // System.out.println(JSON.encode(data));
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            ThrowableUtil.wrapAndThrow(e);
        }
    }

}
