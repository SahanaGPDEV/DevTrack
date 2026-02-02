package com.devtrack.service;

import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.model.DsaProblem;
import com.devtrack.model.DsaProblem.Difficulty;
import com.devtrack.model.DsaProblem.Platform;
import com.devtrack.model.User;
import com.devtrack.repository.DsaProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DsaProblemService {

    @Autowired
    private DsaProblemRepository dsaProblemRepository;

    @Autowired
    private UserService userService;

    public DsaProblem addProblem(Long userId, DsaProblem problem) {
        User user = userService.getUserById(userId);
        problem.setUser(user);
        DsaProblem savedProblem = dsaProblemRepository.save(problem);

        // Update user stats
        if (problem.getStatus() == DsaProblem.Status.SOLVED) {
            userService.addProblemsSolved(userId, 1);
        }

        return savedProblem;
    }

    public List<DsaProblem> getProblemsByUserId(Long userId) {
        return dsaProblemRepository.findByUserUserIdOrderBySolvedDateDesc(userId);
    }

    public DsaProblem getProblemById(Long problemId) {
        return dsaProblemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with id: " + problemId));
    }

    public List<DsaProblem> getProblemsByDifficulty(Long userId, Difficulty difficulty) {
        return dsaProblemRepository.findByUserUserIdAndDifficulty(userId, difficulty);
    }

    public List<DsaProblem> getProblemsByPlatform(Long userId, Platform platform) {
        return dsaProblemRepository.findByUserUserIdAndPlatform(userId, platform);
    }

    public List<DsaProblem> getProblemsByTopic(Long userId, String topic) {
        return dsaProblemRepository.findByUserUserIdAndTopicContainingIgnoreCase(userId, topic);
    }

    public DsaProblem updateProblem(Long problemId, DsaProblem problemDetails) {
        DsaProblem problem = getProblemById(problemId);

        if (problemDetails.getTitle() != null)
            problem.setTitle(problemDetails.getTitle());
        if (problemDetails.getPlatform() != null)
            problem.setPlatform(problemDetails.getPlatform());
        if (problemDetails.getDifficulty() != null)
            problem.setDifficulty(problemDetails.getDifficulty());
        if (problemDetails.getTopic() != null)
            problem.setTopic(problemDetails.getTopic());
        if (problemDetails.getProblemLink() != null)
            problem.setProblemLink(problemDetails.getProblemLink());
        if (problemDetails.getStatus() != null)
            problem.setStatus(problemDetails.getStatus());
        if (problemDetails.getTimeTaken() != null)
            problem.setTimeTaken(problemDetails.getTimeTaken());
        if (problemDetails.getApproach() != null)
            problem.setApproach(problemDetails.getApproach());
        if (problemDetails.getNotes() != null)
            problem.setNotes(problemDetails.getNotes());

        return dsaProblemRepository.save(problem);
    }

    public void deleteProblem(Long problemId) {
        DsaProblem problem = getProblemById(problemId);
        dsaProblemRepository.delete(problem);
    }

    public Integer getProblemsCountInRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return dsaProblemRepository.countProblemsInRange(userId, startDate, endDate);
    }

    public Integer getTotalProblems(Long userId) {
        return dsaProblemRepository.countByUserUserId(userId);
    }
}
