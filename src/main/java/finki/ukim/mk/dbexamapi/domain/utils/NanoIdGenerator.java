package finki.ukim.mk.dbexamapi.domain.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import java.util.EnumSet;

public class NanoIdGenerator implements BeforeExecutionGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session,
                           Object owner, Object currentValue, EventType eventType) {
        return NanoIdUtils.randomNanoId();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }
}


