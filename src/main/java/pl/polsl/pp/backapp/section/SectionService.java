package pl.polsl.pp.backapp.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.polsl.pp.backapp.exception.IdNotFoundInDatabaseException;
import pl.polsl.pp.backapp.post.Post;
import pl.polsl.pp.backapp.post.PostRepository;
import pl.polsl.pp.backapp.topic.Topic;
import pl.polsl.pp.backapp.topic.TopicRepository;
import pl.polsl.pp.backapp.user.User;
import pl.polsl.pp.backapp.user.UserRepository;

import java.util.Collections;
import java.util.List;


@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private TopicRepository topicRepository;

    public SectionService(SectionRepository sectionRepository, UserRepository userRepository, PostRepository postRepository,
                          TopicRepository topicRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
    }

    public Iterable<Section> getSections() {
        return sectionRepository.findAll();
    }

    public Section getSection(String id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + id + " not found"));

        return section;
    }

    public Section addSection(SectionRequest request) {

        User moderator = userRepository.findById(request.getModeratorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + request.getModeratorId() + " not found"));

        Section section = new Section(request.getName(), 0, moderator.getId(), Collections.<Topic>emptyList());

        return sectionRepository.save(section);
    }

    public Section updateSection(String id, SectionRequest request) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + id + " not found"));

        User moderator = userRepository.findById(request.getModeratorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + request.getModeratorId() + " not found"));

        section.setModeratorId(moderator.getId());
        section.setName(request.getName());

        return sectionRepository.save(section);
    }

    // TODO dodać usuwanie postów
    public void deleteSection(String id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + id + " not found"));

        List<Topic> topics = section.getTopics();

        for (Topic t: topics) {
            List <Post> posts = t.getPosts();
            postRepository.deleteAll(posts);
            topicRepository.save(t);
            String topicID = t.getId();
            topicRepository.deleteById(topicID);
        }

        sectionRepository.deleteById(id);
    }
}
