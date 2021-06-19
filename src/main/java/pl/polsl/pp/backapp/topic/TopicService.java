package pl.polsl.pp.backapp.topic;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.polsl.pp.backapp.auth.UserPrincipal;
import pl.polsl.pp.backapp.exception.IdNotFoundInDatabaseException;
import pl.polsl.pp.backapp.post.Post;
import pl.polsl.pp.backapp.section.Section;
import pl.polsl.pp.backapp.section.SectionRepository;
import pl.polsl.pp.backapp.user.User;
import pl.polsl.pp.backapp.user.UserRepository;

import java.util.Collections;
import java.util.Date;

@Service
public class TopicService {

    private TopicRepository topicRepository;
    private UserRepository userRepository;
    private SectionRepository sectionRepository;

    public TopicService(TopicRepository topicRepository, UserRepository userRepository, SectionRepository sectionRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
    }

    public Iterable<Topic> getTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopic(String id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + id + " not found"));

        return topic;
    }

    public Iterable<Topic> getSectionsTopics(String id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + id + " not found"));

        return section.getTopics();
    }

    public Topic addTopicToSection(String id, TopicRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(userPrincipal.getUsername()).
                orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find currently logged in user " +
                        userPrincipal.getUsername() + " in database!"));

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find section of id " + id + " in database!"));

        Topic topic = new Topic(request.getTitle(), user, new Date(), new Date(),
                request.getDescription(), Collections.<Post>emptyList());

        section.incTopicsNumber();
        user.incPostsNumber();

        userRepository.save(user);
        Topic topicWithId = topicRepository.save(topic);

        section.getTopics().add(topicWithId);
        sectionRepository.save(section);

        return topicWithId;
    }

    public Topic updateTopicInSection(String sectionId, String topicId, TopicRequest request) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find section of id " + sectionId + " in database!"));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + topicId + " not found"));

        topic.setLastChange(new Date());
        topic.setDescription(request.getDescription());
        topic.setTitle(request.getTitle());

        for (Topic t : section.getTopics()) {
            if (t.getId().equals(topicId)) {
                t.setDescription(topic.getDescription());
                t.setTitle(topic.getTitle());

                sectionRepository.save(section);
                break;
            }
        }

        return topicRepository.save(topic);
    }

    // TODO dodać zeby tylko autor, moderator albo admin mogli usuwac posty
    // TODO dodać usuwanie postów przy usuwaniu tematów
    public void deleteTopic(String sectionId, String topicId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(userPrincipal.getUsername())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find currently logged in user " +
                    userPrincipal.getUsername() + " in database!"));

        user.decPostsNumber();
        userRepository.save(user);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find section of id " + sectionId + " in database!"));

        section.decTopicsNumber();

        for (Topic t : section.getTopics()) {
            if (t.getId().equals(topicId)) {
                section.getTopics().remove(t);
                sectionRepository.save(section);
                break;
            }
        }

        topicRepository.deleteById(topicId);
    }
}
