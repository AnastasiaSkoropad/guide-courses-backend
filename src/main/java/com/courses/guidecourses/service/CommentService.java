package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.CommentDto;
import com.courses.guidecourses.dto.CreateCommentDto;
import com.courses.guidecourses.entity.Comment;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.User;
import com.courses.guidecourses.exception.ResourceNotFoundException;
import com.courses.guidecourses.mapper.CommentMapper;
import com.courses.guidecourses.mapper.UserMapper;
import com.courses.guidecourses.repository.CommentRepository;
import com.courses.guidecourses.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final CourseRepository courseRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public Page<CommentDto> getComments(Long courseId, Pageable pageable) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        return commentRepository.findByCourseId(course.getId(), pageable)
                .map(commentMapper::toDto);
    }

    @Transactional
    public CommentDto createComment(Long courseId,
                                    CreateCommentDto dto,
                                    String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        Comment comment = new Comment();
        comment.setCourse(course);
        comment.setUser(user);
        comment.setText(dto.getText());

        Comment saved = commentRepository.save(comment);

        course.setCommentCount(course.getCommentCount() + 1);

        return new CommentDto(
                saved.getId(),
                saved.getText(),
                saved.getCreatedAt(),
                userMapper.toDto(user)
        );
    }


    @Transactional
    public void deleteComment(Long commentId, String requester) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));

        commentRepository.delete(comment);

        Course course = comment.getCourse();
        course.setCommentCount(Math.max(0, course.getCommentCount() - 1));
    }
}
