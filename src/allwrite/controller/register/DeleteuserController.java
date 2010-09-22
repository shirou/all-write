package allwrite.controller.register;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import java.util.Map;

import org.slim3.datastore.Datastore;
import org.slim3.util.RequestMap;

import allwrite.model.UserInfo;
import allwrite.meta.UserInfoMeta;

public class DeleteuserController extends Controller {

    @Override
    public Navigation run() throws Exception {
        Map m = new RequestMap(request);
        String mail = (String)m.get("mailaddress");
        if (mail!=null){
            delete(mail);
        }else{
            return forward("deleteuser.jsp");
        }
        return null;
    }
    
    private void delete(String mailaddress){
        UserInfoMeta u = new UserInfoMeta();
        UserInfo user = Datastore.query(u).filter(u.mailAddress.equal(mailaddress)).asSingle();
        System.out.println(mailaddress + "/" + user);
        System.out.println(user.getMailAddress());
        Datastore.deleteAll(user.getKey());
    }
    
}
