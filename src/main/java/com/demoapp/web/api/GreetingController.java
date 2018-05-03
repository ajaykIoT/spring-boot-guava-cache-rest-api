package  com.demoapp.web.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.model.Greeting;
import com.demoapp.service.GreetingService;



@RestController
@RequestMapping(value = "/v1")
public class GreetingController extends BaseController
{
	
    @Autowired
    private GreetingService greetingService;

    
    @GetMapping(
    		path = "/api/greetings",
            produces="application/json")
    public ResponseEntity<Collection<Greeting>> getGreetings() 
    {
        logger.info("> getGreetings");

        Collection<Greeting> greetings = greetingService.findAll();

        logger.info("< getGreetings");
        return new ResponseEntity<Collection<Greeting>>(greetings,
                HttpStatus.OK);
    }


    @GetMapping(
    		 path = "/api/greetings/{id}",      
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Greeting> getGreeting(@PathVariable("id") Long id) {
        logger.info("> getGreeting id:{}", id);

        Greeting greeting = greetingService.findOne(id);
        if (greeting == null) {
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }

        logger.info("< getGreeting id:{}", id);
        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }

   
    @PostMapping(
    	    path = "/api/greetings",          
            consumes = "application/json")
    public ResponseEntity<Greeting> createGreeting(
            @RequestBody Greeting greeting) {
        logger.info("> createGreeting");

        Greeting savedGreeting = greetingService.create(greeting);

        logger.info("< createGreeting");
        return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);
    }

   
    @PutMapping(
    		path = "/api/greetings/{id}",           
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Greeting> updateGreeting(
            @RequestBody Greeting greeting) {
        logger.info("> updateGreeting id:{}", greeting.getId());

        Greeting updatedGreeting = greetingService.update(greeting);
        if (updatedGreeting == null) {
            return new ResponseEntity<Greeting>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateGreeting id:{}", greeting.getId());
        return new ResponseEntity<Greeting>(updatedGreeting, HttpStatus.OK);
    }

  
    @DeleteMapping(
    		 path = "/api/greetings/{id}"
           )
    public ResponseEntity<Greeting> deleteGreeting(
            @PathVariable("id") Long id) {
        logger.info("> deleteGreeting id:{}", id);

        greetingService.delete(id);

        logger.info("< deleteGreeting id:{}", id);
        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
    }   
    
    @DeleteMapping(
   		 path = "/api/greetings"
          )
   public String deleteALLGreeting()
    {
       logger.info("> deleteAllGreetings");

       greetingService.evictCache();

       logger.info("< deleteAllGreetings");
       return "All data from cache deleted";
   }   
}
