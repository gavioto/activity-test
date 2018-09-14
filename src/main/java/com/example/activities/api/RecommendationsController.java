package com.example.activities.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.activities.respository.ActivityRepository;

@RestController(value="/recommendations")
public class RecommendationsController {

//    @Autowired
//    ActivityRepository activityRepository;

//    @GetMapping("/activity")
//    public Page<Activity> getAllActivities(Pageable pageable) {
//        return activityRepository.findAll(pageable);
//    }
//
//    @PutMapping("/posts/{postId}")
//    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
//        return activityRepository.findById(postId).map(post -> {
//            post.setTitle(postRequest.getTitle());
//            post.setDescription(postRequest.getDescription());
//            post.setContent(postRequest.getContent());
//            return activityRepository.save(post);
//        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
//    }
//
//
//    @DeleteMapping("/posts/{postId}")
//    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
//        return activityRepository.findById(postId).map(post -> {
//            activityRepository.delete(post);
//            return ResponseEntity.ok().build();
//        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
//    }

}