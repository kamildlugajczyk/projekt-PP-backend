package pl.polsl.pp.backapp.post;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.polsl.pp.backapp.auth.UserPrincipal;
import pl.polsl.pp.backapp.exception.IdNotFoundInDatabaseException;
import pl.polsl.pp.backapp.exception.ZeroResultsException;
import pl.polsl.pp.backapp.topic.Topic;
import pl.polsl.pp.backapp.topic.TopicRepository;
import pl.polsl.pp.backapp.user.User;
import pl.polsl.pp.backapp.user.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PostService {

    private PostRepository postRepository;
    private TopicRepository topicRepository;
    private UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, TopicRepository topicRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    public Iterable<PostDTO> getPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postsDTO = new ArrayList<>();

        for (Post post: posts) {
            User author = userRepository.findById(post.getAuthorId())
                    .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + post.getAuthorId() + " not found"));

            postsDTO.add(new PostDTO(author, post));
        }

        return postsDTO;
    }

    public PostDTO getPostDTO(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Post of id " + id + " not found"));

        User author = userRepository.findById(post.getAuthorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + post.getAuthorId() + " not found"));

        return new PostDTO(author, post);
    }

    public Iterable<PostDTO> getTopicsPosts(String id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + id + " not found"));

        List<PostDTO> postsDTO = new ArrayList<>();

        for (Post post: topic.getPosts()) {
            User author = userRepository.findById(post.getAuthorId())
                    .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + post.getAuthorId() + " not found"));

            postsDTO.add(new PostDTO(author, post));
        }

        return postsDTO;
    }

    public List<PostDTO> getPostsContainingKeyword(String keyword) {
        List<Post> foundPosts = postRepository.findByTextContaining(keyword);
        if (foundPosts.isEmpty())
            throw new ZeroResultsException(keyword);

        List<PostDTO> postsDTO = new ArrayList<>();

        for (Post post: foundPosts) {
            User author = userRepository.findById(post.getAuthorId())
                    .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + post.getAuthorId() + " not found"));

            postsDTO.add(new PostDTO(author, post));
        }

        return postsDTO;
    }

    public PostDTO addPostToTopic(String id, PostRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(userPrincipal.getUsername())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find currently logged in user " +
                        userPrincipal.getUsername() + " in database!"));

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + id + " not found"));

        Post post = new Post(user.getId(), new Date(), new Date(), request.getText());

        user.incPostsNumber();
        userRepository.save(user);

        Post postWithId = postRepository.save(post);

        topic.getPosts().add(postWithId);
        topicRepository.save(topic);

        return new PostDTO(user, postWithId);
    }

    public PostDTO updatePostInTopic(String topicId, String postId, PostRequest request) {
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new IdNotFoundInDatabaseException("Post of id " + postId + " not found"));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + topicId + " not found"));

        User author = userRepository.findById(post.getAuthorId())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("User of id " + post.getAuthorId() + " not found"));

        post.setLastChange(new Date());
        post.setText(request.getText());

        for (Post p : topic.getPosts()) {
            if (p.getId().equals(postId)) {
                p.setText(post.getText());
                p.setLastChange(post.getLastChange());

                topicRepository.save(topic);
                break;
            }
        }

        Post postWithId = postRepository.save(post);

        return new PostDTO(author, postWithId);
    }

    public void deletePost(String topicId, String postId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(userPrincipal.getUsername())
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Can't find currently logged in user " +
                    userPrincipal.getUsername() + " in database!"));;

        user.decPostsNumber();
        userRepository.save(user);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IdNotFoundInDatabaseException("Topic of id " + topicId + " not found"));

        for (Post p : topic.getPosts()) {
            if (p.getId().equals(postId)) {
                topic.getPosts().remove(p);
                topicRepository.save(topic);
                break;
            }
        }

        postRepository.deleteById(postId);
    }
}
