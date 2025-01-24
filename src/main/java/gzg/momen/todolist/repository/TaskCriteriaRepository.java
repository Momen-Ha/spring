package gzg.momen.todolist.repository;

import gzg.momen.todolist.dto.TaskMapper;
import gzg.momen.todolist.dto.TaskResponse;
import gzg.momen.todolist.entity.Task;
import gzg.momen.todolist.entity.TaskPage;
import gzg.momen.todolist.entity.TaskSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TaskCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    private TaskMapper taskMapper;

    public TaskCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<TaskResponse> findAllWithFilters(TaskPage taskPage,
                                            TaskSearchCriteria taskSearchCriteria,
                                            long userId) {


        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = criteriaQuery.from(Task.class);

        Predicate userPredicate = criteriaBuilder.equal(taskRoot.get("user").get("userId"), userId);
        Predicate filterPredicate = getPredicate(taskSearchCriteria, taskRoot);
        criteriaQuery.where(criteriaBuilder.and(userPredicate, filterPredicate));
        setOrder(taskPage, criteriaQuery, taskRoot);

        TypedQuery<Task> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(taskPage.getPageNumber() * taskPage.getPageSize());
        query.setMaxResults(taskPage.getPageSize());

        Pageable pageable = getPageable(taskPage);

        long tasksCount = getTaskCount(criteriaBuilder.and(userPredicate, filterPredicate));

        PageImpl<TaskResponse> tasksPage = new PageImpl<TaskResponse>(query.getResultList().stream()
                .map(task -> this.taskMapper.taskToTaskResponse(task))
                .collect(Collectors.toList())
                , pageable, tasksCount);
        return tasksPage;

    }


    private Predicate getPredicate(TaskSearchCriteria taskSearchCriteria,
                                   Root<Task> taskRoot) {
        List<Predicate> filterPredicates = new ArrayList<>();
        if(Objects.nonNull(taskSearchCriteria.getTitle())) {
            filterPredicates.add(
                    criteriaBuilder.like(taskRoot.get("title"),
                            "%" + taskSearchCriteria.getTitle() + "%")
            );
        }
        if(Objects.nonNull(taskSearchCriteria.getDescription())) {
            filterPredicates.add(
                    criteriaBuilder.like(taskRoot.get("description"),
                            "%" + taskSearchCriteria.getDescription() + "%")
            );
        }
        return criteriaBuilder.and(filterPredicates.toArray(new Predicate[0]));
    }

    private void setOrder(TaskPage taskPage, CriteriaQuery<Task> criteriaQuery, Root<Task> taskRoot) {
        if(taskPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(taskRoot.get(taskPage.getSortProperty())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(taskRoot.get(taskPage.getSortProperty())));
        }
    }

    private Pageable getPageable(TaskPage taskPage) {
        Sort sort = Sort.by(taskPage.getSortDirection(), taskPage.getSortProperty());
        return PageRequest.of(taskPage.getPageNumber(), taskPage.getPageSize(), sort);
    }

    private long getTaskCount(Predicate filterPredicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(criteriaBuilder.count(countRoot));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}