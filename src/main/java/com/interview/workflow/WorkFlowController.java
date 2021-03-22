package com.interview.workflow;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Component
@Data
public class WorkFlowController {

    @Autowired
    private IProcess validateData;

    @Autowired
    private IProcess updateCache;

    private final Map<WorkFlowEnum, IProcess> workFlowActivitiesReadWrite = new EnumMap<>(WorkFlowEnum.class);

    public Map<WorkFlowEnum, IProcess> workFlowActivities;

    @PostConstruct
    public void init() {
        workFlowActivitiesReadWrite.put(WorkFlowEnum.VALIDATION, validateData);
        workFlowActivitiesReadWrite.put(WorkFlowEnum.UPDATE_CACHE, updateCache);

        workFlowActivities = Collections.unmodifiableMap(workFlowActivitiesReadWrite);

    }

    public Map<WorkFlowEnum, IProcess> getWorkFlowActivities() {
        return workFlowActivities;
    }
}
