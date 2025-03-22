package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.Entities.Activity;
import com.agendaedu.educacional.Services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        Activity created = activityService.createActivity(activity);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<Activity>> getActivitiesBySala(@PathVariable Long salaId) {
        return ResponseEntity.ok(activityService.getActivitiesBySala(salaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAll() {
        return ResponseEntity.ok(activityService.getAll());
    }
}
