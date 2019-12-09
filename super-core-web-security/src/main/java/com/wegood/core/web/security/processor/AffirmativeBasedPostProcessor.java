package com.wegood.core.web.security.processor;

import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.ObjectPostProcessor;

/**
 * @author Rain
 */
public class AffirmativeBasedPostProcessor implements ObjectPostProcessor<AffirmativeBased> {

    @Override
    public <O extends AffirmativeBased> O postProcess(O object) {
        object.getDecisionVoters().add(new RoleVoter());
        return object;
    }

}
