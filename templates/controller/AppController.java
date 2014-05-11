package ${config.project.packageName}.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by sundaramss on 10/05/14.
 */
public interface AppController<MB> {

    ResponseEntity<MB> create(MB modelValue);

    ResponseEntity<MB> update(MB modelValue);

    ResponseEntity<MB> delete(MB modelValue);

    ResponseEntity<List<MB>> getAll();

}
