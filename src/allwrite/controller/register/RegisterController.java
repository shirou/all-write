package allwrite.controller.register;


import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import allwrite.service.RegisterService;
import allwrite.meta.UserInfoMeta;
import allwrite.model.UserInfo;

public class RegisterController extends Controller {
    UserInfoMeta u = new UserInfoMeta();
    private RegisterService registerSerivce = new RegisterService();

    @Override
    public Navigation run() throws Exception {
        UserInfo user;
        if (validate()){
            user = registerSerivce.register(new RequestMap(request));
            if (user == null){
                return forward("fail.jsp");
            }else{
                return forward("done.jsp");
            }
        }
        return forward(basePath); 
    }


    protected boolean validate() {
        Validators v = new Validators(request);
        //v.add(u.mailAddress, v.required());
        //v.add(u.hashedPassword, v.required());
        return v.validate();
    }

}
