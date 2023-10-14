package br.com.pauloarantes.todolist.task;

import br.com.pauloarantes.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity creat(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUSer((UUID) idUser);
        var currentDate = LocalDateTime.now();
        if(currentDate.isBefore(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início deve ser maior do que a data atual");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início deve ser maior do que a data de Término");
        }
        var task = this.taskRepository.save(taskModel);
        return  ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
        var task = this.taskRepository.findById(id).orElse(null);

        if(task == null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!task.getIdUSer().equals(idUser)){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário não tem permissão para alterar essa tarefa ");
        }
        Utils.copyNonNullProperties(taskModel, task);
        var responseTask = this.taskRepository.save(task);
        return ResponseEntity.ok().body(this.taskRepository.save(responseTask));
    }
}
