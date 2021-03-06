package de.tum.in.www1.exerciseapp.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Course.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Course.class.getName() + ".exercises", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Exercise.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Exercise.class.getName() + ".participations", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.LtiOutcomeUrl.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.LtiUserId.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Participation.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Participation.class.getName() + ".results", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Result.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.ProgrammingExercise.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.ModelingExercise.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.QuizExercise.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.QuizExercise.class.getName() + ".questions", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.SubmittedAnswer.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Question.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.MultipleChoiceQuestion.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.MultipleChoiceQuestion.class.getName() + ".answerOptions", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.AnswerOption.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.MultipleChoiceSubmittedAnswer.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.MultipleChoiceSubmittedAnswer.class.getName() + ".selectedOptions", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragAndDropQuestion.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragAndDropQuestion.class.getName() + ".dropLocations", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragAndDropQuestion.class.getName() + ".dragItems", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DropLocation.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragItem.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Submission.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.ModelingSubmission.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.QuizSubmission.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.QuizSubmission.class.getName() + ".submittedAnswers", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragAndDropSubmittedAnswer.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragAndDropSubmittedAnswer.class.getName() + ".assignments", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.DragAndDropAssignment.class.getName(), jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Question.class.getName() + ".quizExercises", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Result.class.getName() + ".feedbacks", jcacheConfiguration);
            cm.createCache(de.tum.in.www1.exerciseapp.domain.Feedback.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
