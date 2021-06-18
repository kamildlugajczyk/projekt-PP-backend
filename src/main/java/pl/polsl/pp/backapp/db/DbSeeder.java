/*
package pl.polsl.pp.backapp.db;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.polsl.pp.backapp.post.Post;
import pl.polsl.pp.backapp.post.PostRepository;
import pl.polsl.pp.backapp.section.Section;
import pl.polsl.pp.backapp.section.SectionRepository;
import pl.polsl.pp.backapp.topic.Topic;
import pl.polsl.pp.backapp.topic.TopicRepository;
import pl.polsl.pp.backapp.user.UserRepository;
import pl.polsl.pp.backapp.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DbSeeder implements CommandLineRunner {
    private UserRepository userRepo;
    private SectionRepository sectionRepo;
    private TopicRepository topicRepo;
    private PostRepository postRepo;

    public DbSeeder(UserRepository userRepo, SectionRepository sectionRepo,
                    TopicRepository topicRepo, PostRepository postRepo) {
        this.userRepo = userRepo;
        this.sectionRepo = sectionRepo;
        this.topicRepo = topicRepo;
        this.postRepo = postRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // password - Janek
        User zbychu = new User("zbigpajak@wp.pl", "zbychu", "$2a$12$wqIUs8iJ9.AMv9XT6pNMNeLKi3cQYJhzOi2r1KoGvcSxDmHvpJ3LO", "USER",
                true, 0, new Date(), null);
        // password - kanowak
        User weronika = new User("weranowa22@interia.pl", "weronika", "$2a$12$MMEXbUyxXSm8zTI5TifIqemOYh6deBZ5K0gbBpKvC4VZDeAU43r26", "USER",
                true, 0, new Date(), null);
        // password - michalwisnia
        //User wisnia = new User("wisnia123@onet.pl", "michalwisnia", "$2y$12$lQxZDTOPNkj/CtzQJtnbE.liaOatvaaGtmv1GpQpsWhQPn/3rt6eq", "USER",
        //        true, 0, new Date(), null);

        userRepo.save(zbychu);
        userRepo.save(weronika);
        //userRepo.save(wisnia);

        Post match1 = new Post(zbychu, new Date(), new Date(), "Dzis wygra Polska");
        Post match2 = new Post(weronika, new Date(), new Date(), "Ciekawe jak poradzi sobie Lewy?");
        Post match3 = new Post(zbychu, new Date(), new Date(), "Moze w koncu strzeli gola!");

        List<Post> matchPosts = new ArrayList<>();
        matchPosts.add(match1);
        matchPosts.add(match2);
        matchPosts.add(match3);

        Post kitchen1 = new Post(weronika, new Date(), new Date(), "Robię dziś schabowego na obiad.");
        Post kitchen2 = new Post(zbychu, new Date(), new Date(), "Mniam. Pyszne");
        Post kitchen3 = new Post(weronika, new Date(), new Date(), "A Ty co robisz na obiad?");
        Post kitchen4 = new Post(zbychu, new Date(), new Date(), "Zjem coś na mieście.");

        List<Post> kitchenPosts = new ArrayList<>();
        kitchenPosts.add(kitchen1);
        kitchenPosts.add(kitchen2);
        kitchenPosts.add(kitchen3);
        kitchenPosts.add(kitchen4);

        postRepo.save(match1);
        postRepo.save(match2);
        postRepo.save(match3);
        postRepo.save(kitchen1);
        postRepo.save(kitchen2);
        postRepo.save(kitchen3);
        postRepo.save(kitchen4);

        Topic topicMatch = new Topic("Mecz Polska-Hiszpania", weronika, new Date(), java.sql.Date.valueOf(LocalDate.now()),
                "Dzis nasza cudowna kadra gra mecz. Kto wygra Waszym zdaniem?", matchPosts);
        Topic topicKitchen = new Topic("Co na obiad?", zbychu, new Date(), java.sql.Date.valueOf(LocalDate.now()),
                "Co jecie dzis na obiad?", kitchenPosts);

        topicRepo.save(topicMatch);
        topicRepo.save(topicKitchen);

        List<Topic> footballTopics = new ArrayList<>();
        footballTopics.add(topicMatch);
        List<Topic> kitchenTopics = new ArrayList<>();
        kitchenTopics.add(topicKitchen);

        Section sectionFootball = new Section("Football", 0, null, footballTopics);
        Section sectionKitchen = new Section("Kitchen", 0, null, kitchenTopics);

        sectionRepo.save(sectionFootball);
        sectionRepo.save(sectionKitchen);
    }
}
*/

