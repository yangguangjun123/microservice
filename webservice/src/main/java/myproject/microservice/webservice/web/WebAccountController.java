package myproject.microservice.webservice.web;

//import myproject.microservice.accountservice.domain.Account;
import myproject.microservice.webservice.service.WebAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/account")
public class WebAccountController {
    @Autowired
    private WebAccountService webAccountService;

    private static final Logger logger = Logger.getLogger(WebAccountController.class.getName());

    public WebAccountController(WebAccountService webAccountService) {
        this.webAccountService = webAccountService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("accountNumber", "searchText");
    }

    @RequestMapping("/")
    public String goHome() {
        return "index";
    }

    @RequestMapping("/{accountNumber}")
    public String byNumber(Model model, @PathVariable("accountNumber") String accountNumber) {

        logger.info("web-service byNumber() invoked: " + accountNumber);

        Optional<AccountDTO> account = webAccountService.getByNumber(accountNumber);
        if(account.isPresent()) {
            logger.info("web-service byNumber() found: " + account.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Account %s Not Found", accountNumber));
        }
        model.addAttribute("account", account.get());
        return "account";
    }

    @RequestMapping("/owner/{text}")
    public String ownerSearch(Model model, @PathVariable("text") String name) {
        logger.info("web-service byOwner() invoked: " + name);

        List<AccountDTO> accounts = webAccountService.byOwnerContains(name);
        logger.info("web-service byOwner() found: " + accounts);
        model.addAttribute("search", name);
        if (accounts != null)
            model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchForm(Model model) {
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "accountSearch";
    }

    @RequestMapping(value = "/dosearch")
    public String doSearch(Model model, SearchCriteria criteria,
                           BindingResult result) {
        logger.info("web-service search() invoked: " + criteria);

        criteria.validate(result);

        if (result.hasErrors())
            return "accountSearch";

        String accountNumber = criteria.getAccountNumber();
        if (StringUtils.hasText(accountNumber)) {
            return byNumber(model, accountNumber);
        } else {
            String searchText = criteria.getSearchText();
            return ownerSearch(model, searchText);
        }
    }

}
