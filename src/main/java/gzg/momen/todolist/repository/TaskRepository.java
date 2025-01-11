package gzg.momen.todolist.repository;

import gzg.momen.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
