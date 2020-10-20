package my.lsge.controller;

import my.lsge.domain.logic.PostLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController extends BaseController {

    @Autowired
    private PostLogic postLogic;

    @PostMapping("filter")
    public
}
