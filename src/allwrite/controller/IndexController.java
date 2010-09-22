package allwrite.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import allwrite.model.Note;
import allwrite.service.NoteService;

import java.util.List;
import java.util.logging.Logger;


public class IndexController extends Controller {
    private static final Logger logger = Logger.getLogger(IndexController.class.getName());
    
    private NoteService noteService = new NoteService();
    @Override
    public Navigation run() throws Exception {
                
        return forward("index.jsp");
    }
}