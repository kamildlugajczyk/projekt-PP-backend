package pl.polsl.pp.backapp.topic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.polsl.pp.backapp.exception.IdNotFoundInDatabaseException;
import pl.polsl.pp.backapp.exception.ItemExistsInDatabaseException;

import java.util.List;

@RestController
public class TopicController {

    private TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/topic")
    public Iterable<TopicDTO> getTopics(){
        return topicService.getTopics();
    }

    @GetMapping("/section/{sectionId}/topic/{topicId}")
    public TopicDTO getTopic(@PathVariable String sectionId, @PathVariable String topicId) {
        try {
            return topicService.getTopic(sectionId, topicId);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/section/{id}/topic")
    public Iterable<TopicDTO> getSectionsTopics(@PathVariable String id) {
        try {
            return topicService.getSectionsTopics(id);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("mostPopularTopics/{keyPageViews}")
    public List<TopicDTO> getMostPopularTopics(@PathVariable Integer keyPageViews){
        try{
            return topicService.getMostPopularTopics(keyPageViews);
        }
        catch (RuntimeException  e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping("/section/{id}/topic")
    public TopicDTO addTopicToSection(@PathVariable String id, @RequestBody TopicRequest request) {
        try {
            return topicService.addTopicToSection(id, request);
        } catch (ItemExistsInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (RuntimeException  e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping("/section/{sectionId}/topic/{topicId}")
    public TopicDTO updateTopic(@PathVariable String sectionId, @PathVariable String topicId, @RequestBody TopicRequest request) {
        try {
            return topicService.updateTopicInSection(sectionId, topicId, request);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ItemExistsInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @DeleteMapping("/section/{sectionId}/topic/{topicId}")
    public void deleteTopic(@PathVariable String sectionId, @PathVariable String topicId) {
        try {
            topicService.deleteTopic(sectionId, topicId);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
