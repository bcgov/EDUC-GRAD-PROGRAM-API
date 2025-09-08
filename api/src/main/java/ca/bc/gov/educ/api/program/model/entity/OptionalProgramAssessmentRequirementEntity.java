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
@Table(name = "OPTIONAL_PROGRAM_ASSESSMENT_REQUIREMENT")
public class OptionalProgramAssessmentRequirementEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "OPTIONAL_PROGRAM_ASSESSMENT_REQUIREMENT_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID optionalProgramAssessmentRequirementId;

    @NotNull(message = "assessment code cannot be null")
    @Size(max = 7, message = "assessment code cannot exceed 7 characters")
    @Column(name = "ASSESSMENT_TYPE_CODE", nullable = false, length = 7)
    private String assessmentCode;

    @NotNull(message= "optional program requirement code cannot be null")
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "OPTIONAL_PROGRAM_RQMT_CODE", referencedColumnName = "OPTIONAL_PROGRAM_RQMT_CODE")
    private OptionalProgramRequirementCodeEntity optionalProgramRequirementCodeEntity;

}
