package com.bright.thought.repository;

import com.bright.thought.entity.SituationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SituationRepository extends JpaRepository<SituationEntity, UUID> {
    @Query(value = """
           select s
           from SituationEntity s
           left join ThoughtEntity t
           on s.id = t.situation.id
           where s.userId = :userId and (t.realContent = '' or t.id is null)
           """)
    List<SituationEntity> getEditedSituations(@Param("userId") UUID userId);

    @Query(value= """
            select s
            from SituationEntity s
            where s.userId = :userId and not exists (
                select 1
                from SituationEntity s1
                left join ThoughtEntity t1
                on s1.id = t1.situation.id
                where s.id = s1.id and (t1.realContent = '' or t1.id is null)
            )
            """)
    List<SituationEntity> getDoneSituations(@Param("userId") UUID userId);

    List<SituationEntity> findByUserId(UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
