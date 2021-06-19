package pl.polsl.pp.backapp.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.polsl.pp.backapp.exception.IdNotFoundInDatabaseException;
import pl.polsl.pp.backapp.exception.ItemExistsInDatabaseException;
import pl.polsl.pp.backapp.exception.ZeroResultsException;

import java.util.List;

@RestController
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    public Iterable<Post> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable String id) {
        try {
            return postService.getPost(id);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/topic/{id}/post")
    public Iterable<Post> getTopicsPosts(@PathVariable String id) {
        try {
            return postService.getTopicsPosts(id);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/post/filter/{keyword}")
    public List<Post> getPostsFoundByKeyword(@PathVariable String keyword)
    {
        try {
            return postService.getPostsContainingKeyword(keyword);
        } catch (ZeroResultsException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping("/topic/{id}/post")
    public Post addPostToTopic(@PathVariable String id, @RequestBody PostRequest request) {
        try {
            return postService.addPostToTopic(id, request);
        } catch (ItemExistsInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (RuntimeException  e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping("/topic/{topicId}/post/{postId}")
    public Post updatePost(@PathVariable String topicId, @PathVariable String postId, @RequestBody PostRequest request) {
        try {
            return postService.updatePostInTopic(topicId, postId, request);
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

    @DeleteMapping("/topic/{topicId}/post/{postId}")
    public void deletePost(@PathVariable String topicId, @PathVariable String postId) {
        try {
            postService.deletePost(topicId, postId);
        } catch (IdNotFoundInDatabaseException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
