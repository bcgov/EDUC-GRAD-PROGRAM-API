package ca.bc.gov.educ.api.program.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
@Builder
@Table(name = "PROGRAM_ASSESSMENT_REQUIREMENT")
public class ProgramAssessmentRequirementEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "PROGRAM_ASSESSMENT_REQUIREMENT_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID programAssessmentRequirementId;

    @NotNull(message = "assessment code cannot be null")
    @Size(max = 7, message = "assessment code cannot exceed 7 characters")
    @Column(name = "ASSESSMENT_TYPE_CODE", nullable = false, length = 7)
    private String assessmentCode;

    @NotNull(message= "program requirement code cannot be null")
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "PROGRAM_REQUIREMENT_CODE", referencedColumnName = "PROGRAM_REQUIREMENT_CODE")
    private ProgramRequirementCodeEntity programRequirementCodeEntity;


}
