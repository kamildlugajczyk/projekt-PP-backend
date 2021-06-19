package pl.polsl.pp.backapp.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.polsl.pp.backapp.exception.IdNotFoundInDatabaseException;
import pl.polsl.pp.backapp.topic.Topic;
import pl.polsl.pp.backapp.user.User;
import pl.polsl.pp.backapp.user.UserRepository;

import java.util.Collections;


@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private UserRepository userRepository;

    public SectionService(SectionRepository sectionRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
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

        Section section = new Section(request.getName(), 0, moderator , Collections.<Topic>emptyList());

        return sectionRepository.save(section);
    }

    public Section updateSection(String id, SectionRequest request) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Section of id " + id + " not found"));

        User moderator = userRepository.findById(request.getModeratorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + request.getModeratorId() + " not found"));

        section.setModerator(moderator);
        section.setName(request.getName());

        return sectionRepository.save(section);
    }

    // TODO dodać usuwanie tematów i postów
    public void deleteSection(String id) {
        sectionRepository.deleteById(id);
    }
}
