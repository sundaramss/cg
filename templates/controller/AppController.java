package ${config.project.packageName}.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by sundaramss on 10/05/14.
 */
public interface AppController<MB> {

    ResponseEntity<MB> create(MB modelValue);

    ResponseEntity<MB> update(MB modelValue,String skGuid);

    ResponseEntity<MB> delete(MB modelValue,String skGuid);

	ResponseEntity<MB> get(MB modelValue,String skGuid);

    ResponseEntity<List<MB>> getAll();

}
