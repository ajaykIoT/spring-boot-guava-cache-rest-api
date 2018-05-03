package com.demoapp.service;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import com.demoapp.model.Greeting;
import com.demoapp.repository.GreetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingServiceBean implements GreetingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

  
    @Autowired
    private GreetingRepository greetingRepository;

    @Override
    @Cacheable(value = "greetings")
    public Collection<Greeting> findAll() 
    {
        logger.info("> findAll");

        Collection<Greeting> greetings = greetingRepository.findAll();

        logger.info("< findAll");
        return greetings;
    }

    @Override
    @Cacheable(value = "greetings", key = "#id")
    public Greeting findOne(Long id) 
    {
        logger.info("> findOne id:{}", id);

         Greeting greeting = greetingRepository.findOne(id);

        logger.info("< findOne id:{}", id);
        return greeting;
    }

    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(value = "greetings",key = "#result.id")
    public Greeting create(Greeting greeting) 
    {
        logger.info("> create");

        if (greeting.getId() != null)
        {
          
        	logger.error(
                    "Attempted to create a Greeting, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        Greeting savedGreeting = greetingRepository.save(greeting);

        logger.info("< create");
        return savedGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(value = "greetings",key = "#greeting.id")
    public Greeting update(Greeting greeting) 
    {
        logger.info("> update id:{}", greeting.getId());
  
        Greeting greetingToUpdate = findOne(greeting.getId());
        if (greetingToUpdate == null) 
        {
            logger.error(
                    "Attempted to update a Greeting, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        greetingToUpdate.setText(greeting.getText());
        Greeting updatedGreeting = greetingRepository.save(greetingToUpdate);

        logger.info("< update id:{}", greeting.getId());
        return updatedGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @CacheEvict(value = "greetings",key = "#id")
    public void delete(Long id)
    {
        logger.info("> delete id:{}", id);

        greetingRepository.delete(id);

        logger.info("< delete id:{}", id);
    }

    @Override
    @CacheEvict(value = "greetings", allEntries = true)
    public void evictCache() 
    {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}
