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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    public Iterable<TopicDTO> getTopics() {
        List<Topic> topics = topicRepository.findAll();
        List<TopicDTO> topicsDTO = new ArrayList<>();

        for (Topic topic: topics) {
            User author = userRepository.findById(topic.getAuthorId())
                    .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + topic.getAuthorId() + " not found"));

            topicsDTO.add(new TopicDTO(author, topic));
        }

        return topicsDTO;
    }

    public TopicDTO getTopic(String sectionId, String topicId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + sectionId + " not found"));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + topicId + " not found"));

        User author = userRepository.findById(topic.getAuthorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + topic.getAuthorId() + " not found"));

        topic.incPageViews();
        for (Topic t : section.getTopics()) {
            if (t.getId().equals(topicId)) {
                sectionRepository.save(section);
                break;
            }
        }

        Topic topicWithId = topicRepository.save(topic);

        return new TopicDTO(author, topicWithId);
    }

    public Iterable<TopicDTO> getSectionsTopics(String id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + id + " not found"));

        List<TopicDTO> topicsDTO = new ArrayList<>();

        for (Topic topic: section.getTopics()) {
            User author = userRepository.findById(topic.getAuthorId())
                    .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + topic.getAuthorId() + " not found"));

            topicsDTO.add(new TopicDTO(author, topic));
        }

        return topicsDTO;
    }

    public List<TopicDTO> getMostPopularTopics(Integer keyPageViews){
        List<Topic> mostPopularTopics = topicRepository.findTopicsByPageViewsGreaterThan(keyPageViews);
        Collections.sort(mostPopularTopics,Collections.reverseOrder(new TopicComparator()));

        List<TopicDTO> topicsDTO = new ArrayList<>();

        for (Topic topic: mostPopularTopics) {
            User author = userRepository.findById(topic.getAuthorId())
                    .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + topic.getAuthorId() + " not found"));

            topicsDTO.add(new TopicDTO(author, topic));
        }

        return topicsDTO;
    }

    public TopicDTO addTopicToSection(String id, TopicRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(userPrincipal.getUsername()).
                orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find currently logged in user " +
                        userPrincipal.getUsername() + " in database!"));

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find section of id " + id + " in database!"));

        Topic topic = new Topic(request.getTitle(), user.getId(), new Date(), new Date(),
                request.getDescription(), 0, Collections.<Post>emptyList());

        section.incTopicsNumber();
        user.incPostsNumber();

        userRepository.save(user);
        Topic topicWithId = topicRepository.save(topic);

        section.getTopics().add(topicWithId);
        sectionRepository.save(section);

        return new TopicDTO(user, topicWithId);
    }

    public TopicDTO updateTopicInSection(String sectionId, String topicId, TopicRequest request) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find section of id " + sectionId + " in database!"));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + topicId + " not found"));

        User author = userRepository.findById(topic.getAuthorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + topic.getAuthorId() + " not found"));

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

        Topic topicWithId = topicRepository.save(topic);

        return new TopicDTO(author, topic);
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
