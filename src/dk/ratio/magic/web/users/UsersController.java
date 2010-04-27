package dk.ratio.magic.web.users;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.web._404Exception;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsersController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDao userDao;

    @RequestMapping("/users")
    public ModelAndView defaultHandler()
    {
        // return the first page
        return pageHandler(1);
    }

    @RequestMapping("/users/page/{pageNumber}")
    public ModelAndView pageHandler(@PathVariable("pageNumber") Integer pageNumber)
    {
        final Page<User> userPage = userDao.getUserPage(pageNumber);
        if (userPage.getPageCount() < pageNumber) {
            throw new _404Exception();
        }
        ModelAndView mv = new ModelAndView("/users/list");
        mv.addObject("userPage", userPage);
        return mv;
    }
}
